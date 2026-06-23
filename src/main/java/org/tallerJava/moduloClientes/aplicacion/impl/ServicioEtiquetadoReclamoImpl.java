package org.tallerJava.moduloClientes.aplicacion.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.tallerJava.moduloClientes.aplicacion.ServicioEtiquetadoReclamo;
import org.tallerJava.moduloClientes.dominio.EtiquetaReclamo;
import org.tallerJava.moduloClientes.dominio.Reclamo;
import org.tallerJava.moduloClientes.dominio.repositorio.ReclamoRepositorio;
import org.tallerJava.moduloClientes.interfase.evento.ReclamoNegativoEvento;

@ApplicationScoped
@Transactional
public class ServicioEtiquetadoReclamoImpl implements ServicioEtiquetadoReclamo {

    @Inject
    private ReclamoRepositorio reclamoRepositorio;

    @Inject
    private Event<ReclamoNegativoEvento> reclamoNegativoEvent;

    @Override
    public void etiquetar(Long reclamoId, EtiquetaReclamo etiqueta) {
        Reclamo reclamo = reclamoRepositorio.buscarPorId(reclamoId)
                .orElseThrow(() -> new IllegalArgumentException("Reclamo no encontrado: " + reclamoId));

        reclamo.setEtiqueta(etiqueta);
        reclamoRepositorio.guardar(reclamo);

        if (etiqueta == EtiquetaReclamo.NEGATIVO) {
            reclamoNegativoEvent.fire(new ReclamoNegativoEvento(reclamoId));
        }
    }
}
