package com.horarios.horarios_unsis.data.classrooms.application.mapper;

import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsRequestDTO;
import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsResponseDTO;
import com.horarios.horarios_unsis.data.classrooms.domain.model.Classrooms;

public class ClassroomsMapper {
    
    public static Classrooms toEntity(ClassroomsRequestDTO dto) {
        Classrooms classrooms = new Classrooms();
        classrooms.setNombre(dto.getNombre());
        classrooms.setCapacidad(dto.getCapacidad());
        return classrooms;
    }
    
    public static ClassroomsResponseDTO toDTO(Classrooms classrooms) {
        return new ClassroomsResponseDTO(
            classrooms.getIdAula(),
            classrooms.getNombre(),
            classrooms.getCapacidad()
        );
    }
}
