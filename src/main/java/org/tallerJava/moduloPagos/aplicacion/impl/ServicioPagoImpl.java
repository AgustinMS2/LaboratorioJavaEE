package org.tallerJava.moduloPagos.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.tallerJava.moduloPagos.aplicacion.ServicioPago;
import org.tallerJava.moduloPagos.aplicacion.puerto.ConsultaMedioPago;
import org.tallerJava.moduloPagos.dominio.Pago;
import org.tallerJava.moduloPagos.dominio.repositorio.PagoRepositorio;
import org.tallerJava.moduloPagos.infraestructura.integracion.ClienteFacturaUTEHTTP;
import org.tallerJava.moduloPagos.infraestructura.integracion.ClienteMedioPagoHTTP;
import org.tallerJava.moduloPagos.interfase.evento.PagoRechazadoEvento;
import org.tallerJava.moduloPagos.interfase.evento.PagoTarjetaEvento;
import org.tallerJava.moduloPagos.interfase.evento.PagoUTEEvento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class ServicioPagoImpl implements ServicioPago {

    @Inject
    private PagoRepositorio pagoRepositorio;

    @Inject
    private ConsultaMedioPago consultaMedioPago;

    @Inject
    private ClienteMedioPagoHTTP clienteMedioPagoHTTP;

    @Inject
    private ClienteFacturaUTEHTTP clienteFacturaUTEHTTP;

    @Inject
    private Event<PagoTarjetaEvento> pagoTarjetaEvent;

    @Inject
    private Event<PagoUTEEvento> pagoUTEEvent;

    @Inject
    private Event<PagoRechazadoEvento> pagoRechazadoEvent;

    @Override
    public Pago pagarCarga(Long clienteId, Long cargaId, Double importe, Long medioPagoId) {
        ConsultaMedioPago.DatosMedioPago datos = consultaMedioPago.obtener(medioPagoId);

        String estado;
        if ("TARJETA".equals(datos.tipo())) {
            boolean aprobado = clienteMedioPagoHTTP.autorizar(
                    clienteId.toString(),
                    datos.numeroTarjeta(),
                    BigDecimal.valueOf(importe)
            );
            estado = aprobado ? "APROBADO" : "RECHAZADO";
            if (aprobado) {
                pagoTarjetaEvent.fire(new PagoTarjetaEvento(clienteId, cargaId));
            } else {
                pagoRechazadoEvent.fire(new PagoRechazadoEvento(clienteId, cargaId));
            }
        } else if ("CUENTA_UTE".equals(datos.tipo())) {
            clienteFacturaUTEHTTP.notificarPago(clienteId, cargaId, datos.numeroCuenta(), importe);
            estado = "PROCESADO";
            pagoUTEEvent.fire(new PagoUTEEvento(clienteId, cargaId));
        } else {
            estado = "PROCESADO";
        }

        Pago pago = new Pago(clienteId, cargaId, medioPagoId, importe);
        pago.setEstado(estado);
        return pagoRepositorio.guardar(pago);
    }

    @Override
    public List<Pago> consultarPagos(Long clienteId, LocalDateTime desde, LocalDateTime hasta) {
        return pagoRepositorio.buscarPorClienteYFecha(clienteId, desde, hasta);
    }

    @Override
    public void pagarDeuda(Long clienteId) {
        Pago pago = pagoRepositorio.buscarPagoRechazado(clienteId)
                .orElseThrow(() -> new IllegalStateException("El cliente no tiene deuda pendiente"));

        ConsultaMedioPago.DatosMedioPago datos = consultaMedioPago.obtener(pago.getMedioPagoId());
        clienteMedioPagoHTTP.pagarDeuda(datos.numeroTarjeta());

        pago.setEstado("SALDADO");
        pagoRepositorio.guardar(pago);
    }
}
