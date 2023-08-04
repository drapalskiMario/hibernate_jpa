import org.example.dao.IProdutoDao;
import org.example.dao.impl.ProdutoDaoImpl;
import org.example.domain.Produto;
import org.junit.Assert;
import org.junit.Test;

public class CursoTest {

    private IProdutoDao produtoDao;

    public CursoTest() {
        produtoDao = new ProdutoDaoImpl();
    }

    @Test
    public void cadastrar() {

        var produto = produtoDao.cadastrar(new Produto("Produto de Teste", 19.99 ));

        Assert.assertNotNull(produto);
        Assert.assertNotNull(produto.getId());
    }
}
