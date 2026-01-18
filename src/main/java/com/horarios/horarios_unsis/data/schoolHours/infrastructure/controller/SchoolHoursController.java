package com.horarios.horarios_unsis.data.schoolHours.infrastructure.controller;

import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursRequestDTO;
import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursResponseDTO;
import com.horarios.horarios_unsis.data.schoolHours.domain.port.in.SchoolHoursServicePort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para gestionar horarios escolares
 */
@RestController
@RequestMapping("/api/school-hours")
@PreAuthorize("hasRole('ADMIN') or hasRole('JEFE')")
@Tag(name = "School Hours", description = "Gestión de horarios escolares")
@SecurityRequirement(name = "bearerAuth")
public class SchoolHoursController {
    
    @Autowired
    private SchoolHoursServicePort schoolHoursService;

    /**
     * Crea un nuevo horario escolar
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear horario escolar", description = "Crea un nuevo horario escolar en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Horario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public ResponseEntity<SchoolHoursResponseDTO> createSchoolHours(
            @Valid @RequestBody SchoolHoursRequestDTO request) {
        SchoolHoursResponseDTO response = schoolHoursService.createSchoolHours(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Obtiene un horario escolar por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener horario escolar por ID", description = "Obtiene un horario específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario encontrado"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<SchoolHoursResponseDTO> getSchoolHours(
            @PathVariable @Parameter(description = "ID del horario escolar") Long id) {
        SchoolHoursResponseDTO response = schoolHoursService.getSchoolHours(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los horarios escolares
     */
    @GetMapping
    @Operation(summary = "Obtener todos los horarios", description = "Obtiene la lista completa de horarios escolares")
    public ResponseEntity<List<SchoolHoursResponseDTO>> getAllSchoolHours() {
        List<SchoolHoursResponseDTO> response = schoolHoursService.getAllSchoolHours();
        return ResponseEntity.ok(response);
    }

    /**
     * Actualiza un horario escolar existente
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar horario escolar", description = "Actualiza un horario escolar existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horario actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<SchoolHoursResponseDTO> updateSchoolHours(
            @PathVariable Long id,
            @Valid @RequestBody SchoolHoursRequestDTO request) {
        SchoolHoursResponseDTO response = schoolHoursService.updateSchoolHours(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un horario escolar
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar horario escolar", description = "Elimina un horario escolar del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Horario eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Horario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<Void> deleteSchoolHours(
            @PathVariable Long id) {
        schoolHoursService.deleteSchoolHours(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * ENDPOINT CLAVE: Importar horarios desde API externa
     * Este endpoint consume una API externa y guarda los datos en la BD
     */
    @PostMapping("/importar-api")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Importar horarios desde API", 
               description = "Consume una API externa y guarda los horarios escolares en la BD")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horarios importados exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al consumir API"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public ResponseEntity<List<SchoolHoursResponseDTO>> importarHorariosDelAPI() {
        List<SchoolHoursResponseDTO> imported = schoolHoursService.importarHorariosDelAPI();
        return new ResponseEntity<>(imported, HttpStatus.OK);
    }
}
