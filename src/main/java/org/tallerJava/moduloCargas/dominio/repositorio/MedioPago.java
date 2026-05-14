package org.tallerJava.moduloCargas.dominio.repositorio;

public abstract class MedioPago {

    private Long id;
    private String tipo;
    private String numero;

    public MedioPago() {
    }

    public MedioPago(Long id, String tipo, String numero) {
        this.id = id;
        this.tipo = tipo;
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
