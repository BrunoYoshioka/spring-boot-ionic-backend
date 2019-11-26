package com.bruno.cursomc.dominio.enums;

public enum TipoCliente {
	
	PESSOAFISICA(1, "Pessoa Jurídica"),
	PESSOAJURIDICA(2, "Pessoa Física");
	
	private int cod;
	private String descricao;
	
	private TipoCliente(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	public String getDescricao() {
		return descricao;
	}
	
	// metodo do tipo static para que a operação se possivel de serem executada mesmo sem instanciar objetos
	public static TipoCliente toEnum(Integer cod) {
		
		if (cod == null) {
			return null;
		}
		
		// buscar todo objeto x nos valores possiveis do cliente 
		for(TipoCliente x : TipoCliente.values()) {
			// testar se o cod que veio no argumento comparar com metodo equal for igual com o qual estou procurando, vou retornar o TipoCliente
			if(cod.equals(x.getCod())){
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
