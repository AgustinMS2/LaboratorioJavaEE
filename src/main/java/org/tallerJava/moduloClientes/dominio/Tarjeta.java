package org.tallerJava.moduloClientes.dominio;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Tarjeta extends MedioPago {

    private String numero;

    private LocalDate fechaVencimiento;

    private String digitoVerificacion;

    @Enumerated(EnumType.STRING)
    private TipoTarjeta tipo;

    public Tarjeta() {
        super();
    }

    public Tarjeta(Long id,
                   String numero,
                   LocalDate fechaVencimiento,
                   String digitoVerificacion,
                   TipoTarjeta tipo) {

        super(id);
        this.numero = numero;
        this.fechaVencimiento = fechaVencimiento;
        this.digitoVerificacion = digitoVerificacion;
        this.tipo = tipo;
    }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getDigitoVerificacion() { return digitoVerificacion; }
    public void setDigitoVerificacion(String digitoVerificacion) { this.digitoVerificacion = digitoVerificacion; }

    public TipoTarjeta getTipo() { return tipo; }
    public void setTipo(TipoTarjeta tipo) { this.tipo = tipo; }
}
