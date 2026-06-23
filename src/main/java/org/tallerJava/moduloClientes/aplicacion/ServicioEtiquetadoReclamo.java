package org.tallerJava.moduloClientes.aplicacion;

import org.tallerJava.moduloClientes.dominio.EtiquetaReclamo;

public interface ServicioEtiquetadoReclamo {

    // Persiste la etiqueta asignada a un reclamo y notifica al monitoreo si es negativo
    void etiquetar(Long reclamoId, EtiquetaReclamo etiqueta);
}
