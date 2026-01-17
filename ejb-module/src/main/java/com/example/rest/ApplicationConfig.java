package com.example.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuração principal do JAX-RS.
 * Define o caminho base para todos os recursos REST.
 */
@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    // O servidor descobre automaticamente os endpoints e mappers.
}