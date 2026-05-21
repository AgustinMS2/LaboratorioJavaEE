package org.tallerJava.moduloCargas.interfase.remota.rest;

import org.tallerJava.moduloCargas.dominio.EstacionCarga;

public class EstacionDTO {
    public Long id;
    public String descripcion;
    public String calle;
    public String departamento;
    public int longitud;
    public int latitud;

    public static EstacionDTO from(EstacionCarga estacion) {
        EstacionDTO dto = new EstacionDTO();
        dto.id = estacion.getId();
        dto.descripcion = estacion.getDescripcion();
        dto.calle = estacion.getCalle();
        dto.departamento = estacion.getDepartamento();
        dto.longitud = estacion.getLongitud();
        dto.latitud = estacion.getLatitud();
        return dto;
    }
}
