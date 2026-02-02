package com.horarios.horarios_unsis.data.group.infrastructure.controller;

import com.horarios.horarios_unsis.data.group.application.dto.GroupResponseDTO;
import com.horarios.horarios_unsis.data.group.domain.port.in.GroupUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@PreAuthorize("hasRole('ADMIN') or hasRole('JEFE') or hasRole('PROFESOR') or hasRole('ALUMNO')") // Amplio acceso para lectura si es necesario, o restringir según reglas de negocio. Usaré el estándar visto.
@Tag(name = "Groups", description = "Gestión de grupos académicos")
@SecurityRequirement(name = "bearerAuth")
public class GroupController {

    @Autowired
    private GroupUseCase groupUseCase;

    @GetMapping
    @Operation(summary = "Obtener grupos", description = "Recupera la lista de grupos. Permite filtrar por carrera y/o periodo.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de grupos recuperada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<GroupResponseDTO>> getGroups(
            @Parameter(description = "Filtrar por clave de carrera") @RequestParam(required = false) String claveCarrera,
            @Parameter(description = "Filtrar por clave de periodo") @RequestParam(required = false) String clavePeriodo) {
        
        List<GroupResponseDTO> response;
        
        if (claveCarrera != null && clavePeriodo != null) {
            response = groupUseCase.getGroupsByCareerAndPeriod(claveCarrera, clavePeriodo);
        } else if (claveCarrera != null) {
            response = groupUseCase.getGroupsByCareer(claveCarrera);
        } else if (clavePeriodo != null) {
            response = groupUseCase.getGroupsByPeriod(clavePeriodo);
        } else {
            response = groupUseCase.getAllGroups();
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener grupo por ID", description = "Recupera un grupo específico por su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grupo encontrado"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<GroupResponseDTO> getGroupById(
            @Parameter(description = "ID del grupo a buscar", required = true) @PathVariable Integer id) {
        try {
            GroupResponseDTO response = groupUseCase.getGroupById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
