package net.bonsamigos.util;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class Auditoria {

	@Column(name = "data_criacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataCriacao;

	@Column(name = "data_atualizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataAtualizacao;

	@PrePersist
	public void prePersist() {
		dataCriacao = Calendar.getInstance();
	}

	@PreUpdate
	public void preUpdate() {
		dataAtualizacao = Calendar.getInstance();
	}

	public Calendar getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Calendar dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public Calendar getDataAtualizacao() {
		return dataAtualizacao;
	}
	
	public void setDataAtualizacao(Calendar dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
}
