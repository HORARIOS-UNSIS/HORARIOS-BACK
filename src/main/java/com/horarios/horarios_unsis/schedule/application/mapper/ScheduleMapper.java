package com.horarios.horarios_unsis.schedule.application.mapper;

import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.entity.ScheduleEntity;
import com.horarios.horarios_unsis.integration.Consume.DTO.ExamenExternoDTO;


public final class ScheduleMapper {

    private ScheduleMapper() {
    }

    public static Schedule toModel(ScheduleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Schedule(
                entity.getIdExamen(),
                entity.getIdMateria(),
                entity.getIdAula(),
                entity.getIdHorario(),
                entity.getIdTipo(),
                entity.getIdPeriodo(),
                entity.getProfesorId(),
                entity.getFecha(),
                entity.getGrupo(),
                entity.getStatus()
        );
    }

    public static ScheduleEntity toEntity(Schedule model) {
        if (model == null) {
            return null;
        }
        ScheduleEntity entity = new ScheduleEntity();
        entity.setIdExamen(model.getIdExamen());
        entity.setIdMateria(model.getIdMateria());
        entity.setIdAula(model.getIdAula());
        entity.setIdHorario(model.getIdHorario());
        entity.setIdTipo(model.getIdTipo());
        entity.setIdPeriodo(model.getIdPeriodo());
        entity.setProfesorId(model.getProfesorId());
        entity.setFecha(model.getFecha());
        entity.setGrupo(model.getGrupo());
        entity.setStatus(model.getStatus());
        return entity;
    }

    /**
     * Convierte DTO externo → Modelo de dominio
     */
    public static Schedule toModelFromExternal(ExamenExternoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Schedule(
                null, // ID será generado por la BD
                dto.getIdMateria(),
                dto.getIdAula(),
                dto.getIdHorario(),
                dto.getIdTipo(),
                dto.getIdPeriodo(),
                dto.getProfesorId(),
                dto.getFecha(),
                dto.getGrupo(),
                dto.getStatus()
        );
    }
}
