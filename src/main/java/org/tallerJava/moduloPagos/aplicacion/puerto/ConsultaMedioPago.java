package org.tallerJava.moduloPagos.aplicacion.puerto;

public interface ConsultaMedioPago {

    DatosMedioPago obtener(Long medioPagoId);

    record DatosMedioPago(String tipo, String numeroTarjeta, String numeroCuenta) {}
}
