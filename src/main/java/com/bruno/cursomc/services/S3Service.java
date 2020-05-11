package com.bruno.cursomc.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile; // Permite Enviar arquivo na requisição

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Service
public class S3Service {

    private Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    // Método responsável por fazer upload do arquivo local pro S3
    public URI uploadFile(MultipartFile multipartFile) /*throws IOException*/ {
        try {
            String fileName = multipartFile.getOriginalFilename(); // Extrair o nome do arquivo que foi enviado
            InputStream inputStream = multipartFile.getInputStream(); // básico de leitura
            String contentType = multipartFile.getContentType(); // String correspondendo o tipo do arquivo q foi enviado
            return uploadFile(inputStream, fileName, contentType);
        } catch (IOException e){
            throw new RuntimeException("Erro de IO: " + e.getMessage());
        }
    }

    public URI uploadFile(InputStream is, String fileName, String contentType) /*throws URISyntaxException*/ {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            logger.info("Iniciando upload...");
            s3Client.putObject(bucketName, fileName, is, metadata);
            logger.info("Upload finalizado.");
            return s3Client.getUrl(bucketName, fileName).toURI();
        } catch (URISyntaxException e){
            throw new RuntimeException("Erro ao converter URL para URI");
        }
    }
}
