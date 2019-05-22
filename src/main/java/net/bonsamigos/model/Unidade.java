package net.bonsamigos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import net.bonsamigos.enums.Area;
import net.bonsamigos.enums.Status;
import net.bonsamigos.util.Auditoria;
import net.bonsamigos.util.Estilo;
import net.bonsamigos.util.NomeComInicialMaiscula;

@Entity
public class Unidade extends Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private Long codigo;

	@NotBlank
	@Column(length = 60, unique = true)
	private String nome;
	
	private String endereco;

	@NotNull
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private Area area;

	@NotNull
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	public Unidade() {
		status = Status.ATIVADO;
	}
	
	public String getNomeInicialMaiuscula() {
		return NomeComInicialMaiscula.iniciaisMaiuscula(nome);
	}
	
	public String getCodigoCompleto() {
		String format = String.format ("%02d", codigo);
		return format;
	}
	
	public boolean isAtivo() {
		return status.equals(Status.ATIVADO);
	}
	
	public boolean isUnidadeExistente() {
		return id == null ? false : true;
	}
	
	@Transient
	public String getTitulo() {
		return id == null ? "Cadastrar" : "Editar";
	}
	
	public String getCorParaStatus() {
		return Estilo.corParaStatus(status);
	}
	
	public String getIconeParaStatus() {
		return Estilo.iconeParaStatus(status);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
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
		Unidade other = (Unidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
