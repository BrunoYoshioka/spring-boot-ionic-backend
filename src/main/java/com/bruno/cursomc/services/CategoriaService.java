package com.bruno.cursomc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.Categoria;
import com.bruno.cursomc.repositories.CategoriaRepository;
import com.bruno.cursomc.services.exceptions.ObjectNotFoundException;

// Classe responsável por realizar consultas no repositório.
@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	// criando uma operação para buscar a categoria por id
	// chamar a operação do acesso a dados que é o repository
//	public Categoria buscar(Integer id) {
//		Categoria obj = repo.findOne(id);
//		return obj;
//	}
	
	// O Spring Boot versão 2.X.X só aceita versão 8 em diante.
	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id); // busca o objeto no banco
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName())); // caso não existir
	}
	
	
	public Categoria insert(Categoria obj) {
		obj.setId(null); // objeto novo a ser inserido tem que ter o id nulo 
		return repo.save(obj); // método salvar
	}
	
	
	public Categoria update(Categoria obj) {
		find(obj.getId()); // repete o mesmo procedimento do método find antes de salvar
		return repo.save(obj);
	}

}
