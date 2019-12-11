package com.bruno.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.bruno.cursomc.dominio.Pedido;

public interface EmailService {
	void sendOrderConfirmationEmail(Pedido obj); // email de confirmação de pedido
	
	void sendEmail(SimpleMailMessage msg);
	
	// Métodos versão HTML
	void sendOrderConfirmationHtmlEmail(Pedido obj); 
	void sendHtmlEmail(MimeMessage msg);
}
