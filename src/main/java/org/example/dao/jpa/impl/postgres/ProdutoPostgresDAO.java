package org.example.dao.jpa.impl.postgres;

import org.example.dao.generic.impl.PostgresDAO;
import org.example.dao.jpa.IProdutoJpaDAO;
import org.example.domain.ProdutoJpa;

public class ProdutoPostgresDAO extends PostgresDAO<ProdutoJpa, Long> implements IProdutoJpaDAO {

    public ProdutoPostgresDAO() {
        super(ProdutoJpa.class);
    }

}
