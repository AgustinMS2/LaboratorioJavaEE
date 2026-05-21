package org.tallerJava.moduloCargas.interfase.remota.rest;

import org.tallerJava.moduloCargas.dominio.Cargador;

public class CargadorDTO {
    public Long id;
    public String tipo;
    public Boolean tieneCable;
    public String tipoConector;
    public Integer potenciaMinima;
    public String estado;

    public static CargadorDTO from(Cargador cargador) {
        CargadorDTO dto = new CargadorDTO();
        dto.id = cargador.getId();
        dto.tipo = cargador.getTipo().name();
        dto.tieneCable = cargador.getTieneCable();
        dto.tipoConector = cargador.getTipoConector().name();
        dto.potenciaMinima = cargador.getPotenciaMinima();
        dto.estado = cargador.getEstado().name();
        return dto;
    }
}
