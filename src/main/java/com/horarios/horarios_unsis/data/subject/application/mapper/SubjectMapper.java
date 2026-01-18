package com.horarios.horarios_unsis.data.subject.application.mapper;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import com.horarios.horarios_unsis.integration.Consume.DTO.MateriaExternaDTO;

/**
 * Mapper para convertir entre diferentes capas de Subject
 */
public final class SubjectMapper {

    private SubjectMapper() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Convierte RequestDTO → Modelo de dominio
     */
    public static Subject toEntity(SubjectRequestDTO dto) {
        if (dto == null) return null;
        Subject subject = new Subject();
        subject.setNombre(dto.getNombre());
        return subject;
    }

    /**
     * Convierte Modelo de dominio → ResponseDTO
     */
    public static SubjectResponseDTO toDTO(Subject subject) {
        if (subject == null) return null;
        return new SubjectResponseDTO(
            subject.getIdMateria(),
            subject.getNombre()
        );
    }

    /**
     * Convierte Entity JPA → Modelo de dominio
     */
    public static Subject toDomain(SubjectEntity entity) {
        if (entity == null) return null;
        Subject subject = new Subject();
        subject.setIdMateria(entity.getIdMateria());
        subject.setNombre(entity.getNombre());
        return subject;
    }

    /**
     * Convierte Modelo de dominio → Entity JPA
     */
    public static SubjectEntity toEntityJPA(Subject model) {
        if (model == null) return null;
        return new SubjectEntity(
            model.getIdMateria(),
            model.getNombre()
        );
    }

    /**
     * Convierte DTO externo → Modelo de dominio
     */
    public static Subject toModelFromExternal(MateriaExternaDTO dto) {
        if (dto == null) return null;
        Subject subject = new Subject();
        subject.setNombre(dto.getNombre());
        return subject;
    }
}