package com.bruno.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.cursomc.dominio.Pedido;
import com.bruno.cursomc.services.PedidoService;

@RestController
@RequestMapping(value="/pedidos")
public class PedidoResources {
	
	@Autowired
	private PedidoService service;
	
	// para ser uma função tem que associar a ela o verbo do http
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> listar(@PathVariable Integer id){
		Pedido obj = service.buscar(id);
		return ResponseEntity.ok().body(obj);
	}
}
