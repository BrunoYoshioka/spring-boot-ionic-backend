package com.bruno.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {
	// Exibir email no log no servidor
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class); // log será referente a classe MockEmailService
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando o envio de email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
	}

	// Implementar novos contratos de EmailService
	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		// Enviar email de "Mentirinha" da MimeMessage
		LOG.info("Simulando o envio de email HTML...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
	}
}