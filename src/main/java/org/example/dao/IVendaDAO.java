package org.example.dao;

import org.example.dao.generic.IGenericDAO;
import org.example.domain.Venda;
import org.example.exceptions.DAOException;
import org.example.exceptions.TipoChaveNaoEncontradaException;

public interface IVendaDAO extends IGenericDAO<Venda, String> {

	 void finalizarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
	
	 void cancelarVenda(Venda venda) throws TipoChaveNaoEncontradaException, DAOException;
}
