package org.tallerJava.moduloPagos.dominio;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos_pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencias por ID para mantener desacoplamiento entre módulos
    private Long clienteId;

    private Long cargaId;

    private Long medioPagoId;

    private Double importe;

    private LocalDateTime fecha;

    private String estado;

    public Pago() {
    }

    public Pago(Long clienteId, Long cargaId, Long medioPagoId, Double importe) {
        this.clienteId = clienteId;
        this.cargaId = cargaId;
        this.medioPagoId = medioPagoId;
        this.importe = importe;
        this.fecha = LocalDateTime.now();
        this.estado = "PROCESADO";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getCargaId() { return cargaId; }
    public void setCargaId(Long cargaId) { this.cargaId = cargaId; }

    public Long getMedioPagoId() { return medioPagoId; }
    public void setMedioPagoId(Long medioPagoId) { this.medioPagoId = medioPagoId; }

    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
