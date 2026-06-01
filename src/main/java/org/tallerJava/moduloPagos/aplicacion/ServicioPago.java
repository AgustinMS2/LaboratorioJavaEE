package org.tallerJava.moduloPagos.aplicacion;

import org.tallerJava.moduloPagos.dominio.Pago;

import java.time.LocalDateTime;
import java.util.List;

public interface ServicioPago {

    // Cobra la carga utilizando el medio de pago correspondiente (invocado por módulo de carga)
    Pago pagarCarga(Long clienteId, Long cargaId, Double importe, Long medioPagoId);

    // Retorna pagos del cliente en el rango de fechas (gestor web)
    List<Pago> consultarPagos(Long clienteId, LocalDateTime desde, LocalDateTime hasta);

    // Salda la deuda pendiente del cliente invocando al sistema externo de medio de pago
    void pagarDeuda(Long clienteId);
}
