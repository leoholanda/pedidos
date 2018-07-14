package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.FaixaEtaria;
import br.com.aee.util.EntityRepository;

@Repository
public interface FaixaEtariaRepository extends EntityRepository<FaixaEtaria, Long> {

    /**
     * Lista todos as faixas por ordem de registro
     *
     * @return
     */
    @Query("SELECT f FROM FaixaEtaria f WHERE f.status = true ORDER BY f.periodoInicial, f.periodoFinal")
    List<FaixaEtaria> findByPeriodo();

    /**
     * Checa periodo inicial e final
     *
     * @param periodoInicial
     * @param periodoFinal
     * @return
     */
    @Query("SELECT f FROM FaixaEtaria f WHERE f.periodoInicial = ?1 AND f.periodoFinal = ?2 AND f.status = true")
    List<FaixaEtaria> findAllByPeriodo(Integer periodoInicial, Integer periodoFinal);

}
