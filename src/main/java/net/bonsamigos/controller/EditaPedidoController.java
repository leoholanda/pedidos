package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import net.bonsamigos.model.Item;
import net.bonsamigos.model.Pedido;
import net.bonsamigos.model.Produto;
import net.bonsamigos.model.Unidade;
import net.bonsamigos.security.Seguranca;
import net.bonsamigos.service.ItemService;
import net.bonsamigos.service.PedidoService;
import net.bonsamigos.service.ProdutoService;
import net.bonsamigos.service.UnidadeService;
import net.bonsamigos.util.FacesUtil;
import net.bonsamigos.util.NegocioException;

@Named
@ViewScoped
public class EditaPedidoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoService pedidoService;

	@Inject
	private UnidadeService unidadeService;

	@Inject
	private ProdutoService produtoService;

	@Inject
	private ItemService itemService;

	@Inject
	private Seguranca seguranca;

	private Pedido pedido;

	private Pedido ultimoPedido;

	private Produto produto;

	private Item item;

	private List<Unidade> unidades;

	private List<Produto> produtos;

	private List<Item> itens;

	@PostConstruct
	public void init() {
		pedido = new Pedido();
		ultimoPedido = new Pedido();
		produto = new Produto();
		item = new Item();
		unidades = unidadeService.findByArea(seguranca.getUsuarioLogado().getUsuario().getArea());
		produtos = produtoService.findAll();
		itens = new ArrayList<Item>();
	}

	public void salvar() {
		try {
			pedido.setEditadoPor(seguranca.usuarioLogado());
			pedido.setDataEdicao(Calendar.getInstance());

			pedidoService.save(pedido);

			FacesUtil.info("Pedido atualizado!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
	}

	/**
	 * Adiciona item
	 */
	public void addItem() {
		item.setPedido(pedido);
		item.setProduto(produto);
		itens.add(item);
		pedido.getItens().add(item);
		item = new Item();
	}

	/**
	 * Remove item
	 * @throws NegocioException 
	 */
	public void removeItem() throws NegocioException {
		item.setAtivo(false);
		itemService.save(item);
		itens.remove(item);
		
		FacesUtil.info("Produto '" + item.getProduto().getNomeInicialMaiuscula() + "' retirado da lista");
		item = new Item();
	}
	
	/**
	 * Lista os itens do pedido
	 * @return
	 */
	public List<Item> getItens() {
		return itens;
	}

	/**
	 * Exporta pedido para pdf
	 */
	public void imprimirPedido() {
		try {
			pedidoService.printPedido(pedido);
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
	}



	/**
	 * Select produto dataTable
	 *
	 * @param event
	 */
	public void eventoSeleciona(SelectEvent event) {
		produto = (Produto) event.getObject();
	}

	public void carregarPedido() {
		pedido = pedidoService.findBy(pedido.getId());
		itens = itemService.findByPedido(pedido);
	}

	public List<Unidade> getUnidades() {
		return unidades;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Pedido getUltimoPedido() {
		return ultimoPedido;
	}

	public void setUltimoPedido(Pedido ultimoPedido) {
		this.ultimoPedido = ultimoPedido;
	}
}
