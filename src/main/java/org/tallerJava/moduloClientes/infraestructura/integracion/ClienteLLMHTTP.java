package org.tallerJava.moduloClientes.infraestructura.integracion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.tallerJava.moduloClientes.dominio.EtiquetaReclamo;

import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class ClienteLLMHTTP {

    private static final Logger log = Logger.getLogger(ClienteLLMHTTP.class);

    private static final String URL_GENERATE = "http://localhost:11434/api/generate";

    // Usamos llama3.2: más liviano y rápido que llama2
    private static final String MODELO =
            System.getenv().getOrDefault("OLLAMA_MODEL", "llama3.2");

    public EtiquetaReclamo clasificar(String comentario) {
        Client client = ClientBuilder.newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.MINUTES)
                .build();
        try {
            String prompt = "Cataloga el siguiente reclamo de un cliente respondiendo unicamente"
                    + " con una palabra: positivo, negativo o neutro. No agregues explicaciones."
                    + " Reclamo: " + comentario;

            Map<String, Object> body = Map.of(
                    "model", MODELO,
                    "prompt", prompt,
                    "stream", false
            );

            String json = client.target(URL_GENERATE)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(body), String.class);

            JsonObject respuesta = Json.createReader(new StringReader(json)).readObject();
            String texto = respuesta.getString("response", "").toLowerCase();

            EtiquetaReclamo etiqueta = interpretar(texto);
            log.infof("LLM clasificó el reclamo como %s (respuesta cruda: %s)", etiqueta, texto.trim());
            return etiqueta;
        } catch (Exception e) {
            log.warnf(e, "No se pudo clasificar el reclamo con el LLM, se etiqueta como NEUTRO");
            return EtiquetaReclamo.NEUTRO;
        } finally {
            client.close();
        }
    }

    private EtiquetaReclamo interpretar(String texto) {
        if (texto.contains("negativ") || texto.contains("problem")) {
            return EtiquetaReclamo.NEGATIVO;
        }
        if (texto.contains("positiv")) {
            return EtiquetaReclamo.POSITIVO;
        }
        return EtiquetaReclamo.NEUTRO;
    }
}
