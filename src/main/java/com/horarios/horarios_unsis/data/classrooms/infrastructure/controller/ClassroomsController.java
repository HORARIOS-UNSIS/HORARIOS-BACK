package com.horarios.horarios_unsis.data.classrooms.infrastructure.controller;

import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsRequestDTO;
import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsResponseDTO;
import com.horarios.horarios_unsis.data.classrooms.domain.port.in.ClassroomsUseCase;

import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/classrooms")
@PreAuthorize("hasRole('ADMIN') or hasRole('JEFE')")
@Tag(name = "Classrooms", description = "Gestión de aulas")
@SecurityRequirement(name = "bearerAuth")
public class ClassroomsController {
    
    @Autowired
    private ClassroomsUseCase classroomsUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear aula", description = "Crea una nueva aula en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aula creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public ResponseEntity<ClassroomsResponseDTO> createClassroom(@RequestBody ClassroomsRequestDTO request) {
        ClassroomsResponseDTO response = classroomsUseCase.createClassroom(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassroomsResponseDTO> getClassroom(@PathVariable Integer id) {
        ClassroomsResponseDTO response = classroomsUseCase.getClassroom(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ClassroomsResponseDTO>> getAllClassrooms() {
        List<ClassroomsResponseDTO> response = classroomsUseCase.getAllClassrooms();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassroomsResponseDTO> updateClassroom(
            @PathVariable Integer id, 
            @RequestBody ClassroomsRequestDTO request) {
        ClassroomsResponseDTO response = classroomsUseCase.updateClassroom(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable Integer id) {
        classroomsUseCase.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }
}
