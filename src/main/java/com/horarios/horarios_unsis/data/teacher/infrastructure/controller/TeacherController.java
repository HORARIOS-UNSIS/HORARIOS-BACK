package com.horarios.horarios_unsis.data.teacher.infrastructure.controller;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.domain.port.in.TeacherUseCase;

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
@RequestMapping("/api/teachers")
@PreAuthorize("hasRole('ADMIN') or hasRole('JEFE')")
@Tag(name = "Teachers", description = "Gestión de profesores")
@SecurityRequirement(name = "bearerAuth")
public class TeacherController {
    
    @Autowired
    private TeacherUseCase teacherUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear profesor", description = "Crea un nuevo profesor en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Profesor creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public ResponseEntity<TeacherResponseDTO> createTeacher(@RequestBody TeacherRequestDTO request) {
        TeacherResponseDTO response = teacherUseCase.createTeacher(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener profesor por ID", description = "Obtiene un profesor específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profesor encontrado"),
        @ApiResponse(responseCode = "404", description = "Profesor no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    public ResponseEntity<TeacherResponseDTO> getTeacher(
            @PathVariable @Parameter(description = "ID del profesor") Integer id) {
        TeacherResponseDTO response = teacherUseCase.getTeacher(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        List<TeacherResponseDTO> response = teacherUseCase.getAllTeachers();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> updateTeacher(
            @PathVariable Integer id, 
            @RequestBody TeacherRequestDTO request) {
        TeacherResponseDTO response = teacherUseCase.updateTeacher(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Integer id) {
        teacherUseCase.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}