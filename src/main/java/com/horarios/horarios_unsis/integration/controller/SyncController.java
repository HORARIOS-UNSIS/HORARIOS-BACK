package com.horarios.horarios_unsis.integration.controller;

import com.horarios.horarios_unsis.integration.sync.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para sincronización de datos desde la API externa
 */
@RestController
@RequestMapping("/api/sync")
@CrossOrigin(origins = "*")
public class SyncController {

    private static final Logger logger = LoggerFactory.getLogger(SyncController.class);

    private final DataSyncService dataSyncService;

    public SyncController(DataSyncService dataSyncService) {
        this.dataSyncService = dataSyncService;
    }

    /**
     * Sincroniza todos los datos desde la API externa
     * POST /api/sync/todo
     */
    @PostMapping("/todo")
    public ResponseEntity<Map<String, Object>> sincronizarTodo() {
        logger.info("Solicitud de sincronización completa");
        
        try {
            DataSyncService.SyncResult result = dataSyncService.sincronizarTodo();
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", result.getMessage());
            response.put("periodosSincronizados", result.getPeriodosSincronizados());
            response.put("carrerasSincronizadas", result.getCarrerasSincronizadas());
            response.put("gruposSincronizados", result.getGruposSincronizados());
            response.put("profesoresSincronizados", result.getProfesoresSincronizados());
            response.put("materiasSincronizadas", result.getMateriasSincronizadas());
            response.put("aulasSincronizadas", result.getAulasSincronizadas());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error en sincronización: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Sincroniza asignaciones profesor-materia desde el endpoint de horarios
     * POST /api/sync/asignaciones/{clavePeriodo}
     */
    @PostMapping("/asignaciones/{clavePeriodo}")
    public ResponseEntity<Map<String, Object>> sincronizarAsignaciones(
            @PathVariable String clavePeriodo,
            @RequestParam(required = false) String claveCarrera) {
        
        logger.info("Solicitud de sincronización de asignaciones para período: {}", clavePeriodo);
        
        try {
            int cantidad;
            if (claveCarrera != null && !claveCarrera.isEmpty()) {
                cantidad = dataSyncService.sincronizarAsignacionesPorCarrera(clavePeriodo, claveCarrera);
            } else {
                cantidad = dataSyncService.sincronizarAsignacionesPorPeriodo(clavePeriodo);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sincronización de asignaciones completada");
            response.put("asignacionesSincronizadas", cantidad);
            response.put("periodo", clavePeriodo);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error sincronizando asignaciones: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}
