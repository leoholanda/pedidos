package br.com.aee.repository;

import java.util.List;

import org.apache.deltaspike.data.api.Query;
import org.apache.deltaspike.data.api.Repository;

import br.com.aee.model.Setor;
import br.com.aee.util.EntityRepository;

@Repository
public interface SetorRepository extends EntityRepository<Setor, Long> {

    /**
     * Lista setor em ordem alfabetica
     *
     * @return
     */
    @Query("SELECT s FROM Setor s ORDER BY s.nome")
    List<Setor> findOrderByNome();

    /**
     * Procura nome do setor na lista
     *
     * @param nome
     * @return
     */
    @Query("SELECT s FROM Setor s WHERE s.nome = ?1")
    List<Setor> findAllByName(String nome);

    /**
     * Pesquisa setor por nome
     *
     * @param nome
     * @return
     */
    List<Setor> findByNomeLikeOrderByNomeAsc(String nome);

}
