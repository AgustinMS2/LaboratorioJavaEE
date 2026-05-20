package org.tallerJava.moduloCargas.dominio;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Cargador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoCargador tipo;

    private Boolean tieneCable;

    @Enumerated(EnumType.STRING)
    private TipoConector tipoConector;

    @Enumerated(EnumType.STRING)
    private EstadoCargador estado;

    // Solo si estado = OCUPADO
    private LocalDateTime tiempoEstimadoFinalizacion;

    // Solo si estado = FUERA_SERVICIO
    private LocalDate fechaEstimadaReparacion;

    // Solo si tipo = RAPIDO
    private Integer potenciaMinima;

    public Cargador() {
        this.estado = EstadoCargador.DISPONIBLE;
    }

    public Cargador(TipoCargador tipo,
                    Boolean tieneCable,
                    TipoConector tipoConector,
                    Integer potenciaMinima) {
        this.tipo = tipo;
        this.tieneCable = tieneCable;
        this.tipoConector = tipoConector;
        this.potenciaMinima = potenciaMinima;
        this.estado = EstadoCargador.DISPONIBLE;
    }

    public void ocupar(LocalDateTime tiempoEstimadoFinalizacion) {
        this.estado = EstadoCargador.OCUPADO;
        this.tiempoEstimadoFinalizacion = tiempoEstimadoFinalizacion;
    }

    public void liberar() {
        this.estado = EstadoCargador.DISPONIBLE;
        this.tiempoEstimadoFinalizacion = null;
    }

    public void fueraDeServicio(LocalDate fechaEstimadaReparacion) {
        this.estado = EstadoCargador.FUERA_SERVICIO;
        this.fechaEstimadaReparacion = fechaEstimadaReparacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TipoCargador getTipo() { return tipo; }
    public void setTipo(TipoCargador tipo) { this.tipo = tipo; }

    public Boolean getTieneCable() { return tieneCable; }
    public void setTieneCable(Boolean tieneCable) { this.tieneCable = tieneCable; }

    public TipoConector getTipoConector() { return tipoConector; }
    public void setTipoConector(TipoConector tipoConector) { this.tipoConector = tipoConector; }

    public EstadoCargador getEstado() { return estado; }
    public void setEstado(EstadoCargador estado) { this.estado = estado; }

    public LocalDateTime getTiempoEstimadoFinalizacion() { return tiempoEstimadoFinalizacion; }
    public void setTiempoEstimadoFinalizacion(LocalDateTime t) { this.tiempoEstimadoFinalizacion = t; }

    public LocalDate getFechaEstimadaReparacion() { return fechaEstimadaReparacion; }
    public void setFechaEstimadaReparacion(LocalDate f) { this.fechaEstimadaReparacion = f; }

    public Integer getPotenciaMinima() { return potenciaMinima; }
    public void setPotenciaMinima(Integer potenciaMinima) { this.potenciaMinima = potenciaMinima; }
}
