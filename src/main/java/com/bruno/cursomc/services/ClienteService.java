package com.bruno.cursomc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bruno.cursomc.dominio.Cidade;
import com.bruno.cursomc.dominio.Cliente;
import com.bruno.cursomc.dominio.Endereco;
import com.bruno.cursomc.dominio.enums.Perfil;
import com.bruno.cursomc.dominio.enums.TipoCliente;
import com.bruno.cursomc.dto.ClienteDTO;
import com.bruno.cursomc.dto.ClienteNewDTO;
import com.bruno.cursomc.repositories.ClienteRepository;
import com.bruno.cursomc.repositories.EnderecoRepository;
import com.bruno.cursomc.security.UserSS;
import com.bruno.cursomc.services.exceptions.AuthorizationException;
import com.bruno.cursomc.services.exceptions.DataIntegrityException;
import com.bruno.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.web.multipart.MultipartFile;

// Classe responsável por realizar consultas no repositório.
@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private S3Service s3Service;

	@Autowired
	private ImageService imageService;

	@Value("${img.prefix.client.profile}")
	private String prefix;

	@Value("${img.profile.size}")
	private Integer size;
	
	// criando uma operação para buscar a categoria por id
	// chamar a operação do acesso a dados que é o repository
//	public Cliente buscar(Integer id) {
//		Cliente obj = repo.findOne(id);
//		return obj;
//	}
	
	// O Spring Boot versão 2.X.X só aceita versão 8 em diante.
	// Responsável por receber id e retornar cliente correspondente ao id 
	public Cliente find(Integer id) {
		
		// verificação: se o cliente logado não for ADMIN e não for o cliente do id solicitado, lançar uma exceção
		UserSS user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
			
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional // usar essa anotação transactional para salvar tanto o cliente quanto endereços na mesma no banco de dados
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos()); // detalhe, antes tem q implementar cli.getEnderecos().add(end); no método fromDTO
		return obj;
	}
	
	
	
	public Cliente update(Cliente obj) {
//		find(obj.getId()); // repete o mesmo procedimento do método find antes de salvar
//		return repo.save(obj);
		
		// Após alterar dados como nome e email pelo DTO, simplesmente salvou o cliente temporário que foi criado apartir do DTO
		Cliente newObj = find(obj.getId()); // Instanciar cliente apartir do banco de dados
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
//			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionadas");
//		}
//	}
	
	public void delete(Integer id){

		Cliente cliente = find(id);
		
		if (cliente.getPedidos().isEmpty()) {
			repo.deleteById(id);
		}
		else {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionadas");
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
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(),
				/*Convertendo para o tipo cliente*/ TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()) /* a senha digitada pelo usuário será encodada com algorítmo BCrypt */);
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli /*O endereço já conhece os clientes*/, cid);
		// Incluir Endereço end na lista do cliente
		cli.getEnderecos().add(end); // O cliente irá conhecer os endereços
		cli.getTelefones().add(objDto.getTelefone1()); // O cliente irá conhecer pelo menos um telefone obrigatoriamente
		if(objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2()); // se o telefone 2 for diferente de nulo, eu adiciono telefone 2 no cliente
		}
		if(objDto.getTelefone3()!=null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		return cli;
	}
	
	// será tipo private pq ele é metodo auxiliar de dentro da classe, pois não tem motivo de ficar exposto pra fora
	private void updateData(Cliente newObj, Cliente obj) {
		// Essa variavel newObj que busquei todos os dados do banco irá atualizar para os novos valores fornecidos no obj
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}

	// upload de uma foto do perfil do cliente
	public URI uploadProfilePicture(MultipartFile multipartFile){

		UserSS user = UserService.authenticated();
		if (user == null){
			throw new AuthorizationException("Acesso negado");
		}

		// Extrair o jpg apartir do arquivo enviado na requisição
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		jpgImage = imageService.cropSquare(jpgImage); // recortar a imagem de forma q fica quadrada
		jpgImage = imageService.resize(jpgImage, size);

		String fileName = prefix + user.getId() + ".jpg";

		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
	}
}
