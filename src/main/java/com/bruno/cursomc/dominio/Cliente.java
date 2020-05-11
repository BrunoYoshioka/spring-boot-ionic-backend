package com.bruno.cursomc.dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.bruno.cursomc.dominio.enums.Perfil;
import com.bruno.cursomc.dominio.enums.TipoCliente;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	
	@Column(unique = true) // Faz o banco de dados garantir que não irá ter repetição com esse campo
	private String email;
	private String cpfOuCnpj;
	private Integer tipo; // Armazenar internamente com dado da classe e não um dado TipoCliente, então será do tipo Inteiro
	
	@JsonIgnore
	private String senha;
	
	@OneToMany(mappedBy = "cliente", cascade=CascadeType.ALL)
	private List<Endereco> enderecos = new ArrayList<>();
	
	// usar set, pois set é um conjunto de strings e não aceita repetição
	@ElementCollection
	@CollectionTable(name="TELEFONE")
	private Set<String> telefones = new HashSet<>();
	
	// Um atributo correspondente aos perfis do usuário a serem armazenados na base de dados
	@ElementCollection(fetch=FetchType.EAGER) // acrescentar fetch para garantir que sempre que buscar cliente no banco, busca perfil também
	@CollectionTable(name="PERFIS")
	private Set<Integer> perfis = new HashSet<>();
	
	@JsonIgnore // os pedidos do cliente não serão serializados
	@OneToMany(mappedBy = "cliente")
	private List<Pedido> pedidos = new ArrayList<>();

	private String imageUrl;
	
	public Cliente() {
		addPerfil(Perfil.CLIENTE); // todo cliente, no padrão é perfil cliente
	}

	public Cliente(Integer id, String nome, String email, String cpfouCnpj, TipoCliente tipo, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpfOuCnpj = cpfouCnpj;
		//this.tipo = tipo.getCod(); // Ele não pode ser nulo (NullPointerException) Teria que criar condicional ternária para isso
		this.tipo = (tipo == null) /*Caso vier nulo*/ ? null : /*Caso contrario*/ tipo.getCod();
		this.senha = senha;
		addPerfil(Perfil.CLIENTE);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpfouCnpj() {
		return cpfOuCnpj;
	}

	public void setCpfouCnpj(String cpfouCnpj) {
		this.cpfOuCnpj = cpfouCnpj;
	}

	public TipoCliente getTipo() {
		return TipoCliente.toEnum(tipo);
	}

	public void setTipo(TipoCliente tipo) {
		this.tipo = tipo.getCod();
	}
	
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public Set<Perfil> getPerfis() /*retornar os perfis correspondentes a cliente*/ {
		return perfis.stream().map(x /*para cada elemento x da coleção*/ -> Perfil.toEnum(x)).collect(Collectors.toSet()); //Percorrendo a coleção convertendo todos para o tipo enumerado perfil
	}
	
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Set<String> getTelefones() {
		return telefones;
	}

	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
