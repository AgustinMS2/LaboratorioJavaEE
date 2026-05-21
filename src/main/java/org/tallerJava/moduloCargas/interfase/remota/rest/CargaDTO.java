package org.tallerJava.moduloCargas.interfase.remota.rest;

import org.tallerJava.moduloCargas.dominio.Carga;

public class CargaDTO {
    public Long id;
    public Long clienteId;
    public String estado;
    public String horaInicio;
    public String horaFin;
    public Float importeTotal;
    public Float recargoPorDemora;
    public Integer porcentajeAvance;

    public static CargaDTO from(Carga carga) {
        CargaDTO dto = new CargaDTO();
        dto.id = carga.getId();
        dto.clienteId = carga.getClienteId();
        dto.estado = carga.getEstado().name();
        dto.horaInicio = carga.getHoraInicio() != null ? carga.getHoraInicio().toString() : null;
        dto.horaFin = carga.getHoraFin() != null ? carga.getHoraFin().toString() : null;
        dto.importeTotal = carga.getImporteTotal();
        dto.recargoPorDemora = carga.getRecargoPorDemora();
        dto.porcentajeAvance = carga.getPorcentajeAvance();
        return dto;
    }
}