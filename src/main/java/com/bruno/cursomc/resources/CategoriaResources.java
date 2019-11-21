package com.bruno.cursomc.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResources {
	// para ser uma função tem que associar a ela o verbo do http
	@RequestMapping(method = RequestMethod.GET)
	public String Listar() {
		return "Rest está funcionando!";
	}

}
