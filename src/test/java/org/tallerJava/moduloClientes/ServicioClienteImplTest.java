package org.tallerJava.moduloClientes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tallerJava.moduloClientes.aplicacion.impl.ServicioClienteImpl;
import org.tallerJava.moduloClientes.dominio.ClienteComun;
import org.tallerJava.moduloClientes.dominio.repositorio.ClienteRepositorio;
import org.tallerJava.moduloClientes.dominio.repositorio.MedioPagoRepositorio;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioClienteImplTest {

    @Mock
    private ClienteRepositorio clienteRepositorio;

    @Mock
    private MedioPagoRepositorio medioPagoRepositorio;

    @InjectMocks
    private ServicioClienteImpl servicioCliente;

    @Test
    void registrarCliente_cedulaDuplicada_lanzaExcepcion() {
        ClienteComun cliente = new ClienteComun();
        cliente.setCedula("12345678");

        when(clienteRepositorio.buscarPorCedula("12345678"))
                .thenReturn(Optional.of(new ClienteComun()));

        assertThatThrownBy(() -> servicioCliente.registrarCliente(cliente))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("12345678");

        verify(clienteRepositorio, never()).guardar(any());
    }

    @Test
    void registrarCliente_cedulaNueva_guardaYDevuelveCliente() {
        ClienteComun cliente = new ClienteComun();
        cliente.setCedula("87654321");
        cliente.setNombreCompleto("Ana Pérez");

        when(clienteRepositorio.buscarPorCedula("87654321")).thenReturn(Optional.empty());
        when(clienteRepositorio.guardar(cliente)).thenReturn(cliente);

        ClienteComun resultado = (ClienteComun) servicioCliente.registrarCliente(cliente);

        assertThat(resultado.getCedula()).isEqualTo("87654321");
        verify(clienteRepositorio).guardar(cliente);
    }
}
