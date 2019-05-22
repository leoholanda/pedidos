package net.bonsamigos.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Item;
import net.bonsamigos.model.Pedido;

@Repository
public interface ItemRepository extends EntityRepository<Item, Long> {
	
	List<Item> findByPedidoOrderByOrdem(Pedido pedido);

}
