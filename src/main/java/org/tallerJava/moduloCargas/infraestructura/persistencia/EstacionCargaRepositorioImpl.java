package org.tallerJava.moduloCargas.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloCargas.dominio.EstacionCarga;
import org.tallerJava.moduloCargas.dominio.repositorio.EstacionCargaRepositorio;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EstacionCargaRepositorioImpl implements EstacionCargaRepositorio {

    @PersistenceContext(unitName = "tallerPU")
    private EntityManager em;

    @Override
    public EstacionCarga guardar(EstacionCarga estacion) {
        if (estacion.getId() == null) {
            em.persist(estacion);
            return estacion;
        }
        return em.merge(estacion);
    }

    @Override
    public Optional<EstacionCarga> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(EstacionCarga.class, id));
    }

    @Override
    public List<EstacionCarga> obtenerTodas() {
        return em.createQuery("SELECT e FROM EstacionCarga e", EstacionCarga.class).getResultList();
    }
}
