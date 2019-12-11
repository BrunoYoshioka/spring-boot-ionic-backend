package com.bruno.cursomc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.bruno.cursomc.dominio.Pedido;

public abstract class AbstractEmailService implements EmailService {
	// dizer pro framework puxar pra essa variavel o valor que está no Aplication.properties
	@Value("${default.sender}")
	private String sender;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Override // indicar que está sendo sobrescrito do metodo do EmailService
	public void sendOrderConfirmationEmail(Pedido obj) {
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
	
	// responsável por retornar o HTML preenchido com os dados de um pedido, a partir do template Thymeleaf:
	protected String htmlFromTemplatePedido(Pedido obj) {
		Context context = new Context(); // context do thymeleaf
		context.setVariable("pedido", obj); // define que o template irá utilizar o obj com apelido "pedido"
		return templateEngine.process("email/confirmacaoPedido", context); // Processar o template para retornar o HTML na forma de string e retorna o HTML na forma de string
	}
	
	// Implementar o novo contrato
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		try {
			// tentar mandar email em HTML
			MimeMessage mm = prepareMimeMessageFromPedido(obj); // instanciar o email massage a partir do meu obj
			sendHtmlEmail(mm);
		}
		catch(MessagingException e){
			// caso der problema
			// Enviar com texto plano (sem ser html)
			sendOrderConfirmationEmail(obj);
		}
	}

	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException /*Esse método pode lançar uma excessão*/ {
		// Pegar o pedido e gerar objeto do tipo MimeMessage
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail()); // Será por email do cliente associado com Pedido obj
		mmh.setFrom(sender); // remetente do email
		mmh.setSubject("Pedido confirmado! Código:" + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true); // corpo do email
		
		return mimeMessage;
	}
}