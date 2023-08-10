package jpa;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ClientePostgresDaoTest.class,
        ProdutoPostgresDAOTest.class,
        VendaPostgresDAOTest.class,
        ClienteMySqlDaoTest.class,
        ProdutoMySqlDAOTest.class,
        VendaMySqlDAOTest.class
})
public class AllJpaTests {
}
