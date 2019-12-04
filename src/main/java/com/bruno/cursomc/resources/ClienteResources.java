package com.bruno.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import com.bruno.cursomc.dominio.Cliente;
import com.bruno.cursomc.dto.ClienteDTO;
import com.bruno.cursomc.dto.ClienteNewDTO;
import com.bruno.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/clientes")
public class ClienteResources {
	
	@Autowired
	private ClienteService service;
	
	// para ser uma função tem que associar a ela o verbo do http
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id){
		Cliente obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}
	
	// Incluir operação de inserir cliente
	@RequestMapping(method = RequestMethod.POST)
	// Será do tipo ResponseEntity ou seja resposta do Http
	// @RequestBody faz que o json seja convertido em java automático
	public ResponseEntity<Void> insert(/*Para que o objeto dto seja validado antes de passar pra frente*/ @Valid @RequestBody ClienteNewDTO objDto){
		// convertendo objeto para entity
		Cliente obj = service.fromDTO(objDto);
		obj = service.insert(obj); // obj recebe um serviço onde insere a novo cliente no banco.
		//pegar o novo id e fornecer como argumento da URI
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri();
		
		return ResponseEntity.created(uri).build(); // gerar 201 como argumento de resposta
	}
	
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	//Retornar corpo vazio quando atualização ocorre com sucesso
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
		// convertendo objeto para entity
		Cliente obj = service.fromDTO(objDto);
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
	public ResponseEntity <List<ClienteDTO>> findAll(){
		List<Cliente> list = service.findAll();
		// percorrendo a lista, instanciando DTO correspondente
		List<ClienteDTO> listDto = list.stream().map(obj /*dando apelido objeto*/ -> // aplicar operação map que vai percorrer para cada elemento da lista
			new ClienteDTO(obj/*Passando o objeto (senão retorna nulo!*/)).collect(Collectors.toList()); 
		return ResponseEntity.ok().body(listDto);
	}
	
	// Mostrar todas as categorias
	@RequestMapping(value="/page", method = RequestMethod.GET)
	public ResponseEntity <Page<ClienteDTO>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page /*Pagina começa com 0*/, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage /*Linhas por páginas*/, 
			@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy /*ordenar atributo por Id, Nome, etc*/, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction){
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		// percorrendo a lista, instanciando DTO correspondente
		Page<ClienteDTO> listDto = list.map(obj /*dando apelido objeto*/ -> // aplicar operação map que vai percorrer para cada elemento da lista
			new ClienteDTO(obj/*Passando o objeto (senão retorna nulo!*/));
		return ResponseEntity.ok().body(listDto);
	}
}
