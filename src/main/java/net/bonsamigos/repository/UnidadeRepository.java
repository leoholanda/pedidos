package net.bonsamigos.repository;

import java.util.List;
import java.util.Optional;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import net.bonsamigos.enums.Area;
import net.bonsamigos.model.Unidade;

@Repository
public interface UnidadeRepository extends EntityRepository<Unidade, Long> {
	
	List<Unidade> findAllOrderByCodigo();
	
	List<Unidade> findByNomeLikeOrderByCodigo(String nome);
	
	@Query("SELECT u FROM Unidade u WHERE u.area = ?1 ORDER BY u.codigo ASC")
	List<Unidade> findByArea(Area area);
	
	@Query("SELECT u FROM Unidade u WHERE u.nome LIKE ?1 ORDER BY u.codigo ASC")
	List<Unidade> findByNome(String nome);

	@Query("SELECT u FROM Unidade u WHERE u.codigo = ?1 AND u.area = ?2 ORDER BY u.codigo ASC")
	Optional<Unidade> findByCodigo(Long codigo, Area area);

	@Query("SELECT count(u) FROM Unidade u WHERE u.area = ?1")
	Long countByArea(Area area);
    
}
