package org.tallerJava.moduloCargas.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.tallerJava.moduloCargas.aplicacion.ServicioCarga;
import org.tallerJava.moduloCargas.dominio.Carga;
import org.tallerJava.moduloCargas.dominio.Cargador;
import org.tallerJava.moduloCargas.dominio.EstacionCarga;
import org.tallerJava.moduloCargas.dominio.repositorio.CargaRepositorio;
import org.tallerJava.moduloCargas.dominio.repositorio.CargadorRepositorio;
import org.tallerJava.moduloCargas.dominio.repositorio.EstacionCargaRepositorio;
import org.tallerJava.moduloCargas.interfase.evento.CargaFinalizadaEvento;
import org.tallerJava.moduloPagos.aplicacion.ServicioPago;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Transactional
public class ServicioCargaImpl implements ServicioCarga {

    @Inject
    private CargaRepositorio cargaRepositorio;

    @Inject
    private CargadorRepositorio cargadorRepositorio;

    @Inject
    private EstacionCargaRepositorio estacionCargaRepositorio;

    @Inject
    private Event<CargaFinalizadaEvento> cargaFinalizadaEvent;

    @Override
    public Carga iniciarCarga(Long clienteId, Long cargadorId, Long medioPagoId) {
        cargaRepositorio.buscarCargaActivaPorCliente(clienteId).ifPresent(c -> {
            throw new IllegalStateException("El cliente ya tiene una carga activa");
        });

        Cargador cargador = cargadorRepositorio.buscarPorId(cargadorId)
                .orElseThrow(() -> new IllegalArgumentException("Cargador no encontrado: " + cargadorId));

        cargador.ocupar(LocalDateTime.now().plusHours(1));
        cargadorRepositorio.guardar(cargador);

        Carga carga = new Carga(clienteId, cargador);
        return cargaRepositorio.guardar(carga);
    }

    @Override
    public Carga verCargaActual(Long clienteId) {
        return cargaRepositorio.buscarCargaActivaPorCliente(clienteId)
                .orElseThrow(() -> new IllegalStateException("El cliente no tiene una carga activa"));
    }

    @Override
    public List<Carga> verHistorico(Long clienteId, LocalDateTime desde, LocalDateTime hasta) {
        return cargaRepositorio.buscarPorClienteYFecha(clienteId, desde, hasta);
    }

    @Override
    public Carga finalizarCarga(Long cargadorId, Long cargaId, Double consumoKwh, Double recargo) {
        Carga carga = cargaRepositorio.buscarPorId(cargaId)
                .orElseThrow(() -> new IllegalArgumentException("Carga no encontrada: " + cargaId));

        Float importe = consumoKwh.floatValue();
        carga.finalizar(importe, recargo.floatValue());
        cargaRepositorio.guardar(carga);

        Cargador cargador = cargadorRepositorio.buscarPorId(cargadorId)
                .orElseThrow(() -> new IllegalArgumentException("Cargador no encontrado: " + cargadorId));
        cargador.liberar();
        cargadorRepositorio.guardar(cargador);

        cargaFinalizadaEvent.fire(new CargaFinalizadaEvento(carga.getClienteId(), cargaId, (double) importe));

        return carga;
    }

    @Override
    public EstacionCarga altaEstacion(EstacionCarga estacion) {
        return estacionCargaRepositorio.guardar(estacion);
    }

    @Override
    public void altaCargador(Long estacionId, Long cargadorId) {
        EstacionCarga estacion = estacionCargaRepositorio.buscarPorId(estacionId)
                .orElseThrow(() -> new IllegalArgumentException("Estación no encontrada: " + estacionId));

        Cargador cargador = cargadorRepositorio.buscarPorId(cargadorId)
                .orElseThrow(() -> new IllegalArgumentException("Cargador no encontrado: " + cargadorId));

        estacion.agregarCargador(cargador);
        estacionCargaRepositorio.guardar(estacion);
    }

    @Override
    public List<EstacionCarga> obtenerEstaciones() {
        return estacionCargaRepositorio.obtenerTodas();
    }

    @Override
    public Cargador altaCargador(Cargador cargador) {
        return cargadorRepositorio.guardar(cargador);
    }
}
