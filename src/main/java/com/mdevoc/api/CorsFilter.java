package com.mdevoc.api;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final String ORIGIN_HEADER_KEY = "Origin";
  private static final String OPTIONS_METHOD = "OPTIONS";
  private static final String ALLOWED_ORIGINS = "*";

  @Override
  public void filter(ContainerRequestContext request) throws IOException {
    if (isPreflightRequest(request)) {
      request.abortWith(Response.ok().build());
    }
  }

  @Override
  public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
    if (!isOriginEmpty(request)) {
      if (isPreflightRequest(request)) {
        addPreflightHeadersToResponse(response);
      }

      response.getHeaders().add("Access-Control-Allow-Origin", ALLOWED_ORIGINS);
    }
  }

  private boolean isPreflightRequest(ContainerRequestContext request) {
    return !isOriginEmpty(request) && isOptionsRequest(request);
  }

  private boolean isOriginEmpty(ContainerRequestContext request) {
    return request.getHeaderString(ORIGIN_HEADER_KEY) == null;
  }

  private boolean isOptionsRequest(ContainerRequestContext request) {
    return request.getMethod().equalsIgnoreCase(OPTIONS_METHOD);
  }

  private void addPreflightHeadersToResponse(ContainerResponseContext response) {
    response.getHeaders().add("Access-Control-Allow-Credentials", "true");
    response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
    response.getHeaders().add("Access-Control-Allow-Headers",
        "X-Requested-With, Authorization, Accept-Version, Content-MD5, CSRF-Token, Content-Type");
  }
}