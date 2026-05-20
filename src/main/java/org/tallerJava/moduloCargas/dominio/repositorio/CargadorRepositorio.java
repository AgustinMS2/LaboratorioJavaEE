package org.tallerJava.moduloCargas.dominio.repositorio;

import org.tallerJava.moduloCargas.dominio.Cargador;

import java.util.Optional;

public interface CargadorRepositorio {
    Cargador guardar(Cargador cargador);
    Optional<Cargador> buscarPorId(Long id);
}
