package org.example.services;


import org.example.domain.Cliente;
import org.example.exceptions.DAOException;
import org.example.services.generic.IGenericService;

public interface IClienteService extends IGenericService<Cliente, Long> {

    Cliente buscarPorCPF(Long cpf) throws DAOException;


}
