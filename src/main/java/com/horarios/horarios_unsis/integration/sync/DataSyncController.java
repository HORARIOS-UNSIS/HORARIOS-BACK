package com.horarios.horarios_unsis.integration.sync;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador para sincronización de datos desde API externa
 * 
 * Endpoints:
 * - POST /api/sync/all - Sincroniza todos los datos
 * - POST /api/sync/periods - Sincroniza solo períodos
 * - POST /api/sync/careers - Sincroniza solo carreras
 * - POST /api/sync/groups - Sincroniza solo grupos
 * - POST /api/sync/teachers - Sincroniza solo profesores
 * - POST /api/sync/subjects - Sincroniza solo materias
 * - POST /api/sync/classrooms - Sincroniza solo aulas
 */
@RestController
@RequestMapping("/api/sync")
@Tag(name = "Sincronización", description = "Endpoints para sincronizar datos desde API externa")
public class DataSyncController {

    private final DataSyncService dataSyncService;

    public DataSyncController(DataSyncService dataSyncService) {
        this.dataSyncService = dataSyncService;
    }

    @PostMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sincronizar todos los datos", description = "Sincroniza períodos, carreras, grupos, profesores, materias y aulas desde la API externa")
    public ResponseEntity<DataSyncService.SyncResult> sincronizarTodo() {
        DataSyncService.SyncResult result = dataSyncService.sincronizarTodo();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/periods")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sincronizar períodos", description = "Sincroniza solo períodos desde la API externa")
    public ResponseEntity<SyncResponse> sincronizarPeriodos() {
        int count = dataSyncService.sincronizarPeriodos();
        return ResponseEntity.ok(new SyncResponse(true, "Períodos sincronizados: " + count, count));
    }

    @PostMapping("/careers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sincronizar carreras", description = "Sincroniza solo carreras desde la API externa")
    public ResponseEntity<SyncResponse> sincronizarCarreras() {
        int count = dataSyncService.sincronizarCarreras();
        return ResponseEntity.ok(new SyncResponse(true, "Carreras sincronizadas: " + count, count));
    }

    @PostMapping("/groups")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sincronizar grupos", description = "Sincroniza solo grupos desde la API externa")
    public ResponseEntity<SyncResponse> sincronizarGrupos() {
        int count = dataSyncService.sincronizarGrupos();
        return ResponseEntity.ok(new SyncResponse(true, "Grupos sincronizados: " + count, count));
    }

    @PostMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sincronizar profesores", description = "Sincroniza solo profesores desde la API externa")
    public ResponseEntity<SyncResponse> sincronizarProfesores() {
        int count = dataSyncService.sincronizarProfesores();
        return ResponseEntity.ok(new SyncResponse(true, "Profesores sincronizados: " + count, count));
    }

    @PostMapping("/subjects")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sincronizar materias", description = "Sincroniza solo materias desde la API externa")
    public ResponseEntity<SyncResponse> sincronizarMaterias() {
        int count = dataSyncService.sincronizarMaterias();
        return ResponseEntity.ok(new SyncResponse(true, "Materias sincronizadas: " + count, count));
    }

    @PostMapping("/classrooms")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Sincronizar aulas", description = "Sincroniza solo aulas desde la API externa")
    public ResponseEntity<SyncResponse> sincronizarAulas() {
        int count = dataSyncService.sincronizarAulas();
        return ResponseEntity.ok(new SyncResponse(true, "Aulas sincronizadas: " + count, count));
    }

    /**
     * Respuesta simple para sincronizaciones individuales
     */
    public static class SyncResponse {
        private boolean success;
        private String message;
        private int count;

        public SyncResponse(boolean success, String message, int count) {
            this.success = success;
            this.message = message;
            this.count = count;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public int getCount() { return count; }
        public void setCount(int count) { this.count = count; }
    }
}
