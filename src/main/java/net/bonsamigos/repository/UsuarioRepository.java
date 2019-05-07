package net.bonsamigos.repository;

import java.util.List;
import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.model.Usuario;

@Repository
public interface UsuarioRepository extends EntityRepository<Usuario, Long> {
	
	List<Usuario> findAllOrderByNomeAscSobrenomeAsc();

	Optional<Usuario> findByCpf(String cpf);
	
	@Query("SELECT u FROM Usuario u WHERE u.cpf = ?1")
	Usuario checksSecurity(String cpf);
	
	@Query("SELECT u FROM Usuario u WHERE u.cpf = ?1 AND u.senha = ?2")
	Usuario checkAutentication(String cpf, String senha);
	
	@Query("SELECT u FROM Usuario u JOIN FETCH u.perfil p WHERE u.id = ?1")
	Usuario load(Long id);
}
