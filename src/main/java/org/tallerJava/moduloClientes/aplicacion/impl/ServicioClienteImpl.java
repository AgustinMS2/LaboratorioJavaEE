package org.tallerJava.moduloClientes.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.tallerJava.moduloClientes.aplicacion.ServicioCliente;
import org.tallerJava.moduloClientes.dominio.Cliente;
import org.tallerJava.moduloClientes.dominio.MedioPago;
import org.tallerJava.moduloClientes.dominio.Reclamo;
import org.tallerJava.moduloClientes.dominio.repositorio.ClienteRepositorio;
import org.tallerJava.moduloClientes.dominio.repositorio.MedioPagoRepositorio;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class ServicioClienteImpl implements ServicioCliente {

    @Inject
    private ClienteRepositorio clienteRepositorio;

    @Inject
    private MedioPagoRepositorio medioPagoRepositorio;

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        clienteRepositorio.buscarPorCedula(cliente.getCedula()).ifPresent(c -> {
            throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cliente.getCedula());
        });
        return clienteRepositorio.guardar(cliente);
    }

    @Override
    public void altaMedioPago(Long clienteId, MedioPago medioPago) {
        Cliente cliente = clienteRepositorio.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));

        medioPagoRepositorio.guardar(medioPago);
        cliente.agregarMedioPago(medioPago);
        clienteRepositorio.guardar(cliente);
    }

    @Override
    public List<Cliente> obtenerClientes() {
        return clienteRepositorio.obtenerTodos();
    }

    @Override
    public void realizarReclamo(Long clienteId, String comentario) {
        Cliente cliente = clienteRepositorio.buscarPorId(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + clienteId));

        Reclamo reclamo = new Reclamo(null, comentario, LocalDateTime.now());
        cliente.agregarReclamo(reclamo);
        clienteRepositorio.guardar(cliente);
    }
}
