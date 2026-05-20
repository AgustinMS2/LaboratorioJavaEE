package org.tallerJava.moduloPagos.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.tallerJava.moduloPagos.aplicacion.ServicioPago;
import org.tallerJava.moduloPagos.dominio.Pago;
import org.tallerJava.moduloPagos.dominio.repositorio.PagoRepositorio;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class ServicioPagoImpl implements ServicioPago {

    @Inject
    private PagoRepositorio pagoRepositorio;

    @Override
    public Pago pagarCarga(Long clienteId, Long cargaId, Double importe, Long medioPagoId) {
        Pago pago = new Pago(clienteId, cargaId, medioPagoId, importe);
        return pagoRepositorio.guardar(pago);
    }

    @Override
    public List<Pago> consultarPagos(Long clienteId, LocalDateTime desde, LocalDateTime hasta) {
        return pagoRepositorio.buscarPorClienteYFecha(clienteId, desde, hasta);
    }
}
