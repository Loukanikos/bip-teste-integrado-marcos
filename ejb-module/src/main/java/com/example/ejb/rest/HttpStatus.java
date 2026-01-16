package com.example.ejb.rest;

import jakarta.ws.rs.core.Response;

public class HttpStatus {

    public static final Response.StatusType UNPROCESSABLE_ENTITY =
            new Response.StatusType() {
                @Override
                public int getStatusCode() {
                    return 422;
                }

                @Override
                public Response.Status.Family getFamily() {
                    return Response.Status.Family.CLIENT_ERROR;
                }

                @Override
                public String getReasonPhrase() {
                    return "Unprocessable Entity";
                }
            };
}
