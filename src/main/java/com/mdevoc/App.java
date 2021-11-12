package com.mdevoc;

import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class App extends ResourceConfig {
    public App() {
        packages("com.mdevoc");
    }
}
