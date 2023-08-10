package jpa;

import dao.VendaExclusaoMySqlDAO;
import org.example.dao.jpa.IClienteJpaDAO;
import org.example.dao.jpa.IProdutoJpaDAO;
import org.example.dao.jpa.IVendaJpaDAO;
import org.example.dao.jpa.impl.mysql.ClienteMySqlDAO;
import org.example.dao.jpa.impl.mysql.ProdutoMySqlDAO;
import org.example.dao.jpa.impl.mysql.VendaMySqlDAO;
import org.example.domain.ClienteJpa;
import org.example.domain.ProdutoJpa;
import org.example.domain.VendaJpa;
import org.example.exceptions.DAOException;
import org.example.exceptions.MaisDeUmRegistroException;
import org.example.exceptions.TableException;
import org.example.exceptions.TipoChaveNaoEncontradaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Random;

public class VendaMySqlDAOTest {

    private IVendaJpaDAO vendaDao;

    private IVendaJpaDAO vendaExclusaoDao;

    private IClienteJpaDAO clienteDao;

    private IProdutoJpaDAO produtoDao;

    private Random rd;

    private ClienteJpa cliente;

    private ProdutoJpa produto;

    public VendaMySqlDAOTest() {
        this.vendaDao = new VendaMySqlDAO();
        vendaExclusaoDao = new VendaExclusaoMySqlDAO();
        this.clienteDao = new ClienteMySqlDAO();
        this.produtoDao = new ProdutoMySqlDAO();
        rd = new Random();
    }

    @Before
    public void init() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        this.cliente = cadastrarCliente();
        this.produto = cadastrarProduto("A1", BigDecimal.TEN);
    }

    @After
    public void end() throws DAOException {
        excluirVendas();
        excluirProdutos();
        clienteDao.excluir(this.cliente);
    }

    @Test
    public void pesquisar() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        var venda = criarVenda("A1");
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        var vendaConsultada = vendaDao.consultar(venda.getId());
        Assert.assertNotNull(vendaConsultada);
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        var venda = criarVenda("A2");
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);

        Assert.assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
        Assert.assertTrue(venda.getStatus().equals(VendaJpa.Status.INICIADA));

        var vendaConsultada = vendaDao.consultar(venda.getId());
        Assert.assertTrue(vendaConsultada.getId() != null);
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void cancelarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        var codigoVenda = "A3";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        retorno.setStatus(VendaJpa.Status.CANCELADA);
        vendaDao.cancelarVenda(venda);

        var vendaConsultada = vendaDao.consultar(venda.getId());
        Assert.assertEquals(codigoVenda, vendaConsultada.getCodigo());
        Assert.assertEquals(VendaJpa.Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A4";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        var vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(produto, 1);

        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(VendaJpa.Status.INICIADA));
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        var codigoVenda = "A5";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        var prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        var vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);

        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        var valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(VendaJpa.Status.INICIADA));
    }

    @Test(expected = DAOException.class)
    public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {
        var venda = criarVenda("A6");
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);

        var venda1 = criarVenda("A6");
        var retorno1 = vendaDao.cadastrar(venda1);
        Assert.assertNull(retorno1);
        Assert.assertTrue(venda.getStatus().equals(VendaJpa.Status.INICIADA));
    }

    @Test
    public void removerProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        var codigoVenda = "A7";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        var prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        var vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(VendaJpa.Status.INICIADA));
    }

    @Test
    public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A8";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        ProdutoJpa prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        var vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(VendaJpa.Status.INICIADA));
    }

    @Test
    public void removerTodosProdutos() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A9";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        ProdutoJpa prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        var vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerTodosProdutos();
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
        Assert.assertTrue(vendaConsultada.getStatus().equals(VendaJpa.Status.INICIADA));
    }

    @Test
    public void finalizarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A10";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(VendaJpa.Status.CONCLUIDA);
        vendaDao.finalizarVenda(venda);

        var vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(VendaJpa.Status.CONCLUIDA, vendaConsultada.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A11";
        var venda = criarVenda(codigoVenda);
        var retorno = vendaDao.cadastrar(venda);
        Assert.assertNotNull(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        venda.setStatus(VendaJpa.Status.CONCLUIDA);
        vendaDao.finalizarVenda(venda);

        var vendaConsultada = vendaDao.consultarComCollection(venda.getId());
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(VendaJpa.Status.CONCLUIDA, vendaConsultada.getStatus());

        vendaConsultada.adicionarProduto(this.produto, 1);

    }


    private void excluirProdutos() throws DAOException {
        var list = this.produtoDao.buscarTodos();
        list.forEach(prod -> {
            try {
                this.produtoDao.excluir(prod);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private void excluirVendas() throws DAOException {
        var list = this.vendaExclusaoDao.buscarTodos();
        list.forEach(prod -> {
            try {
                this.vendaExclusaoDao.excluir(prod);
            } catch (DAOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    private ProdutoJpa cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        ProdutoJpa produto = new ProdutoJpa();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(valor);
        produtoDao.cadastrar(produto);
        return produto;
    }

    private ClienteJpa cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
        ClienteJpa cliente = new ClienteJpa();
        cliente.setCpf(rd.nextLong());
        cliente.setNome("Rodrigo");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        clienteDao.cadastrar(cliente);
        return cliente;
    }

    private VendaJpa criarVenda(String codigo) {
        var venda = new VendaJpa();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(VendaJpa.Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }
}
