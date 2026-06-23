package org.tallerJava.moduloClientes.dominio.repositorio;

import org.tallerJava.moduloClientes.dominio.Reclamo;

import java.util.Optional;

public interface ReclamoRepositorio {

    Reclamo guardar(Reclamo reclamo);

    Optional<Reclamo> buscarPorId(Long id);
}
