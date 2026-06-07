package org.tallerJava.moduloCargas.interfase.evento;

public class CargaIniciadaEvento {
    private final Long clienteId;
    private final Long cargaId;

    public CargaIniciadaEvento(Long clienteId, Long cargaId) {
        this.clienteId = clienteId;
        this.cargaId = cargaId;
    }

    public Long getClienteId() { return clienteId; }
    public Long getCargaId() { return cargaId; }
}