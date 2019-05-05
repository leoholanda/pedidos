package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Usuario;
import net.bonsamigos.service.UsuarioService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.Paginacao;

@Named
@ViewScoped
public class PesquisaUsuarioController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;

	private Usuario usuario;

	private List<Usuario> usuarios;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
		usuarios = usuarioService.findAll();
	}

	/**
	 * Desativa usuario
	 */
	public void desativar() {
		usuarioService.remove(usuario);
		FacesUtil.info("Usuário " + usuario.getNomeCompleto() + " desativado!");
	}
	
	public List<String> getListaStatus() {
		return Arrays.asList(Status.AUTORIZADO.getDescricao(), Status.DESATIVADO.getDescricao());
	}
	
	@Override
	public String toString() {
		return usuario.getCpf() + " | " + usuario.getNomeCompleto();
	}

	/**
	 * Verifica necessidade de paginação
	 * 
	 */
	public boolean isPaginator() {
		return usuarios.size() > Paginacao.ROW ? true : false;
	}

	public List<Usuario> getListaTodos() {
		return usuarioService.findAll();
	}

	public Usuario buscaPorCPF(String cpf) {
		return usuarioService.findByCpf(cpf);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
}
