package org.tallerJava.moduloCargas.dominio.repositorio;

public abstract class MedioPago {

    private Long id;

    public MedioPago() {
    }

    public MedioPago(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
