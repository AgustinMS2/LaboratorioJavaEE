package org.tallerJava.moduloClientes;

import org.junit.jupiter.api.Test;
import org.tallerJava.moduloClientes.dominio.ClienteComun;
import org.tallerJava.moduloClientes.dominio.Reclamo;
import org.tallerJava.moduloClientes.dominio.Tarjeta;
import org.tallerJava.moduloClientes.dominio.TipoTarjeta;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {

    @Test
    void agregarMedioPago_aumentaLaLista() {
        ClienteComun cliente = new ClienteComun();
        Tarjeta tarjeta = new Tarjeta(null, "4111111111111111",
                LocalDate.of(2030, 12, 31), "123", TipoTarjeta.CREDITO);

        cliente.agregarMedioPago(tarjeta);

        assertThat(cliente.getMediosPago()).hasSize(1).contains(tarjeta);
    }

    @Test
    void agregarReclamo_aumentaLaLista() {
        ClienteComun cliente = new ClienteComun();
        Reclamo reclamo = new Reclamo(null, "Cargador fuera de servicio", LocalDateTime.now());

        cliente.agregarReclamo(reclamo);

        assertThat(cliente.getReclamos()).hasSize(1).contains(reclamo);
    }
}
