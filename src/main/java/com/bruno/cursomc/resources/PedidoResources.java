package com.bruno.cursomc.resources;

import java.net.URI;

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

import com.bruno.cursomc.dominio.Pedido;
import com.bruno.cursomc.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResources {

	@Autowired
	private PedidoService service;

	// para ser uma função tem que associar a ela o verbo do http
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido obj = service.find(id);
		return ResponseEntity.ok().body(obj);
	}

	// Incluir operação de inserir pedido
	@RequestMapping(method = RequestMethod.POST)
	// Será do tipo ResponseEntity ou seja resposta do Http
	// @RequestBody faz que o json seja convertido em java automático
	public ResponseEntity<Void> insert(
			/* Para que o objeto seja validado antes de passar pra frente */ @Valid @RequestBody Pedido obj) {
		obj = service.insert(obj); // obj recebe um serviço onde insere a nova categoria no banco.
		// pegar o novo id e fornecer como argumento da URI
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest() // pegar URI
				.path("/{id}"/* Acrescentando id do novo recurso */).buildAndExpand(obj.getId()).toUri();

		return ResponseEntity.created(uri).build(); // gerar 201 como argumento de resposta
	}

	// O sistema informa os nomes de todas categorias ordenadamente.
	// Buscar todas as categorias com paginação
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Pedido>> findPage(
			@RequestParam(value = "page", defaultValue = "0") Integer page /* Pagina começa com 0 */,
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage /* Linhas por páginas */,
			@RequestParam(value = "orderBy", defaultValue = "Instante") String orderBy /*ordenar atributo por Id, Data (Instante),etc*/,
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) /*Aparecer os últimos pedidos realizados*/ {
		Page<Pedido> list = service.findPage(page, linesPerPage, orderBy, direction);
		return ResponseEntity.ok().body(list);
	}
}
