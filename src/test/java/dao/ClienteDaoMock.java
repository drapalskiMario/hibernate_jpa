package dao;

import org.example.dao.IClienteDAO;
import org.example.domain.Cliente;
import org.example.exceptions.TipoChaveNaoEncontradaException;

import java.util.Collection;

public class ClienteDaoMock implements IClienteDAO {

	@Override
	public Boolean cadastrar(Cliente entity) throws TipoChaveNaoEncontradaException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void excluir(Long valor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void alterar(Cliente entity) throws TipoChaveNaoEncontradaException {
		// TODO Auto-generated method stub

	}

	@Override
	public Cliente consultar(Long valor) {
		var cliente = new Cliente();
		cliente.setCpf(valor);
		return cliente;
	}

	@Override
	public Collection<Cliente> buscarTodos() {
		// TODO Auto-generated method stub
		return null;
	}


}
