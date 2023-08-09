import org.example.dao.IClienteDAO;
import org.example.dao.IProdutoDAO;
import org.example.dao.IVendaDAO;
import org.example.dao.generic.jdbc.ConnectionFactory;
import org.example.dao.impl.ClienteDAO;
import org.example.dao.impl.ProdutoDAO;
import org.example.dao.impl.VendaDAO;
import org.example.domain.Cliente;
import org.example.domain.Produto;
import org.example.domain.Venda;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;

public class VendaDAOTest {

    private IVendaDAO vendaDao;

    private IClienteDAO clienteDao;

    private IProdutoDAO produtoDao;

    private Cliente cliente;

    private Produto produto;

    public VendaDAOTest() {
        vendaDao = new VendaDAO();
        clienteDao = new ClienteDAO();
        produtoDao = new ProdutoDAO();
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
        clienteDao.excluir(this.cliente.getCpf());
    }


    private void excluirProdutos() throws DAOException {
       var list = this.produtoDao.buscarTodos();
        for (Produto prod : list) {
            this.produtoDao.excluir(prod.getCodigo());
        }
    }

    @Test
    public void pesquisar() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        var venda = criarVenda("A1");
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Venda vendaConsultada = vendaDao.consultar(venda.getCodigo());
        Assert.assertNotNull(vendaConsultada);
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }

    @Test
    public void salvar() throws TipoChaveNaoEncontradaException, DAOException, MaisDeUmRegistroException, TableException {
        Venda venda = criarVenda("A2");
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);

        Assert.assertTrue(venda.getValorTotal().equals(BigDecimal.valueOf(20)));
        Assert.assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));

        Venda vendaConsultada = vendaDao.consultar(venda.getCodigo());
        Assert.assertTrue(vendaConsultada.getId() != null);
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
    }


    @Test
    public void cancelarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A3";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        vendaDao.cancelarVenda(venda);

        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        Assert.assertEquals(codigoVenda, vendaConsultada.getCodigo());
        Assert.assertEquals(Venda.Status.CANCELADA, vendaConsultada.getStatus());
    }

    @Test
    public void adicionarMaisProdutosDoMesmo() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A4";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(produto, 1);

        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(30).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void adicionarMaisProdutosDiferentes() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A5";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);

        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test(expected = DAOException.class)
    public void salvarVendaMesmoCodigoExistente() throws TipoChaveNaoEncontradaException, DAOException {
        Venda venda = criarVenda("A6");
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);

        Boolean retorno1 = vendaDao.cadastrar(venda);
        Assert.assertFalse(retorno1);
        Assert.assertTrue(venda.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A7";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerApenasUmProduto() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A8";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 2);
        valorTotal = BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));
        Assert.assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void removerTodosProdutos() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A9";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        Produto prod = cadastrarProduto(codigoVenda, BigDecimal.valueOf(50));
        Assert.assertNotNull(prod);
        Assert.assertEquals(codigoVenda, prod.getCodigo());

        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        vendaConsultada.adicionarProduto(prod, 1);
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 3);
        BigDecimal valorTotal = BigDecimal.valueOf(70).setScale(2, RoundingMode.HALF_DOWN);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(valorTotal));


        vendaConsultada.removerTodosProdutos();
        Assert.assertTrue(vendaConsultada.getQuantidadeTotalProdutos() == 0);
        Assert.assertTrue(vendaConsultada.getValorTotal().equals(BigDecimal.valueOf(0)));
        Assert.assertTrue(vendaConsultada.getStatus().equals(Venda.Status.INICIADA));
    }

    @Test
    public void finalizarVenda() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A10";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        vendaDao.finalizarVenda(venda);

        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void tentarAdicionarProdutosVendaFinalizada() throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        String codigoVenda = "A11";
        Venda venda = criarVenda(codigoVenda);
        Boolean retorno = vendaDao.cadastrar(venda);
        Assert.assertTrue(retorno);
        Assert.assertNotNull(venda);
        Assert.assertEquals(codigoVenda, venda.getCodigo());

        vendaDao.finalizarVenda(venda);
        Venda vendaConsultada = vendaDao.consultar(codigoVenda);
        Assert.assertEquals(venda.getCodigo(), vendaConsultada.getCodigo());
        Assert.assertEquals(Venda.Status.CONCLUIDA, vendaConsultada.getStatus());

        vendaConsultada.adicionarProduto(this.produto, 1);

    }

    private Produto cadastrarProduto(String codigo, BigDecimal valor) throws TipoChaveNaoEncontradaException, MaisDeUmRegistroException, TableException, DAOException {
        Produto produto = new Produto();
        produto.setCodigo(codigo);
        produto.setDescricao("Produto 1");
        produto.setNome("Produto 1");
        produto.setValor(valor);
        produto.setCor("Azul");
        produtoDao.cadastrar(produto);
        return produto;
    }

    private Cliente cadastrarCliente() throws TipoChaveNaoEncontradaException, DAOException {
        Cliente cliente = new Cliente();
        cliente.setCpf(12312312312L);
        cliente.setNome("Rodrigo");
        cliente.setCidade("São Paulo");
        cliente.setEnd("End");
        cliente.setEstado("SP");
        cliente.setNumero(10);
        cliente.setTel(1199999999L);
        cliente.setSobrenome("Silva");
        clienteDao.cadastrar(cliente);
        return cliente;
    }

    private Venda criarVenda(String codigo) {
        var venda = new Venda();
        venda.setCodigo(codigo);
        venda.setDataVenda(Instant.now());
        venda.setCliente(this.cliente);
        venda.setStatus(Venda.Status.INICIADA);
        venda.adicionarProduto(this.produto, 2);
        return venda;
    }

    private void excluirVendas() throws DAOException {
        String sqlProd = "DELETE FROM PRODUTO_QUANTIDADE";
        executeDelete(sqlProd);

        String sqlV = "DELETE FROM VENDA";
        executeDelete(sqlV);
    }

    private void executeDelete(String sql) throws DAOException {
        Connection connection = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            stm = connection.prepareStatement(sql);
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("ERRO EXLUINDO OBJETO ", e);
        } finally {
            closeConnection(connection, stm, rs);
        }
    }

    protected void closeConnection(Connection connection, PreparedStatement stm, ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
            if (connection != null && !stm.isClosed()) {
                connection.close();
            }
        } catch (SQLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    protected Connection getConnection() throws DAOException {
        try {
            return ConnectionFactory.getConnection();
        } catch (SQLException e) {
            throw new DAOException("ERRO ABRINDO CONEXAO COM BANCO DE DADOS ", e);
        }
    }

}
