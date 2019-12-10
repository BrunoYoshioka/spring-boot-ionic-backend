package com.bruno.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.bruno.cursomc.dominio.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	// dizer pro framework puxar pra essa variavel o valor que está no Aplication.properties
	@Value("${default.sender}")
	private String sender;
	
	@Override // indicar que está sendo sobrescrito do metodo do EmailService
	public void senderOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj); // instanciar o email massage a partir do meu obj
		sendEmail(sm); // chamar metodo da interface EmailService
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail()); // será o destinatário do email
		sm.setFrom(sender); // será o email remetente
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis())); // garanta que será criado uma data com horário do meu servidor
		sm.setText(obj.toString()); // Corpo do email
		return sm;
	}

}
