package org.tallerJava.moduloClientes.interfase.evento;

public class ReclamoNegativoEvento {
    private final Long reclamoId;

    public ReclamoNegativoEvento(Long reclamoId) {
        this.reclamoId = reclamoId;
    }

    public Long getReclamoId() { return reclamoId; }
}
