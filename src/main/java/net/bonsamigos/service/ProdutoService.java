package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import net.bonsamigos.model.Produto;
import net.bonsamigos.repository.ProdutoRepository;
import net.bonsamigos.util.NegocioException;

public class ProdutoService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ProdutoRepository produtoRepository;
	
	/**
	 * Busca pelo codigo
	 * @param codigo
	 * @return
	 */
	public Produto findById(Long codigo) {
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
//		Optional<Produto> existente = produtoRepository.findByCodigo(produto.getCodigo());
		
		System.out.println("Cadastro realizado com sucesso!");
		return produtoRepository.save(produto);
	}


}
