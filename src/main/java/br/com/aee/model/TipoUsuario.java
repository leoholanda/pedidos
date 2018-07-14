package br.com.aee.model;

public enum TipoUsuario {
	ADMINISTRATIVO("Administrativo"),
	FINANCAS("Finanças"),
	DIRETOR_ADMINISTRATIVO("Diretor Administrativo"),
	DIRETOR_FINANCAS("Diretor Financeiro"),
	PRESIDENTE("Presidente");
	
	private String descricao;

	TipoUsuario(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
