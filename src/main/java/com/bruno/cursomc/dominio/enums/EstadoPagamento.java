package com.bruno.cursomc.dominio.enums;

public enum EstadoPagamento {
	PENDENTE(1, "Pendente"),
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");
	
	private int cod;
	private String descricao;
	
	private EstadoPagamento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	public int getCod() {
		return cod;
	}
	public void setCod(int cod) {
		this.cod = cod;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	// metodo do tipo static para que a operação se possivel de serem executada mesmo sem instanciar objetos
	public static EstadoPagamento toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		
		// buscar todo objeto x nos valores possiveis do pagamento
		for(EstadoPagamento x : EstadoPagamento.values()) {
			// testar se o cod que veio no argumento comparar com metodo equal for igual com o qual estou procurando, vou retornar o EstadoPagamento
			if(cod.equals(x.getCod())) {
				return x;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
	
}
