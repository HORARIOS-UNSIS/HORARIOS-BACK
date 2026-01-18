package com.horarios.horarios_unsis.data.classrooms.domain.service;

import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsRequestDTO;
import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsResponseDTO;
import com.horarios.horarios_unsis.data.classrooms.application.mapper.ClassroomsMapper;
import com.horarios.horarios_unsis.data.classrooms.domain.model.Classrooms;
import com.horarios.horarios_unsis.data.classrooms.domain.port.in.ClassroomsUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomsService implements ClassroomsUseCase {
    
    // TODO: Implementar repositorio cuando esté disponible
    
    @Override
    public ClassroomsResponseDTO createClassroom(ClassroomsRequestDTO request) {
        Classrooms classrooms = ClassroomsMapper.toEntity(request);
        // TODO: Guardar en repositorio
        return ClassroomsMapper.toDTO(classrooms);
    }

    @Override
    public ClassroomsResponseDTO getClassroom(Integer id) {
        // TODO: Buscar en repositorio
        return null;
    }

    @Override
    public List<ClassroomsResponseDTO> getAllClassrooms() {
        // TODO: Obtener todos del repositorio
        return null;
    }

    @Override
    public ClassroomsResponseDTO updateClassroom(Integer id, ClassroomsRequestDTO request) {
        // TODO: Actualizar en repositorio
        return null;
    }

    @Override
    public void deleteClassroom(Integer id) {
        // TODO: Eliminar del repositorio
    }
}
