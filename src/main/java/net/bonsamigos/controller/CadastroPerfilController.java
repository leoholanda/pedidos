package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.model.DualListModel;

import net.bonsamigos.model.Modulo;
import net.bonsamigos.model.Perfil;
import net.bonsamigos.service.ModuloService;
import net.bonsamigos.service.PerfilService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.NegocioException;

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

	private final Log logger = LogFactory.getLog(this.getClass());

	public void inicializar() {
		
		List<Modulo> lista = moduloService.findAll();
		if (perfil == null) {
			perfil = new Perfil();
			listaModulos = new DualListModel<>(lista, new ArrayList<>());
		} else {
			lista.removeAll(perfil.getModulos());
			listaModulos = new DualListModel<>(lista, new ArrayList<>(perfil.getModulos()));
		}
	}

	public void salvar() {
		try {
			logger.info("### Salvando perfil ###" + perfil.toString());
			
			perfil.setModulos(listaModulos.getTarget());
			perfil = perfilService.save(perfil);
			List<Modulo> lista = moduloService.findAll();
			listaModulos = new DualListModel<>(lista, new ArrayList<>());
			
			perfilService.save(perfil);
			perfil = new Perfil();
			
			FacesUtil.info("Salvo com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}

	}

	/**
	 * Lista os modulos
	 * 
	 * @return
	 */
	public List<Modulo> getModulos() {
		return moduloService.findAll();
	}

	public void carregarPerfil() {
		perfil = perfilService.findBy(perfil.getId());
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	public DualListModel<Modulo> getListaModulos() {
		return listaModulos;
	}
	
	public void setListaModulos(DualListModel<Modulo> listaModulos) {
		this.listaModulos = listaModulos;
	}
}
