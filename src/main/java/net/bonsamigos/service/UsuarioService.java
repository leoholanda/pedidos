package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Usuario;
import net.bonsamigos.repository.UsuarioRepository;
import net.bonsamigos.util.NegocioException;

@Service
public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository usuarioRepository;

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
}
