package org.tallerJava.moduloClientes.dominio;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes_clienteProfesional")
public class ClienteProfesional extends Cliente {

    @Enumerated(EnumType.STRING)
    private TipoProfesional tipo;

    private Float porcentajeDescuento;

    public ClienteProfesional() {
        super();
    }

    public ClienteProfesional(Long id,
                              String cedula,
                              String nombreCompleto,
                              String telefono,
                              String contrasena,
                              TipoProfesional tipo,
                              Float porcentajeDescuento) {

        super(id, cedula, nombreCompleto, telefono, contrasena);
        this.tipo = tipo;
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public TipoProfesional getTipo() { return tipo; }
    public void setTipo(TipoProfesional tipo) { this.tipo = tipo; }

    public Float getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(Float porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }
}
