package net.bonsamigos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import net.bonsamigos.enums.Area;
import net.bonsamigos.util.NomeComInicialMaiscula;

@Entity
public class Unidade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long codigo;

	@NotBlank
	@Column(length = 60, unique = true)
	private String nome;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Area area;
	
	public String getNomeInicialMaiuscula() {
		return NomeComInicialMaiscula.iniciaisMaiuscula(nome);
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
