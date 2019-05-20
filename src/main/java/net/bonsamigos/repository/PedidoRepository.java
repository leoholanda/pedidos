package net.bonsamigos.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Pedido;
import net.bonsamigos.model.Unidade;

@Repository
public interface PedidoRepository extends EntityRepository<Pedido, Long> {

	@Query("SELECT p FROM Pedido p ORDER BY p.unidade.codigo ASC, p.dataCriacao")
	List<Pedido> findAllOrderByUnidade();
	
	@Query("SELECT p FROM Pedido p WHERE p.unidade = ?1 ORDER BY p.id DESC ")
	List<Pedido> findLastPedido(Unidade unidade);
	
	List<Pedido> findTop1findByUnidadeOrderByIdDesc(Unidade unidade);
	

}
