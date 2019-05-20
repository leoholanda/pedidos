package net.bonsamigos.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bonsamigos.enums.Area;
import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Unidade;
import net.bonsamigos.repository.UnidadeRepository;
import net.bonsamigos.security.Seguranca;
import net.bonsamigos.util.NegocioException;

@Service
public class UnidadeService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UnidadeRepository unidadeRepository;
	
	@Inject
	private Seguranca seguranca;

	/**
	 * Busca pelo codigo
	 * 
	 * @param codigo
	 * @return
	 */
	public Unidade findBy(Long id) {
		return unidadeRepository.findBy(id);
	}

	public List<Unidade> findAll() {
		return unidadeRepository.findAllOrderByCodigo();
	}
	
	/**
	 * Lista unidades por area
	 * @param area
	 * @return
	 */
	public List<Unidade> findByArea(Area area) {
		return unidadeRepository.findByArea(area);
	}
	
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
		Optional<Unidade> unidadeExistente = unidadeRepository.findByCodigo(unidade.getCodigo(), unidade.getArea());

		if (unidadeExistente.isPresent() && !unidadeExistente.get().equals(unidade)) {
			throw new NegocioException("O código da unidade já existe!");
		}

		return unidadeRepository.save(unidade);
	}
	
	/**
	 * Muda status para Desativado
	 * @param unidade
	 */
	public void remove(Unidade unidade) {
		unidade.setStatus(Status.DESATIVADO);
		unidadeRepository.save(unidade);
	}
	
	/**
	 * Quantidade total de registro
	 * @return
	 */
	public Long countAll(Area area) {
		return unidadeRepository.countByArea(area);
	}
	
	/**
	 * Quantidade total d registro por área(Educacao ou Saude)
	 * @return
	 */
	public Long countByArea() {
		return unidadeRepository.countByArea(seguranca.getUsuarioLogado().getUsuario().getArea());
	}
}
