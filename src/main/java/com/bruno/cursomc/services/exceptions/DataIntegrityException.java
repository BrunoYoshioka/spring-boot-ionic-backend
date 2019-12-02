package com.bruno.cursomc.services.exceptions;

public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	// criando construtor (reaproveitando a infraestrutura de excessões da linguagem java
	public DataIntegrityException(String msg) {
		super(msg);
	}
	
	// criando outra sobrecarga do construtor (recebe a mensagem e a outra excessão q é a causa que ocorreu antes
	public DataIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
