package org.tallerJava.infraestructura.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class GestionExcepcionesMapper implements ExceptionMapper<RuntimeException> {

    @Override
    public Response toResponse(RuntimeException excepcion) {
        if (excepcion instanceof IllegalStateException) {
            return Response.status(Response.Status.CONFLICT)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(Map.of("error", excepcion.getMessage()))
                    .build();
        }
        if (excepcion instanceof IllegalArgumentException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(Map.of("error", excepcion.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of("error", "Error interno del servidor"))
                .build();
    }
}
