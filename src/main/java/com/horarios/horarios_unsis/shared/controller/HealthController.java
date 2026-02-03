package com.horarios.horarios_unsis.shared.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para verificar el estado de salud de la aplicación
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    /**
     * Endpoint para verificar que la aplicación está funcionando
     * @return Estado de la aplicación
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "horarios-unsis-api");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }

    /**
     * Endpoint raíz de la API
     * @return Información básica de la API
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> root() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "HORARIOS-UNSIS API");
        info.put("version", "1.0.0");
        info.put("description", "API REST para gestión de horarios universitarios");
        info.put("documentation", "/swagger-ui.html");
        info.put("api-docs", "/api-docs");
        info.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(info);
    }
}
