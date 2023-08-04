package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.domain.Acessorio;
import org.example.domain.Carro;
import org.example.domain.Marca;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        var emf = Persistence.createEntityManagerFactory("exemplo-jpa");
        var em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            var marca1 = new Marca();
            marca1.setNome("Marca 1");

            var carro1 = new Carro();
            carro1.setModelo("Carro Modelo 1");
            carro1.setMarca(marca1);

            var carro2 = new Carro();
            carro2.setModelo("Carro Modelo 2");
            carro2.setMarca(marca1);

            var acessorio1 = new Acessorio();
            acessorio1.setNome("Acessorio 1");

            var acessorio2 = new Acessorio();
            acessorio2.setNome("Acessorio 2");

            var acessoriosCarro1 = new ArrayList<Acessorio>();
            acessoriosCarro1.add(acessorio1);

            var acessoriosCarro2 = new ArrayList<Acessorio>();
            acessoriosCarro2.add(acessorio1);
            acessoriosCarro2.add(acessorio2);

            carro1.setAcessorios(acessoriosCarro1);
            carro2.setAcessorios(acessoriosCarro2);

            var carrosMarca1 = new ArrayList<Carro>();
            carrosMarca1.add(carro1);
            carrosMarca1.add(carro2);

            marca1.setCarros(carrosMarca1);

            em.persist(marca1);
            em.persist(carro1);
            em.persist(carro2);
            em.persist(acessorio1);
            em.persist(acessorio2);

            em.getTransaction().commit();

            em.getTransaction().begin();

            List<Marca> marcas = em.createQuery("from Marca", Marca.class).getResultList();
            for (Marca marca : marcas) {
                System.out.println("Marca: " + marca.getNome());
                for (Carro carro : marca.getCarros()) {
                    System.out.println("  Carro Modelo: " + carro.getModelo());
                    System.out.println("  Acessórios: ");
                    for (Acessorio acessorio : carro.getAcessorios()) {
                        System.out.println("    - " + acessorio.getNome());
                    }
                }
            }

            em.getTransaction().commit();
        } finally {
            em.close();
            emf.close();
        }
    }
}
