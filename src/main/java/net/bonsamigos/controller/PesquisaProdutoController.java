package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.model.Produto;
import net.bonsamigos.service.ProdutoService;
import net.bonsamigos.util.Paginacao;

@Named
@ViewScoped
public class PesquisaProdutoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoService produtoService;

	private Produto produto;

	private List<Produto> produtos;

	@PostConstruct
	public void init() {
		produto = new Produto();
		produtos = produtoService.findAll();
	}

	/**
	 * Quantidade total de produtos
	 * @return
	 */
	public Long getContaTodos() {
		return produtoService.countAll();
	}
	
	/**
	 * Verifica necessidade de paginação
	 * 
	 */
	public boolean isPaginator() {
		return produtos.size() > Paginacao.ROW ? true : false;
	}
	
	@Override
	public String toString() {
		return produto.getId() + " | " + produto.getNome();
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

}
