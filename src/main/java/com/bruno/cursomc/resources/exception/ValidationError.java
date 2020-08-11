package com.bruno.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;
 // classe auxiliar tendo todos os dados do StandardError mais a lista de mensagens
public class ValidationError extends StandardError {
	
	private static final long serialVersionUID = 1L;
	
	// listar uma lista
	private List<FieldMessage> errors = new ArrayList<>();

	 public ValidationError(Long timeStamp, Integer status, String error, String message, String path) {
		 super(timeStamp, status, error, message, path);
	 }

	 public List<FieldMessage> getErrors() {
		return errors;
	}

	// Acrescentar uma lista de uma vez
//	public void setList(List<FieldMessage> list) {
//		this.list = list;
//	}
	
	// acrescentando um erro de cada vez na lista de FieldMessage
	public void addError(String fieldName, String message) {
		errors.add(new FieldMessage(fieldName, message));
	}
	
}
