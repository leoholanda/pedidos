package net.bonsamigos.enums;

public enum Area {
	SAUDE("Saúde"),
	EDUCACAO("Educação");
	
	private Area(String descricao) {
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
