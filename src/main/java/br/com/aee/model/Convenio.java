package br.com.aee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.aee.converter.BaseEntity;

@Entity
@Table(name = "convenio")
public class Convenio implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty
	private String nome;

	private String descricao;

	@NotEmpty
	private String tipo;

	@Column(name = "data_abertura")
	@Temporal(TemporalType.DATE)
	private Date dataAbertura;

	@Column(name = "data_vencimento")
	@Temporal(TemporalType.DATE)
	private Date dataVencimento;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Status status = Status.ATIVADO;
	
	private Double valor = 0.00;
	
	@NotBlank
	private String tipoConvenio;
	
	private String link;
	
	private Boolean exibe = true;
	
	@Column(name="foto")
	private String nomeArquivo;
	
	public Boolean getSemImagem() {
		return nomeArquivo != null;
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

	public String getNomeComIniciaisMaiuscula() {

		String sNova = "";
		for (String sNome : nome.toLowerCase().split(" ")) {
			if (!"".equals(sNome)) {
				if (!"".equals(sNova))
					sNova += " ";
				if (sNome.length() > 2) {
					sNova += sNome.substring(0, 1).toUpperCase() + sNome.substring(1);
				} else {
					sNova += sNome;
				}
			}
		}
		return sNova;
	}

	public void setNome(String nome) {
		this.nome = nome.toUpperCase();
	}

	public Status getStatus() {
		return status = Status.ATIVADO;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDataAbertura() {
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public Double getValor() {
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public String getTipoConvenio() {
		return tipoConvenio;
	}
	
	public void setTipoConvenio(String tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}
	
	public Boolean getExibe() {
		return exibe;
	}


	public void setExibe(Boolean exibe) {
		this.exibe = exibe;
	}
	

	public boolean isMensalidade() {
		return id == 1;
	}

	public boolean isContribuinte() {
		return id == 2;
	}

	public boolean isPlanoDeSaude() {
		return id == 3;
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
		Convenio other = (Convenio) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
