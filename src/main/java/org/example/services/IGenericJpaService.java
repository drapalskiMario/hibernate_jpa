package org.example.services;

import org.example.domain.Persistente;
import org.example.exceptions.DAOException;
import org.example.exceptions.MaisDeUmRegistroException;
import org.example.exceptions.TableException;
import org.example.exceptions.TipoChaveNaoEncontradaException;

import java.io.Serializable;
import java.util.Collection;

public interface IGenericJpaService<T extends Persistente, E extends Serializable> {

    T cadastrar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

    void excluir(T entity) throws DAOException;

    T alterar(T entity) throws TipoChaveNaoEncontradaException, DAOException;

    T consultar(E valor) throws MaisDeUmRegistroException, TableException, DAOException;

    Collection<T> buscarTodos() throws DAOException;

}
