package org.example.dao.jpa.impl.mysql;

import org.example.dao.generic.impl.MySqlDAO;
import org.example.dao.jpa.IClienteJpaDAO;
import org.example.domain.ClienteJpa;

public class ClienteMySqlDAO extends MySqlDAO<ClienteJpa, Long> implements IClienteJpaDAO {

    public ClienteMySqlDAO() {
        super(ClienteJpa.class);
    }

}
