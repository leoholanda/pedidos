package net.bonsamigos.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.model.Unidade;
import net.bonsamigos.security.Seguranca;
import net.bonsamigos.service.UnidadeService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.NegocioException;

@Named
@ViewScoped
public class CadastroUnidadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeService unidadeService;

	@Inject
	private Seguranca seguranca;
	
	private Unidade unidade;
	
	@PostConstruct
	public void init() {
		unidade = new Unidade();
	}
	
	public void salvar() {
		try {
			unidade.setArea(seguranca.getUsuarioLogado().getUsuario().getArea());
			unidadeService.save(unidade);
			
			unidade = new Unidade();
			FacesUtil.info("Salvo com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
		
	}
	
	public void carregarUnidade() {
		unidade = unidadeService.findBy(unidade.getId());
	}
	
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
}
