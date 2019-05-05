package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bonsamigos.model.Produto;
import net.bonsamigos.repository.ProdutoRepository;
import net.bonsamigos.util.NegocioException;

@Service
public class ProdutoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoRepository produtoRepository;
	
	/**
	 * Busca pelo codigo
	 * @param codigo
	 * @return
	 */
	public Produto findBy(Long codigo) {
		return produtoRepository.findBy(codigo);
	}
	
	public List<Produto> findAll() {
		return produtoRepository.findAllOrderByNome();
	}
	
	/**
	 * Grava dados
	 * @param produto
	 * @return
	 */
	public Produto save(Produto produto) throws NegocioException {
		return produtoRepository.save(produto);
	}


}
