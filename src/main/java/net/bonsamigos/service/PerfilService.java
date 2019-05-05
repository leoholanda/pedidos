package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bonsamigos.model.Perfil;
import net.bonsamigos.repository.PerfilRepository;
import net.bonsamigos.util.NegocioException;

@Service
public class PerfilService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PerfilRepository perfilRepository;
	
	/**
	 * Busca pelo codigo
	 * @param codigo
	 * @return
	 */
	public Perfil findBy(Long codigo) {
		return perfilRepository.findBy(codigo);
	}
	
	public List<Perfil> findAll() {
		return perfilRepository.findAllOrderByNome();
	}
	
	public List<Perfil> findAllByAtivado() {
		return perfilRepository.findAllByAtivado();
	}
	
	/**
	 * Grava dados
	 * @param perfil
	 * @return
	 */
	public Perfil save(Perfil perfil) throws NegocioException {
		return perfilRepository.save(perfil);
	}


}
