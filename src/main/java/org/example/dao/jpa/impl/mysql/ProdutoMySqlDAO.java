package org.example.dao.jpa.impl.mysql;

import org.example.dao.generic.impl.MySqlDAO;
import org.example.dao.generic.impl.PostgresDAO;
import org.example.dao.jpa.IProdutoJpaDAO;
import org.example.domain.ProdutoJpa;

public class ProdutoMySqlDAO extends MySqlDAO<ProdutoJpa, Long> implements IProdutoJpaDAO {

    public ProdutoMySqlDAO() {
        super(ProdutoJpa.class);
    }

}
