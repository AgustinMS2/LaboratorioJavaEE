package org.tallerJava.moduloCargas.aplicacion;

import org.tallerJava.moduloCargas.dominio.Carga;
import org.tallerJava.moduloCargas.dominio.EstacionCarga;

import java.time.LocalDateTime;
import java.util.List;

public interface ServicioCarga {

    // El cliente manifiesta la intención de realizar una carga (app móvil)
    Carga iniciarCarga(Long clienteId, Long cargadorId, Long medioPagoId);

    // Devuelve información de la carga activa del cliente (app móvil)
    Carga verCargaActual(Long clienteId);

    // Devuelve el histórico de cargas del cliente en el rango de fechas (app móvil)
    List<Carga> verHistorico(Long clienteId, LocalDateTime desde, LocalDateTime hasta);

    // Invocado por el cargador cuando el cable es desconectado (cargador)
    Carga finalizarCarga(Long cargadorId, Long cargaId, Double consumoKwh, Double recargo);

    // Da de alta una estación de carga (gestor web)
    EstacionCarga altaEstacion(EstacionCarga estacion);

    // Da de alta un cargador asociado a una estación (gestor web)
    void altaCargador(Long estacionId, Long cargadorId);

    // Retorna las estaciones disponibles con sus cargadores (app móvil)
    List<EstacionCarga> obtenerEstaciones();
}
