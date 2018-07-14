package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Perfil;
import br.com.aee.util.EntityRepository;

@Repository
public interface PerfilRepository extends EntityRepository<Perfil, Long> {
	
	List<Perfil> findAllOrderByNomeAsc();


}
