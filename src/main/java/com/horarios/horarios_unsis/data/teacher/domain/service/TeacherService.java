package com.horarios.horarios_unsis.data.teacher.domain.service;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.application.mapper.TeacherMapper;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.teacher.domain.port.in.TeacherUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService implements TeacherUseCase {
    
    // TODO: Implementar repositorio cuando esté disponible
    
    @Override
    public TeacherResponseDTO createTeacher(TeacherRequestDTO request) {
        Teacher teacher = TeacherMapper.toEntity(request);
        // TODO: Guardar en repositorio
        return TeacherMapper.toDTO(teacher);
    }

    @Override
    public TeacherResponseDTO getTeacher(Integer id) {
        // TODO: Buscar en repositorio
        return null;
    }

    @Override
    public List<TeacherResponseDTO> getAllTeachers() {
        // TODO: Obtener todos del repositorio
        return null;
    }

    @Override
    public TeacherResponseDTO updateTeacher(Integer id, TeacherRequestDTO request) {
        // TODO: Actualizar en repositorio
        return null;
    }

    @Override
    public void deleteTeacher(Integer id) {
        // TODO: Eliminar del repositorio
    }
}