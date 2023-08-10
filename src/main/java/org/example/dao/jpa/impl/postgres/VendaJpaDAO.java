package org.example.dao.jpa.impl.postgres;

import org.example.dao.generic.impl.PostgresDAO;
import org.example.dao.jpa.IVendaJpaDAO;
import org.example.domain.VendaJpa;
import org.example.exceptions.DAOException;
import org.example.exceptions.TipoChaveNaoEncontradaException;

public class VendaJpaDAO extends PostgresDAO<VendaJpa, Long> implements IVendaJpaDAO {

    public VendaJpaDAO() {
        super(VendaJpa.class);
    }

    @Override
    public void finalizarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException {
        super.alterar(venda);
    }

    @Override
    public void cancelarVenda(VendaJpa venda) throws TipoChaveNaoEncontradaException, DAOException {
        super.alterar(venda);
    }

    @Override
    public void excluir(VendaJpa entity) throws DAOException {
        throw new UnsupportedOperationException("OPERAÇÃO NÃO PERMITIDA");
    }

    @Override
    public VendaJpa cadastrar(VendaJpa entity) throws TipoChaveNaoEncontradaException, DAOException {
        try {
            openConnection();
            entity.getProdutos().forEach(prod -> {
                var prodJpa = entityManager.merge(prod.getProduto());
                prod.setProduto(prodJpa);
            });
            var cliente = entityManager.merge(entity.getCliente());
            entity.setCliente(cliente);
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            closeConnection();
            return entity;
        } catch (Exception e) {
            throw new DAOException("ERRO SALVANDO VENDA ", e);
        }

    }

    @Override
    public VendaJpa consultarComCollection(Long id) {
        openConnection();

        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(VendaJpa.class);
        var root = query.from(VendaJpa.class);
        root.fetch("cliente");
        root.fetch("produtos");
        query.select(root).where(builder.equal(root.get("id"), id));
        var tpQuery = entityManager.createQuery(query);
        VendaJpa venda = tpQuery.getSingleResult();
        closeConnection();
        return venda;
    }


}
