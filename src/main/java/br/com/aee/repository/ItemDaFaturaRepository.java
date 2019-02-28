package br.com.aee.repository;

import java.util.List;

import javax.enterprise.context.Dependent;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Fatura;
import br.com.aee.model.ItemDaFatura;
import br.com.aee.util.EntityRepository;

@Repository
@Dependent
public interface ItemDaFaturaRepository extends EntityRepository<ItemDaFatura, Long> {
	
	@Query("SELECT i FROM ItemDaFatura i WHERE i.fatura = ?1 AND i.ordem = 1")
	List<ItemDaFatura> findAllOrderByOrdem(Fatura fatura);
	
	@Query("SELECT i FROM ItemDaFatura i WHERE i.fatura = ?1 AND i.ordem != 1")
	List<ItemDaFatura> findAllExcetoPlanoDeSaude(Fatura fatura);

}
