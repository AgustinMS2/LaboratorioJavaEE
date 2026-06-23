package org.tallerJava.moduloClientes.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloClientes.dominio.Reclamo;
import org.tallerJava.moduloClientes.dominio.repositorio.ReclamoRepositorio;

import java.util.Optional;

@ApplicationScoped
public class ReclamoRepositorioImpl implements ReclamoRepositorio {

    @PersistenceContext(unitName = "tallerPU")
    private EntityManager em;

    @Override
    public Reclamo guardar(Reclamo reclamo) {
        if (reclamo.getId() == null) {
            em.persist(reclamo);
            return reclamo;
        }
        return em.merge(reclamo);
    }

    @Override
    public Optional<Reclamo> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Reclamo.class, id));
    }
}
