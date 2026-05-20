package org.tallerJava.moduloCargas.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloCargas.dominio.Cargador;
import org.tallerJava.moduloCargas.dominio.repositorio.CargadorRepositorio;

import java.util.Optional;

@ApplicationScoped
public class CargadorRepositorioImpl implements CargadorRepositorio {

    @PersistenceContext(unitName = "tallerPU")
    private EntityManager em;

    @Override
    public Cargador guardar(Cargador cargador) {
        if (cargador.getId() == null) {
            em.persist(cargador);
            return cargador;
        }
        return em.merge(cargador);
    }

    @Override
    public Optional<Cargador> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Cargador.class, id));
    }
}
