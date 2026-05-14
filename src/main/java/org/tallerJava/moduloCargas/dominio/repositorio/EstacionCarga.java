package org.tallerJava.moduloCargas.dominio.repositorio;
import java.util.ArrayList;
import java.util.List;

public class EstacionCarga {
    private Long id;

    private String nombre;

    private String direccion;

    private String ciudad;

    private List<Cargador> cargadores;

    public EstacionCarga() {
        this.cargadores = new ArrayList<>();
    }

    public EstacionCarga(Long id,
                    String nombre,
                    String direccion,
                    String ciudad) {

        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;

        this.cargadores = new ArrayList<>();
    }

    public void agregarCargador(Cargador cargador) {
        this.cargadores.add(cargador);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public List<Cargador> getCargadores() {
        return cargadores;
    }

    public void setCargadores(List<Cargador> cargadores) {
        this.cargadores = cargadores;
    }
}
