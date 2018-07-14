package br.com.aee.repository;

import java.util.Calendar;
import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.MesFatura;
import br.com.aee.util.EntityRepository;

@Repository
public interface MesFaturaRepository extends EntityRepository<MesFatura, Long> {

	/**
	 * Verifica se as faturas do mes ja foram geradas
	 * @param data
	 * @return
	 */
	@Query("SELECT f FROM MesFatura f WHERE dataProcesso = ?1 AND evento = 'FATURA'")
	List<MesFatura> findByDataProcesso(Calendar data);
	
	@Query("SELECT f FROM MesFatura f WHERE dataProcesso = ?1 AND evento = 'COBRANCA'")
	List<MesFatura> findByDataCobranca(Calendar data);	
	
	@Query("SELECT f FROM MesFatura f WHERE dataProcesso = ?1 AND evento = 'JUROS'")
	List<MesFatura> findByDataJuros(Calendar data);	
}
