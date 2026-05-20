package org.tallerJava.moduloCargas.dominio.repositorio;

import org.tallerJava.moduloCargas.dominio.EstacionCarga;

import java.util.List;
import java.util.Optional;

public interface EstacionCargaRepositorio {
    EstacionCarga guardar(EstacionCarga estacion);
    Optional<EstacionCarga> buscarPorId(Long id);
    List<EstacionCarga> obtenerTodas();
}
