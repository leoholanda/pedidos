package net.bonsamigos.repository;

import java.util.List;
import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Produto;

@Repository
public interface ProdutoRepository extends EntityRepository<Produto, Long> {
	
	List<Produto> findAllOrderByNome();

	Optional<Produto> findByCodigo(Long codigo);

}
