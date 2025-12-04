package com.horarios.horarios_unsis.data.teacher.application.mapper;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;

public class TeacherMapper {
    
    public static Teacher toEntity(TeacherRequestDTO dto) {
        Teacher teacher = new Teacher();
        teacher.setNombre(dto.getNombre());
        teacher.setSabatico(dto.getSabatico());
        return teacher;
    }
    
    public static TeacherResponseDTO toDTO(Teacher teacher) {
        return new TeacherResponseDTO(
            teacher.getIdProfesor(),
            teacher.getNombre(),
            teacher.getSabatico()
        );
    }
}