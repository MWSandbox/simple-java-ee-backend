package com.mdevoc.counter;

import com.mdevoc.api.SimpleResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static javax.ws.rs.core.Response.ResponseBuilder;

@Path("/counter")
@Transactional
public class CounterBoundary {

    @Inject
    CounterDAO counterDao;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") long id) {
        Optional<Counter> optionalCounter = counterDao.findById(id);

        if (optionalCounter.isPresent()) {
            return Response.status(Response.Status.OK)
                    .entity(optionalCounter.get())
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new SimpleResponse("Could not find counter with id " + id))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Counter> counters = counterDao.findAll();
        return Response.status(Response.Status.OK)
                .entity(counters)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createCounter(Counter counter) {
        counterDao.insert(counter);
        return Response.status(Response.Status.OK)
                .entity(counter)
                .build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCounter(Counter counter) {
        ResponseBuilder responseBuilder;
        try {
            counterDao.update(counter);
            responseBuilder = Response.status(Response.Status.OK)
                    .entity(counter);
        } catch (IllegalArgumentException ex) {
            responseBuilder = Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleResponse("Could not update entity: " + ex.getMessage()));
        }
        return responseBuilder.build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") long id) {
        ResponseBuilder responseBuilder;
        try {
            counterDao.deleteById(id);
            responseBuilder = Response.status(Response.Status.OK)
                    .entity(new SimpleResponse("Deletion successful"));
        } catch (IllegalArgumentException ex) {
            responseBuilder = Response.status(Response.Status.GONE)
                    .entity(new SimpleResponse("Could not delete counter: " + ex.getMessage()));
        } catch (NoSuchElementException ex) {
            responseBuilder = Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleResponse("Counter is not available"));
        }
        return responseBuilder.build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAll() {
        ResponseBuilder responseBuilder;
        try {
            counterDao.deleteAll();
            responseBuilder = Response.status(Response.Status.OK)
                    .entity(new SimpleResponse("Deletion successful"));
        } catch (IllegalArgumentException ex) {
            responseBuilder = Response.status(Response.Status.GONE)
                    .entity(new SimpleResponse("Could not delete counter: " + ex.getMessage()));
        } catch (NoSuchElementException ex) {
            responseBuilder = Response.status(Response.Status.NOT_FOUND)
                    .entity(new SimpleResponse("Counter is not available"));
        }
        return responseBuilder.build();
    }

}
