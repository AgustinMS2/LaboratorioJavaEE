package org.tallerJava.moduloCargas.interfase.evento;

public class CargaFinalizadaEvento {

    private final Long clienteId;
    private final Long cargaId;
    private final Double importe;
    private final Long medioPagoId;

    public CargaFinalizadaEvento(Long clienteId, Long cargaId, Double importe, Long medioPagoId) {
        this.clienteId = clienteId;
        this.cargaId = cargaId;
        this.importe = importe;
        this.medioPagoId = medioPagoId;
    }

    public Long getClienteId() { return clienteId; }
    public Long getCargaId() { return cargaId; }
    public Double getImporte() { return importe; }
    public Long getMedioPagoId() { return medioPagoId; }
}