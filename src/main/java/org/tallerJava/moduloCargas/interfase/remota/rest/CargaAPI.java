package org.tallerJava.moduloCargas.interfase.remota.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.tallerJava.moduloCargas.aplicacion.ServicioCarga;
import org.tallerJava.moduloCargas.dominio.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/cargas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CargaAPI {

    @Inject
    private ServicioCarga servicioCarga;

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/cargas/iniciar -H "Content-Type: application/json" -d "{\"clienteId\":1,\"cargadorId\":1,\"medioPagoId\":1}"
    @POST
    @Path("/iniciar")
    public Response iniciarCarga(IniciarCargaDTO dto) {
        Carga carga = servicioCarga.iniciarCarga(dto.clienteId, dto.cargadorId, dto.medioPagoId);
        return Response.status(Response.Status.CREATED).entity(CargaDTO.from(carga)).build();
    }

    // curl -X GET http://localhost:8080/LaboratorioJavaEE/gestion/cargas/actual/1
    @GET
    @Path("/actual/{clienteId}")
    public Response verCargaActual(@PathParam("clienteId") Long clienteId) {
        Carga carga = servicioCarga.verCargaActual(clienteId);
        return Response.ok(CargaDTO.from(carga)).build();
    }

    // curl -X GET "http://localhost:8080/LaboratorioJavaEE/gestion/cargas/historico/1?desde=2026-01-01T00:00:00&hasta=2026-12-31T23:59:59"
    @GET
    @Path("/historico/{clienteId}")
    public Response verHistorico(@PathParam("clienteId") Long clienteId,
                                 @QueryParam("desde") String desde,
                                 @QueryParam("hasta") String hasta) {
        List<CargaDTO> resultado = new ArrayList<>();
        for (Carga carga : servicioCarga.verHistorico(clienteId, LocalDateTime.parse(desde), LocalDateTime.parse(hasta))) {
            resultado.add(CargaDTO.from(carga));
        }
        return Response.ok(resultado).build();
    }

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/cargas/finalizar -H "Content-Type: application/json" -d "{\"cargadorId\":1,\"cargaId\":1,\"consumoKwh\":10.5,\"recargo\":0.0}"
    @POST
    @Path("/finalizar")
    public Response finalizarCarga(FinalizarCargaDTO dto) {
        Carga carga = servicioCarga.finalizarCarga(dto.cargadorId, dto.cargaId, dto.consumoKwh, dto.recargo);
        return Response.ok(CargaDTO.from(carga)).build();
    }

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/cargas/estaciones -H "Content-Type: application/json" -d "{\"descripcion\":\"Estacion Centro\",\"calle\":\"Rivera 100\",\"departamento\":\"Maldonado\",\"longitud\":-34,\"latitud\":-54}"
    @POST
    @Path("/estaciones")
    public Response altaEstacion(EstacionDTO dto) {
        EstacionCarga nueva = servicioCarga.altaEstacion(new EstacionCarga(null, dto.descripcion, dto.calle, dto.departamento, dto.longitud, dto.latitud));
        return Response.status(Response.Status.CREATED).entity(EstacionDTO.from(nueva)).build();
    }

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/cargas/estaciones/1/cargadores -H "Content-Type: application/json" -d "{\"tipo\":\"LENTO\",\"tieneCable\":true,\"tipoConector\":\"TIPO2\"}"
    @POST
    @Path("/estaciones/{estacionId}/cargadores")
    public Response altaCargador(@PathParam("estacionId") Long estacionId, CargadorDTO dto) {
        Cargador cargador = new Cargador(TipoCargador.valueOf(dto.tipo), dto.tieneCable, TipoConector.valueOf(dto.tipoConector), dto.potenciaMinima);
        Cargador nuevo = servicioCarga.altaCargador(estacionId, cargador);
        return Response.status(Response.Status.CREATED).entity(CargadorDTO.from(nuevo)).build();
    }

    // curl -X GET http://localhost:8080/LaboratorioJavaEE/gestion/cargas/estaciones
    @GET
    @Path("/estaciones")
    public Response obtenerEstaciones() {
        List<EstacionDTO> resultado = new ArrayList<>();
        for (EstacionCarga estacion : servicioCarga.obtenerEstaciones()) {
            resultado.add(EstacionDTO.from(estacion));
        }
        return Response.ok(resultado).build();
    }

}
