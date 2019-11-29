package com.bruno.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bruno.cursomc.dominio.Categoria;
import com.bruno.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResources {
	
	@Autowired
	private CategoriaService service;
	
	// para ser uma função tem que associar a ela o verbo do http
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Categoria> find(@PathVariable Integer id){
		Categoria obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	// Incluir operação de inserir categoria
	@RequestMapping(method = RequestMethod.POST)
	// Será do tipo ResponseEntity ou seja resposta do Http
	// @RequestBody faz que o json seja convertido em java automático
	public ResponseEntity<Void> insert(@RequestBody Categoria obj){
		obj = service.insert(obj); // obj recebe um serviço onde insere a nova categoria no banco.
		//pegar o novo id e fornecer como argumento da URI
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build(); // gerar 201 como argumento de resposta
	}
	
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	//Retornar corpo vazio quando atualização ocorre com sucesso
	public ResponseEntity<Void> update(@RequestBody Categoria obj, @PathVariable Integer id){
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
}
