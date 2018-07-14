package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Beneficiario;
import br.com.aee.model.Dependente;
import br.com.aee.util.EntityRepository;

@Repository
public interface DependenteRepository extends EntityRepository<Dependente, Long> {

    /**
     * Lista dependente em ordem alfabetica
     *
     * @return
     */
    List<Dependente> findAllOrderByNomeAsc();

    /**
     * Checa se há dados do cpf informado
     *
     * @param cpf
     * @return
     */
    List<Dependente> findByCpf(String cpf);

    /**
     * Utiliza dois parametros para pesquisar dependente
     *
     * @param cpf
     * @param titular
     * @param nome
     * @return
     */
    @Query("SELECT d FROM Dependente d WHERE d.cpf = ?1 or d.beneficiario.nome LIKE ?2 or d.nome LIKE ?3 ORDER BY d.nome")
    List<Dependente> findByCpfTitularNome(String cpf, String titular, String nome);

    /**
     * Soma o plano de saude do dependente
     *
     * @param beneficiario
     * @return
     */
    @Query("SELECT sum(faixaEtaria.valorApartamento) FROM Dependente d WHERE d.beneficiario = ?1")
    Double sumAcomodacaoApartamento(Beneficiario beneficiario);
    
    /**
     * Soma o plano de saude do dependente
     *
     * @param beneficiario
     * @return
     */
    @Query("SELECT sum(faixaEtaria.valorEnfermaria) FROM Dependente d WHERE d.beneficiario = ?1")
    Double sumAcomodacaoEnfermaria(Beneficiario beneficiario);

    /**
     * Total dependente com titular ativo
     *
     * @return
     */
    @Query("SELECT count(d) FROM Dependente d WHERE d.beneficiario.status = 'ATIVADO'")
    Long countDependenteAtivo();
    
    /**
     * Lista dependentes do beneficiario
     * @param beneficiario
     * @return
     */
    @Query("SELECT d FROM Dependente d WHERE d.beneficiario = ?1")
    List<Dependente> findByBeneficiario(Beneficiario beneficiario);
    
    @Query("SELECT d FROM Dependente d WHERE d.status = 'DESATIVADO'")
    List<Dependente> findByStatusDesativado();
}
