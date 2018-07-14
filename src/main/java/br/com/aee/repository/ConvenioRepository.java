package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Convenio;
import br.com.aee.util.EntityRepository;

@Repository
public interface ConvenioRepository extends EntityRepository<Convenio, Long> {

	/**
	 * Lista convenios com status ativado por ordem alfabetica
	 * @return
	 */
	@Query("SELECT c FROM Convenio c WHERE c.status = 'ATIVADO' AND c.tipoConvenio = 'CONVENIO' ORDER BY c.nome")
	List<Convenio> findAllConvenios();
	
	@Query("SELECT c FROM Convenio c WHERE c.status = 'ATIVADO' AND c.tipoConvenio = 'SERVICO' ORDER BY c.nome")
	List<Convenio> findAllServicos();
	
	@Query("SELECT c FROM Convenio c WHERE c.status = 'ATIVADO' AND c.tipo = 'Plano de Saúde' ORDER BY c.nome")
	List<Convenio> findBySomentePlanoDeSaude();

	/**
	 * checa o nome do plano na lista
	 * @param nome
	 * @return
	 */
	@Query("SELECT c FROM Convenio c WHERE c.nome = ?1 AND c.status = 'ATIVADO'")
	List<Convenio> findAllByName(String nome);
	
	/**
	 * Pesquisa nome do convenio
	 * @param nome
	 * @return
	 */
	List<Convenio> findByNomeLike(String nome);
	
	/**
	 * Exibe serviços no index
	 * @return
	 */
	@Query("SELECT c FROM Convenio c WHERE c.status = 'ATIVADO' AND c.tipoConvenio = 'SERVICO' AND c.exibe = 'true' ORDER BY c.nome")
	List<Convenio> findAllServicosIndex();
	
	/**
	 * Exibe convenios no index
	 * @return
	 */
	@Query("SELECT c FROM Convenio c WHERE c.status = 'ATIVADO' AND c.tipoConvenio = 'CONVENIO' AND c.exibe = 'true' ORDER BY c.nome")
	List<Convenio> findAllConveniosIndex();
	
}
