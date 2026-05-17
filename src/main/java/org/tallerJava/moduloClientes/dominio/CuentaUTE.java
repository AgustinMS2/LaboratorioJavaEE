package org.tallerJava.moduloClientes.dominio;

import jakarta.persistence.Entity;

@Entity
public class CuentaUTE extends MedioPago {

    private String numeroCuenta;

    private String titular;

    private String direccionServicio;

    public CuentaUTE() {
        super();
    }

    public CuentaUTE(Long id,
                     String numeroCuenta,
                     String titular,
                     String direccionServicio) {

        super(id);
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.direccionServicio = direccionServicio;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }

    public String getDireccionServicio() { return direccionServicio; }
    public void setDireccionServicio(String direccionServicio) { this.direccionServicio = direccionServicio; }
}
