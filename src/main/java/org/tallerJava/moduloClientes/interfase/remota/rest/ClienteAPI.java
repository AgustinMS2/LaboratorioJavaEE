package org.tallerJava.moduloClientes.interfase.remota.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.tallerJava.moduloClientes.aplicacion.ServicioCliente;
import org.tallerJava.moduloClientes.dominio.*;
import org.tallerJava.moduloClientes.infraestructura.seguridad.AppMovil;
import org.tallerJava.moduloClientes.infraestructura.seguridad.ContextoSeguridad;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteAPI {

    @Inject
    private ServicioCliente servicioCliente;

    @Inject
    private ContextoSeguridad contextoSeguridad;

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/clientes -H "Content-Type: application/json" -d "{\"cedula\":\"12345678\",\"nombreCompleto\":\"Juan Perez\",\"telefono\":\"099123456\",\"contrasena\":\"pass123\",\"tipo\":\"COMUN\"}"
    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/clientes -H "Content-Type: application/json" -d "{\"cedula\":\"12345678\",\"nombreCompleto\":\"Juan Perez\",\"telefono\":\"099123456\",\"contrasena\":\"pass123\",\"tipo\":\"PROFESIONAL\",\"tipoProfesional\":\"TAXI\",\"porcentajeDescuento\":10.0}"
    @POST
    public Response registrarCliente(ClienteDTO dto) {
        Cliente cliente;
        if ("PROFESIONAL".equals(dto.tipo)) {
            cliente = new ClienteProfesional(null, dto.cedula, dto.nombreCompleto, dto.telefono, dto.contrasena, TipoProfesional.valueOf(dto.tipoProfesional), dto.porcentajeDescuento);
        } else {
            cliente = new ClienteComun(null, dto.cedula, dto.nombreCompleto, dto.telefono, dto.contrasena);
        }
        Cliente nuevo = servicioCliente.registrarCliente(cliente);
        return Response.status(Response.Status.CREATED).entity(ClienteDTO.from(nuevo)).build();
    }

    // curl -X GET http://localhost:8080/LaboratorioJavaEE/gestion/clientes
    @GET
    public Response obtenerClientes() {
        List<ClienteDTO> resultado = new ArrayList<>();
        for (Cliente cliente : servicioCliente.obtenerClientes()) {
            resultado.add(ClienteDTO.from(cliente));
        }
        return Response.ok(resultado).build();
    }

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/clientes/1/mediosPago -H "Content-Type: application/json" -d "{\"tipo\":\"TARJETA\",\"numero\":\"1234567890123456\",\"fechaVencimiento\":\"2027-01-01\",\"digitoVerificacion\":\"123\",\"tipoTarjeta\":\"CREDITO\"}"
    @AppMovil
    @POST
    @Path("/{id}/mediosPago")
    public Response altaMedioPago(@PathParam("id") Long clienteId, MedioPagoDTO dto) {
        if (!clienteId.equals(contextoSeguridad.getClienteAutenticadoId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        MedioPago medioPago;
        if ("CUENTA_UTE".equals(dto.tipo)) {
            medioPago = new CuentaUTE(null, dto.numeroCuenta, dto.titular, dto.direccionServicio);
        } else {
            medioPago = new Tarjeta(null, dto.numero, LocalDate.parse(dto.fechaVencimiento), dto.digitoVerificacion, TipoTarjeta.valueOf(dto.tipoTarjeta));
        }
        servicioCliente.altaMedioPago(clienteId, medioPago);
        return Response.status(Response.Status.CREATED).build();
    }

    // curl -X POST http://localhost:8080/LaboratorioJavaEE/gestion/clientes/1/reclamos -H "Content-Type: application/json" -d "\"El cargador no funcionaba correctamente\""
    @AppMovil
    @POST
    @Path("/{id}/reclamos")
    public Response realizarReclamo(@PathParam("id") Long clienteId, String comentario) {
        if (!clienteId.equals(contextoSeguridad.getClienteAutenticadoId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        servicioCliente.realizarReclamo(clienteId, comentario);
        return Response.status(Response.Status.CREATED).build();
    }
}