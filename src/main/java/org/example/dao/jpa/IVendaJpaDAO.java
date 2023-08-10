package org.example.dao.jpa;

import org.example.dao.generic.IGenericJapDAO;
import org.example.domain.VendaJpa;
import org.example.exceptions.DAOException;
import org.example.exceptions.TipoChaveNaoEncontradaException;

public interface IVendaJpaDAO extends IGenericJapDAO<VendaJpa, Long> {

    public void finalizarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException;

    public void cancelarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException;

    public VendaJpa consultarComCollection(Long id);
}