package com.bruno.cursomc.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class S3Service {

    private Logger logger = LoggerFactory.getLogger(S3Service.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucketName;

    // Método responsável por fazer upload do arquivo local pro S3
    public void uploadFile(String localFilePath){
        try{
            File file = new File(localFilePath);
            logger.info("Iniciando upload...");
            s3Client.putObject(new PutObjectRequest(bucketName, "teste.png", file));
            logger.info("Upload finalizado.");
        }
        catch (AmazonServiceException e){
            logger.info("AmazonServiceException: " + e);
            logger.info("Status code: " + e.getErrorCode());
        }
        catch (AmazonClientException e){ // tratando quando houver falha ao enviar arquivo
            logger.info("AmazonClientException: " + e.getMessage());
        }
    }
}
