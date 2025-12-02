package com.horarios.horarios_unsis.data.teacher.infrastructure.controller;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.domain.port.in.TeacherUseCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    
    @Autowired
    private TeacherUseCase teacherUseCase;

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> createTeacher(@RequestBody TeacherRequestDTO request) {
        TeacherResponseDTO response = teacherUseCase.createTeacher(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> getTeacher(@PathVariable Integer id) {
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