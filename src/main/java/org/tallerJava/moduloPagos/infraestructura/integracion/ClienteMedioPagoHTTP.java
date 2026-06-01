package org.tallerJava.moduloPagos.infraestructura.integracion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.Map;

@ApplicationScoped
public class ClienteMedioPagoHTTP {

    private static final String URL_AUTORIZAR =
            "http://localhost:8080/ServicioMedioPagoMock-1.0.0/api/pagos/autorizar";

    private static final String URL_PAGAR_DEUDA =
            "http://localhost:8080/ServicioMedioPagoMock-1.0.0/api/pagos/deuda/pagar";

    public boolean autorizar(String idCliente, String numeroTarjeta, BigDecimal monto) {
        Client client = ClientBuilder.newClient();
        try {
            Map<String, Object> body = Map.of(
                    "idCliente", idCliente,
                    "numeroTarjeta", numeroTarjeta,
                    "monto", monto
            );
            Response response = client
                    .target(URL_AUTORIZAR)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(body));
            return response.getStatus() == Response.Status.OK.getStatusCode();
        } finally {
            client.close();
        }
    }

    public void pagarDeuda(String numeroTarjeta) {
        Client client = ClientBuilder.newClient();
        try {
            Map<String, Object> body = Map.of("numeroTarjeta", numeroTarjeta);
            client.target(URL_PAGAR_DEUDA)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(body));
        } finally {
            client.close();
        }
    }
}
