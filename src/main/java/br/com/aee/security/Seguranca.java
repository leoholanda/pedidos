package br.com.aee.security;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.aee.model.Usuario;

@Named
@RequestScoped
public class Seguranca {

	private ExternalContext externalContext = usuarioSessao();

	public String getNomeUsuario() {
		String nome = null;

		UsuarioSistema usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			nome = usuarioLogado.getUsuario().getNomeComIniciaisMaiuscula();
		}

		return nome;
	}

	public String getPerfilUsuario() {
		String perfil = "";

		UsuarioSistema usuarioLogado = getUsuarioLogado();

		if (usuarioLogado != null) {
			perfil = usuarioLogado.getUsuario().getPerfil().getNome();
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

	public boolean isGerenciarBeneficiario() {
		return externalContext.isUserInRole("CONSULTAR_ASSOCIADO");
	}
	
	public boolean isConsultarBeneficiario() {
		return externalContext.isUserInRole("GERENCIAR_ASSOCIADO");
	}

	public boolean isGerenciarConvenio() {
		return externalContext.isUserInRole("GERENCIAR_CONVENIOS_SERVICOS");
	}

	public boolean isGerenciarFatura() {
		return externalContext.isUserInRole("EDITAR_FATURA");
	}
	
	public boolean isConsultarFatura() {
		return externalContext.isUserInRole("CONSULTAR_FATURA");
	}
	
	public boolean isGerenciarFaixaEtaria() {
		return externalContext.isUserInRole("GERENCIAR_FAIXA_ETARIA");
	}
	
	public boolean isConsultarFaixaEtaria() {
		return externalContext.isUserInRole("CONSULTAR_FAIXA_ETARIA");
	}
}
