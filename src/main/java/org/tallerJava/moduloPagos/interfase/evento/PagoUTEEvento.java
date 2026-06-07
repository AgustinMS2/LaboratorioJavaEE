package org.tallerJava.moduloPagos.interfase.evento;

public class PagoUTEEvento {
    private final Long clienteId;
    private final Long cargaId;

    public PagoUTEEvento(Long clienteId, Long cargaId) {
        this.clienteId = clienteId;
        this.cargaId = cargaId;
    }

    public Long getClienteId() { return clienteId; }
    public Long getCargaId() { return cargaId; }
}