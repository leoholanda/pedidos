package br.com.aee.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fatura_parcelada")
public class FaturaParcelada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "numero")
	private Integer numeroDaParcela;

	@NotNull
	@Column(name = "valor_total")
	private Double valorTotal;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDENTE;

	@NotNull
	@Column(name = "vencimento")
	@Temporal(TemporalType.DATE)
	private Date vencimento;

	@ManyToOne
	@JoinColumn(name = "fatura_id")
	private Fatura fatura;

	public String getConverterValorTotal() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(valorTotal);
	}
	
	public String getConverterDataVencimento() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = sdf.format(vencimento);
		return dataFormatada;
	}
	
	public boolean isPendente() {
		return Status.PENDENTE.equals(this.status);
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumeroDaParcela() {
		return numeroDaParcela;
	}

	public void setNumeroDaParcela(Integer numeroDaParcela) {
		this.numeroDaParcela = numeroDaParcela;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FaturaParcelada other = (FaturaParcelada) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
