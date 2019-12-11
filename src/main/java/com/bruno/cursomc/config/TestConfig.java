package com.bruno.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bruno.cursomc.services.DBService;
import com.bruno.cursomc.services.EmailService;
import com.bruno.cursomc.services.MockEmailService;

@Configuration
@Profile("test")
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbService.instantiateDatabase();
		return true;
	}
	// criar um método @Bean EmailService que retorna uma instância de MockEmailService
	@Bean
	public EmailService emailService() {
		return new MockEmailService();
	}
}