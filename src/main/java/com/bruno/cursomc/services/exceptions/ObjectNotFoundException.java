package com.bruno.cursomc.services.exceptions;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	// criando construtor (reaproveitando a infraestrutura de excessões da linguagem java
	public ObjectNotFoundException(String msg) {
		super(msg);
	}
	
	// criando outra sobrecarga do construtor (recebe a mensagem e a outra excessão q é a causa que ocorreu antes
	public ObjectNotFoundException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
