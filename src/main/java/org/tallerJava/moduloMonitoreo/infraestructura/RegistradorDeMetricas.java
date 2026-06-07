package org.tallerJava.moduloMonitoreo.infraestructura;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.influx.InfluxConfig;
import io.micrometer.influx.InfluxMeterRegistry;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Duration;

@ApplicationScoped
public class RegistradorDeMetricas {

    public static final String CARGAS_ACTIVAS = "cargasActivas";
    public static final String CARGAS_REALIZADAS = "cargasRealizadas";
    public static final String PAGOS_UTE = "pagosUTE";
    public static final String PAGOS_TARJETA = "pagosTarjeta";

    private InfluxConfig config;

    @PostConstruct
    public void init() {
        config = new InfluxConfig() {
            @Override
            public String get(String s) {
                return null;
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(10);
            }

            @Override
            public String db() {
                return "metricasTallerJava";
            }
        };
    }

    public void incrementarCounter(String nombreCounter) {
        MeterRegistry meterRegistry = new InfluxMeterRegistry(config, Clock.SYSTEM);
        meterRegistry.counter(nombreCounter).increment();
    }
}