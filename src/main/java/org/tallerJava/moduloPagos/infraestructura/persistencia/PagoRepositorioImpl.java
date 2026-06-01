package org.tallerJava.moduloPagos.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloPagos.dominio.Pago;
import org.tallerJava.moduloPagos.dominio.repositorio.PagoRepositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PagoRepositorioImpl implements PagoRepositorio {

    @PersistenceContext(unitName = "tallerPU")
    private EntityManager em;

    @Override
    public Pago guardar(Pago pago) {
        if (pago.getId() == null) {
            em.persist(pago);
            return pago;
        }
        return em.merge(pago);
    }

    @Override
    public List<Pago> buscarPorClienteYFecha(Long clienteId, LocalDateTime desde, LocalDateTime hasta) {
        return em.createQuery(
                        "SELECT p FROM Pago p WHERE p.clienteId = :clienteId " +
                        "AND p.fecha >= :desde AND p.fecha <= :hasta",
                        Pago.class)
                .setParameter("clienteId", clienteId)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList();
    }

    @Override
    public Optional<Pago> buscarPagoRechazado(Long clienteId) {
        return em.createQuery(
                        "SELECT p FROM Pago p WHERE p.clienteId = :clienteId AND p.estado = 'RECHAZADO' ORDER BY p.fecha DESC",
                        Pago.class)
                .setParameter("clienteId", clienteId)
                .setMaxResults(1)
                .getResultStream()
                .findFirst();
    }

    @Override
    public boolean tieneDeudaPendiente(Long clienteId) {
        Long count = em.createQuery(
                        "SELECT COUNT(p) FROM Pago p WHERE p.clienteId = :clienteId AND p.estado = 'RECHAZADO'",
                        Long.class)
                .setParameter("clienteId", clienteId)
                .getSingleResult();
        return count > 0;
    }
}
