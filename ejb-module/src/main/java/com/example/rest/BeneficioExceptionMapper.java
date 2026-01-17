package com.example.rest;

import com.example.exception.BeneficioException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BeneficioExceptionMapper implements ExceptionMapper<BeneficioException> {

    @Override
    public Response toResponse(BeneficioException exception) {
        Response.StatusType status;

        switch (exception.getTipo()) {
            case NOT_FOUND:
                status = Response.Status.NOT_FOUND; // 404
                break;
            case INVALID_AMOUNT:
                status = Response.Status.BAD_REQUEST; // 400
                break;
            case INSUFFICIENT_BALANCE:
                status = HttpStatus.UNPROCESSABLE_ENTITY; // 422 custom
                break;
            default:
                status = Response.Status.INTERNAL_SERVER_ERROR; // 500
        }

        ErrorResponse error = new ErrorResponse(
                exception.getTipo().name(),
                exception.getMessage());

        return Response.status(status).entity(error).build();
    }

    // Classe auxiliar para resposta JSON
    public static class ErrorResponse {
        private String tipo;
        private String mensagem;

        public ErrorResponse(String tipo, String mensagem) {
            this.tipo = tipo;
            this.mensagem = mensagem;
        }

        public String getTipo() {
            return tipo;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}
