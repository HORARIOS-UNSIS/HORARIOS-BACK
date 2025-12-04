package com.horarios.horarios_unsis.data.subject.infrastructure.controller;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.domain.port.in.SubjectUseCase;

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

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@PreAuthorize("hasRole('ADMIN') or hasRole('JEFE')")
@Tag(name = "Subjects", description = "Gestión de materias")
@SecurityRequirement(name = "bearerAuth")
public class SubjectController {
    
    @Autowired
    private SubjectUseCase subjectUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear materia", description = "Crea una nueva materia en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Materia creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public ResponseEntity<SubjectResponseDTO> createSubject(@RequestBody SubjectRequestDTO request) {
        SubjectResponseDTO response = subjectUseCase.createSubject(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> getSubject(@PathVariable Integer id) {
        SubjectResponseDTO response = subjectUseCase.getSubject(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        List<SubjectResponseDTO> response = subjectUseCase.getAllSubjects();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> updateSubject(
            @PathVariable Integer id, 
            @RequestBody SubjectRequestDTO request) {
        SubjectResponseDTO response = subjectUseCase.updateSubject(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Integer id) {
        subjectUseCase.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}