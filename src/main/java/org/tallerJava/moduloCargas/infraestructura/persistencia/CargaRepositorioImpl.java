package org.tallerJava.moduloCargas.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloCargas.dominio.Carga;
import org.tallerJava.moduloCargas.dominio.EstadoCarga;
import org.tallerJava.moduloCargas.dominio.repositorio.CargaRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CargaRepositorioImpl implements CargaRepositorio {

    @PersistenceContext(unitName = "tallerPU")
    private EntityManager em;

    @Override
    public Carga guardar(Carga carga) {
        if (carga.getId() == null) {
            em.persist(carga);
            return carga;
        }
        return em.merge(carga);
    }

    @Override
    public Optional<Carga> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Carga.class, id));
    }

    @Override
    public Optional<Carga> buscarCargaActivaPorCliente(Long clienteId) {
        return em.createQuery(
                        "SELECT c FROM Carga c WHERE c.clienteId = :clienteId AND c.estado = :estado",
                        Carga.class)
                .setParameter("estado", EstadoCarga.ACTIVA)
                .setParameter("clienteId", clienteId)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Carga> buscarPorClienteYFecha(Long clienteId, LocalDateTime desde, LocalDateTime hasta) {
        return em.createQuery(
                        "SELECT c FROM Carga c WHERE c.clienteId = :clienteId " +
                        "AND c.fechaInicio >= :desde AND c.fechaInicio <= :hasta",
                        Carga.class)
                .setParameter("clienteId", clienteId)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList();
    }
}
