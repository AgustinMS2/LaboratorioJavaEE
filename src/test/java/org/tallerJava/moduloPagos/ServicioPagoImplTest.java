package org.tallerJava.moduloPagos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tallerJava.moduloPagos.aplicacion.impl.ServicioPagoImpl;
import org.tallerJava.moduloPagos.aplicacion.puerto.ConsultaMedioPago;
import org.tallerJava.moduloPagos.dominio.Pago;
import org.tallerJava.moduloPagos.dominio.repositorio.PagoRepositorio;
import org.tallerJava.moduloPagos.infraestructura.integracion.ClienteFacturaUTEHTTP;
import org.tallerJava.moduloPagos.infraestructura.integracion.ClienteMedioPagoHTTP;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioPagoImplTest {

    @Mock
    private PagoRepositorio pagoRepositorio;

    @Mock
    private ConsultaMedioPago consultaMedioPago;

    @Mock
    private ClienteMedioPagoHTTP clienteMedioPagoHTTP;

    @Mock
    private ClienteFacturaUTEHTTP clienteFacturaUTEHTTP;

    @InjectMocks
    private ServicioPagoImpl servicioPago;

    @Test
    void pagarCarga_guardaPagoEnRepositorio() {
        when(consultaMedioPago.obtener(10L))
                .thenReturn(new ConsultaMedioPago.DatosMedioPago("CUENTA_UTE", null, "UTE-12345"));
        when(pagoRepositorio.guardar(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pago resultado = servicioPago.pagarCarga(1L, 200L, 99.90, 10L);

        assertThat(resultado.getClienteId()).isEqualTo(1L);
        assertThat(resultado.getCargaId()).isEqualTo(200L);
        assertThat(resultado.getMedioPagoId()).isEqualTo(10L);
        assertThat(resultado.getImporte()).isEqualTo(99.90);
        assertThat(resultado.getEstado()).isEqualTo("PROCESADO");
        verify(pagoRepositorio).guardar(any(Pago.class));
    }
}
