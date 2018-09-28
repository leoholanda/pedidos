package br.com.aee.repository;

import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.Dependent;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Beneficiario;
import br.com.aee.model.Fatura;
import br.com.aee.model.Status;
import br.com.aee.util.EntityRepository;

@Repository
@Dependent
public interface FaturaRepository extends EntityRepository<Fatura, Long> {
	
	@Query("SELECT f FROM Fatura f WHERE f.id = ?1")
	List<Fatura> findById(Long id);

    /**
     * Lista Fatura por Status
     *
     * @param status
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.status = ?1 ORDER BY f.vencimento ASC")
    List<Fatura> findByStatus(Status status);
    
    @Query("SELECT f FROM Fatura f WHERE f.status = 'PENDENTE' OR f.status = 'ATRASADO' ORDER BY f.vencimento ASC")
    List<Fatura> findByFaturaPendenteOrAtrasada();

    /**
     * Lista as Fatura vencidas contando com o dia do vencimento
     *
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.status = 'ATRASADO' ORDER BY f.plano.beneficiario.nome, f.vencimento")
    List<Fatura> findByFaturaAtrasada();

    /**
     * Lista as Faturas ainda nao pagas e fechadas
     *
     * @return
     */
    @Query("SELECT f FROM Fatura f "
    		+ "WHERE f.status = 'PENDENTE' "
    		+ "OR f.status = 'FECHADO' "
    		+ "ORDER BY f.plano.beneficiario.nome, f.plano.convenio.nome")
    List<Fatura> findByFaturaPendenteAndFechada();
    
    
    /**
     * Lista as Faturas geradas mas ainda nao pagas
     *
     * @return
     */
    @Query("SELECT f FROM Fatura f "
    		+ "WHERE f.status = 'PENDENTE' "
    		+ "ORDER BY f.plano.beneficiario.nome, f.plano.convenio.nome")
    List<Fatura> findByFaturaPendente();
    
    /**
     * Lista as Faturas pagas do mês atual
     *
     * @param mes
     * @param ano
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE EXTRACT(MONTH FROM f.vencimento) = ?1 "
            + "AND EXTRACT(YEAR FROM f.vencimento) = ?2 "
    		+ "AND f.status = 'PAGO' "
    		+ "OR f.status = 'PARCELADO' "
            + "ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByFaturaPaga(Integer mes, Integer ano);

    /**
     * Pesquisa fatura pelo nome do beneficiario
     *
     * @param nome
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario.nome LIKE ?1 ORDER BY f.plano.beneficiario.nome ASC, f.vencimento DESC")
    List<Fatura> findByBeneficiario(String nome);
    
    /**
     * Pesquisa fatura pelo código
     *
     * @param nome
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.id = ?1 ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByCodigo(Long id);
    
    /**
     * Pesquisa fatura pendente pelo código e filta o status
     *
     * @param nome
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.id = ?1 AND f.status = 'PENDENTE' OR f.status = 'FECHADO' ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByCodigoFaturaPendente(Long id);
    
    /**
     * Pesquisa fatura atrasada pelo código e filta o status
     *
     * @param nome
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.id = ?1 AND f.status = 'ATRASADO' ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByCodigoFaturaAtrasada(Long id);
    
    @Query("SELECT f FROM Fatura f WHERE EXTRACT(MONTH FROM f.vencimento) = ?1 "
            + "AND EXTRACT(YEAR FROM f.vencimento) = ?2 "
            + "AND f.id = ?3 "
    		+ "AND f.status = 'PAGO' "
            + "ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByCodigoFaturaPaga(Integer mes, Integer ano, Long id);

    /**
     * Pesquisa fatura pelo nome do beneficiario e status como parametro
     *
     * @param nome
     * @param status
     * @return
     */
    @Query("SELECT f FROM Fatura f "
    		+ "WHERE f.plano.beneficiario.nome LIKE ?1 AND f.status = ?2 "
    		+ "ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByBeneficiarioFaturaPendente(String nome, Status pendente);

    /**
     * Pesquisa fatura atrasada pelo nome do beneficiario
     *
     * @param nome
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario.nome LIKE ?1 AND f.status = ?2 "
    		+ "ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByBeneficiarioFaturaAtrasada(String nome, Status status);
    
    
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario.nome LIKE ?1 "
    		+ "AND EXTRACT(MONTH FROM f.vencimento) = ?2 "
            + "AND EXTRACT(YEAR FROM f.vencimento) = ?3 "
    		+ "AND f.status = 'PAGO' "
            + "ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByBeneficiarioFaturaPaga(String nome, Integer mes, Integer ano);

    /**
     * Envia lembrete de fatura disponivel
     *
     * @param hoje
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario.status = 'ATIVADO' AND f.dataRegistro = ?1")
    List<Fatura> findByFaturaBeneficiarioAtivo(Calendar hoje);
    
    /**
     * Envia lembrete de fatura disponivel somente para funcionario AEE
     *
     * @param hoje
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario.id in (31,32,46) AND f.dataRegistro = ?1")
    List<Fatura> findByFaturaBeneficiarioFuncionario(Calendar hoje);

    /**
     * Pesquisa fatura pelo mes e ano do vencimento
     *
     * @param mes
     * @param ano
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE EXTRACT(MONTH FROM f.vencimento) = ?1 "
            + "AND EXTRACT(YEAR FROM f.vencimento) = ?2 "
            + "ORDER BY f.plano.beneficiario.nome ASC")
    List<Fatura> findByFaturaMesAno(Integer mes, Integer ano);
    
    @Query("SELECT f FROM Fatura f WHERE EXTRACT(MONTH FROM f.vencimento) = ?1 "
    		+ "AND EXTRACT(YEAR FROM f.vencimento) = ?2 "
            + "AND f.plano.beneficiario = ?3")
    List<Fatura> findByFaturaMesBeneficiario(Integer mes, Integer ano, Beneficiario beneficiario);
    
    /**
     * Lista as faturadas do beneficiario, todas do ano
     *
     * @param beneficiario
     * @param ano
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario.id = ?1 "
            + "AND EXTRACT(YEAR FROM f.vencimento) = ?2 "
            + "ORDER BY f.vencimento ASC")
    List<Fatura> findByFaturaBeneficiarioAno(Long idBeneficiario, Integer ano);

    /**
     * Conta Fatura Pendente
     *
     * @return
     */
    @Query("SELECT count(f) FROM Fatura f WHERE f.status = 'PENDENTE' OR f.status = 'FECHADO'")
    Long countFaturaPendente();

    /**
     * Conta Fatura atrasada
     *
     * @return
     */
    @Query("SELECT count(f) FROM Fatura f WHERE f.status = 'ATRASADO'")
    Long countFaturaAtrasada();
    
    /**
     * Conta Fatura pagas do mês atual
     *
     * @return
     */
    @Query("SELECT count(f) FROM Fatura f "
    		+ "WHERE EXTRACT(MONTH FROM f.vencimento) = ?1 "
            + "AND EXTRACT(YEAR FROM f.vencimento) = ?2 "
            + "AND f.status = 'PAGO' "
    		+ "OR f.status = 'PARCELADO'")
    Long countFaturaPaga(Integer mes, Integer ano);

    @Query("SELECT f FROM Fatura f ORDER BY f.vencimento DESC, f.plano.beneficiario.nome ASC")
    List<Fatura> findAllByBeneficiario();
    
    /**
     * Fatura em aberto do associado
     * @return
     */
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario = ?1 AND f.status = 'PENDENTE'")
    List<Fatura> findFaturaPendenteBeneficiario(Beneficiario beneficirio);
    
    @Query("SELECT f FROM Fatura f WHERE f.plano.beneficiario.id = ?1 ORDER BY f.vencimento DESC")
    List<Fatura> findByFaturasDoMes(Long id);
    
    /**
     * Apenas para calcular multa
     * @return 
     */
    @Query("SELECT f FROM Fatura f WHERE f.status = 'ATRASADO' AND f.multaAplicada = false")
    List<Fatura> findByCalculoDaMulta();
    
    @Query("SELECT f FROM Fatura f WHERE f.status = 'ATRASADO' AND f.multaAplicada = true")
    List<Fatura> findByJuros();
       
}
