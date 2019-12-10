package com.bruno.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.bruno.cursomc.dominio.Pedido;

public interface EmailService {
	void senderOrderConfirmationEmail(Pedido obj); // email de confirmação de pedido
	
	void sendEmail(SimpleMailMessage msg);
}
