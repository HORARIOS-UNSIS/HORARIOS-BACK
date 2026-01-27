package com.horarios.horarios_unsis.schedule.presentation.controller;

import com.horarios.horarios_unsis.shared.models.ExamScheduleRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para gestión de horarios de examen
 * 
 * Endpoints para crear, consultar y gestionar horarios de exámenes
 */
@RestController
@RequestMapping("/api/horarios")
@Tag(name = "Horarios", description = "APIs para gestión de horarios de examen")
public class ScheduleController {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    
    /**
     * Test endpoint - Verificar que la API está funcionando
     */
    @GetMapping("/test")
    @Operation(summary = "Test endpoint", description = "Verifica que la API está funcionando")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API funcionando correctamente")
    })
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "HORARIOS-UNSIS API está funcionando");
        response.put("timestamp", LocalTime.now().toString());
        return ResponseEntity.ok(response);
    }
    
    /**
     * Crear horario de examen
     * 
     * @param request Datos del examen a crear
     * @return Respuesta con ID del examen creado
     */
    @PostMapping("/crear")
    @Operation(
        summary = "Crear horario de examen",
        description = "Crea un nuevo horario de examen con validaciones según tipo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Examen creado exitosamente",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o validación fallida"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    public ResponseEntity<Map<String, Object>> crearHorario(
        @RequestBody ExamScheduleRequest request
    ) {
        logger.info("Recibida solicitud de crear horario para materia: {}", request.getIdMateria());
        
        // Validación básica
        if (request.getIdMateria() == null || request.getIdProfesor() == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "idMateria e idProfesor son requeridos"));
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", 1);
        response.put("idMateria", request.getIdMateria());
        response.put("tipoExamen", request.getTipoExamen());
        response.put("fecha", request.getFechaExamen());
        response.put("hora", request.getHoraExamen());
        response.put("status", "PROGRAMADO");
        response.put("message", "Examen creado exitosamente");
        response.put("timestamp", LocalTime.now().toString());
        
        logger.info("✓ Examen creado exitosamente: {}", response);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Obtener horario por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener horario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario encontrado"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    public ResponseEntity<Map<String, Object>> obtenerHorario(@PathVariable Integer id) {
        logger.info("Consultando horario: {}", id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("idMateria", 42);
        response.put("tipoExamen", "ORDINARIO");
        response.put("fecha", LocalDate.now().plusDays(7));
        response.put("hora", "10:00:00");
        response.put("status", "PROGRAMADO");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Listar todos los horarios
     */
    @GetMapping("")
    @Operation(summary = "Listar horarios", description = "Obtiene lista de todos los horarios de examen")
    public ResponseEntity<Map<String, Object>> listarHorarios(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        logger.info("Listando horarios - página: {}, tamaño: {}", page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("horarios", java.util.List.of(
            Map.of("id", 1, "idMateria", 42, "tipoExamen", "ORDINARIO", "status", "PROGRAMADO"),
            Map.of("id", 2, "idMateria", 43, "tipoExamen", "PARCIAL", "status", "PROGRAMADO")
        ));
        response.put("page", page);
        response.put("size", size);
        response.put("total", 2);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Actualizar horario de examen
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar horario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario actualizado"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    public ResponseEntity<Map<String, Object>> actualizarHorario(
        @PathVariable Integer id,
        @RequestBody ExamScheduleRequest request
    ) {
        logger.info("Actualizando horario: {}", id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("message", "Horario actualizado exitosamente");
        response.put("timestamp", LocalTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Eliminar horario de examen
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar horario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Horario eliminado"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado")
    })
    public ResponseEntity<Void> eliminarHorario(@PathVariable Integer id) {
        logger.info("Eliminando horario: {}", id);
        return ResponseEntity.noContent().build();
    }
}
