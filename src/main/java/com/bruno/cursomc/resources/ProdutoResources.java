package com.bruno.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.cursomc.dominio.Produto;
import com.bruno.cursomc.dto.ProdutoDTO;
import com.bruno.cursomc.resources.utils.URL;
import com.bruno.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value="/produtos")
public class ProdutoResources {
	
	@Autowired
	private ProdutoService service;
	
	// para ser uma função tem que associar a ela o verbo do http
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id){
		Produto obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	// Endpoint para busca
	// O sistema informa os nomes de todas categorias ordenadamente.
	// Buscar todas as categorias com paginação
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity <Page<ProdutoDTO>> findPage(
			@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "page", defaultValue = "0") Integer page /*Pagina começa com 0*/,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage /*Linhas por páginas*/, 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy /*ordenar atributo por Id, Nome, etc*/, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction){
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias); // gerei a lista de inteiros 
		Page<Produto> list = service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		// percorrendo a lista, instanciando DTO correspondente
		Page<ProdutoDTO> listDto = list.map(obj /*dando apelido objeto*/ -> // aplicar operação map que vai percorrer para cada elemento da lista
		new ProdutoDTO(obj/*Passando o objeto (senão retorna nulo!*/));
		return ResponseEntity.ok().body(listDto);
	}
}
