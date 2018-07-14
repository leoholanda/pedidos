package br.com.aee.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import br.com.aee.model.Modulo;
import br.com.aee.repository.ModuloRepository;
import br.com.aee.util.NegocioException;

public class ModuloService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ModuloRepository moduloRepository;

	public Modulo save(Modulo modulo) throws NegocioException {
		Modulo moduloEmail = moduloRepository.findByNome(modulo.getNome());
		if (moduloEmail != null && !moduloEmail.equals(modulo)) {
			throw new NegocioException("Nome do modulo informado já existe!");
		}
		return moduloRepository.save(modulo);
	}
	
	/**
	 * Remove modulo
	 * 
	 * @param modulo
	 */
	public void remove(Modulo modulo) {
		moduloRepository.remove(modulo);
	}
	
	/**
	 * Busca modulo
	 * 
	 * @return
	 */
	public Modulo load(Modulo modulo) {
		return moduloRepository.load(modulo);
	}

	/**
	 * Lista todos
	 * 
	 * @return
	 */
	public List<Modulo> findAll() {
		return moduloRepository.findAll();
	}
	
	/**
	 * Lista todos por nome
	 * 
	 * @return
	 */
	public List<Modulo> findAllOrderByNomeAsc() {
		return moduloRepository.findAllOrderByNomeAsc();
	}
}