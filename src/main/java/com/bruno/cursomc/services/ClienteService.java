package com.bruno.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.Cliente;
import com.bruno.cursomc.dto.ClienteDTO;
import com.bruno.cursomc.repositories.ClienteRepository;
import com.bruno.cursomc.services.exceptions.DataIntegrityException;
import com.bruno.cursomc.services.exceptions.ObjectNotFoundException;

// Classe responsável por realizar consultas no repositório.
@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	// criando uma operação para buscar a categoria por id
	// chamar a operação do acesso a dados que é o repository
//	public Cliente buscar(Integer id) {
//		Cliente obj = repo.findOne(id);
//		return obj;
//	}
	
	// O Spring Boot versão 2.X.X só aceita versão 8 em diante.
	// Responsável por receber id e retornar cliente correspondente ao id 
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente update(Cliente obj) {
//		find(obj.getId()); // repete o mesmo procedimento do método find antes de salvar
//		return repo.save(obj);
		
		// Após alterar dados como nome e email pelo DTO, simplesmente salvou o cliente temporário que foi criado apartir do DTO
		Cliente newObj = find(obj.getId()); // Instanciar cliente apartir do banco de dados
		updateData(newObj, obj);
		return repo.save(newObj); // salva o newObj
	}

	
	public void delete(Integer id) {
		// caso id não existe, irá executar uma exceção do método find
		find(id);
		try {
			repo.deleteById(id);
		}
		// capturar a excessão de Integridade, lançando a excessão personalizada
		catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há entidades relacionadas");
		}
	}

	// serviço para buscar todas as categorias
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	// Função para retornar todas as categorias por paginação
	public Page<Cliente> findPage(Integer page /*Pagina começa com 0*/, Integer linesPerPage /*Linhas por páginas*/, 
			String orderBy /*ordenar atributo por Id, Nome, etc*/, String direction /*Ordenar por qual direção (Ascendente ou descendente)*/){
		//@SuppressWarnings("deprecation")
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	// Método auxiliar que instancia um cliente apartir de um DTO construo objeto categoria
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	// será tipo private pq ele é metodo auxiliar de dentro da classe, pois não tem motivo de ficar exposto pra fora
	private void updateData(Cliente newObj, Cliente obj) {
		// Essa variavel newObj que busquei todos os dados do banco irá atualizar para os novos valores fornecidos no obj
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
