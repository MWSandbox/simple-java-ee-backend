package com.mdevoc.schema;

import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.tools.schemaframework.SchemaManager;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public class SchemaDAO {

    @Inject
    private EntityManager em;

    public void generateSchema() {
        ServerSession session = em.unwrap(ServerSession.class);
        SchemaManager schemaManager = new SchemaManager(session);
        schemaManager.replaceDefaultTables(true, true, true);
    }
}
