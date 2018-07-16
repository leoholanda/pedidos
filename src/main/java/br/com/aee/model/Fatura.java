package br.com.aee.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "fatura")
public class Fatura implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "vencimento")
	@Temporal(TemporalType.DATE)
	private Date vencimento;

	@Column(name = "data_registro")
	@Temporal(TemporalType.DATE)
	private Calendar dataRegistro = Calendar.getInstance();

	@NotNull
	@Column(name = "valor_plano")
	private Double valorPlanoDeSaude;

	@NotNull
	@Column(name = "valor_mensalidade")
	private Double valorMensalidade;

	// TODO Mensalidade + Plano
	@NotNull
	@Column(name = "valor_total_gerado")
	private Double valorTotalGerado;

	// TODO Soma valorTotalGerado + multa por atraso + juros diario (se houver)
	@NotNull
	@Column(name = "valor_total")
	private Double valorTotal;

	// TODO Se houver residuo na fatura inserir na proxima
	@Column(name = "residuo", precision = 2, scale = 2)
	private Double valorDoResiduo;

	// TODO Valor do residuo descontado na fatura atual
	@Column(name = "residuo_descontado", precision = 2, scale = 2)
	private Double residuoDescontado;

	@Column(name = "valor_pago")
	private Double valorPago;

	@Column(name = "data_pagamento")
	private Calendar dataPagamento = Calendar.getInstance();

	@Column(name = "data_aplicacao_juros")
	@Temporal(TemporalType.DATE)
	private Calendar dataJuros = Calendar.getInstance();

	@Column(name = "multa_aplicada")
	private Boolean multaAplicada = false;

	@Column(name = "fatura_parcelada")
	private Boolean parcelada = false;

	private Integer parcelas = 0;

	@Column(name = "valor_servicos_adicionais")
	private Double valorServicosAdicionais;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Status status = Status.PENDENTE;

	@OneToMany(mappedBy = "fatura", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH }, fetch = FetchType.LAZY)
	private List<FaturaParcelada> faturaParcelada = new ArrayList<FaturaParcelada>();

	@NotNull
	@ManyToOne
	@JoinColumn(name = "plano")
	private Plano plano;

	public boolean isPago() {
		return Status.PAGO.equals(this.status);
	}

	public boolean isParcelado() {
		return Status.PARCELADO.equals(this.status);
	}

	public boolean isPendente() {
		return Status.PENDENTE.equals(this.status);
	}

	public boolean isAtrasado() {
		return Status.ATRASADO.equals(this.status);
	}

	public boolean isFechado() {
		return Status.FECHADO.equals(this.status);
	}

	public boolean isServicos() {
		if (valorServicosAdicionais != null) {
			return valorServicosAdicionais > 0.00;
		} else {
			return false;
		}
	}

	public int getDiasAtrasados() {
		Date dataHoje = new java.util.Date();

		int diasAtrasados;
		if (dataPagamento == null) {
			diasAtrasados = (int) ((dataHoje.getTime() - vencimento.getTime()) / 86400000L);

		} else {
			Date dataDoPagamento = dataPagamento.getTime();
			diasAtrasados = (int) ((dataDoPagamento.getTime() - vencimento.getTime()) / 86400000L);
		}
		return diasAtrasados;
	}

	public int getDiasAtrasadosParaDataInformada(Date vencimento) {
		Date dataHoje = new java.util.Date();

		int diasAtrasados;
		if (dataPagamento == null) {
			diasAtrasados = (int) ((dataHoje.getTime() - vencimento.getTime()) / 86400000L);

		} else {
			Date dataDoPagamento = dataPagamento.getTime();
			diasAtrasados = (int) ((dataDoPagamento.getTime() - vencimento.getTime()) / 86400000L);
		}
		return diasAtrasados;
	}

	public String getFormatacaoTextoDiaDeAtraso() {
		if (this.getDiasAtrasados() > 1) {
			return "Dias de atraso";
		} else {
			return "Dia de atraso";
		}
	}

	/**
	 * Usado provisoriamente para o ajuste de 10% em Março de 2018
	 * 
	 * @return
	 */
	public Double getAjuste() {
		return valorPlanoDeSaude * 0.10;
	}

	public String getConverterAjuste() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(this.getAjuste());
	}

	public String getConverterValorPlano() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(valorPlanoDeSaude);
	}

	public String getConverterValorServicos() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(valorServicosAdicionais);
	}

	public String getConverterMensalidade() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(valorMensalidade);
	}

	public String getConverterValorTotalGerado() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(valorTotalGerado);
	}

	public String getConverterValorTotal() {
		Locale ptBR = new Locale("pt", "BR");
		return NumberFormat.getCurrencyInstance(ptBR).format(valorTotal);
	}

	public String getConverterValorPago() {
		Locale ptBR = new Locale("pt", "BR");
		if (valorPago != null) {
			return NumberFormat.getCurrencyInstance(ptBR).format(valorPago);
		} else {
			return NumberFormat.getCurrencyInstance(ptBR).format(0.00);
		}
	}

	public String getConverterValorDoResiduo() {
		if (valorDoResiduo != null) {
			Locale ptBR = new Locale("pt", "BR");
			return NumberFormat.getCurrencyInstance(ptBR).format(valorDoResiduo);
		} else {
			return "0.00";
		}
	}

	public String getConverterResiduoDescontado() {
		Locale ptBR = new Locale("pt", "BR");
		if (residuoDescontado != null) {
			return NumberFormat.getCurrencyInstance(ptBR).format(residuoDescontado);
		} else {
			return NumberFormat.getCurrencyInstance(ptBR).format(0.00);
		}
	}

	public String getConverterDataVencimento() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = sdf.format(vencimento);
		return dataFormatada;
	}

	public String getConverterDataPagamento() {
		Date pagamento = dataPagamento.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dataFormatada = sdf.format(pagamento);
		return dataFormatada;
	}

	/**
	 * Nome por extenso mês da fatura
	 *
	 * @return
	 */
	public String getMesDaFatura() {
		if (vencimento != null) {
			GregorianCalendar dataCal = new GregorianCalendar();
			dataCal.setTime(vencimento);
			int mes = dataCal.get(Calendar.MONTH);
			String meses[] = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
					"Outubro", "Novembro", "Dezembro" };

			return (meses[mes]);
		} else {
			return null;
		}
	}

	/**
	 * Nome por extenso mês e ano da fatura
	 *
	 * @return
	 */
	public String getMesAnoDaFatura() {
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(vencimento);
		int mes = dataCal.get(Calendar.MONTH);
		int ano = dataCal.get(Calendar.YEAR);

		String meses[] = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };

		return (meses[mes]) + "/" + ano;
	}

	/**
	 * Informa o valor da multa por atraso
	 *
	 * @return
	 */
	public String getJurosAoDia() {
		Double juros = 0.00;
		Locale ptBR = new Locale("pt", "BR");

		if (multaAplicada) {
			juros = valorTotalGerado * 0.00033 * getDiasAtrasados();
			return NumberFormat.getCurrencyInstance(ptBR).format(juros);
		}
		return NumberFormat.getCurrencyInstance(ptBR).format(juros);
	}

	/**
	 * Informa o valor da multa por atraso
	 *
	 * @return
	 */
	public String getMultaPorAtraso() {
		Double multa = 0.00;
		Locale ptBR = new Locale("pt", "BR");

		if (multaAplicada) {
			multa = valorTotalGerado * 0.02;
			return NumberFormat.getCurrencyInstance(ptBR).format(multa);
		}
		return NumberFormat.getCurrencyInstance(ptBR).format(multa);
	}

	public String getTotalPago() {
		return status.equals(Status.PAGO) ? "TOTAL PAGO" : "TOTAL À PAGAR";
	}

	public String getQualResiduo() {
		if (valorDoResiduo != null) {
			return "CRÉDITO";
		} else {
			return "DÉBITO";
		}
	}

	public boolean isDebitoDeResiduo() {
		boolean resultado = false;
		if (valorDoResiduo != null) {
			resultado = valorDoResiduo > 0.00 ? true : false;
		}
		return resultado;
	}

	public boolean isNaoTem() {
		return valorDoResiduo != 0.0;
	}

	public boolean isNaoTemResiduo() {
		boolean resultado = false;
		if (valorDoResiduo != null) {
			resultado = valorDoResiduo < 0.00;
		}
		return resultado;
	}

	public boolean isResiduoParaProximaFatura() {
		boolean resultado = false;
		if (valorDoResiduo != null) {
			String resultado1 = String.format("%.2f", valorDoResiduo);
			if (resultado1.equalsIgnoreCase("-0,00") || resultado1.equalsIgnoreCase("0,00")) {
				resultado = false;
			} else {
				resultado = true;
			}
		}
		return resultado;
	}

	// public boolean isDebitoDeResiduoDescontado() {
	public boolean isDebitoFaturaAnterior() {
		boolean debito = false;
		if (residuoDescontado != null) {
			DecimalFormat formato = new DecimalFormat("#.##");
			residuoDescontado = Double.valueOf(formato.format(residuoDescontado));

			if (residuoDescontado > 0.00) {
				debito = true;
			} else {
				debito = false;
			}
		}
		return debito;
	}

	public boolean isTemResiduoDaFaturaAnterior() {
		boolean resultado = false;
		if(residuoDescontado != null) {
			String resultado1 = String.format("%.2f", residuoDescontado);
			
			if (resultado1.equalsIgnoreCase("-0,00") || resultado1.equalsIgnoreCase("0,00")) {
				resultado = false;
			} else {
				resultado = true;
			}
		}
		return resultado;
	}

	public boolean isNaoTemResiduoDescontado() {
		boolean resultado = false;
		if (residuoDescontado != null) {
			resultado = residuoDescontado < 0.00;
		}
		return resultado;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public Calendar getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Calendar dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Plano getPlano() {
		return plano;
	}

	public void setPlano(Plano plano) {
		this.plano = plano;
	}

	public Double getValorPlanoDeSaude() {
		return valorPlanoDeSaude;
	}

	public void setValorPlanoDeSaude(Double valorPlanoDeSaude) {
		this.valorPlanoDeSaude = valorPlanoDeSaude;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Calendar getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Calendar dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Boolean getMultaAplicada() {
		return multaAplicada;
	}

	public void setMultaAplicada(Boolean multaAplicada) {
		this.multaAplicada = multaAplicada;
	}

	public Double getValorMensalidade() {
		return valorMensalidade;
	}

	public void setValorMensalidade(Double valorMensalidade) {
		this.valorMensalidade = valorMensalidade;
	}

	public Double getValorTotalGerado() {
		return valorTotalGerado;
	}

	public void setValorTotalGerado(Double valorTotalGerado) {
		this.valorTotalGerado = valorTotalGerado;
	}

	public Calendar getDataJuros() {
		return dataJuros;
	}

	public void setDataJuros(Calendar dataJuros) {
		this.dataJuros = dataJuros;
	}

	public Boolean getParcelada() {
		return parcelada;
	}

	public void setParcelada(Boolean parcelada) {
		this.parcelada = parcelada;
	}

	public List<FaturaParcelada> getFaturaParcelada() {
		return faturaParcelada;
	}

	public void setFaturaParcelada(List<FaturaParcelada> faturaParcelada) {
		this.faturaParcelada = faturaParcelada;
	}

	public Integer getParcelas() {
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	public Double getValorDoResiduo() {
		return valorDoResiduo;
	}

	public void setValorDoResiduo(Double valorDoResiduo) {
		this.valorDoResiduo = valorDoResiduo;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public Double getResiduoDescontado() {
		return residuoDescontado;
	}

	public void setResiduoDescontado(Double residuoDescontado) {
		this.residuoDescontado = residuoDescontado;
	}

	public Double getValorServicosAdicionais() {
		return valorServicosAdicionais;
	}

	public void setValorServicosAdicionais(Double valorServicosAdicionais) {
		this.valorServicosAdicionais = valorServicosAdicionais;
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
		Fatura other = (Fatura) obj;
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
