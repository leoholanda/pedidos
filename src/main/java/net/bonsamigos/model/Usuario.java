package net.bonsamigos.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import net.bonsamigos.enums.Status;
import net.bonsamigos.util.Auditoria;
import net.bonsamigos.util.Estilo;
import net.bonsamigos.util.NomeComInicialMaiscula;

@Entity
public class Usuario extends Auditoria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CPF
	@NotBlank
	private String cpf;

	@NotBlank
	private String nome;

	@NotBlank
	private String sobrenome;

	@Email
	private String email;

	@NotBlank
	private String senha;

	@NotNull
	@Column(length = 30)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@ManyToOne
	private Perfil perfil;
	
	public Usuario() {
		status = Status.AUTORIZADO;
	}
	
	public String getNomeCompleto() {
		String nomeCompleto = nome + " " + sobrenome;
		return NomeComInicialMaiscula.iniciaisMaiuscula(nomeCompleto);
	}
	
	public boolean isAtivo() {
		return Status.AUTORIZADO.equals(status);
	}
	
	public boolean isUsuarioExistente() {
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

	public String getCpf() {
		return cpf;
	}
	
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome.toUpperCase();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Perfil getPerfil() {
		return perfil;
	}
	
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
