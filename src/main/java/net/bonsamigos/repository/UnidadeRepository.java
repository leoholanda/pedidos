package net.bonsamigos.repository;

import java.util.List;
import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Unidade;

@Repository
public interface UnidadeRepository extends EntityRepository<Unidade, Long> {
	
	List<Unidade> findAllOrderByCodigo();

	Optional<Unidade> findByCodigo(Long codigo);

    
}
