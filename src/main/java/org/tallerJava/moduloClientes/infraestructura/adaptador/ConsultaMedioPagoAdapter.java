package org.tallerJava.moduloClientes.infraestructura.adaptador;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.tallerJava.moduloClientes.dominio.CuentaUTE;
import org.tallerJava.moduloClientes.dominio.MedioPago;
import org.tallerJava.moduloClientes.dominio.Tarjeta;
import org.tallerJava.moduloClientes.dominio.repositorio.MedioPagoRepositorio;
import org.tallerJava.moduloPagos.aplicacion.puerto.ConsultaMedioPago;

@ApplicationScoped
public class ConsultaMedioPagoAdapter implements ConsultaMedioPago {

    @Inject
    private MedioPagoRepositorio medioPagoRepositorio;

    @Override
    public DatosMedioPago obtener(Long medioPagoId) {
        MedioPago mp = medioPagoRepositorio.buscarPorId(medioPagoId)
                .orElseThrow(() -> new IllegalArgumentException("Medio de pago no encontrado: " + medioPagoId));

        if (mp instanceof Tarjeta t) {
            return new DatosMedioPago("TARJETA", t.getNumero(), null);
        }
        if (mp instanceof CuentaUTE c) {
            return new DatosMedioPago("CUENTA_UTE", null, c.getNumeroCuenta());
        }
        throw new IllegalArgumentException("Tipo de medio de pago no soportado: " + mp.getClass().getSimpleName());
    }
}
