package org.example.dao.generic;


import org.example.dao.Persistente;
import org.example.exceptions.DAOException;
import org.example.exceptions.MaisDeUmRegistroException;
import org.example.exceptions.TableException;
import org.example.exceptions.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericDAO<T extends Persistente, E extends Serializable> {

    public Boolean cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

    public void excluir(E valor) throws DAOException;

    public void alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

    public T consultar(E valor) throws MaisDeUmRegistroException, TableException, DAOException;

    public Collection<T> buscarTodos() throws DAOException;
}
