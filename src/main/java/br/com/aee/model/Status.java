package br.com.aee.model;

public enum Status {
	PENDENTE("Pendente"),
	PAGO("Pago"),
	PARCELADO("Parcelado"),
	FECHADO("Parcelado"),
	ATRASADO("Pendente"),
	
	ATIVADO("Permitido"),
	DESATIVADO("Desativado");
	
	
	private String descricao;

	Status(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
