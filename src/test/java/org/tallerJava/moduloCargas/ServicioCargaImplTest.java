package org.tallerJava.moduloCargas;

import jakarta.enterprise.event.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tallerJava.moduloCargas.aplicacion.impl.ServicioCargaImpl;
import org.tallerJava.moduloCargas.dominio.Carga;
import org.tallerJava.moduloCargas.dominio.repositorio.CargaRepositorio;
import org.tallerJava.moduloCargas.dominio.repositorio.CargadorRepositorio;
import org.tallerJava.moduloCargas.dominio.repositorio.EstacionCargaRepositorio;
import org.tallerJava.moduloCargas.interfase.evento.CargaFinalizadaEvento;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServicioCargaImplTest {

    @Mock
    private CargaRepositorio cargaRepositorio;

    @Mock
    private CargadorRepositorio cargadorRepositorio;

    @Mock
    private EstacionCargaRepositorio estacionCargaRepositorio;

    @Mock
    private Event<CargaFinalizadaEvento> cargaFinalizadaEvent;

    @InjectMocks
    private ServicioCargaImpl servicioCarga;

    @Test
    void iniciarCarga_clienteConCargaActiva_lanzaExcepcion() {
        when(cargaRepositorio.buscarCargaActivaPorCliente(1L))
                .thenReturn(Optional.of(new Carga()));

        assertThatThrownBy(() -> servicioCarga.iniciarCarga(1L, 10L, 5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("carga activa");

        verify(cargadorRepositorio, never()).buscarPorId(anyLong());
    }
}
