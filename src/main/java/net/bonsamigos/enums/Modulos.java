package net.bonsamigos.enums;

public enum Modulos {
	UNIDADE("Unidade"),
	USUARIO("Usuário"),
	PRODUTO("Produto"),
	PERFIL("Perfil"),
	PEDIDO("Pedido");
	
	private Modulos(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	

}
