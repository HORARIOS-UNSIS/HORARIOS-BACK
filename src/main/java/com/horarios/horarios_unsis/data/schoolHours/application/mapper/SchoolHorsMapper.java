package com.horarios.horarios_unsis.data.schoolHours.application.mapper;

import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursRequestDTO;
import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursResponseDTO;
import com.horarios.horarios_unsis.data.schoolHours.domain.model.SchoollHors;
import com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.entity.SchoolHoursEntity;

/**
 * Mapper para convertir entre diferentes capas de schoolHours
 */
public final class SchoolHorsMapper {

    private SchoolHorsMapper() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Convierte RequestDTO → Modelo de dominio
     */
    public static SchoollHors toModel(SchoolHoursRequestDTO dto) {
        if (dto == null) return null;
        return new SchoollHors(
            null,
            dto.getPeriodNumber(),
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getIsBreak(),
            dto.getDescription()
        );
    }

    /**
     * Convierte Modelo de dominio → ResponseDTO
     */
    public static SchoolHoursResponseDTO toDTO(SchoollHors model) {
        if (model == null) return null;
        return new SchoolHoursResponseDTO(
            model.getId(),
            model.getPeriodNumber(),
            model.getStartTime(),
            model.getEndTime(),
            model.getIsBreak(),
            model.getDescription()
        );
    }

    /**
     * Convierte Entity JPA → Modelo de dominio
     */
    public static SchoollHors toDomain(SchoolHoursEntity entity) {
        if (entity == null) return null;
        return new SchoollHors(
            entity.getId(),
            entity.getPeriodNumber(),
            entity.getStartTime(),
            entity.getEndTime(),
            entity.getIsBreak(),
            entity.getDescription()
        );
    }

    /**
     * Convierte Modelo de dominio → Entity JPA
     */
    public static SchoolHoursEntity toEntity(SchoollHors model) {
        if (model == null) return null;
        return new SchoolHoursEntity(
            model.getId(),
            model.getPeriodNumber(),
            model.getStartTime(),
            model.getEndTime(),
            model.getIsBreak(),
            model.getDescription()
        );
    }
}
