package net.bonsamigos.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.bonsamigos.enums.Area;
import net.bonsamigos.model.Usuario;
import net.bonsamigos.security.Seguranca;
import net.bonsamigos.service.UsuarioService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.NegocioException;

@Named
@RequestScoped
public class MenuController implements Serializable {

	private static final long serialVersionUID = 1L;
	private final Log logger = LogFactory.getLog(this.getClass());
	
	@Inject
	private UsuarioService usuarioService;
	
	@Inject
	private Seguranca seguranca;
	
	private static final String CONTEXT = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
	
	public void inicial() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(CONTEXT + "/pages/home/pagina-inicial.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Não foi possível atender sua solicitação. Tente novamente!");
		}
	}
	
	public void unidade() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(CONTEXT + "/pages/unidade/pesquisa-unidade.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Não foi possível atender sua solicitação. Tente novamente!");
		}
	}
	
	public void produto() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(CONTEXT + "/pages/produto/pesquisa-produto.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Não foi possível atender sua solicitação. Tente novamente!");
		}
	}
	
	public void pedido() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(CONTEXT + "/pages/pedido/cadastro-pedido.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Não foi possível atender sua solicitação. Tente novamente!");
		}
	}
	
	public void perfil() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(CONTEXT + "/pages/perfil/pesquisa-perfil.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Não foi possível atender sua solicitação. Tente novamente!");
		}
	}
	
	public void usuario() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(CONTEXT + "/pages/usuario/pesquisa-usuario.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Não foi possível atender sua solicitação. Tente novamente!");
		}
	}
	
	public void logout() throws NegocioException {
		try {
			logger.debug("### FAZENDO LOGOUT ###");
			Usuario usuario = seguranca.getUsuarioLogado().getUsuario();
			usuario.setArea(null);
			usuarioService.save(usuario);
			FacesContext.getCurrentInstance().getExternalContext().redirect(CONTEXT + "/logout");
		} catch (IOException e) {
			e.printStackTrace();
			FacesUtil.error("Não foi possível atender sua solicitação. Tente novamente!");
		}
	}
	
	/**
	 * Selecionar educação
	 * @param area
	 * @throws NegocioException
	 */
	public void selecionarEducacao() throws NegocioException {
		logger.debug("### Selecionando Saúde ###");
		Usuario usuario = seguranca.getUsuarioLogado().getUsuario(); 
		usuario.setArea(Area.EDUCACAO);
		usuarioService.save(usuario);
		logger.debug("### Usuário ### " + usuario.getNomeCompleto());
		
		this.pedido();
	}
	
	/**
	 * Selecionar saúde
	 * @param area
	 * @throws NegocioException
	 */
	public void selecionarSaude() throws NegocioException {
		logger.debug("### Selecionando Saúde ###");
		Usuario usuario = seguranca.getUsuarioLogado().getUsuario();
		usuario.setArea(Area.SAUDE);
		usuarioService.save(usuario);
		logger.debug("### Usuário ### " + usuario.getNomeCompleto());
		
		this.pedido();
	}

}
