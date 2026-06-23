package org.tallerJava.moduloClientes.infraestructura.messaging;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@ApplicationScoped
public class ProductorReclamos {

    public static final String PROP_RECLAMO_ID = "reclamoId";

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/jms/queue/reclamos")
    private Queue queueReclamos;

    public void encolar(Long reclamoId, String comentario) {
        context.createProducer()
                .setProperty(PROP_RECLAMO_ID, reclamoId)
                .send(queueReclamos, comentario);
    }
}
