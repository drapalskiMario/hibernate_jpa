package org.example.dao.jpa.impl;

import org.example.dao.generic.jpa.GenericJpaDAO;
import org.example.dao.jpa.IProdutoJpaDAO;
import org.example.domain.jpa.ProdutoJpa;

public class ProdutoJpaDAO extends GenericJpaDAO<ProdutoJpa, Long> implements IProdutoJpaDAO {

    public ProdutoJpaDAO() {
        super(ProdutoJpa.class);
    }

}
