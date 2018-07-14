package br.com.aee.controller;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.aee.model.Fatura;
import br.com.aee.model.FluxoCaixa;
import br.com.aee.model.Mes;
import br.com.aee.model.TipoFluxo;
import br.com.aee.repository.FluxoCaixaRepository;
import br.com.aee.util.JsfUtil;

@Named("fluxoCaixaMB")
@ViewScoped
public class FluxoCaixaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FluxoCaixaRepository repository;

	private FluxoCaixa fluxoCaixa;

	private List<FluxoCaixa> entradas;

	private List<FluxoCaixa> saidas;

	private Integer mes;

	private Integer ano;

	@PostConstruct
	public void init() {
		fluxoCaixa = new FluxoCaixa();
		entradas = repository.findByTipoFluxo(this.getMesAtual(), this.getAnoAtual(), TipoFluxo.ENTRADA);
		saidas = repository.findByTipoFluxo(this.getMesAtual(), this.getAnoAtual(), TipoFluxo.SAIDA);
		mes = this.getMesAtual();
		ano = this.getAnoAtual();
		
		this.pesquisar();
	}
	
	public void pesquisar() {
		entradas = repository.findByTipoFluxo(mes, ano, TipoFluxo.ENTRADA);
		saidas = repository.findByTipoFluxo(mes, ano, TipoFluxo.SAIDA);
	}

	// Actions
	
	public void removerRegistro() {
		fluxoCaixa = repository.findBy(fluxoCaixa.getId());
		repository.remove(fluxoCaixa);
		repository.flush();
		fluxoCaixa = new FluxoCaixa();
		JsfUtil.info("Registro removido!");
		
		entradas = repository.findByTipoFluxo(this.getMesAtual(), this.getAnoAtual(), TipoFluxo.ENTRADA);
		saidas = repository.findByTipoFluxo(this.getMesAtual(), this.getAnoAtual(), TipoFluxo.SAIDA);
	}

	public void eventosDoMes() {
		repository.findByFluxoMes(mes, ano);
	}

	public List<Mes> getListaMes() {
		return Arrays.asList(Mes.values());
	}

	/**
	 * Salva entrada de pagamento fatura
	 */
	@Asynchronous
	public void entradaDeCaixa(Fatura fatura, Double valor, Calendar dataEvento, String associado) {
		try {
			fluxoCaixa.setTipoFluxo(TipoFluxo.ENTRADA);
			fluxoCaixa.setFaturaPaga(fatura);
			fluxoCaixa.setValor(valor);
			fluxoCaixa.setDataEvento(dataEvento);
			fluxoCaixa.setDescricao("Fatura " + associado);
			repository.save(fluxoCaixa);
			fluxoCaixa = new FluxoCaixa();
		} catch (Exception e) {
			JsfUtil.fatal("Algo deu errado, tente novamente!");
			e.getMessage();
		}
	}

	/**
	 * Salva entrada de pagamento fatura
	 */
	public void entradaDeCaixa() {
		try {
			fluxoCaixa.setTipoFluxo(TipoFluxo.ENTRADA);
			repository.save(fluxoCaixa);
			init();
			JsfUtil.info("Registro de entrada efetuado!");
		} catch (Exception e) {
			JsfUtil.fatal("Algo deu errado, tente novamente!");
			e.getMessage();
		}
	}

	/**
	 * Salva entrada de pagamento fatura
	 */
	public void saidaDeCaixa() {
		try {
			fluxoCaixa.setTipoFluxo(TipoFluxo.SAIDA);
			repository.save(fluxoCaixa);
			init();
			JsfUtil.info("Registro de saída efetuado!");
		} catch (Exception e) {
			JsfUtil.fatal("Algo deu errado, tente novamente!");
			e.getMessage();
		}
	}

	public Double getTotalEntrada() {
		Double total = 0.00;
		for (FluxoCaixa entrada : entradas) {
			total += entrada.getValor();
		}
		return total;
	}

	public Double getTotalSaida() {
		Double total = 0.00;
		for (FluxoCaixa saida : saidas) {
			total += saida.getValor();
		}
		return total;
	}

	public Double getTotal() {
		Double entrada = this.getTotalEntrada();
		Double saida = this.getTotalSaida();
		return entrada - saida;
	}

	public String getConverterTotal() {
		Locale ptBR = new Locale("pt", "BR");
		Double entrada = this.getTotalEntrada();
		Double saida = this.getTotalSaida();
		Double total = entrada - saida;
		return NumberFormat.getCurrencyInstance(ptBR).format(total);
	}

	public String getConverterTotalEntrada() {
		Locale ptBR = new Locale("pt", "BR");
		Double total = 0.00;
		for (FluxoCaixa entrada : entradas) {
			total += entrada.getValor();
		}
		return NumberFormat.getCurrencyInstance(ptBR).format(total);
	}

	public String getConverterTotalSaida() {
		Locale ptBR = new Locale("pt", "BR");
		Double total = 0.00;
		for (FluxoCaixa saida : saidas) {
			total += saida.getValor();
		}
		return NumberFormat.getCurrencyInstance(ptBR).format(total);
	}

	/**
	 * Nome por extenso mês da fatura
	 *
	 * @return
	 */
	public String getMesAtualPorExtenso() {
		Date hoje = new Date();
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(hoje);
		int mes = dataCal.get(Calendar.MONTH);

		String meses[] = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };

		return (meses[mes]);
	}

	public int getMesAtual() {
		Calendar hoje = Calendar.getInstance();
		hoje.get(Calendar.MONTH);
		int mes = hoje.get(Calendar.MONTH);

		return mes + 1;
	}

	public int getAnoAtual() {
		Date hoje = new Date();
		GregorianCalendar dataCal = new GregorianCalendar();
		dataCal.setTime(hoje);
		int ano = dataCal.get(Calendar.YEAR);

		return ano;
	}

	public FluxoCaixa getFluxoCaixa() {
		return fluxoCaixa;
	}

	public void setFluxoCaixa(FluxoCaixa fluxoCaixa) {
		this.fluxoCaixa = fluxoCaixa;
	}

	public List<FluxoCaixa> getEntradas() {
		return entradas;
	}

	public List<FluxoCaixa> getSaidas() {
		return saidas;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

}
