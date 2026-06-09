package org.tallerJava.moduloMonitoreo.infraestructura;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class RegistradorDeMetricas {

    public static final String CARGAS_ACTIVAS    = "cargasActivas";
    public static final String CARGAS_REALIZADAS = "cargasRealizadas";
    public static final String PAGOS_UTE         = "pagosUTE";
    public static final String PAGOS_TARJETA     = "pagosTarjeta";
    public static final String PAGOS_RECHAZADOS  = "pagosRechazados";

    private MeterRegistry meterRegistry;
    private AtomicInteger cargasActivas;

    @PostConstruct
    public void init() {
        InfluxConfig config = new InfluxConfig() {
            @Override public String get(String s) { return null; }
            @Override public Duration step() { return Duration.ofSeconds(10); }
            @Override public String db() { return "metricasTallerJava"; }
        };
        meterRegistry = new InfluxMeterRegistry(config, Clock.SYSTEM);
        cargasActivas = new AtomicInteger(0);
        Gauge.builder(CARGAS_ACTIVAS, cargasActivas, AtomicInteger::get)
             .register(meterRegistry);
    }

    public void incrementarCargasActivas() {
        cargasActivas.incrementAndGet();
    }

    public void decrementarCargasActivas() {
        cargasActivas.decrementAndGet();
    }

    public void incrementarCounter(String nombreCounter) {
        meterRegistry.counter(nombreCounter).increment();
    }
}
