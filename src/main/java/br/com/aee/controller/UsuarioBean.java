package br.com.aee.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.aee.converter.StringExtended;
import br.com.aee.model.Perfil;
import br.com.aee.model.Status;
import br.com.aee.model.Usuario;
import br.com.aee.repository.PerfilRepository;
import br.com.aee.repository.UsuarioRepository;
import br.com.aee.security.Seguranca;
import br.com.aee.thread.EnviaEmailSenhaThread;
import br.com.aee.util.Adress;
import br.com.aee.util.JsfUtil;

@Named("usuarioMB")
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository repository;

	@Inject
	private PerfilRepository perfil;

	@Inject
	private Seguranca seguranca;

	private Usuario usuario;

	private List<Usuario> listaUsuarios;

	private String searchValue;

	private String senhaValue;

	private String senhaAtual;

	private String novaSenhaMeuPerfil;

	@PostConstruct
	public void init() {
		usuario = new Usuario();
	}

	// Actions

	/**
	 * envia solicitacao
	 */
	public void save() {
		try {
			if (validacaoMatriculaUsuario()) {
				repository.save(usuario);
				usuario = new Usuario();

				JsfUtil.info("Solicitação enviada com sucesso!");

			} else {
				JsfUtil.error("Matrícula já encontra-se em nossos dados");
			}

		} catch (Exception e) {
			e.getMessage();
		}
	}

	public void update() {
		try {
			repository.save(usuario);
			JsfUtil.info("Usuário atualizado com sucesso!");

		} catch (Exception e) {
			e.getMessage();
		}
	}

	public String updateForPassword() {
		System.out.println(">>> Senha: " + senhaValue);
		// if (senhaValue.equals(usuario.getSenha())) {
		repository.save(usuario);
		JsfUtil.info("Usuário atualizado com sucesso!");
		return Adress.DASHBOARD;
		// } else {
		// JsfUtil.error("Senha incorreta");
		// return null;
		// }
	}

	public void mudarSenha() {
		boolean mudarSenha = StringExtended.toMD5(senhaAtual).equals(usuario.getSenha());
		if (mudarSenha) {
			usuario.setSenha(novaSenhaMeuPerfil);
			repository.save(usuario);
			JsfUtil.info("Usuário atualizado com sucesso!");
		} else {
			JsfUtil.error("Senha atual inválida!");
		}

	}

	/**
	 * Autorizar solicitação do usuario
	 */
	public void autorizarAcesso() {

		if (usuario.getId() != null) {

			usuario.setStatus(Status.ATIVADO);
			repository.save(usuario);

			usuario = new Usuario();
			JsfUtil.info("Usuário autorizado");
		}
	}

	/**
	 * Exlui solicitacao do banco de dados
	 */
	public void naoAutorizarAcesso() {
		if (usuario.getId() != null) {
			repository.remove(usuario);

			JsfUtil.info("Solicitação não autorizada");
		}
	}

	/**
	 * Desativar usuario
	 */
	public String desativar() {
		if (usuario.getId() != null) {
			usuario.setStatus(Status.DESATIVADO);
			repository.save(usuario);

			JsfUtil.info("Usuário desativado");
		}
		return Adress.PESQUISA_USUARIO;
	}

	/**
	 * Reativar usuario
	 */
	public void reativar() {
		if (usuario.getId() != null) {
			usuario.setStatus(Status.ATIVADO);
			repository.save(usuario);

			JsfUtil.info("Usuário ativado com sucesso!");
		}
	}

	public void pesquisar() {
		listaUsuarios = repository.findByMatriculaOrNomeLikeOrderByNomeAsc(searchValue, "%" + searchValue + "%");

		if (listaUsuarios.isEmpty()) {
			JsfUtil.error("Nenhum registro encontrado");
		}
	}

	/**
	 * Metodo esqueci minha senha
	 */
	public void esqueciMinhaSenha() {
		try {
			Usuario u = repository.findByMatricula(usuario.getMatricula());
			String senha = getNovaSenha();
			
			EnviaEmailSenhaThread email = new EnviaEmailSenhaThread(u, senha);
			email.start();
			
			u.setSenha(senha);
			repository.save(u);
			
			JsfUtil.info("Senha enviada para: " + u.getEmail());
		} catch (Exception e) {
			JsfUtil.error("Matrícula não encontrada");
		}

	}

	/**
	 * Cria uma senha aleatoria
	 *
	 * @return
	 */
	private String getNovaSenha() {
		int i = (int) (Math.random() * 25 * 1000000000 / 123 + 45 * 10);

		return String.valueOf(i);
	}

	/**
	 * Busca id do usuario
	 */
	public void buscar() {
		usuario = repository.findBy(usuario.getId());
	}
	
	public void buscarUsuarioLogado() {
		try {
			usuario = repository.findBy(usuario.getId());
			Usuario usuarioLogado = seguranca.getUsuarioLogado().getUsuario();
			if (!usuario.equals(usuarioLogado)) {
				String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(context + "/pages/error/404.xhtml?faces-redirect=true");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	// Listing

	/**
	 * Lista usuario por ordem alfabetica
	 *
	 * @return
	 */
	public List<Usuario> getListaUsuarios() {
		if (listaUsuarios == null) {
			listaUsuarios = repository.findAllOrderByNomeAsc();
		}
		return listaUsuarios;
	}

	/**
	 * Lista usuarios pendentes
	 *
	 * @return
	 */
	public List<Usuario> getListaUsuariosPendentes() {
		return repository.findAllStatusOrderByNome(Status.PENDENTE);
	}

	/**
	 * Lista tipo usuario
	 *
	 * @return
	 */
	public List<Perfil> getListaTipoUsuario() {
		return perfil.findAllOrderByNomeAsc();
	}

	// Count

	/**
	 * Quantidade de solicitacao de acesso
	 *
	 * @return
	 */
	public Long getSolicitacoesPendentes() {
		return repository.countStatus(Status.PENDENTE);
	}

	public boolean isExisteSolicitacao() {
		return !repository.findAllStatusOrderByNome(Status.PENDENTE).isEmpty();
	}

	// Validations

	/**
	 * Valida existencia de registro
	 *
	 * @return
	 */
	public Boolean validacaoMatriculaUsuario() {
		return repository.findAllByMatricula(usuario.getMatricula()).isEmpty();
	}

	/**
	 * Instancia objeto
	 *
	 * @return
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	public boolean isUsuarioExiste() {
		return this.getUsuario().getId() != null;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue.toUpperCase();
	}

	public String getSenhaValue() {
		return senhaValue;
	}

	public void setSenhaValue(String senhaValue) {
		this.senhaValue = StringExtended.toMD5(senhaValue);
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}
	
	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenhaMeuPerfil() {
		return novaSenhaMeuPerfil;
	}

	public void setNovaSenhaMeuPerfil(String novaSenhaMeuPerfil) {
		this.novaSenhaMeuPerfil = novaSenhaMeuPerfil;
	}
}
