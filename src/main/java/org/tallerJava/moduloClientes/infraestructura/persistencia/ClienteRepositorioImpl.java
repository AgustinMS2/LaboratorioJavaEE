package org.tallerJava.moduloClientes.infraestructura.persistencia;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.tallerJava.moduloClientes.dominio.Cliente;
import org.tallerJava.moduloClientes.dominio.repositorio.ClienteRepositorio;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ClienteRepositorioImpl implements ClienteRepositorio {

    @PersistenceContext(unitName = "tallerPU")
    private EntityManager em;

    @Override
    public Cliente guardar(Cliente cliente) {
        if (cliente.getId() == null) {
            em.persist(cliente);
            return cliente;
        }
        return em.merge(cliente);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Cliente.class, id));
    }

    @Override
    public Optional<Cliente> buscarPorCedula(String cedula) {
        return em.createQuery("SELECT c FROM Cliente c WHERE c.cedula = :cedula", Cliente.class)
                .setParameter("cedula", cedula)
                .getResultStream()
                .findFirst();
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
    }
}
