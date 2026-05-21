package org.tallerJava.moduloCargas.interfase.evento;

public class CargaFinalizadaEvento {

    private final Long clienteId;
    private final Long cargaId;
    private final Double importe;

    public CargaFinalizadaEvento(Long clienteId, Long cargaId, Double importe) {
        this.clienteId = clienteId;
        this.cargaId = cargaId;
        this.importe = importe;
    }

    public Long getClienteId() { return clienteId; }
    public Long getCargaId() { return cargaId; }
    public Double getImporte() { return importe; }
}