package org.tallerJava.moduloPagos.repositorio;

import org.tallerJava.moduloPagos.dominio.Pago;

import java.time.LocalDateTime;
import java.util.List;

public interface PagoRepositorio {
    Pago guardar(Pago pago);
    List<Pago> buscarPorClienteYFecha(Long clienteId, LocalDateTime desde, LocalDateTime hasta);
}
