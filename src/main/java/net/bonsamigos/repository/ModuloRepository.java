package net.bonsamigos.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Modulo;

@Repository
public interface ModuloRepository extends EntityRepository<Modulo, Long> {
	
	List<Modulo> findAllOrderByNome();

}
