package org.tallerJava.moduloClientes.infraestructura.seguridad;

import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class ContextoSeguridad {

    private Long clienteAutenticadoId;

    public Long getClienteAutenticadoId() { return clienteAutenticadoId; }
    public void setClienteAutenticadoId(Long clienteAutenticadoId) { this.clienteAutenticadoId = clienteAutenticadoId; }
}
