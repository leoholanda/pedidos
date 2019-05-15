package net.bonsamigos.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.bonsamigos.model.Pedido;
import net.bonsamigos.model.Unidade;
import net.bonsamigos.security.Seguranca;
import net.bonsamigos.service.PedidoService;
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
	private Seguranca seguranca;

	private Pedido pedido;
	
	private List<Unidade> unidades;
	
	@PostConstruct
	public void init() {
		pedido = new Pedido();
		unidades = unidadeService.findByArea(seguranca.getUsuarioLogado().getUsuario().getArea());
	}
	
	public void salvar() {
		try {
			pedidoService.save(pedido);
			
			pedido = new Pedido();
			FacesUtil.info("Salvo com sucesso!");
		} catch (NegocioException e) {
			FacesUtil.error(e.getMessage());
		}
		
	}
	
	public void carregarPedido() {
		pedido = pedidoService.findBy(pedido.getId());
	}
	
	/**
	 * Lista as unidades
	 * @return
	 */
	public List<Unidade> getUnidades() {
		return unidades;
	}
	
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
}
