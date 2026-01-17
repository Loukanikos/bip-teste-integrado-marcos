package com.example.ejb.rest;

import com.example.ejb.model.Beneficio;
import com.example.ejb.BeneficioEjbService;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path("/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioResource {

    @EJB
    private BeneficioEjbService service;

    // CREATE
    @POST
    public Response create(Beneficio beneficio) {
        Beneficio created = service.create(beneficio);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    // READ - todos
    @GET
    public List<Beneficio> findAll() {
        return service.findAll();
    }

    // READ - por ID
    @GET
    @Path("/{id}")
    public Beneficio findById(@PathParam("id") Long id) {
        return service.findById(id);
    }

    // UPDATE
    @PUT
    @Path("/{id}")
    public Beneficio update(@PathParam("id") Long id, Beneficio beneficio) {
        beneficio.setId(id);
        return service.update(beneficio);
    }

    // DELETE
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }

    // TRANSFERÊNCIA
    @POST
    @Path("/transfer")
    public Response transfer(@QueryParam("fromId") Long fromId,
                             @QueryParam("toId") Long toId,
                             @QueryParam("amount") BigDecimal amount) {
        service.transfer(fromId, toId, amount);
        return Response.ok().entity("Transferência realizada com sucesso").build();
    }
    // Métodos internos para testabilidade (não usam Response)
    Beneficio doCreate(Beneficio beneficio) {
        return service.create(beneficio);
    }

    List<Beneficio> doFindAll() {
        return service.findAll();
    }

    Beneficio doFindById(Long id) {
        return service.findById(id);
    }

    Beneficio doUpdate(Long id, Beneficio beneficio) {
        beneficio.setId(id);
        return service.update(beneficio);
    }

    void doDelete(Long id) {
        service.delete(id);
    }

    void doTransfer(Long fromId, Long toId, BigDecimal amount) {
        service.transfer(fromId, toId, amount);
    }


}