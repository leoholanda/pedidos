package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Status;
import br.com.aee.model.Usuario;
import br.com.aee.util.EntityRepository;

@Repository
public interface UsuarioRepository extends EntityRepository<Usuario, Long> {

    @Query("SELECT distinct(u) FROM Usuario u join fetch u.permissoes")
    List<Usuario> findAllUsuarioPermissao();
	
	/**
	 * Lista usuario em ordem alfabetica
	 * @return
	 */
	List<Usuario> findAllOrderByNomeAsc();
	
	/**
	 * Lista usuario por status
	 * @return
	 */
//    @Query("SELECT distinct(u) FROM Usuario u join fetch u.permissoes where u.status = ?1")
	List<Usuario> findAllStatusOrderByNome(Status status);
	
	
    /**
     * Verifica se matricula do usuario já existe
     * Serve para não duplicar solicitacao
     * @param matricula
     * @return
     */
    @Query("SELECT u FROM Usuario u WHERE u.matricula = ?1")
    List<Usuario> findAllByMatricula(String matricula);
    
    @Query("SELECT u FROM Usuario u WHERE u.matricula = ?1 AND u.status = 'ATIVADO'")
    Usuario findByMatricula(String matricula);
    
    @Query("SELECT u FROM Usuario u WHERE u.matricula = ?1 AND u.senha = ?2 AND u.status = 'ATIVADO'")
    Usuario findByMatriculaAndSenha(String matricula, String senha);
    
    /**
     * Confere matricula, senha e status
     * Criado para autenticação, para isso o status deve ser ATIVADO 
     * @param matricula
     * @param senha
     * @param status
     * @return
     */
    @Query("SELECT u FROM Usuario u WHERE u.matricula = ?1 AND u.senha = ?2 AND u.status = ?3")
    List<Usuario> findAllByMatriculaAndSenhaAndStatus(String matricula, String senha, Status status);
    
    // Count
    
    /**
     * Count para quantidade de solicitações
     * Pode ser utilizado em outros casos (ex: usuario ATIVO)
     * @param status
     * @return
     */
    @Query("SELECT COUNT(p) FROM Usuario p where p.status = ?1")
    Long countStatus(Status status);
    
    List<Usuario> findByMatriculaOrNomeLikeOrderByNomeAsc(String matricula, String nome);
    
}
