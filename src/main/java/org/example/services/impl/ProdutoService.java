package org.example.services.impl;


import org.example.dao.generic.IGenericDAO;
import org.example.domain.Produto;
import org.example.services.IProdutoService;
import org.example.services.generic.impl.GenericService;

public class ProdutoService extends GenericService<Produto, String> implements IProdutoService {


    public ProdutoService(IGenericDAO<Produto, String> dao) {
        super(dao);
    }
}
