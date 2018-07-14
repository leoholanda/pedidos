package br.com.aee.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.aee.model.Modulo;


public class ModuloRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	/**
	 * Persiste modulo
	 * 
	 * @param modulo
	 * @return
	 */
	public Modulo save(Modulo modulo) {
		return manager.merge(modulo);
	}
	
	/**
	 * Remove modulo do banco (utilizado para remover solicitacao) - Muito cuidade
	 * ao utilizar
	 * 
	 * @param modulo
	 */
	public void remove(Modulo modulo) {
		manager.remove(manager.getReference(Modulo.class, modulo.getId()));
	}
	
	/**
	 * Carrega modulo
	 * 
	 * @param modulo
	 * @return
	 */
	public Modulo load(Modulo modulo) {
		return manager.find(Modulo.class, modulo.getId());
	}
	
	/**
	 * Carrega Modulo com id (necessario utilizar converter)
	 * 
	 * @param id
	 * @return
	 */
	public Modulo findById(Long id) {
		return manager.find(Modulo.class, id);
	}

	/**
	 * Lista todos
	 * @return
	 */
	public List<Modulo> findAll() {
		return manager.createQuery("FROM Modulo p", Modulo.class).getResultList();
	}
	
	/**
	 * Lista todos por ordem nome
	 * @return
	 */
	public List<Modulo> findAllOrderByNomeAsc() {
		return manager.createQuery("FROM Modulo p ORDER BY p.nome", Modulo.class).getResultList();
	}
	
	/**
	 * Procura modulo por email
	 * 
	 * @param email
	 * @return
	 */
	public Modulo findByNome(String nome) {
		try {
			return manager.createQuery("FROM Modulo p WHERE p.nome = :nome", Modulo.class)
					.setParameter("nome", nome).getSingleResult();
		} catch (NoResultException nr) {
			return null;
		}
	}

}