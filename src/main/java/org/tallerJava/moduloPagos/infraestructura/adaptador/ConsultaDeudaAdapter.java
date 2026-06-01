package org.tallerJava.moduloPagos.infraestructura.adaptador;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tallerJava.moduloCargas.aplicacion.puerto.ConsultaDeuda;
import org.tallerJava.moduloPagos.dominio.repositorio.PagoRepositorio;

@ApplicationScoped
public class ConsultaDeudaAdapter implements ConsultaDeuda {

    @Inject
    private PagoRepositorio pagoRepositorio;

    @Override
    public boolean tieneDeudaPendiente(Long clienteId) {
        return pagoRepositorio.tieneDeudaPendiente(clienteId);
    }
}
