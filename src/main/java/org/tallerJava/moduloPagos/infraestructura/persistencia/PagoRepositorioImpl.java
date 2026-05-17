package org.tallerJava.moduloPagos.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloPagos.dominio.Pago;
import org.tallerJava.moduloPagos.repositorio.PagoRepositorio;

import java.time.LocalDateTime;
import java.util.List;

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
}
