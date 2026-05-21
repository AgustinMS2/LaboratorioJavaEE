package org.tallerJava.moduloClientes.interfase.remota.rest;

import org.tallerJava.moduloClientes.dominio.Cliente;

public class ClienteDTO {
    public Long id;
    public String cedula;
    public String nombreCompleto;
    public String telefono;
    public String contrasena;
    public String tipo;
    public String tipoProfesional;
    public Float porcentajeDescuento;

    public static ClienteDTO from(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.id = cliente.getId();
        dto.cedula = cliente.getCedula();
        dto.nombreCompleto = cliente.getNombreCompleto();
        dto.telefono = cliente.getTelefono();
        return dto;
    }
}