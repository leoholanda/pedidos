package net.bonsamigos.repository;

import java.util.List;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Perfil;

@Repository
public interface PerfilRepository extends EntityRepository<Perfil, Long> {
	
	List<Perfil> findAllOrderByNome();
	
	@Query("SELECT p FROM Perfil p WHERE p.status = 'ATIVADO'")
	List<Perfil> findAllByAtivado();
	
	@Query("SELECT distinct(p) FROM Perfil p JOIN FETCH p.modulos WHERE p.id = ?1")
	Perfil findByPerfil(Long id);

}
