package org.tallerJava.moduloClientes.dominio;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes_cliente")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cedula;

    private String nombreCompleto;

    private String telefono;

    private String contrasena;

    @OneToMany(cascade = CascadeType.ALL)
    private List<MedioPago> mediosPago;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Reclamo> reclamos;

    public Cliente() {
        this.mediosPago = new ArrayList<>();
        this.reclamos = new ArrayList<>();
    }

    public Cliente(Long id,
                   String cedula,
                   String nombreCompleto,
                   String telefono,
                   String contrasena) {

        this.id = id;
        this.cedula = cedula;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.contrasena = contrasena;
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

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public List<MedioPago> getMediosPago() { return mediosPago; }
    public void setMediosPago(List<MedioPago> mediosPago) { this.mediosPago = mediosPago; }

    public List<Reclamo> getReclamos() { return reclamos; }
    public void setReclamos(List<Reclamo> reclamos) { this.reclamos = reclamos; }
}
