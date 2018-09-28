package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Beneficiario;
import br.com.aee.model.Plano;
import br.com.aee.model.Status;
import br.com.aee.util.EntityRepository;

@Repository
public interface PlanoRepository extends EntityRepository<Plano, Long> {
	
//	@Query("SELECT p FROM Plano p WHERE p.beneficiario.status = ?1 AND p.convenio.id IN (1,2)")
	@Query("SELECT p FROM Plano p WHERE p.beneficiario.status = ?1 AND p.convenio.tipoConvenio != 'SERVICO' AND p.ativo = true")
	List<Plano> findByPlanoBeneficiarioAtivado(Status status);
	
	@Query("SELECT p FROM Plano p "
			+ " WHERE p.beneficiario.status = 'ATIVADO'"
			+ " AND p.beneficiario = ?1"
			+ " AND p.ativo = true")
	List<Plano> findByServico(Beneficiario beneficiario);
	
//	@Query("SELECT p FROM Plano p WHERE p.beneficiario = ?1 AND p.convenio.id IN (1,2) AND p.ativo = true")
	@Query("SELECT p FROM Plano p WHERE p.beneficiario = ?1 AND p.convenio.tipoConvenio != 'SERVICO' AND p.ativo = true")
	List<Plano> findByPlanoBeneficiario(Beneficiario beneficiario);

	@Query("SELECT p FROM Plano p WHERE p.beneficiario = ?1 AND p.ativo = true")
	List<Plano> findByPlanoAtivo(Beneficiario beneficiario);

	@Query("SELECT p FROM Plano p WHERE p.beneficiario = ?1 AND p.convenio.tipoConvenio != 'SERVICO' AND p.ativo = true")
	Plano findByPlanoDeSaude(Beneficiario beneficiario);
	
}
