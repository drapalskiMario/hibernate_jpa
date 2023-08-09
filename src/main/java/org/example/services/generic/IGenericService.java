package org.example.services.generic;


import org.example.dao.Persistente;
import org.example.exceptions.DAOException;
import org.example.exceptions.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericService<T extends Persistente, E extends Serializable> {

    Boolean cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

    void excluir(E valor) throws DAOException;

    void alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

    T consultar(E valor) throws DAOException;

    Collection<T> buscarTodos() throws DAOException;

}
