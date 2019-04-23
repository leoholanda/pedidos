package net.bonsamigos.repository;

import java.util.List;
import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Produto;
import net.bonsamigos.model.Unidade;

@Repository
public interface ProdutoRepository extends EntityRepository<Produto, Long> {
	
	List<Unidade> findAllOrderByNome();

	Optional<Unidade> findByCodigo(Long codigo);

}
