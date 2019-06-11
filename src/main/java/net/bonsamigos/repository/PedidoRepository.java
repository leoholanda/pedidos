package net.bonsamigos.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.enums.Area;
import net.bonsamigos.enums.Status;
import net.bonsamigos.model.Pedido;
import net.bonsamigos.model.Unidade;

@Repository
public interface PedidoRepository extends EntityRepository<Pedido, Long> {

	@Query("SELECT p FROM Pedido p WHERE p.unidade.area = ?1 ORDER BY p.status, p.unidade.codigo ASC, p.dataCriacao")
	List<Pedido> findAllOrderByUnidade(Area area);
	
	@Query("SELECT p FROM Pedido p WHERE p.unidade = ?1 AND (p.status = 'AUTORIZADO' OR p.status = 'ENTREGUE') ORDER BY p.id DESC")
	List<Pedido> findLastPedido(Unidade unidade);
	
	@Query("SELECT p FROM Pedido p WHERE p.unidade.area = ?1 AND p.status = ?2 ORDER BY p.unidade.codigo ASC, p.dataCriacao")
	List<Pedido> findPedidoByStatusOrderByUnidade(Area area, Status status);
	
	@Query("SELECT count(p) FROM Pedido p WHERE p.unidade.area = ?1 AND p.status = ?2")
	Long countPedidosByStatus(Area area, Status status);
	

}
