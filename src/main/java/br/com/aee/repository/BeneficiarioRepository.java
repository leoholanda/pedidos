package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Beneficiario;
import br.com.aee.util.EntityRepository;

@Repository
public interface BeneficiarioRepository extends EntityRepository<Beneficiario, Long> {

    /**
     * Lista todos os beneficiarios por ordem alfabetica
     *
     * @return
     */
    List<Beneficiario> findAllOrderByNomeAsc();

    /**
     * Utiliza tres parametros para pesquisar beneficiario
     *
     * @param cpf, nome, matricula
     * @return
     */
    List<Beneficiario> findByCpfOrMatriculaOrNomeLikeOrderByNomeAsc(String cpf, String matricula, String nome);

    /**
     * Verifica se cpf do titular ja foi cadastrado
     *
     * @param cpf
     * @return
     */
    List<Beneficiario> findByCpf(String cpf);

    /**
     *
     * @param cpf
     * @return
     */
    @Query("SELECT b FROM Beneficiario b WHERE b.cpf = ?1 AND b.status = 'ATIVADO'")
    Beneficiario findByCpfParaBeneficiario(String cpf);

    @Query("SELECT b FROM Beneficiario b WHERE b.cpf = ?1 AND b.sus = ?2 AND b.status = 'ATIVADO'")
    List<Beneficiario> findByCpfAndSus(String cpf, Long sus);

    @Query("SELECT b FROM Beneficiario b WHERE b.cpf = ?1 AND b.status = 'ATIVADO'")
    List<Beneficiario> findByCpfAndStatus(String cpf);

    /**
     * Busca beneficiario pelo nome
     *
     * @param nome
     * @return
     */
    @Query("SELECT b FROM Beneficiario b WHERE b.nome LIKE ?1 ORDER BY b.nome ASC")
    List<Beneficiario> findByNome(String nome);

    /**
     * Busca beneficiario pela matricula
     *
     * @param matricula
     * @return
     */
    List<Beneficiario> findByMatriula();

    /**
     * Busca somente beneficiario servidor
     *
     * @param matricula
     * @return
     */
    @Query("SELECT b FROM Beneficiario b WHERE b.matricula != null ORDER BY b.nome ASC")
    List<Beneficiario> findByServidor();

    /**
     * Busca os beneficiarios para gerar fatura
     *
     * @return
     */
    @Query("SELECT b FROM Beneficiario b WHERE b.status = 'ATIVADO'")
    List<Beneficiario> findBeneficiarioAtivado();

    /**
     * Total beneficiario ativo
     *
     * @return
     */
    @Query("SELECT count(b) FROM Beneficiario b WHERE b.status = 'ATIVADO'")
    Long countBeneficiarioAtivo();
    
    /**
     * Total beneficiario ativo
     *
     * @return
     */
    @Query("SELECT count(b) FROM Beneficiario b WHERE b.status = 'ATIVADO' AND b.temPlanoDeSaude = true")
    Long countBeneficiarioAtivo2();

}
