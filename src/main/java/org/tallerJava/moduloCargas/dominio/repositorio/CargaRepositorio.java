package org.tallerJava.moduloCargas.dominio.repositorio;

import org.tallerJava.moduloCargas.dominio.Carga;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CargaRepositorio {
    Carga guardar(Carga carga);
    Optional<Carga> buscarPorId(Long id);
    Optional<Carga> buscarCargaActivaPorCliente(Long clienteId);
    List<Carga> buscarPorClienteYFecha(Long clienteId, LocalDateTime desde, LocalDateTime hasta);
}
