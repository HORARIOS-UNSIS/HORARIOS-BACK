package com.horarios.horarios_unsis.data.subject.infrastructure.controller;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.domain.port.in.SubjectUseCase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    
    @Autowired
    private SubjectUseCase subjectUseCase;

    @PostMapping
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