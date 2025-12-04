package com.horarios.horarios_unsis.data.subject.domain.service;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.application.mapper.SubjectMapper;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.domain.port.in.SubjectUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService implements SubjectUseCase {
    
    // TODO: Implementar repositorio cuando esté disponible
    
    @Override
    public SubjectResponseDTO createSubject(SubjectRequestDTO request) {
        Subject subject = SubjectMapper.toEntity(request);
        // TODO: Guardar en repositorio
        return SubjectMapper.toDTO(subject);
    }

    @Override
    public SubjectResponseDTO getSubject(Integer id) {
        // TODO: Buscar en repositorio
        return null;
    }

    @Override
    public List<SubjectResponseDTO> getAllSubjects() {
        // TODO: Obtener todos del repositorio
        return null;
    }

    @Override
    public SubjectResponseDTO updateSubject(Integer id, SubjectRequestDTO request) {
        // TODO: Actualizar en repositorio
        return null;
    }

    @Override
    public void deleteSubject(Integer id) {
        // TODO: Eliminar del repositorio
    }
}