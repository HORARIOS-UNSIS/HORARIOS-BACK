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
        if (dto == null) {
            return null;
        }
        SchoollHors model = new SchoollHors();
        model.setId(null);
        model.setPeriodNumber(dto.getPeriodNumber());
        model.setStartTime(dto.getStartTime());
        model.setEndTime(dto.getEndTime());
        model.setIsBreak(dto.getIsBreak());
        model.setDescription(dto.getDescription());
        return model;
    }

    /**
     * Convierte Modelo de dominio → ResponseDTO
     */
    public static SchoolHoursResponseDTO toDTO(SchoollHors model) {
        if (model == null) {
            return null;
        }
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
        if (entity == null) {
            return null;
        }
        SchoollHors model = new SchoollHors();
        model.setId(entity.getId());
        model.setPeriodNumber(entity.getPeriodNumber());
        model.setStartTime(entity.getStartTime());
        model.setEndTime(entity.getEndTime());
        model.setIsBreak(entity.getIsBreak());
        model.setDescription(entity.getDescription());
        return model;
    }

    /**
     * Convierte Modelo de dominio → Entity JPA
     */
    public static SchoolHoursEntity toEntity(SchoollHors model) {
        if (model == null) {
            return null;
        }
        SchoolHoursEntity entity = new SchoolHoursEntity();
        entity.setId(model.getId());
        entity.setPeriodNumber(model.getPeriodNumber());
        entity.setStartTime(model.getStartTime());
        entity.setEndTime(model.getEndTime());
        entity.setIsBreak(model.getIsBreak());
        entity.setDescription(model.getDescription());
        return entity;
    }
}

