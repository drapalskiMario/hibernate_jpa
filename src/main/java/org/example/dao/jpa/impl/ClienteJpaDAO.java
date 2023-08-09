package org.example.dao.jpa.impl;

import org.example.dao.generic.jpa.GenericJpaDAO;
import org.example.dao.jpa.IClienteJpaDAO;
import org.example.domain.jpa.ClienteJpa;

public class ClienteJpaDAO extends GenericJpaDAO<ClienteJpa, Long> implements IClienteJpaDAO {

    public ClienteJpaDAO() {
        super(ClienteJpa.class);
    }

}
