package com.bruno.cursomc;

import com.bruno.cursomc.services.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}
	
	public void run(String... args) throws Exception {
		s3Service.uploadFile("C:\\Users\\BOYA\\Desktop\\TASKS\\ESTUDOS E REVISOES\\UDEMY - Spring Boot, Hibernate, REST, Ionic, Jwt, S3, MySQL, MongoDB\\Seção 2\\diagrama\\diagrama.png");
	}
}