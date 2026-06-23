package org.tallerJava.moduloClientes.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes_reclamo")
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    private EtiquetaReclamo etiqueta;

    public Reclamo() {
    }

    public Reclamo(Long id, String comentario, LocalDateTime fecha) {
        this.id = id;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public EtiquetaReclamo getEtiqueta() { return etiqueta; }
    public void setEtiqueta(EtiquetaReclamo etiqueta) { this.etiqueta = etiqueta; }
}
