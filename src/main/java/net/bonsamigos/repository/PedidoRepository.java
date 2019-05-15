package net.bonsamigos.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Pedido;

@Repository
public interface PedidoRepository extends EntityRepository<Pedido, Long> {

}
