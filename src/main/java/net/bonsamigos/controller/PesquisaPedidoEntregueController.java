package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Pedido;
import net.bonsamigos.service.PedidoService;
import net.bonsamigos.util.Mes;
import net.bonsamigos.util.Paginacao;

@Named
@ViewScoped
public class PesquisaPedidoEntregueController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoService pedidoService;

	private Pedido pedido;

	private List<Pedido> pedidos;

	@PostConstruct
	public void init() {
		pedido = new Pedido();
		pedidos = pedidoService.findPedidoByStatusOrderByUnidade(Status.ENTREGUE);
	}

	/**
	 * Quantidade de pedidos autorizados
	 * @return
	 */
	public Long getContaPedidos() {
		return pedidoService.countPedidosByStatus(Status.ENTREGUE);
	}
	
	public List<String> getListaStatus() {
		return Arrays.asList(Status.ABERTO.getDescricao(), 
				Status.AUTORIZADO.getDescricao(), 
				Status.ENTREGUE.getDescricao(), 
				Status.CANCELADO.getDescricao());
	}
	
	/**
	 * Lista meses do ano
	 * @return
	 */
	public List<String> getListaMeses() {
		return Arrays.asList(Mes.listaMeses());
	}
	
	/**
	 * Verifica necessidade de paginação
	 * 
	 */
	public boolean isPaginator() {
		return pedidos.size() > Paginacao.ROW ? true : false;
	}
	
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

}
