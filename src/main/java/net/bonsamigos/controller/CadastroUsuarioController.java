package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.model.Perfil;
import net.bonsamigos.model.Usuario;
import net.bonsamigos.service.PerfilService;
import net.bonsamigos.service.UsuarioService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.NegocioException;

@Named
@ViewScoped
public class CadastroUsuarioController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private PerfilService perfilService;

	private Usuario usuario;
	
	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}
	
	public void salvar() {
		try {
			usuarioService.save(usuario);
			
			usuario = new Usuario();
			FacesUtil.info("Salvo com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
		
	}
	
	public List<Perfil> getPerfils() {
		return perfilService.findAllByAtivado();
	}
	
	public void carregarUsuario() {
		usuario = usuarioService.findBy(usuario.getId());
	}
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
