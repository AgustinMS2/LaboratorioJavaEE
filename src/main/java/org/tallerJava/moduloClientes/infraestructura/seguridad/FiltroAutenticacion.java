package org.tallerJava.moduloClientes.infraestructura.seguridad;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.tallerJava.moduloClientes.dominio.Cliente;
import org.tallerJava.moduloClientes.dominio.repositorio.ClienteRepositorio;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Provider
@AppMovil
@ApplicationScoped
public class FiltroAutenticacion implements ContainerRequestFilter {

    @Inject
    private ClienteRepositorio clienteRepositorio;

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        String authHeader = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            rechazar(ctx);
            return;
        }

        String decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
        String[] partes = decoded.split(":", 2);

        if (partes.length != 2) {
            rechazar(ctx);
            return;
        }

        String cedula = partes[0];
        String contrasena = partes[1];

        Optional<Cliente> cliente = clienteRepositorio.buscarPorCedula(cedula);

        if (cliente.isEmpty() || !contrasena.equals(cliente.get().getContrasena())) {
            rechazar(ctx);
        }
    }

    private void rechazar(ContainerRequestContext ctx) {
        ctx.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"GestorMovilidad\"")
                        .build()
        );
    }
}
