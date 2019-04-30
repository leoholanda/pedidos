package net.bonsamigos.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Unidade;

@Repository
public interface UnidadeRepository extends EntityRepository<Unidade, Long> {
	
	List<Unidade> findAllOrderByCodigo();
	
	List<Unidade> findByNomeLikeOrderByCodigo(String nome);
	
	@Query("SELECT u FROM Unidade u WHERE u.nome LIKE ?1 ORDER BY u.codigo ASC ")
	List<Unidade> findByNome(String nome);

	Unidade findByCodigo(Long codigo);
    
}
