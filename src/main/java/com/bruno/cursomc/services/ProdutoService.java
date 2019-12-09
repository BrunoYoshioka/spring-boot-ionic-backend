package com.bruno.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.Categoria;
import com.bruno.cursomc.dominio.Produto;
import com.bruno.cursomc.repositories.CategoriaRepository;
import com.bruno.cursomc.repositories.ProdutoRepository;
import com.bruno.cursomc.services.exceptions.ObjectNotFoundException;

// Classe responsável por realizar consultas no repositório.
@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	// criando uma operação para buscar a categoria por id
	// chamar a operação do acesso a dados que é o repository
//	public Produto buscar(Integer id) {
//		Produto obj = repo.findOne(id);
//		return obj;
//	}
	
	// O Spring Boot versão 2.X.X só aceita versão 8 em diante.
	public Produto find(Integer id) {
		Optional<Produto> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	// Operação busca paginada
	public Page<Produto> search(String nome, List<Integer> ids, Integer page /*Pagina começa com 0*/, Integer linesPerPage /*Linhas por páginas*/, 
			String orderBy /*ordenar atributo por Id, Nome, etc*/, String direction /*Ordenar por qual direção (Ascendente ou descendente)*/){
		//@SuppressWarnings("deprecation")
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		// Acrescentar detalhes que não foram previstos no nivel do projeto
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.search(nome, categorias, pageRequest); // criar metodo search no meu repository, passando 
	}
}
