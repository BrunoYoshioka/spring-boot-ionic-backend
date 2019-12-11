package com.bruno.cursomc.services;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.ItemPedido;
import com.bruno.cursomc.dominio.PagamentoComBoleto;
import com.bruno.cursomc.dominio.Pedido;
import com.bruno.cursomc.dominio.enums.EstadoPagamento;
import com.bruno.cursomc.repositories.ItemPedidoRepository;
import com.bruno.cursomc.repositories.PagamentoRepository;
import com.bruno.cursomc.repositories.PedidoRepository;
import com.bruno.cursomc.services.exceptions.ObjectNotFoundException;
// Classe responsável por realizar consultas no repositório.
@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	// incluir dependencia para pagamento repository
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	// criando uma operação para buscar a categoria por id
	// chamar a operação do acesso a dados que é o repository
//	public Pedido buscar(Integer id) {
//		Pedido obj = repo.findOne(id);
//		return obj;
//	}
	
	// O Spring Boot versão 2.X.X só aceita versão 8 em diante.
	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null); // garantir que seja realmente novo pedido
		obj.setInstante(new Date()); // cria uma nova data atual do pedido
		obj.setCliente(clienteService.find(obj.getCliente().getId())); // usar o id para buscar do banco o cliente
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		 // associação de mão dupla do pagamento que tem que conhecer o pedido dele
		obj.getPagamento().setPedido(obj);
		// se o meu pagamento for do tipo pagamento com boleto
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			// gero a data
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.PreencherPagamentoComBoleto(pagto, obj.getInstante()); // metodo que irá preencher a data de vencimento que será por enquanto uma semana depois do instante do pedido
		}
		obj = repo.save(obj); // salvar pedido no banco
		pagamentoRepository.save(obj.getPagamento()); // salvar pagamento no banco
		
		// salvar itens do pedido
		// percorrer todos os itens do pedido associado objeto do getItens
		for(ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId())); // associando produto que busco pelo banco
			ip.setPreco(ip.getProduto().getPreco()); // pegar o preço do produto
			ip.setPedido(obj); // associar esse item pedido com o pedido que estou inserindo
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.senderOrderConfirmationEmail(obj); // mandar o email quando acabar de ser inserido um novo pedido
		return obj;
	}
}