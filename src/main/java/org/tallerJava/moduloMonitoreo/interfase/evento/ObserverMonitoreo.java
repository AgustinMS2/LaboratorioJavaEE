package org.tallerJava.moduloMonitoreo.interfase.evento;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.tallerJava.moduloCargas.interfase.evento.CargaFinalizadaEvento;
import org.tallerJava.moduloCargas.interfase.evento.CargaIniciadaEvento;
import org.tallerJava.moduloPagos.interfase.evento.PagoTarjetaEvento;
import org.tallerJava.moduloPagos.interfase.evento.PagoUTEEvento;
import org.tallerJava.moduloMonitoreo.infraestructura.RegistradorDeMetricas;

@ApplicationScoped
public class ObserverMonitoreo {
    private static final Logger log = Logger.getLogger(ObserverMonitoreo.class);

    @Inject
    private RegistradorDeMetricas registrador;

    public void onCargaIniciada(@Observes CargaIniciadaEvento evento) {
        log.infof("Evento procesado: CargaIniciada cliente %s", evento.getClienteId());
        registrador.incrementarCounter(RegistradorDeMetricas.CARGAS_ACTIVAS);
    }

    public void onCargaFinalizada(@Observes CargaFinalizadaEvento evento) {
        log.infof("Evento procesado: CargaFinalizada cliente %s", evento.getClienteId());
        registrador.incrementarCounter(RegistradorDeMetricas.CARGAS_REALIZADAS);
    }

    public void onPagoTarjeta(@Observes PagoTarjetaEvento evento) {
        log.infof("Evento procesado: PagoTarjeta cliente %s", evento.getClienteId());
        registrador.incrementarCounter(RegistradorDeMetricas.PAGOS_TARJETA);
    }

    public void onPagoUTE(@Observes PagoUTEEvento evento) {
        log.infof("Evento procesado: PagoUTE cliente %s", evento.getClienteId());
        registrador.incrementarCounter(RegistradorDeMetricas.PAGOS_UTE);
    }
}