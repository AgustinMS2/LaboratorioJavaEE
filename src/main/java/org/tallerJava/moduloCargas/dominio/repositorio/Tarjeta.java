package org.tallerJava.moduloCargas.dominio.repositorio;

import java.time.LocalDate;

public class Tarjeta extends MedioPago{
    private String numero;

    private String titular;

    private String vencimiento;

    private String codigoSeguridad;

    private String marca;

    public Tarjeta(Long id,
                   String numero,
                   String titular,
                   String vencimiento,
                   String codigoSeguridad,
                   String marca) {

        super(id);

        this.numero = numero;
        this.titular = titular;
        this.vencimiento = vencimiento;
        this.codigoSeguridad = codigoSeguridad;
        this.marca = marca;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public String getCodigoSeguridad() {
        return codigoSeguridad;
    }

    public void setCodigoSeguridad(String codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

}
