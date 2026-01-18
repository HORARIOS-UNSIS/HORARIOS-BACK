package com.horarios.horarios_unsis.data.subject.application.mapper;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;

public class SubjectMapper {
    
    // De DTO a Dominio
    public static Subject toDomain(SubjectRequestDTO dto) {
        if (dto == null) return null;
        Subject subject = new Subject();
        subject.setNombre(dto.getNombre());
        return subject;
    }

    // De Entidad a Dominio
    public static Subject entityToDomain(SubjectEntity entity) {
        if (entity == null) return null;
        Subject subject = new Subject();
        subject.setIdMateria(entity.getIdMateria()); // Usamos el ID de la DB
        subject.setNombre(entity.getNombre());
        return subject;
    }
    
    public static SubjectResponseDTO toDTO(Subject subject) {
        if (subject == null) return null;
        return new SubjectResponseDTO(
            subject.getIdMateria(),
            subject.getNombre()
        );
    }
}