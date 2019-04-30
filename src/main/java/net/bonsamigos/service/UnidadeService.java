package net.bonsamigos.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Unidade;
import net.bonsamigos.repository.UnidadeRepository;
import net.bonsamigos.util.NegocioException;

public class UnidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeRepository unidadeRepository;

	/**
	 * Busca pelo codigo
	 * 
	 * @param codigo
	 * @return
	 */
	public Unidade findByCodigo(Long codigo) {
		return unidadeRepository.findByCodigo(codigo);
	}

	public List<Unidade> findAll() {
		return unidadeRepository.findAllOrderByCodigo();
	}
	
//	public List<Unidade> findByCodigo(String codigo) {
//		List<Unidade> lista = new ArrayList<Unidade>();
//		
//		if (codigo == null || codigo == "") {
//			lista = this.findAll();
//		} else {
//			lista = unidadeRepository.findByCodigo(Long.valueOf(codigo));
//		}
//		
//		return lista;
//	}

	public List<Unidade> findByNomeLikeOrderByCodigo(String nome) {
		List<Unidade> lista = new ArrayList<Unidade>();
		
		if (nome == null) {
			lista = this.findAll();
		} else {
			lista = unidadeRepository.findByNomeLikeOrderByCodigo("%" + nome.toUpperCase() + "%");
		}

		return lista;
	}

	/**
	 * Grava dados
	 * 
	 * @param unidade
	 * @return
	 */
	public Unidade save(Unidade unidade) throws NegocioException {
		Unidade unidadeExistente = unidadeRepository.findBy(unidade.getCodigo());

		if (unidadeExistente != null && !unidadeExistente.equals(unidade)) {
			throw new NegocioException("O código da unidade já existe!");
		}

		return unidadeRepository.save(unidade);
	}
	
	public void remove(Unidade unidade) {
		unidade.setStatus(Status.DESATIVADO);
		unidadeRepository.save(unidade);
	}

}
