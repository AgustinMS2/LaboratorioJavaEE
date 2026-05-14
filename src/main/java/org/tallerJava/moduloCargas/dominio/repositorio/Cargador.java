package org.tallerJava.moduloCargas.dominio.repositorio;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Cargador {
    private Long id;

    private String codigo;

    private Double potenciaKw;

    private EstadoCargador estado;

    public Cargador() {
        this.estado = EstadoCargador.DISPONIBLE;
    }

    public Cargador(Long id, String codigo, Double potenciaKw) {
        this.id = id;
        this.codigo = codigo;
        this.potenciaKw = potenciaKw;
        this.estado = EstadoCargador.DISPONIBLE;
    }

    public void ocupar() {
        this.estado = EstadoCargador.OCUPADO;
    }

    public void liberar() {
        this.estado = EstadoCargador.DISPONIBLE;
    }

    public void fueraDeServicio() {
        this.estado = EstadoCargador.FUERA_SERVICIO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getPotenciaKw() {
        return potenciaKw;
    }

    public void setPotenciaKw(Double potenciaKw) {
        this.potenciaKw = potenciaKw;
    }

    public EstadoCargador getEstado() {
        return estado;
    }

    public void setEstado(EstadoCargador estado) {
        this.estado = estado;
    }

}
