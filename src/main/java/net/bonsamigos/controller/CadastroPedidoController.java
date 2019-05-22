package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import net.bonsamigos.enums.Status;
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
public class CadastroPedidoController implements Serializable {

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

	@PostConstruct
	public void init() {
		pedido = new Pedido();
		ultimoPedido = new Pedido();
		produto = new Produto();
		item = new Item();
		unidades = unidadeService.findByArea(seguranca.getUsuarioLogado().getUsuario().getArea());
		produtos = produtoService.findAll();
	}

	public void salvar() {
		try {
			pedido.setUsuario(seguranca.getUsuarioLogado().getUsuario());
			pedido.setStatus(Status.ABERTO);
			pedido.setCodigo(pedido.getUnidade().getCodigo());
			
			pedidoService.save(pedido);

			pedido = new Pedido();
			FacesUtil.info("Pedido aberto com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
	}
	
	public void autorizarPedido() {
		try {
			pedido.setAutorizadoPor(seguranca.getUsuarioLogado().getUsuario());
			pedido.setStatus(Status.AUTORIZADO);
			
			pedidoService.save(pedido);

			FacesUtil.info("Pedido autorizado com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
	}
	
	/**
	 * Cancela pedido
	 */
	public void cancelarPedido() {
		pedidoService.cancel(pedido);
		FacesUtil.info("Pedido cancelado!");
	}

	/**
	 * Adiciona item
	 */
	public void addItem() {
		
		item.setPedido(pedido);
		item.setProduto(produto);
//		item.setOrdem();
		pedido.getItens().add(item);
		item = new Item();
	}
	
	/**
	 * Remove item
	 */
	public void removeItem() {
		pedido.getItens().remove(item);
		item = new Item();
	}
	
	/**
	 * Lista o ultimo pedido realizado
	 * @return
	 */
	public List<Pedido> getListaUltimosPedidos() {
		return pedidoService.findLastPedido(pedido.getUnidade());
	}
	
	/**
	 * Lista os itens do pedido
	 * @return
	 */
	public List<Item> getListaItensDoPedido() {
		return itemService.findByPedido(pedido);
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
