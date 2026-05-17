package org.tallerJava.moduloClientes.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloClientes.dominio.MedioPago;
import org.tallerJava.moduloClientes.repositorio.MedioPagoRepositorio;

import java.util.Optional;

@ApplicationScoped
public class MedioPagoRepositorioImpl implements MedioPagoRepositorio {

    @PersistenceContext(unitName = "tallerPU")
    private EntityManager em;

    @Override
    public MedioPago guardar(MedioPago medioPago) {
        if (medioPago.getId() == null) {
            em.persist(medioPago);
            return medioPago;
        }
        return em.merge(medioPago);
    }

    @Override
    public Optional<MedioPago> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(MedioPago.class, id));
    }
}
