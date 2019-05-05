package net.bonsamigos.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.model.Produto;
import net.bonsamigos.service.ProdutoService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.NegocioException;

@Named
@ViewScoped
public class CadastroProdutoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ProdutoService produtoService;

	private Produto produto;

	@PostConstruct
	public void init() {
		produto = new Produto();
	}
	
	public void salvar() {
		try {
			produtoService.save(produto);
			
			produto = new Produto();
			FacesUtil.info("Salvo com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
		
	}
	
	public void carregarProduto() {
		produto = produtoService.findBy(produto.getId());
	}
	
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}
}
