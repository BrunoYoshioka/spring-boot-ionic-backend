package com.bruno.cursomc.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.cursomc.dominio.Categoria;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResources {
	// para ser uma função tem que associar a ela o verbo do http
	@RequestMapping(method = RequestMethod.GET)
	public List<Categoria> listar(){
		//instanciando novos objetos
		Categoria cat1 = new Categoria(1, "Informática");
		Categoria cat2 = new Categoria(2, "Escritório");
		
		List<Categoria> lista = new ArrayList<Categoria>();
		lista.add(cat1);
		lista.add(cat2);
		
		return lista;
		
	}
}
