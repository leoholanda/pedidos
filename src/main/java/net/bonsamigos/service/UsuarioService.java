package net.bonsamigos.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Usuario;
import net.bonsamigos.repository.UsuarioRepository;
import net.bonsamigos.security.Seguranca;
import net.bonsamigos.util.NegocioException;

@Service
public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository usuarioRepository;
	
	@Inject
	private Seguranca seguranca;
	
	private final Log logger = LogFactory.getLog(this.getClass());

	/**
	 * Busca pelo codigo
	 * 
	 * @param codigo
	 * @return
	 */
	public Usuario findBy(Long id) {
		return usuarioRepository.findBy(id);
	}

	public List<Usuario> findAll() {
		return usuarioRepository.findAllOrderByNomeAscSobrenomeAsc();
	}
	
	public Usuario findByCpf(String cpf) {
		Optional<Usuario> usuario = usuarioRepository.findByCpf(cpf);
		return usuario.get();
	}
	
	public Usuario loadUsuarioAutorizado(String cpf) {
		return usuarioRepository.loadUsuarioAutorizado(cpf);
	}
	
	/**
	 * Busca cpf
	 * @param cpf
	 * @return
	 */
	public Usuario checksSecurity(String cpf) {
		Usuario usuario = null;
		try {
			if (cpf.isEmpty()) {
				cpf = "0";
			}
			usuarioRepository.checksSecurity(cpf);
		} catch (NoResultException e) {
			// Nenhum usuário encontrado informado
		}
		return usuario;
	}
	
	/**
	 * Verifica autenticação
	 * @return
	 */
	public Usuario checkAutentication(String cpf, String senha) {
		logger.debug("### Autenticando ###");
		Usuario usuarioExistente = checksSecurity(cpf);
		
		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
		boolean match = bCryptEncoder.matches(senha, usuarioExistente.getSenha());
		
		if(match) {
			logger.debug("### Autenticado com sucesso ###");
			return usuarioExistente;
		}
		
		logger.debug("### Não autenticado ###");
		return null;
	}

	/**
	 * Grava dados
	 * 
	 * @param usuario
	 * @return
	 */
	public Usuario save(Usuario usuario) throws NegocioException {
		Optional<Usuario> usuarioExistente = usuarioRepository.findByCpf(usuario.getCpf());

		if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException("Este CPF já está atribuído a um usuário");
		}
		
		// Gera um hash utilizando o BCrypt.
//		BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();
//		usuario.setSenha(bCryptEncoder.encode(usuario.getSenha()));
		
		return usuarioRepository.save(usuario);
	}
	
	/**
	 * Muda status para Desativado
	 * @param usuario
	 */
	public void remove(Usuario usuario) {
		usuario.setStatus(Status.DESATIVADO);
		usuarioRepository.save(usuario);
	}
	
	/**
	 * Busca usuario logado - (somente o usuario logado)
	 * 
	 */
	public Usuario loadUsuarioLogado(Usuario usuario) {
		try {
			usuario = usuarioRepository.load(usuario.getId());
			Usuario usuarioLogado = seguranca.getUsuarioLogado().getUsuario();
			if (!usuario.equals(usuarioLogado)) {
				String context = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				FacesContext.getCurrentInstance().getExternalContext()
						.redirect(context + "/access?faces-redirect=true");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return usuario;
	}
}
