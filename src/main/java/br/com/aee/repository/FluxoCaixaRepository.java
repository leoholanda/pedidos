package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.FluxoCaixa;
import br.com.aee.model.TipoFluxo;
import br.com.aee.util.EntityRepository;

@Repository
public interface FluxoCaixaRepository extends EntityRepository<FluxoCaixa, Long> {
	
	/**
	 * Exibe o fluxo passando o tipo
	 * @param tipoFluxo
	 * @return
	 */
	@Query("SELECT f FROM FluxoCaixa f WHERE EXTRACT(MONTH FROM f.dataEvento) = ?1 "
            + "AND EXTRACT(YEAR FROM f.dataEvento) = ?2 "
    		+ "AND f.tipoFluxo = ?3 "
            + "ORDER BY f.dataEvento DESC")
	List<FluxoCaixa> findByTipoFluxo(Integer mes, Integer ano, TipoFluxo tipoFluxo);
	
	@Query("SELECT f FROM FluxoCaixa f WHERE EXTRACT(MONTH FROM f.dataEvento) = ?1 "
            + "AND EXTRACT(YEAR FROM f.dataEvento) = ?2 "
            + "ORDER BY f.id ASC")
	List<FluxoCaixa> findByFluxoMes(Integer mes, Integer ano);
	
}
