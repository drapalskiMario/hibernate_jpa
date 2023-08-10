package org.example.dao.generic.impl;

import org.example.dao.generic.GenericJpaDAO;
import org.example.domain.Persistente;

import java.io.Serializable;

public class PostgresDAO<T extends Persistente, E extends Serializable> extends GenericJpaDAO<T, E> {

    public PostgresDAO(Class<T> persistenteClass) {
        super(persistenteClass, "postgres");
    }
}
