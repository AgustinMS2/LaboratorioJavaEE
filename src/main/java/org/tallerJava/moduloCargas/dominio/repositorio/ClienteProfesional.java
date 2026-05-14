package org.tallerJava.moduloCargas.dominio.repositorio;

public class ClienteProfesional extends Cliente{

    private String empresa;

    private String rut;

    public ClienteProfesional() {
        super();
    }

    public ClienteProfesional(Long id,
                              String nombre,
                              String apellido,
                              String email,
                              String telefono,
                              String empresa,
                              String rut) {

        super(id, nombre, apellido, email, telefono);

        this.empresa = empresa;
        this.rut = rut;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

}
