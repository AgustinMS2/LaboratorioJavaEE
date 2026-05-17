package org.tallerJava.moduloClientes.dominio;

import jakarta.persistence.Entity;

@Entity
public class ClienteComun extends Cliente {

    public ClienteComun() {
        super();
    }

    public ClienteComun(Long id,
                        String nombre,
                        String apellido,
                        String email,
                        String telefono) {

        super(id, nombre, apellido, email, telefono);
    }
}
