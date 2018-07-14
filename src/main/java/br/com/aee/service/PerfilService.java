package br.com.aee.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.aee.model.Perfil;
import br.com.aee.repository.PerfilRepository;
import br.com.aee.util.NegocioException;

public class PerfilService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilRepository perfilRepository;

	public Perfil save(Perfil perfil) throws NegocioException {
//		Perfil perfilEmail = perfilRepository.findByNome(perfil.getNome());
//		if (perfilEmail != null && !perfilEmail.equals(perfil)) {
//			throw new NegocioException("Nome do perfil informado já existe!");
//		}
		return perfilRepository.save(perfil);
	}
	
	/**
	 * Remove perfil
	 * 
	 * @param perfil
	 */
	public void remove(Perfil perfil) throws NegocioException {
		perfilRepository.remove(perfil);
	}
	
	/**
	 * Busca perfil
	 * 
	 * @return
	 */
	public Perfil load(Perfil perfil) {
		return perfilRepository.findBy(perfil.getId());
	}

	/**
	 * Lista todos
	 * 
	 * @return
	 */
	public List<Perfil> findAll() {
		return perfilRepository.findAll();
	}
	
	/**
	 * Lista todos por nome
	 * 
	 * @return
	 */
	public List<Perfil> findAllOrderByNomeAsc() {
		return perfilRepository.findAllOrderByNomeAsc();
	}
}