package com.mdevoc.schema;

import com.mdevoc.api.SimpleResponse;

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
    public Response generateSchema() {
        schemaDao.generateSchema();
        return Response.status(Response.Status.OK)
                .entity(new SimpleResponse("Schema Generation successful"))
                .build();
    }
}
