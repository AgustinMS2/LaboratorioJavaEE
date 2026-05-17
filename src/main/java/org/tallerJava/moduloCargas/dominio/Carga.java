package org.tallerJava.moduloCargas.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Carga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia al cliente por ID para mantener desacoplamiento entre módulos
    private Long clienteId;

    @ManyToOne
    private Cargador cargador;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private Double consumoKwh;

    private Double importe;

    private Boolean finalizada;

    public Carga() {
    }

    public Carga(Long id, Long clienteId, Cargador cargador, LocalDateTime fechaInicio) {
        this.id = id;
        this.clienteId = clienteId;
        this.cargador = cargador;
        this.fechaInicio = fechaInicio;
        this.finalizada = false;
    }

    public void finalizar(Double consumoKwh, Double importe) {
        this.consumoKwh = consumoKwh;
        this.importe = importe;
        this.fechaFin = LocalDateTime.now();
        this.finalizada = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Cargador getCargador() { return cargador; }
    public void setCargador(Cargador cargador) { this.cargador = cargador; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public Double getConsumoKwh() { return consumoKwh; }
    public void setConsumoKwh(Double consumoKwh) { this.consumoKwh = consumoKwh; }

    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }

    public Boolean getFinalizada() { return finalizada; }
    public void setFinalizada(Boolean finalizada) { this.finalizada = finalizada; }
}
