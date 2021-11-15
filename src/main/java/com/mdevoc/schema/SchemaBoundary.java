package com.mdevoc.schema;

import com.mdevoc.api.SimpleResponse;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/schema")
public class SchemaBoundary {

    @Inject
    SchemaDAO schemaDao;

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Generates the database schema for the application.")
    @APIResponse(responseCode = "200", description = "DB schema generation successful",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = SimpleResponse.class)))
    public Response generateSchema() {
        schemaDao.generateSchema();
        return Response.status(Response.Status.OK)
                .entity(new SimpleResponse("Schema Generation successful"))
                .build();
    }
}
