package com.horarios.horarios_unsis.data.teacher.application.mapper;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.entity.TeacherEntity;

public class TeacherMapper {
    
    // De DTO a Dominio
    public static Teacher toDomain(TeacherRequestDTO dto) {
        if (dto == null) return null;
        Teacher teacher = new Teacher();
        teacher.setNombre(dto.getNombre());
        teacher.setSabatico(dto.getSabatico());
        return teacher;
    }

    // De Entidad (DB) a Dominio 
    public static Teacher entityToDomain(TeacherEntity entity) {
        if (entity == null) return null;
        Teacher teacher = new Teacher();
        teacher.setIdProfesor(entity.getIdProfesor()); 
        teacher.setNombre(entity.getNombre());
        teacher.setSabatico(entity.getSabatico());
        return teacher;
    }
    
    // De Dominio a DTO de respuesta
    public static TeacherResponseDTO toDTO(Teacher teacher) {
        if (teacher == null) return null;
        return new TeacherResponseDTO(
            teacher.getIdProfesor(),
            teacher.getNombre(),
            teacher.getSabatico()
        );
    }
}