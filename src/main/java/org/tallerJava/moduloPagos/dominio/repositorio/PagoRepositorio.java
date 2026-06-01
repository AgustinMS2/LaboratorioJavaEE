package org.tallerJava.moduloPagos.dominio.repositorio;

import org.tallerJava.moduloPagos.dominio.Pago;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PagoRepositorio {
    Pago guardar(Pago pago);
    List<Pago> buscarPorClienteYFecha(Long clienteId, LocalDateTime desde, LocalDateTime hasta);
    boolean tieneDeudaPendiente(Long clienteId);
    Optional<Pago> buscarPagoRechazado(Long clienteId);
}
