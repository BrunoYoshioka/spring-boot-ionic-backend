package com.bruno.cursomc.services;

import com.bruno.cursomc.services.exceptions.FileException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageService {
    // função para obter uma imagem JPG a partir do arquivo
    public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) /*throws IOException*/ {
        // Pegar o MultipartFile(algum tipo de imagem) e converter para BufferedImage(jpg)
        String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename()); // Pegar a extensão do arquivo
        if (!"png".equals(ext) && !"jpg".equals(ext)) { // testar se a extensão não for png e não for jpg
            throw new FileException("Somente imagens PNG e JPG são permitidas"); // rejeitar a requisição
        }

        //Tentar obter BufferedImage apartir de MultipartFile
        try {
            BufferedImage image = ImageIO.read(uploadedFile.getInputStream()); // ler uma imagem do arquivo
            // Para ler a imagem jpg
            //Então, para isso, devo testar se o arquivo é png
            if ("png".equals(ext)){
                image = pngToJpg(image); // converter a imagem para jpg
            }
            return image;
        } catch (IOException e){
            throw new FileException("Erro ao ler arquivo");
        }
    }

    public BufferedImage pngToJpg(BufferedImage image) {
        BufferedImage jpgImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        jpgImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return jpgImage;
    }

    // O método que faz o upload lá para o S3, ele recebe o InputStream
    // Esse método,
    public InputStream getInputStream(BufferedImage image, String extension){
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, extension, outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e){
            throw new FileException("Erro ao ler arquivo");
        }
    }
}
