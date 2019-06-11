package net.bonsamigos.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bonsamigos.model.Item;
import net.bonsamigos.model.Pedido;
import net.bonsamigos.repository.ItemRepository;
import net.bonsamigos.util.NegocioException;

@Service
public class ItemService implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ItemRepository itemRepository;
	
	/**
	 * Busca pelo pedido
	 * @param codigo
	 * @return
	 */
	public List<Item> findByPedido(Pedido pedido) {
		return itemRepository.findByPedido(pedido);
	}
	
	/**
	 * Grava dados
	 * @param item
	 * @return
	 */
	public Item save(Item item) throws NegocioException {
		return itemRepository.save(item);
	}

	/**
	 * Remove item
	 * @param item
	 */
	public void remove(Item item) {
		itemRepository.remove(item);
	}
}
