package com.bruno.cursomc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
//  Implementar o SmtpEmailService utilizando nele uma instância de MailSender
public class SmtpEmailService extends AbstractEmailService {

	@Autowired
	private MailSender mailSender; // O framework irá instanciar com todos os dados do email do application-test.properties

	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailService.class); // log será referente a classe SmtpEmailService
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando o email...");
		mailSender.send(msg);
		LOG.info("Email enviado");
	}
}