package br.com.aee.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.com.aee.model.Modulo;
import br.com.aee.model.Perfil;
import br.com.aee.service.ModuloService;
import br.com.aee.service.PerfilService;
import br.com.aee.util.JsfUtil;
import br.com.aee.util.NegocioException;

@Named
@ViewScoped
public class CadastroPerfilController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilService perfilService;

	@Inject
	private ModuloService moduloService;

	private Perfil perfil;

	private DualListModel<Modulo> listaModulos;

	public CadastroPerfilController() {
		limpar();
	}
	
	@PostConstruct
	public void init() {
		List<Modulo> lista = moduloService.findAll();
		listaModulos = new DualListModel<>(lista, new ArrayList<>());

		limpar();
	}

	public void limpar() {
		perfil = new Perfil();
	}

	/**
	 * Salvar um novo perfil
	 */
	public void salvar() {
		try {
			perfil.setModulos(listaModulos.getTarget());
			perfil = perfilService.save(perfil);
			
			List<Modulo> lista = moduloService.findAll();
			listaModulos = new DualListModel<>(lista, new ArrayList<>());
			limpar();
			JsfUtil.info("Perfil Salvo com sucesso!");
		} catch (NegocioException e) {
			JsfUtil.error(e.getMessage());
		}
	}
	
	/**
	 * Remove Perfil Somente para Developer
	 */
	public void remover() {
		try {
			perfilService.remove(perfil);
			JsfUtil.info("Perfil " + perfil.getNome() + " removido!");
			limpar();
		} catch (NegocioException e) {
			JsfUtil.error(e.getMessage());
		}

	}
	
	/**
	 * Busca Bancada
	 * 
	 */
	public void buscarPerfil() {
		perfil = perfilService.load(perfil);
		
		List<Modulo> lista = moduloService.findAllOrderByNomeAsc();
		lista.removeAll(perfil.getModulos());
		listaModulos = new DualListModel<>(lista, new ArrayList<>(perfil.getModulos()));
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public boolean isEditando() {
		return this.perfil.getId() != null;
	}

	public DualListModel<Modulo> getListaModulos() {
		return listaModulos;
	}

	public void setListaModulos(DualListModel<Modulo> listaModulos) {
		this.listaModulos = listaModulos;
	}

}
