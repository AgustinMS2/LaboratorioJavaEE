package org.tallerJava.moduloPagos.infraestructura.integracion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@ApplicationScoped
public class ClienteFacturaUTEHTTP {

    private static final String URL_NOTIFICAR =
            "http://localhost:8080/FacturaUTEMock/api/factura-ute/notificar-pago";

    public void notificarPago(Long clienteId, Long cargaId, String numeroCuenta, Double importe) {
        Client client = ClientBuilder.newClient();
        try {
            Map<String, Object> body = Map.of(
                    "clienteId", clienteId,
                    "cargaId", cargaId,
                    "numeroCuenta", numeroCuenta,
                    "importe", importe
            );
            client.target(URL_NOTIFICAR)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(body));
        } finally {
            client.close();
        }
    }
}
