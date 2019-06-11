package net.bonsamigos.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Item;
import net.bonsamigos.model.Pedido;

@Repository
public interface ItemRepository extends EntityRepository<Item, Long> {
	
	@Query("SELECT i FROM Item i WHERE i.pedido = ?1 AND i.ativo = true")
	List<Item> findByPedido(Pedido pedido);

}
