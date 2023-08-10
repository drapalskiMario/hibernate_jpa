package org.example.dao.jpa.impl.postgres;

import org.example.dao.generic.impl.PostgresDAO;
import org.example.dao.jpa.IClienteJpaDAO;
import org.example.domain.ClienteJpa;

public class ClientePostgresDAO extends PostgresDAO<ClienteJpa, Long> implements IClienteJpaDAO {

    public ClientePostgresDAO() {
        super(ClienteJpa.class);
    }

}
