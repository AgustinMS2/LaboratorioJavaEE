package org.tallerJava.moduloClientes.repositorio;

import org.tallerJava.moduloClientes.dominio.MedioPago;

import java.util.Optional;

public interface MedioPagoRepositorio {
    MedioPago guardar(MedioPago medioPago);
    Optional<MedioPago> buscarPorId(Long id);
}
