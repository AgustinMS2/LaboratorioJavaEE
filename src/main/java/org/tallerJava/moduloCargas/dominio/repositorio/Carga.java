package org.tallerJava.moduloCargas.dominio.repositorio;

import org.tallerJava.moduloCargas.dominio.repositorio.Cliente;
import java.time.LocalDateTime;
import jakarta.persistence.*;
@Entity
public class Carga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cliente cliente;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    private Double consumoKwh;

    private Double importe;

    private Boolean finalizada;

    public Carga() {
    }

    public Carga(Long id, Cliente cliente, LocalDateTime fechaInicio) {
        this.id = id;
        this.cliente = cliente;
        this.fechaInicio = fechaInicio;
        this.finalizada = false;
    }

    public void finalizar(Double consumoKwh, Double importe) {
        this.consumoKwh = consumoKwh;
        this.importe = importe;
        this.fechaFin = LocalDateTime.now();
        this.finalizada = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Double getConsumoKwh() {
        return consumoKwh;
    }

    public void setConsumoKwh(Double consumoKwh) {
        this.consumoKwh = consumoKwh;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Boolean getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(Boolean finalizada) {
        this.finalizada = finalizada;
    }
}