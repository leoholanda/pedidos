package br.com.aee.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.aee.util.Adress;
import br.com.aee.model.FaixaEtaria;
import br.com.aee.repository.FaixaEtariaRepository;
import br.com.aee.util.JsfUtil;

@Named("faixaEtariaMB")
@ViewScoped
public class FaixaEtariaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FaixaEtariaRepository repository;

	private List<FaixaEtaria> listaFaixaEtaria;

	private FaixaEtaria faixaEtaria;

	private String searchValue;

	// Actions

	public void save() {
		if (validacaoPeriodo()) {
			repository.save(faixaEtaria);
			faixaEtaria = new FaixaEtaria();
			JsfUtil.info("Salvo com sucesso!");
		} else {
			JsfUtil.error("Período já existe");
		}
	}

	public void update() {
		repository.save(faixaEtaria);
		JsfUtil.info("Salvo com sucesso!");
	}

	public String desativar() {
		if (faixaEtaria != null) {
			faixaEtaria.setStatus(false);
			repository.save(faixaEtaria);
			JsfUtil.info("Removido com sucesso!");

			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}
		return Adress.PESQUISA_FAIXA;
	}

	/**
	 * Busca id da faixa
	 */
	public void buscar() {
		faixaEtaria = repository.findBy(this.getFaixaEtaria().getId());
	}

	// Listing

	/**
	 * Lista todas as faixas sem excecao
	 * 
	 * @return
	 */
	public List<FaixaEtaria> getListaFaixaEtaria() {
		if (listaFaixaEtaria == null) {
			listaFaixaEtaria = repository.findByPeriodo();
		}
		return listaFaixaEtaria;
	}

	/**
	 * Seleciona objeto na tabela via ajax
	 * 
	 * @param event
	 * @return
	 */
	public FaixaEtaria selecionaFaixaNaTabela(SelectEvent event) {
		return faixaEtaria;
	}

	// Validations

	public boolean isFaixaExiste() {
		return this.getFaixaEtaria().getId() != null;
	}

	/**
	 * Valida existencia de registro do periodo
	 * 
	 * @return
	 */
	public boolean validacaoPeriodo() {
		return repository.findAllByPeriodo(faixaEtaria.getPeriodoInicial(), faixaEtaria.getPeriodoFinal()).isEmpty();
	}

	public boolean isExisteFaixa() {
		return getFaixaEtaria().getId() != null;
	}

	public FaixaEtaria getFaixaEtaria() {
		if (faixaEtaria == null) {
			faixaEtaria = new FaixaEtaria();
		}
		return faixaEtaria;
	}

	public void setFaixaEtaria(FaixaEtaria faixaEtaria) {
		this.faixaEtaria = faixaEtaria;
	}

	public String getSearchValue() {
		return searchValue;
	}
}
