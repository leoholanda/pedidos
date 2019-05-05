package net.bonsamigos.repository;

import java.util.List;
import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Usuario;

@Repository
public interface UsuarioRepository extends EntityRepository<Usuario, Long> {
	
	List<Usuario> findAllOrderByNomeAscSobrenomeAsc();

	Optional<Usuario> findByCpf(String cpf);

}
