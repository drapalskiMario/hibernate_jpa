package jpa;

import org.example.dao.jpa.IProdutoJpaDAO;
import org.example.dao.jpa.impl.postgres.ProdutoPostgresDAO;
import org.example.domain.ProdutoJpa;
import org.example.exceptions.DAOException;
import org.example.exceptions.MaisDeUmRegistroException;
import org.example.exceptions.TableException;
import org.example.exceptions.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ProdutoPostgresDAOTest {

    private IProdutoJpaDAO produtoDao;

    public ProdutoPostgresDAOTest() {
        this.produtoDao = new ProdutoPostgresDAO();
    }

    @After
    public void end() throws DAOException {
        var list = produtoDao.buscarTodos();
        list.forEach(cli -> {
            try {
                produtoDao.excluir(cli);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    @Test
    public void pesquisar() throws MaisDeUmRegistroException, TableException, DAOException, TipoChaveNaoEncontradaException {
        var produto = criarProduto("A1");
        Assert.assertNotNull(produto);
        var produtoDB = this.produtoDao.consultar(produto.getId());
        Assert.assertNotNull(produtoDB);
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontradaException, DAOException {
        var produto = criarProduto("A2");
        Assert.assertNotNull(produto);
    }

    @Test
    public void excluir() throws DAOException, TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException {
        var produto = criarProduto("A3");
        Assert.assertNotNull(produto);
        this.produtoDao.excluir(produto);
        var produtoBD = this.produtoDao.consultar(produto.getId());
        Assert.assertNull(produtoBD);
    }

    @Test
    public void alterarCliente() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        var produto = criarProduto("A4");
        produto.setNome("Rodrigo Pires");
        produtoDao.alterar(produto);
        var produtoBD = this.produtoDao.consultar(produto.getId());
        Assert.assertNotNull(produtoBD);
        Assert.assertEquals("Rodrigo Pires", produtoBD.getNome());
    }

    @Test
    public void buscarTodos() throws DAOException, TipoChaveNaoEncontradaException {
        criarProduto("A5");
        criarProduto("A6");
        var list = produtoDao.buscarTodos();
        Assert.assertTrue(list != null);
        Assert.assertTrue(list.size() == 2);

        for (ProdutoJpa prod : list) {
            this.produtoDao.excluir(prod);
        }

        list = produtoDao.buscarTodos();
        Assert.assertTrue(list != null);
        Assert.assertTrue(list.size() == 0);

    }

    private ProdutoJpa criarProduto(String codigo) throws TipoChaveNaoEncontradaException, DAOException {
        var produto = new ProdutoJpa();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(BigDecimal.TEN);
        produtoDao.cadastrar(produto);
        return produto;
    }
}
