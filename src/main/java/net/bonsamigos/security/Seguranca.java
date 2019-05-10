package net.bonsamigos.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import net.bonsamigos.model.Usuario;

@Named
@RequestScoped
public class Seguranca {

	private ExternalContext externalContext = usuarioSessao();

	public String getNomeUsuario() {
		String nome = null;

		UsuarioSistema usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			nome = usuarioLogado.getUsuario().getNomeCompleto();
		}

		return nome;
	}

	public String getPerfilUsuario() {
		String perfil = "";

		UsuarioSistema usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			perfil = usuarioLogado.getUsuario().getPerfil().getNomeInicialMaiuscula();
		}

		return perfil;
	}

	public Usuario usuarioLogado() {
		UsuarioSistema usuarioLogado = getUsuarioLogado();
		return usuarioLogado.getUsuario();
	}

	@Produces
	@UsuarioLogado
	public UsuarioSistema getUsuarioLogado() {

		UsuarioSistema usuario = null;

		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext
				.getCurrentInstance().getExternalContext().getUserPrincipal();

		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();
		}

		return usuario;
	}

	public ExternalContext usuarioSessao() {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getExternalContext();
	}

	public boolean isDeveloper() {
		return externalContext.isUserInRole("DEVELOPER");
	}

	public boolean isUsuario() {
		return externalContext.isUserInRole("USUARIO");
	}
	
	public boolean isPerfil() {
		return externalContext.isUserInRole("PERFIL");
	}

	public boolean isProduto() {
		return externalContext.isUserInRole("PRODUTO");
	}
	
	public boolean isUnidade() {
		return externalContext.isUserInRole("UNIDADE");
	}
	
	public boolean isPesquisaPedido() {
		return externalContext.isUserInRole("CONSULTA_PEDIDO");
	}
	
	public boolean isCadastroPedido() {
		return externalContext.isUserInRole("CADASTRO_PEDIDO");
	}
}
