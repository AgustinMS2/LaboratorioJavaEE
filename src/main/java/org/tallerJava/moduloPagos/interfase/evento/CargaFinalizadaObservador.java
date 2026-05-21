package org.tallerJava.moduloPagos.interfase.evento;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.tallerJava.moduloCargas.interfase.evento.CargaFinalizadaEvento;
import org.tallerJava.moduloPagos.aplicacion.ServicioPago;

@ApplicationScoped
public class CargaFinalizadaObservador {

    @Inject
    private ServicioPago servicioPago;

    public void onCargaFinalizada(@Observes CargaFinalizadaEvento evento) {
        servicioPago.pagarCarga(
                evento.getClienteId(),
                evento.getCargaId(),
                evento.getImporte(),
                null
        );
    }
}
