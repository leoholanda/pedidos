package net.bonsamigos.enums;

public enum Status {
	// Para Pedido
	ABERTO("Aberto"),
	AUTORIZADO("Autorizado"),
	ENTREGUE("Entregue"),
	
	// Para Unidade
	SOLICITADO("Solicitado"),
	DESATIVADO("Desativado"),
	ATIVADO("Ativado");
	
	
	private Status(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}

}
