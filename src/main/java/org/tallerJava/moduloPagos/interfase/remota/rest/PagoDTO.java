package org.tallerJava.moduloPagos.interfase.remota.rest;

import org.tallerJava.moduloPagos.dominio.Pago;

public class PagoDTO {
    public Long id;
    public Long clienteId;
    public Long cargaId;
    public Double importe;
    public String fecha;
    public String estado;

    public static PagoDTO from(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.id = pago.getId();
        dto.clienteId = pago.getClienteId();
        dto.cargaId = pago.getCargaId();
        dto.importe = pago.getImporte();
        dto.fecha = pago.getFecha() != null ? pago.getFecha().toString() : null;
        dto.estado = pago.getEstado();
        return dto;
    }
}
