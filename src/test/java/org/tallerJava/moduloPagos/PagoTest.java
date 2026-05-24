package org.tallerJava.moduloPagos;

import org.junit.jupiter.api.Test;
import org.tallerJava.moduloPagos.dominio.Pago;

import static org.assertj.core.api.Assertions.assertThat;

class PagoTest {

    @Test
    void nuevoPago_tieneEstadoProcesadoYDatosCorrectos() {
        Pago pago = new Pago(1L, 100L, 50L, 250.75);

        assertThat(pago.getClienteId()).isEqualTo(1L);
        assertThat(pago.getCargaId()).isEqualTo(100L);
        assertThat(pago.getMedioPagoId()).isEqualTo(50L);
        assertThat(pago.getImporte()).isEqualTo(250.75);
        assertThat(pago.getEstado()).isEqualTo("PROCESADO");
        assertThat(pago.getFecha()).isNotNull();
    }
}
