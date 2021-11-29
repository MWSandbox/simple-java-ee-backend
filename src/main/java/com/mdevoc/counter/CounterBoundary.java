package com.mdevoc.counter;

import com.mdevoc.api.SimpleResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

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

  private static final String DELETION_SUCCESS_MESSAGE = "Deletion successful";
  private static final String DELETION_NOT_FOUND_MESSAGE = "Counter is not available";
  private static final String DELETION_ERROR_MESSAGE = "Could not delete counter: ";

  @Inject
  CounterDAO counterDao;

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Gets a single counter")
  @Parameter(name = "id", description = "Primary key of the counter", required = true)
  @APIResponse(responseCode = "200", description = "Found counter in database",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Counter.class)))
  @APIResponse(responseCode = "404", description = "The provided ID does not match any counter in the database",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
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
  @Operation(summary = "Gets all counters")
  @APIResponse(responseCode = "200",
      description = "Successfully query for all counters. Will return an empty array, if no counters have been found.",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Counter.class, type = SchemaType.ARRAY)))
  public Response getAll() {
    List<Counter> counters = counterDao.findAll();
    return Response.status(Response.Status.OK)
        .entity(counters)
        .build();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Creates a single counter")
  @RequestBody(description = "Counter to be created. ID can be left empty and will be overwritten if provided.",
      required = true)
  @APIResponse(responseCode = "200", description = "Counter created successfully",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Counter.class)))
  public Response createCounter(Counter counter) {
    counterDao.insert(counter);
    return Response.status(Response.Status.OK)
        .entity(counter)
        .build();
  }

  @PUT
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Updates a single counter")
  @RequestBody(description = "Counter with updated values.", required = true)
  @APIResponse(responseCode = "200", description = "Counter updated successfully",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = Counter.class)))
  @APIResponse(responseCode = "404",
      description = "The ID of the provided counter does not match any counter in the database",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
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
  @Operation(summary = "Deletes a single counter")
  @Parameter(name = "id", description = "Primary key of the counter", required = true)
  @APIResponse(responseCode = "200", description = "Counter deleted successfully",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
  @APIResponse(responseCode = "404",
      description = "The provided ID does not match any counter in the database",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
  @APIResponse(responseCode = "410",
      description = "There was an error when trying to delete the counter",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
  public Response delete(@PathParam("id") long id) {
    ResponseBuilder responseBuilder;
    try {
      counterDao.deleteById(id);
      responseBuilder = Response.status(Response.Status.OK)
          .entity(new SimpleResponse(DELETION_SUCCESS_MESSAGE));
    } catch (NoSuchElementException ex) {
      responseBuilder = Response.status(Response.Status.NOT_FOUND)
          .entity(new SimpleResponse(DELETION_NOT_FOUND_MESSAGE));
    } catch (IllegalArgumentException ex) {
      responseBuilder = Response.status(Response.Status.GONE)
          .entity(new SimpleResponse(DELETION_ERROR_MESSAGE + ex.getMessage()));
    }
    return responseBuilder.build();
  }

  @DELETE
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Deletes all counters")
  @APIResponse(responseCode = "200", description = "Successfully deleted all counters",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
  @APIResponse(responseCode = "404",
      description = "The provided ID does not match any counter in the database",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
  @APIResponse(responseCode = "410",
      description = "There was an error when trying to delete the counter",
      content = @Content(mediaType = MediaType.APPLICATION_JSON,
          schema = @Schema(implementation = SimpleResponse.class)))
  public Response deleteAll() {
    ResponseBuilder responseBuilder;
    try {
      counterDao.deleteAll();
      responseBuilder = Response.status(Response.Status.OK)
          .entity(new SimpleResponse(DELETION_SUCCESS_MESSAGE));
    } catch (NoSuchElementException ex) {
      responseBuilder = Response.status(Response.Status.NOT_FOUND)
          .entity(new SimpleResponse(DELETION_NOT_FOUND_MESSAGE));
    } catch (IllegalArgumentException ex) {
      responseBuilder = Response.status(Response.Status.GONE)
          .entity(new SimpleResponse(DELETION_ERROR_MESSAGE + ex.getMessage()));
    }
    return responseBuilder.build();
  }
}
