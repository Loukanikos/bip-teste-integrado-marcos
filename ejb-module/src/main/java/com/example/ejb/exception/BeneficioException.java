package com.example.ejb.exception;

public class BeneficioException extends RuntimeException {

    public enum Tipo {
        NOT_FOUND,
        INVALID_AMOUNT,
        INSUFFICIENT_BALANCE
    }

    private final Tipo tipo;

    public BeneficioException(Tipo tipo, String message) {
        super(message);
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }
}