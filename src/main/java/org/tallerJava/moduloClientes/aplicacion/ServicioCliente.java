package org.tallerJava.moduloClientes.aplicacion;

import org.tallerJava.moduloClientes.dominio.Cliente;
import org.tallerJava.moduloClientes.dominio.MedioPago;

import java.util.List;

public interface ServicioCliente {

    // Registra un nuevo cliente en el sistema (app móvil)
    Cliente registrarCliente(Cliente cliente);

    // Agrega un medio de pago a un cliente (app móvil)
    void altaMedioPago(Long clienteId, MedioPago medioPago);

    // Devuelve todos los clientes registrados (gestor web)
    List<Cliente> obtenerClientes();

    // Registra un reclamo del cliente (app móvil)
    void realizarReclamo(Long clienteId, String comentario);
}
