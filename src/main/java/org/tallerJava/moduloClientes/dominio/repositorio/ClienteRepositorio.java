package org.tallerJava.moduloClientes.dominio.repositorio;

import org.tallerJava.moduloClientes.dominio.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositorio {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorEmail(String email);
    List<Cliente> obtenerTodos();
}
