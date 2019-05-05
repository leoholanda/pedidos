package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.model.Perfil;
import net.bonsamigos.service.PerfilService;
import net.bonsamigos.util.Paginacao;

@Named
@ViewScoped
public class PesquisaPerfilController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilService perfilService;

	private Perfil perfil;

	private List<Perfil> perfils;
	
	@PostConstruct
	public void init() {
		perfil = new Perfil();
		perfils = perfilService.findAll();
	}

	/**
	 * Verifica necessidade de paginação
	 * 
	 */
	public boolean isPaginator() {
		return perfils.size() > Paginacao.ROW ? true : false;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public List<Perfil> getPerfils() {
		return perfils;
	}

	public void setPerfils(List<Perfil> perfils) {
		this.perfils = perfils;
	}

}
