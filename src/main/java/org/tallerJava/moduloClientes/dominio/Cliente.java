package org.tallerJava.moduloClientes.dominio;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MedioPago> mediosPago;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Reclamo> reclamos;

    public Cliente() {
        this.mediosPago = new ArrayList<>();
        this.reclamos = new ArrayList<>();
    }

    public Cliente(Long id,
                   String nombre,
                   String apellido,
                   String email,
                   String telefono) {

        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;

        this.mediosPago = new ArrayList<>();
        this.reclamos = new ArrayList<>();
    }

    public void agregarMedioPago(MedioPago medioPago) {
        this.mediosPago.add(medioPago);
    }

    public void agregarReclamo(Reclamo reclamo) {
        this.reclamos.add(reclamo);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public List<MedioPago> getMediosPago() { return mediosPago; }
    public void setMediosPago(List<MedioPago> mediosPago) { this.mediosPago = mediosPago; }

    public List<Reclamo> getReclamos() { return reclamos; }
    public void setReclamos(List<Reclamo> reclamos) { this.reclamos = reclamos; }
}
