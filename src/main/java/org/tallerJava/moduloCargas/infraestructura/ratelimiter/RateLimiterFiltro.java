package org.tallerJava.moduloCargas.infraestructura.ratelimiter;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@PreMatching
@Provider
public class RateLimiterFiltro implements ContainerRequestFilter {

    @Inject
    private RateLimiter rateLimiter;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();

        if (!path.contains("/historico/")) {
            return;
        }

        boolean sePermiteEjecutar = rateLimiter.consumir();
        if (!sePermiteEjecutar) {
            System.out.println("El servidor no acepta mensajes");
            requestContext.abortWith(
                    Response.status(Response.Status.TOO_MANY_REQUESTS).entity("").build()
            );
        }
    }
}