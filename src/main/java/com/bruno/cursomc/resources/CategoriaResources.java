package com.bruno.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bruno.cursomc.dominio.Categoria;
import com.bruno.cursomc.dto.CategoriaDTO;
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
	
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	// Mostrar todas as categorias
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity <List<CategoriaDTO>> findAll(){
		List<Categoria> list = service.findAll();
		// percorrendo a lista, instanciando DTO correspondente
		List<CategoriaDTO> listDto = list.stream().map(obj /*dando apelido objeto*/ -> // aplicar operação map que vai percorrer para cada elemento da lista
			new CategoriaDTO(obj/*Passando o objeto (senão retorna nulo!*/)).collect(Collectors.toList()); 
		return ResponseEntity.ok().body(listDto);
	}
	
	// Mostrar todas as categorias
		@RequestMapping(value="/page", method = RequestMethod.GET)
		public ResponseEntity <Page<CategoriaDTO>> findPage(
				@RequestParam(value = "page", defaultValue = "0") Integer page /*Pagina começa com 0*/, 
				@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage /*Linhas por páginas*/, 
				@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy /*ordenar atributo por Id, Nome, etc*/, 
				@RequestParam(value = "direction", defaultValue = "ASC") String direction){
			Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
			// percorrendo a lista, instanciando DTO correspondente
			Page<CategoriaDTO> listDto = list.map(obj /*dando apelido objeto*/ -> // aplicar operação map que vai percorrer para cada elemento da lista
				new CategoriaDTO(obj/*Passando o objeto (senão retorna nulo!*/));
			return ResponseEntity.ok().body(listDto);
		}
}
