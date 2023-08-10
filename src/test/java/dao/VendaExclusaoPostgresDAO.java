package dao;

import org.example.dao.generic.impl.PostgresDAO;
import org.example.dao.jpa.IVendaJpaDAO;
import org.example.domain.VendaJpa;
import org.example.exceptions.DAOException;
import org.example.exceptions.TipoChaveNaoEncontradaException;

public class VendaExclusaoPostgresDAO extends PostgresDAO<VendaJpa, Long> implements IVendaJpaDAO {

    public VendaExclusaoPostgresDAO() {
        super(VendaJpa.class);
    }

    @Override
    public void finalizarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public void cancelarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public VendaJpa consultarComCollection(Long id) {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

}