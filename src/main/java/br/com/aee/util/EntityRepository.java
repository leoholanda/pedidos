package br.com.aee.util;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface EntityRepository<E, PK extends Serializable> {

    @Transactional
    E save(E entity);

    @Transactional
    void remove(E entity);

    void refresh(E entity);

    void flush();

    E findBy(PK primaryKey);

    List<E> findAll();

    Long count();
}
