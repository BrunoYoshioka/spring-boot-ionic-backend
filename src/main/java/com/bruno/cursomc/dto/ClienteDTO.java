package com.bruno.cursomc.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.bruno.cursomc.dominio.Cliente;

// Classe para ter dados basicos para atualizar, deletar e listar os clientes
public class ClienteDTO {
	// o cliente em hipotese nenhuma deve alterar o cpf ou cnpj
	
	private Integer id;
	
	@NotEmpty(/*Caso não validação não seja verificada*/message = "Preenchimento Obrigatório")
	@Length(min=5, max=120, message = "O tamanho de caracteres deve ser de 5 a 120")
	private String nome;
	
	@NotEmpty(message = "Preenchimento Obrigatório")
	@Email(message = "Email inválido")
	private String email;
	
	public ClienteDTO() {
	}
	
	public ClienteDTO(Cliente obj) {
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
