package org.example.dao.generic.impl;

import org.example.dao.generic.GenericJpaDAO;
import org.example.domain.Persistente;

import java.io.Serializable;

public class MySqlDAO<T extends Persistente, E extends Serializable> extends GenericJpaDAO<T, E> {

    public MySqlDAO(Class<T> persistenteClass) {
        super(persistenteClass, "mysql");
    }
}
