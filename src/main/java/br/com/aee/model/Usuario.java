package br.com.aee.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.aee.converter.StringExtended;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nome")
	@NotEmpty
	private String nome;

	@NotEmpty
	private String matricula;

	@NotEmpty
	private String senha;

	@Email
	private String email;

	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne
	@JoinColumn(name = "acesso")
	private Perfil perfil;

	@ManyToOne
	private Setor setor;

	public Usuario() {
		status = Status.PENDENTE;
	}

	public boolean isPendente() {
		return Status.PENDENTE.equals(status);
	}

	public boolean isDesativado() {
		return Status.DESATIVADO.equals(status);
	}

	public boolean isAtivado() {
		return Status.ATIVADO.equals(status);
	}
	
	public String getNomeComIniciaisMaiuscula() {
		return StringExtended.getNomeComIniciaisMaiuscula(nome);
	}

	// Perfil de usuario

	public boolean isAcessoParaTodos() {
		return isUsuarioAdminitrativo() || isUsuarioFinancas() || isDiretorFinancas() || isDiretorAdministrativo()
				|| isPresidente();
	}

	public boolean isAcessoParaDiretoresAndPresidente() {
		return isDiretorAdministrativo() || isDiretorFinancas() || isPresidente();
	}

	public boolean isAcessoParaSetorDeFinancas() {
		return isUsuarioFinancas() || isDiretorFinancas() || isPresidente();
	}

	public boolean isAcessoParaSetorAdministracao() {
		return isUsuarioAdminitrativo() || isDiretorAdministrativo() || isPresidente();
	}

	public boolean isAcessoParaSetorAdministracaoSemPresidente() {
		return isUsuarioAdminitrativo() || isDiretorAdministrativo();
	}

	public boolean isUsuarioAdminitrativo() {
		return perfil.getNome().equalsIgnoreCase("Administrativo");
	}

	public boolean isDiretorAdministrativo() {
		return perfil.getNome().equalsIgnoreCase("Diretor Administrativo");
	}

	public boolean isUsuarioFinancas() {
		return perfil.getNome().equalsIgnoreCase("Finanças");
	}

	public boolean isDiretorFinancas() {
		return perfil.getNome().equalsIgnoreCase("Diretor Financeiro");
	}

	public boolean isVicePresidente() {
		return perfil.getNome().equalsIgnoreCase("Vice-Presidente");
	}

	public boolean isPresidente() {
		return perfil.getNome().equalsIgnoreCase("Presidente") || isVicePresidente();
	}

	// Getters and Setters

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
		this.nome = nome.toUpperCase();
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula.toUpperCase();
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = StringExtended.toMD5(senha);
	}

	/**
	 * Novo usuario sempre com status pendente
	 *
	 * @return
	 */
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
