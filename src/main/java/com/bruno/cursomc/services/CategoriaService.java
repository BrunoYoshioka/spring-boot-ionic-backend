package com.bruno.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.Categoria;
import com.bruno.cursomc.dto.CategoriaDTO;
import com.bruno.cursomc.repositories.CategoriaRepository;
import com.bruno.cursomc.services.exceptions.DataIntegrityException;
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
//		find(obj.getId()); // repete o mesmo procedimento do método find antes de salvar
//		return repo.save(obj);
		
		// Após alterar dados como nome pelo DTO, simplesmente salvou a categoria temporário que foi criado apartir do DTO
		Categoria newObj = find(obj.getId()); // Instanciar categoria apartir do banco de dados
		updateData(newObj, obj);
		return repo.save(newObj); // salva o newObj
	}
	
	// Esse método foi comentada porque excluia categoria mesmo tendo produtos relacionados.
//	public void delete(Integer id) {
//		// caso id não existe, irá executar uma exceção do método find
//		find(id);
//		try {
//			repo.deleteById(id);
//		}
//		// capturar a excessão de Integridade, lançando a excessão personalizada
//		catch (DataIntegrityViolationException e) {
//			throw new DataIntegrityException("Não é possível excluir categoria que possui produtos");
//		}
//	}
	
	public void delete(Integer id){

		Categoria categoria = find(id);
		
		if (categoria.getProdutos().isEmpty()) {
			repo.deleteById(id);
		} else {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}

	// serviço para buscar todas as categorias
	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	// Função para retornar todas as categorias por paginação
	public Page<Categoria> findPage(Integer page /*Pagina começa com 0*/, Integer linesPerPage /*Linhas por páginas*/, 
			String orderBy /*ordenar atributo por Id, Nome, etc*/, String direction /*Ordenar por qual direção (Ascendente ou descendente)*/){
		//@SuppressWarnings("deprecation")
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	// Método auxiliar que instancia uma categoria apartir de um DTO construo objeto categoria
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	// será tipo private pq ele é metodo auxiliar de dentro da classe, pois não tem motivo de ficar exposto pra fora
	private void updateData(Categoria newObj, Categoria obj) {
		// Essa variavel newObj que busquei todos os dados do banco irá atualizar para os novos valores fornecidos no obj
		newObj.setNome(obj.getNome());
	}
}
