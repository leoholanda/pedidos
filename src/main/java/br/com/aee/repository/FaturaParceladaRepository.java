package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Fatura;
import br.com.aee.model.FaturaParcelada;
import br.com.aee.util.EntityRepository;

@Repository
public interface FaturaParceladaRepository extends EntityRepository<FaturaParcelada, Long> {
	
	/**
     * Lista fatura parcelada pelo mês atual
     *
     * @param mes
     * @param ano
     * @return
     */
    @Query("SELECT f FROM FaturaParcelada f WHERE EXTRACT(MONTH FROM f.vencimento) = ?1 "
            + "AND EXTRACT(YEAR FROM f.vencimento) = ?2 "
    		+ "AND f.status = 'PENDENTE' "
            + "ORDER BY f.fatura.plano.beneficiario.nome ASC")
    List<FaturaParcelada> findByFaturaParceladaDoMesAtual(Integer mes, Integer ano);
    
    @Query("SELECT f FROM FaturaParcelada f WHERE f.fatura = ?1 ORDER BY f.numeroDaParcela")
    List<FaturaParcelada> findByParcelasDaFatura(Fatura fatura);

}
