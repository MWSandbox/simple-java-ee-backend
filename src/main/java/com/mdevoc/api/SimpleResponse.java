package com.mdevoc.api;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Objects;

@Schema(description = "A simple success / error response containing a message with more details")
public class SimpleResponse {

    @Schema(description = "Contains more details about the response. In case of an error, the message will include information about the cause.",
            nullable = true)
    private String message;

    public SimpleResponse(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        SimpleResponse that = (SimpleResponse) object;
        return Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
