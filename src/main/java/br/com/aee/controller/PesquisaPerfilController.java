package br.com.aee.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.aee.model.Perfil;
import br.com.aee.service.PerfilService;

@Named
@ViewScoped
public class PesquisaPerfilController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilService perfilService;

	private List<Perfil> listaPerfil;

	private Perfil perfil;

	@PostConstruct
	public void iniciar() {
		perfil = new Perfil();
		listaPerfil = perfilService.findAllOrderByNomeAsc();
	}
	
	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	public List<Perfil> getListaPerfil() {
		return listaPerfil;
	}
	
	public void setListaPerfil(List<Perfil> listaPerfil) {
		this.listaPerfil = listaPerfil;
	}

}
