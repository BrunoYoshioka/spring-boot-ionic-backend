package com.bruno.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {

	// Exibir email no log no servidor
	private final static Logger LOG = LoggerFactory.getLogger(MockEmailService.class); // log será referente a classe MockEmailService
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando o envio de email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
	}

}
