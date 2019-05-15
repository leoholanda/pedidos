package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Pedido;
import net.bonsamigos.repository.PedidoRepository;
import net.bonsamigos.util.NegocioException;

@Service
public class PedidoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PedidoRepository pedidoRepository;

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
	 * @return
	 */
	public List<Pedido> findAll() {
		return pedidoRepository.findAll();
	}
	
	/**
	 * Grava dados
	 * 
	 * @param pedido
	 * @return
	 */
	public Pedido save(Pedido pedido) throws NegocioException {
		return pedidoRepository.save(pedido);
	}
	
	/**
	 * Muda status para Desativado
	 * @param pedido
	 */
	public void remove(Pedido pedido) {
		pedido.setStatus(Status.DESATIVADO);
		pedidoRepository.save(pedido);
	}
	
	/**
	 * Quantidade total de registro
	 * @return
	 */
	public Long countAll() {
		return pedidoRepository.count();
	}
}
