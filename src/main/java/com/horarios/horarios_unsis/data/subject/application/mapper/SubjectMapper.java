package com.horarios.horarios_unsis.data.subject.application.mapper;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;

public class SubjectMapper {
    
    public static Subject toEntity(SubjectRequestDTO dto) {
        Subject subject = new Subject();
        subject.setNombre(dto.getNombre());
        return subject;
    }
    
    public static SubjectResponseDTO toDTO(Subject subject) {
        return new SubjectResponseDTO(
            subject.getIdMateria(),
            subject.getNombre()
        );
    }
}