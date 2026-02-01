package com.horarios.horarios_unsis.schedule.application.mapper;

import com.horarios.horarios_unsis.schedule.domain.model.Schedule;
import com.horarios.horarios_unsis.schedule.infrastructure.persistence.entity.ScheduleEntity;
import com.horarios.horarios_unsis.schedule.application.dto.ScheduleResponseDTO;
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
                entity.getStatus(),
                entity.getClaveMateria(),
                entity.getNombreAula(),
                entity.getNombreMateria(),
                entity.getNombreProfesor(),
                entity.getEnHorarioOficial(),
                entity.getHoraInicio(),
                entity.getHoraFin(),
                entity.getIsLocked()
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
        entity.setClaveMateria(model.getClaveMateria());
        entity.setNombreAula(model.getNombreAula());
        entity.setNombreMateria(model.getNombreMateria());
        entity.setNombreProfesor(model.getNombreProfesor());
        entity.setEnHorarioOficial(model.getEnHorarioOficial());
        entity.setHoraInicio(model.getHoraInicio());
        entity.setHoraFin(model.getHoraFin());
        entity.setIsLocked(model.getIsLocked());
        return entity;
    }

    public static ScheduleResponseDTO toResponseDTO(Schedule schedule) {
        if (schedule == null) {
            return null;
        }
        return new ScheduleResponseDTO(
                schedule.getIdExamen(),
                schedule.getIdMateria(),
                schedule.getIdAula(),
                schedule.getIdHorario(),
                schedule.getIdTipo(),
                schedule.getIdPeriodo(),
                schedule.getProfesorId(),
                schedule.getFecha(),
                schedule.getGrupo(),
                schedule.getStatus(),
                schedule.getClaveMateria(),
                schedule.getNombreAula(),
                schedule.getNombreMateria(),
                schedule.getNombreProfesor(),
                schedule.getEnHorarioOficial(),
                schedule.getHoraInicio(),
                schedule.getHoraFin(),
                schedule.getIsLocked()
        );
    }

    /**
     * Convierte DTO externo → Modelo de dominio
     */
    public static Schedule toModelFromExternal(ExamenExternoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Asignamos valores por defecto a los nuevos campos, ya que pueden no venir del DTO externo
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
                dto.getStatus(),
                null, // claveMateria
                null, // nombreAula
                null, // nombreMateria
                null, // nombreProfesor
                false, // enHorarioOficial
                null, // horaInicio
                null, // horaFin
                false  // isLocked
        );
    }
}
