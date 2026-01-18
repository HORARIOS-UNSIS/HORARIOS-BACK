package com.horarios.horarios_unsis.data.synodals.infrastructure.controller;

import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalRequestDTO;
import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalResponseDTO;
import com.horarios.horarios_unsis.data.synodals.domain.port.in.SynodalUseCase;

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

@RestController
@RequestMapping("/api/synodals")
@PreAuthorize("hasRole('ADMIN') or hasRole('JEFE')")
@Tag(name = "Synodals", description = "Gestión de sinodales")
@SecurityRequirement(name = "bearerAuth")
public class SynodalController {
    
    @Autowired
    private SynodalUseCase synodalUseCase;

    @PostMapping
    @Operation(summary = "Crear un nuevo sinodal", description = "Crea un nuevo registro de sinodal")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sinodal creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "No tiene permiso para crear sinodales")
    })
    public ResponseEntity<SynodalResponseDTO> createSynodal(@Valid @RequestBody SynodalRequestDTO request) {
        SynodalResponseDTO synodal = synodalUseCase.createSynodal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(synodal);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un sinodal por ID", description = "Recupera los detalles de un sinodal específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sinodal encontrado"),
        @ApiResponse(responseCode = "404", description = "Sinodal no encontrado"),
        @ApiResponse(responseCode = "403", description = "No tiene permiso para ver sinodales")
    })
    public ResponseEntity<SynodalResponseDTO> getSynodal(
            @Parameter(description = "ID del sinodal", example = "1")
            @PathVariable Integer id) {
        SynodalResponseDTO synodal = synodalUseCase.getSynodal(id);
        return synodal != null ? ResponseEntity.ok(synodal) : ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Obtener todos los sinodales", description = "Recupera la lista de todos los sinodales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sinodales"),
        @ApiResponse(responseCode = "403", description = "No tiene permiso para ver sinodales")
    })
    public ResponseEntity<List<SynodalResponseDTO>> getAllSynodals() {
        List<SynodalResponseDTO> synodals = synodalUseCase.getAllSynodals();
        return ResponseEntity.ok(synodals);
    }

    @GetMapping("/materia/{idMateria}")
    @Operation(summary = "Obtener sinodales por materia", description = "Recupera todos los sinodales asignados a una materia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sinodales de la materia"),
        @ApiResponse(responseCode = "403", description = "No tiene permiso para ver sinodales")
    })
    public ResponseEntity<List<SynodalResponseDTO>> getSynodalsByMateria(
            @Parameter(description = "ID de la materia", example = "1")
            @PathVariable Integer idMateria) {
        List<SynodalResponseDTO> synodals = synodalUseCase.getSynodalsByMateria(idMateria);
        return ResponseEntity.ok(synodals);
    }

    @GetMapping("/profesor/{idProfesor}")
    @Operation(summary = "Obtener sinodales por profesor", description = "Recupera todos los sinodales relacionados con un profesor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de sinodales del profesor"),
        @ApiResponse(responseCode = "403", description = "No tiene permiso para ver sinodales")
    })
    public ResponseEntity<List<SynodalResponseDTO>> getSynodalsByProfesor(
            @Parameter(description = "ID del profesor", example = "1")
            @PathVariable Integer idProfesor) {
        List<SynodalResponseDTO> synodals = synodalUseCase.getSynodalsByProfesor(idProfesor);
        return ResponseEntity.ok(synodals);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un sinodal", description = "Actualiza los datos de un sinodal existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sinodal actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sinodal no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "No tiene permiso para actualizar sinodales")
    })
    public ResponseEntity<SynodalResponseDTO> updateSynodal(
            @Parameter(description = "ID del sinodal", example = "1")
            @PathVariable Integer id,
            @Valid @RequestBody SynodalRequestDTO request) {
        SynodalResponseDTO synodal = synodalUseCase.updateSynodal(id, request);
        return synodal != null ? ResponseEntity.ok(synodal) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un sinodal", description = "Elimina un sinodal del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sinodal eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Sinodal no encontrado"),
        @ApiResponse(responseCode = "403", description = "No tiene permiso para eliminar sinodales")
    })
    public ResponseEntity<Void> deleteSynodal(
            @Parameter(description = "ID del sinodal", example = "1")
            @PathVariable Integer id) {
        synodalUseCase.deleteSynodal(id);
        return ResponseEntity.noContent().build();
    }
}
