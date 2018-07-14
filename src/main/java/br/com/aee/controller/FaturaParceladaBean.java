package br.com.aee.controller;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.aee.model.Fatura;
import br.com.aee.model.FaturaParcelada;
import br.com.aee.model.Status;
import br.com.aee.repository.FaturaParceladaRepository;
import br.com.aee.util.JsfUtil;

@Named("faturaParceladaMB")
@ViewScoped
public class FaturaParceladaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FaturaParceladaRepository repository;

	private FaturaParcelada parcela = new FaturaParcelada();

	private List<FaturaParcelada> listaFaturaParcelada;

	// Actions

	/**
	 * Salva faturaParcelada
	 */
	public void pagarParcela() {
		parcela.setStatus(Status.PAGO);
		repository.save(parcela);

		JsfUtil.info("Fatura paga com sucesso!");
		System.out.println(">>> Fatura paga com sucesso");

	}

	public int mesAtual() {
		Calendar hoje = Calendar.getInstance();
		hoje.get(Calendar.MONTH);
		int mes = hoje.get(Calendar.MONTH);

		return mes + 1;
	}

	public int anoAtual() {
		Calendar hoje = Calendar.getInstance();
		hoje.get(Calendar.YEAR);
		int ano = hoje.get(Calendar.YEAR);

		return ano;
	}

	// Listing

	/**
	 * Lista faturaParcelada por ordem alfabetica
	 * 
	 * @return
	 */
	public List<FaturaParcelada> getListaFaturaParcelada() {
		if (listaFaturaParcelada == null) {
			listaFaturaParcelada = repository.findByFaturaParceladaDoMesAtual(mesAtual(), anoAtual());
		}
		return listaFaturaParcelada;
	}
	
	public boolean isFaturaParcelada() {
		return repository.findByFaturaParceladaDoMesAtual(mesAtual(), anoAtual()).isEmpty();
	}
	
	public List<FaturaParcelada> getParcelasDaFatura(Fatura fatura) {
		return repository.findByParcelasDaFatura(fatura);
	}

	public FaturaParcelada getParcela() {
		return parcela;
	}
	
	public void setParcela(FaturaParcelada parcela) {
		this.parcela = parcela;
	}

}
