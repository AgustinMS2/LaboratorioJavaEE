package org.tallerJava.moduloCargas.dominio;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Carga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al cliente por ID para mantener desacoplamiento entre módulos
    private Long clienteId;

    private Long medioPagoId;

    @ManyToOne
    private Cargador cargador;

    private LocalDate fecha;

    private LocalDateTime horaInicio;

    private LocalDateTime horaFin;

    private Float importeTotal;

    private Float recargoPorDemora;

    // 0..100, solo si estado = ACTIVA
    private Integer porcentajeAvance;

    // Solo si estado = ACTIVA
    private LocalDateTime horaEstimadaFin;

    @Enumerated(EnumType.STRING)
    private EstadoCarga estado;

    public Carga() {
    }

    public Carga(Long clienteId, Cargador cargador, Long medioPagoId) {
        this.clienteId = clienteId;
        this.cargador = cargador;
        this.medioPagoId = medioPagoId;
        this.fecha = LocalDate.now();
        this.horaInicio = LocalDateTime.now();
        this.estado = EstadoCarga.ACTIVA;
        this.porcentajeAvance = 0;
    }

    public void finalizar(Float importeTotal, Float recargoPorDemora) {
        this.importeTotal = importeTotal;
        this.recargoPorDemora = recargoPorDemora;
        this.horaFin = LocalDateTime.now();
        this.estado = EstadoCarga.FINALIZADA;
        this.porcentajeAvance = 100;
        this.horaEstimadaFin = null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Cargador getCargador() { return cargador; }
    public void setCargador(Cargador cargador) { this.cargador = cargador; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalDateTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalDateTime horaFin) { this.horaFin = horaFin; }

    public Float getImporteTotal() { return importeTotal; }
    public void setImporteTotal(Float importeTotal) { this.importeTotal = importeTotal; }

    public Float getRecargoPorDemora() { return recargoPorDemora; }
    public void setRecargoPorDemora(Float recargoPorDemora) { this.recargoPorDemora = recargoPorDemora; }

    public Integer getPorcentajeAvance() { return porcentajeAvance; }
    public void setPorcentajeAvance(Integer porcentajeAvance) { this.porcentajeAvance = porcentajeAvance; }

    public LocalDateTime getHoraEstimadaFin() { return horaEstimadaFin; }
    public void setHoraEstimadaFin(LocalDateTime horaEstimadaFin) { this.horaEstimadaFin = horaEstimadaFin; }

    public EstadoCarga getEstado() { return estado; }
    public void setEstado(EstadoCarga estado) { this.estado = estado; }

    public Long getMedioPagoId() { return medioPagoId; }
    public void setMedioPagoId(Long medioPagoId) { this.medioPagoId = medioPagoId; }
}
