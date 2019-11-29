package com.bruno.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.Cliente;
import com.bruno.cursomc.repositories.ClienteRepository;
import com.bruno.cursomc.services.exceptions.ObjectNotFoundException;

// Classe responsável por realizar consultas no repositório.
@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	// criando uma operação para buscar a categoria por id
	// chamar a operação do acesso a dados que é o repository
//	public Cliente buscar(Integer id) {
//		Cliente obj = repo.findOne(id);
//		return obj;
//	}
	
	// O Spring Boot versão 2.X.X só aceita versão 8 em diante.
	// Responsável por receber id e retornar cliente correspondente ao id 
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

}
