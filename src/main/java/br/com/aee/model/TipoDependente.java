package br.com.aee.model;

public enum TipoDependente {
	AGREGADO("Agregado"),
	CONJUGE_COMPANHEIRO("Conjuge/Companheiro(a)"),
	FILHO("Filho(a)"),
	FILHO_CURSANDO_NIVEL_SUPERIOR("Filho(a) cursando nível superior"),
	PAIS("Pais"),
	TITULAR("Titular"),
	TITULAR_MENOR("Menor com CPF do responsável");
	
	
	private String descricao;

	TipoDependente(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
