package org.example.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.dao.IProdutoDao;
import org.example.domain.Produto;

public class ProdutoDaoImpl implements IProdutoDao {

    @Override
    public Produto cadastrar(Produto produto) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(produto);
        em.getTransaction().commit();

        em.close();
        emf.close();

        return produto;
    }
}
