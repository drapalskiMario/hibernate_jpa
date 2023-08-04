package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.domain.Produto;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
        EntityManager em = emf.createEntityManager();

        Produto produto = new Produto();
        produto.setNome("Produto de Teste");
        produto.setPreco(19.99);

        em.getTransaction().begin();
        em.persist(produto);
        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}