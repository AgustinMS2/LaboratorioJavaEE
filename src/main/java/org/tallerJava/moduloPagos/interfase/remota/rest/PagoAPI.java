package org.tallerJava.moduloPagos.interfase.remota.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.tallerJava.moduloClientes.infraestructura.seguridad.AppMovil;
import org.tallerJava.moduloPagos.aplicacion.ServicioPago;
import org.tallerJava.moduloPagos.dominio.Pago;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/pagos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PagoAPI {

    @Inject
    private ServicioPago servicioPago;

    // curl -X GET "http://localhost:8080/LaboratorioJavaEE/gestion/pagos/1?desde=2026-01-01T00:00:00&hasta=2026-12-31T23:59:59"
    @GET
    @Path("/{clienteId}")
    public Response consultarPagos(@PathParam("clienteId") Long clienteId,
                                   @QueryParam("desde") String desde,
                                   @QueryParam("hasta") String hasta) {

        List<PagoDTO> resultado = new ArrayList<>();
        for (Pago pago : servicioPago.consultarPagos(clienteId, LocalDateTime.parse(desde), LocalDateTime.parse(hasta))) {
            resultado.add(PagoDTO.from(pago));
        }
        return Response.ok(resultado).build();
    }

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/pagos/1/pagar-deuda -H "Authorization: Basic <base64(cedula:contrasena)>"
    @AppMovil
    @POST
    @Path("/{clienteId}/pagar-deuda")
    public Response pagarDeuda(@PathParam("clienteId") Long clienteId) {
        servicioPago.pagarDeuda(clienteId);
        return Response.ok().build();
    }
}