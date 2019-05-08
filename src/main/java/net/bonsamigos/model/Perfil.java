package net.bonsamigos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import net.bonsamigos.enums.Status;
import net.bonsamigos.util.Auditoria;
import net.bonsamigos.util.NomeComInicialMaiscula;

@Entity
public class Perfil extends Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(length = 100)
	private String nome;
	
	@NotNull
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "perfil_modulo", joinColumns = {
			@JoinColumn(name = "perfil", foreignKey = @ForeignKey(name = "perfil")) }, inverseJoinColumns = {
					@JoinColumn(name = "modulo", foreignKey = @ForeignKey(name = "modulo")) })
	private List<Modulo> modulos = new ArrayList<>();
	
	public Perfil() {
		status = Status.ATIVADO;
	}

	public String getNomeInicialMaiuscula() {
		return NomeComInicialMaiscula.iniciaisMaiuscula(nome);
	}

	public boolean isUnidadeExistente() {
		return id == null ? false : true;
	}

	@Transient
	public String getTitulo() {
		return id == null ? "Cadastrar" : "Editar";
	}
	
	@Override
	public String toString() {
		return nome + " " + modulos.size();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Perfil other = (Perfil) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
