package com.bruno.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.Pedido;
import com.bruno.cursomc.repositories.PedidoRepository;
import com.bruno.cursomc.services.exceptions.ObjectNotFoundException;

// Classe responsável por realizar consultas no repositório.
@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	// criando uma operação para buscar a categoria por id
	// chamar a operação do acesso a dados que é o repository
//	public Pedido buscar(Integer id) {
//		Pedido obj = repo.findOne(id);
//		return obj;
//	}
	
	// O Spring Boot versão 2.X.X só aceita versão 8 em diante.
	public Pedido buscar(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

}
