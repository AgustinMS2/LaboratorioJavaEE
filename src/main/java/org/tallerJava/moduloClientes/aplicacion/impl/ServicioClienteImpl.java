package org.tallerJava.moduloClientes.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import org.tallerJava.moduloClientes.aplicacion.ServicioCliente;
import org.tallerJava.moduloClientes.dominio.Cliente;
import org.tallerJava.moduloClientes.dominio.MedioPago;
import org.tallerJava.moduloClientes.dominio.Reclamo;
import org.tallerJava.moduloClientes.dominio.repositorio.ClienteRepositorio;
import org.tallerJava.moduloClientes.dominio.repositorio.MedioPagoRepositorio;
import org.tallerJava.moduloClientes.dominio.repositorio.ReclamoRepositorio;
import org.tallerJava.moduloClientes.infraestructura.messaging.ProductorReclamos;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class ServicioClienteImpl implements ServicioCliente {

    @Inject
    private ClienteRepositorio clienteRepositorio;

    @Inject
    private MedioPagoRepositorio medioPagoRepositorio;

    @Inject
    private ReclamoRepositorio reclamoRepositorio;

    @Inject
    private ProductorReclamos productorReclamos;

    @Override
    public Cliente registrarCliente(Cliente cliente) {
        clienteRepositorio.buscarPorCedula(cliente.getCedula()).ifPresent(c -> {
            throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cliente.getCedula());
        });
        cliente.setContrasena(BCrypt.hashpw(cliente.getContrasena(), BCrypt.gensalt()));
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
        reclamoRepositorio.guardar(reclamo);
        cliente.agregarReclamo(reclamo);
        clienteRepositorio.guardar(cliente);

        // Procesamiento asincrónico: respondemos rápido al cliente y el
        // etiquetado (vía LLM) se hace en segundo plano consumiendo la queue.
        productorReclamos.encolar(reclamo.getId(), comentario);
    }
}
