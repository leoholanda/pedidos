package net.bonsamigos.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Pedido;
import net.bonsamigos.model.Unidade;
import net.bonsamigos.repository.PedidoRepository;
import net.bonsamigos.security.Seguranca;
import net.bonsamigos.util.ExecutorRelatorio;
import net.bonsamigos.util.NegocioException;

@Service
public class PedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository pedidoRepository;

	@Inject
	private Seguranca seguranca;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	/**
	 * Busca pelo codigo
	 * 
	 * @param codigo
	 * @return
	 */
	public Pedido findBy(Long id) {
		return pedidoRepository.findBy(id);
	}

	/**
	 * Lista todos os pedidos
	 * 
	 * @return
	 */
	public List<Pedido> findAll() {
		return pedidoRepository.findAllOrderByUnidade(seguranca.getUsuarioLogado().getUsuario().getArea());
	}

	/**
	 * Lista pedidos referente ao status
	 * 
	 * @param status
	 * @return
	 */
	public List<Pedido> findPedidoByStatusOrderByUnidade(Status status) {
		return pedidoRepository.findPedidoByStatusOrderByUnidade(seguranca.getUsuarioLogado().getUsuario().getArea(),
				status);
	}

	/**
	 * Conta pedidos referente ao status
	 * 
	 * @return
	 */
	public Long countPedidosByStatus(Status status) {
		return pedidoRepository.countPedidosByStatus(seguranca.getUsuarioLogado().getUsuario().getArea(), status);
	}

	/**
	 * Grava dados
	 * 
	 * @param pedido
	 * @return
	 */
	public Pedido save(Pedido pedido) throws NegocioException {
		if (pedido.getItens().isEmpty()) {
			throw new NegocioException("A lista de produtos do pedido está vazia!");
		}
		return pedidoRepository.save(pedido);
	}

	/**
	 * Cancela pedido
	 * 
	 * @param pedido
	 */
	public void cancel(Pedido pedido) {
		pedido.setStatus(Status.CANCELADO);
		pedidoRepository.save(pedido);
	}

	/**
	 * Quantidade total de registro
	 * 
	 * @return
	 */
	public Long countAll() {
		return pedidoRepository.count();
	}

	/**
	 * Lista o ultimo pedido
	 * 
	 * @param unidade
	 * @return
	 */
	public List<Pedido> findLastPedido(Unidade unidade) {
		return pedidoRepository.findLastPedido(unidade);
	}

	/**
	 * Exporta pedido para pdf
	 * @throws NegocioException 
	 */
	public void printPedido(Pedido pedido) throws NegocioException {
		if (pedido != null) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("p_pedido_id", pedido.getId());
			
			ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/pedidos.jasper", this.response, parametros,
					"Controle.pdf");

			Session session = manager.unwrap(Session.class);
			session.doWork(executor);

			if (executor.isRelatorioGerado()) {
				facesContext.responseComplete();
			} else {
				throw new NegocioException("A execução do relatório não retornou dados");
			}
		} else {
			throw new NegocioException("Pedido não encontrado!");
		}
	}
}
