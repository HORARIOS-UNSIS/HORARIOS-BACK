package com.horarios.horarios_unsis.data.subject.domain.service;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.application.mapper.SubjectMapper;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.domain.port.in.SubjectUseCase;
import com.horarios.horarios_unsis.data.subject.domain.port.out.SubjectRepositoryPort;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService implements SubjectUseCase {
    
    private final SubjectRepositoryPort subjectRepositoryPort;

    public SubjectService(SubjectRepositoryPort subjectRepositoryPort) {
        this.subjectRepositoryPort = subjectRepositoryPort;
    }

    @Override
    public SubjectResponseDTO createSubject(SubjectRequestDTO request) {
        Subject subject = SubjectMapper.toDomain(request);
        Subject savedSubject = subjectRepositoryPort.save(subject);
        return SubjectMapper.toDTO(savedSubject);
    }

    @Override
    public SubjectResponseDTO getSubject(Integer id) {
        return subjectRepositoryPort.findById(id)
                .map(SubjectMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Materia con ID " + id + " no encontrada"));
    }

    @Override
    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepositoryPort.findAll().stream()
                .map(SubjectMapper::toDTO)
                .toList();
    }

    @Override
    public SubjectResponseDTO updateSubject(Integer id, SubjectRequestDTO request) {
        Subject existingSubject = subjectRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Materia no encontrada"));
        
        existingSubject.setNombre(request.getNombre());
        
        Subject updatedSubject = subjectRepositoryPort.save(existingSubject);
        return SubjectMapper.toDTO(updatedSubject);
    }

    @Override
    public void deleteSubject(Integer id) {
        // Verificamos existencia antes de borrar (opcional, pero recomendado)
        if (!subjectRepositoryPort.findById(id).isPresent()) {
            throw new RuntimeException("No se puede eliminar: Materia no encontrada");
        }
        subjectRepositoryPort.deleteById(id);
    }
}