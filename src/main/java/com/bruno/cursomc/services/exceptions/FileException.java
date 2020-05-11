package com.bruno.cursomc.services.exceptions;
// classe de excessão personalizada
public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// criando construtor (reaproveitando a infraestrutura de excessões da linguagem java
	public FileException(String msg) {
		super(msg);
	}

	// criando outra sobrecarga do construtor (recebe a mensagem e a outra excessão q é a causa que ocorreu antes
	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
