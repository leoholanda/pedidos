package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.model.Unidade;
import net.bonsamigos.service.UnidadeService;

@Named
@ViewScoped
public class PesquisaUnidadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeService unidadeService;

	private Unidade unidade;

	private List<Unidade> unidades;

	@PostConstruct
	public void init() {
		unidade = new Unidade();
		unidades = unidadeService.findAll();
	}

	public List<Unidade> getListaTodos() {
		return unidadeService.findAll();
	}

	public Unidade buscaPorCodigo(Long codigo) {
		return unidadeService.findById(codigo);
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public List<Unidade> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidade> unidades) {
		this.unidades = unidades;
	}

}
