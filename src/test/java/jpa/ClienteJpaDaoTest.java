package jpa;

import org.example.dao.jpa.IClienteJpaDAO;
import org.example.dao.jpa.impl.ClienteJpaDAO;
import org.example.domain.jpa.ClienteJpa;
import org.example.exceptions.DAOException;
import org.example.exceptions.MaisDeUmRegistroException;
import org.example.exceptions.TableException;
import org.example.exceptions.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.Random;

public class ClienteJpaDaoTest {

    private IClienteJpaDAO clienteDao;

    private Random rd;

    public ClienteJpaDaoTest() {
        this.clienteDao = new ClienteJpaDAO();
        rd = new Random();
    }

    @After
    public void end() throws DAOException {
        var list = clienteDao.buscarTodos();
        list.forEach(cli -> {
            try {
                clienteDao.excluir(cli);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @Test
    public void pesquisarCliente() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        var cliente = criarCliente();
        clienteDao.cadastrar(cliente);

        var clienteConsultado = clienteDao.consultar(cliente.getId());
        Assert.assertNotNull(clienteConsultado);

    }

    @Test
    public void salvarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        ClienteJpa cliente = criarCliente();
        ClienteJpa retorno = clienteDao.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        ClienteJpa clienteConsultado = clienteDao.consultar(retorno.getId());
        Assert.assertNotNull(clienteConsultado);

        clienteDao.excluir(cliente);

        ClienteJpa clienteConsultado1 = clienteDao.consultar(retorno.getId());
        Assert.assertNull(clienteConsultado1);
    }

    @Test
    public void excluirCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        ClienteJpa cliente = criarCliente();
        ClienteJpa retorno = clienteDao.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        ClienteJpa clienteConsultado = clienteDao.consultar(cliente.getId());
        Assert.assertNotNull(clienteConsultado);

        clienteDao.excluir(cliente);
        clienteConsultado = clienteDao.consultar(cliente.getId());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void alterarCliente() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        ClienteJpa cliente = criarCliente();
        ClienteJpa retorno = clienteDao.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        ClienteJpa clienteConsultado = clienteDao.consultar(cliente.getId());
        Assert.assertNotNull(clienteConsultado);

        clienteConsultado.setNome("Rodrigo Pires");
        clienteDao.alterar(clienteConsultado);

        ClienteJpa clienteAlterado = clienteDao.consultar(clienteConsultado.getId());
        Assert.assertNotNull(clienteAlterado);
        Assert.assertEquals("Rodrigo Pires", clienteAlterado.getNome());

        clienteDao.excluir(cliente);
        clienteConsultado = clienteDao.consultar(clienteAlterado.getId());
        Assert.assertNull(clienteConsultado);
    }

    @Test
    public void buscarTodos() throws TipoChaveNaoEncontradaException, DAOException {
        ClienteJpa cliente = criarCliente();
        ClienteJpa retorno = clienteDao.cadastrar(cliente);
        Assert.assertNotNull(retorno);

        ClienteJpa cliente1 = criarCliente();
        ClienteJpa retorno1 = clienteDao.cadastrar(cliente1);
        Assert.assertNotNull(retorno1);

        Collection<ClienteJpa> list = clienteDao.buscarTodos();
        Assert.assertTrue(list != null);
        Assert.assertTrue(list.size() == 2);

        list.forEach(cli -> {
            try {
                clienteDao.excluir(cli);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });

        Collection<ClienteJpa> list1 = clienteDao.buscarTodos();
        Assert.assertTrue(list1 != null);
        Assert.assertTrue(list1.size() == 0);
    }

    private ClienteJpa criarCliente() {
        var cliente = new ClienteJpa();
        cliente.setCpf(rd.nextLong());
        cliente.setNome("Mario");
        cliente.setCidade("Curitiba");
        cliente.setEnd("End");
        cliente.setEstado("PR");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        return cliente;
    }

}
