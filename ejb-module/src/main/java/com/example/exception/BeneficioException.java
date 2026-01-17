package com.example.exception;

public class BeneficioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

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