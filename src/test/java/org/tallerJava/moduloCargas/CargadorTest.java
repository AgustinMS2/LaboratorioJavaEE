package org.tallerJava.moduloCargas;

import org.junit.jupiter.api.Test;
import org.tallerJava.moduloCargas.dominio.Carga;
import org.tallerJava.moduloCargas.dominio.Cargador;
import org.tallerJava.moduloCargas.dominio.EstadoCarga;
import org.tallerJava.moduloCargas.dominio.EstadoCargador;
import org.tallerJava.moduloCargas.dominio.TipoCargador;
import org.tallerJava.moduloCargas.dominio.TipoConector;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CargadorTest {

    @Test
    void nuevoCargador_estaDisponible() {
        Cargador cargador = new Cargador(TipoCargador.RAPIDO, true, TipoConector.CCS, 50);

        assertThat(cargador.getEstado()).isEqualTo(EstadoCargador.DISPONIBLE);
    }

    @Test
    void ocuparYLiberar_cambiaEstado() {
        Cargador cargador = new Cargador();
        LocalDateTime finEstimado = LocalDateTime.now().plusHours(2);

        cargador.ocupar(finEstimado);
        assertThat(cargador.getEstado()).isEqualTo(EstadoCargador.OCUPADO);
        assertThat(cargador.getTiempoEstimadoFinalizacion()).isEqualTo(finEstimado);

        cargador.liberar();
        assertThat(cargador.getEstado()).isEqualTo(EstadoCargador.DISPONIBLE);
        assertThat(cargador.getTiempoEstimadoFinalizacion()).isNull();
    }

    @Test
    void finalizarCarga_marcaComoFinalizada() {
        Cargador cargador = new Cargador(TipoCargador.LENTO, false, TipoConector.TIPO2, null);
        Carga carga = new Carga(1L, cargador);

        carga.finalizar(150.5f, 10f);

        assertThat(carga.getEstado()).isEqualTo(EstadoCarga.FINALIZADA);
        assertThat(carga.getImporteTotal()).isEqualTo(150.5f);
        assertThat(carga.getRecargoPorDemora()).isEqualTo(10f);
        assertThat(carga.getPorcentajeAvance()).isEqualTo(100);
        assertThat(carga.getHoraFin()).isNotNull();
    }
}
