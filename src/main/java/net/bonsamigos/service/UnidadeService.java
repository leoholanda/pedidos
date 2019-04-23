package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import net.bonsamigos.model.Unidade;
import net.bonsamigos.repository.UnidadeRepository;
import net.bonsamigos.util.NegocioException;

public class UnidadeService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private UnidadeRepository unidadeRepository;
	
	/**
	 * Busca pelo codigo
	 * @param codigo
	 * @return
	 */
	public Unidade findById(Long codigo) {
		return unidadeRepository.findBy(codigo);
	}
	
	public List<Unidade> findAll() {
		return unidadeRepository.findAllOrderByCodigo();
	}
	
	/**
	 * Grava dados
	 * @param unidade
	 * @return
	 */
	public Unidade save(Unidade unidade) throws NegocioException {
		Optional<Unidade> existente = unidadeRepository.findByCodigo(unidade.getCodigo());
		
		if(existente.isPresent() && !existente.get().equals(unidade)) {
			throw new NegocioException("O código da unidade já existe!");
		}
		
		System.out.println("Cadastro realizado com sucesso!");
		return unidadeRepository.save(unidade);
	}


}
