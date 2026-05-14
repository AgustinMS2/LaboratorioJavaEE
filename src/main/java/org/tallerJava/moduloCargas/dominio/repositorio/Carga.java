package org.tallerJava.moduloCargas.dominio.repositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Carga {
    private LocalDate fecha;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private float importeTotal;
    private float recargoPorDemora;
    private int porcentajeAvance;
    private LocalDateTime horaEstimadaFin;
    //private EstadoCarga estado;
}
