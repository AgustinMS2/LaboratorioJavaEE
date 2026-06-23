package org.tallerJava.moduloClientes.infraestructura.messaging;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.jboss.logging.Logger;
import org.tallerJava.moduloClientes.aplicacion.ServicioEtiquetadoReclamo;
import org.tallerJava.moduloClientes.dominio.EtiquetaReclamo;
import org.tallerJava.moduloClientes.infraestructura.integracion.ClienteLLMHTTP;

/*
 Cataloga cada reclamo (positivo / negativo / neutro) con ayuda del LLM y persiste la etiqueta.
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/reclamos"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ConsumidorReclamos implements MessageListener {

    private static final Logger log = Logger.getLogger(ConsumidorReclamos.class);

    @Inject
    private ClienteLLMHTTP clienteLLM;

    @Inject
    private ServicioEtiquetadoReclamo servicioEtiquetado;

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            Long reclamoId = textMessage.getLongProperty(ProductorReclamos.PROP_RECLAMO_ID);
            String comentario = textMessage.getText();

            log.infof("Procesando reclamo %d desde la queue", reclamoId);
            EtiquetaReclamo etiqueta = clienteLLM.clasificar(comentario);
            servicioEtiquetado.etiquetar(reclamoId, etiqueta);
            log.infof("Reclamo %d etiquetado como %s", reclamoId, etiqueta);
        } catch (JMSException e) {
            log.error("Error al procesar el mensaje de reclamo", e);
        }
    }
}
