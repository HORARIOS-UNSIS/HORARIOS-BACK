package com.horarios.horarios_unsis.data.teacher.domain.service;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.application.mapper.TeacherMapper;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.teacher.domain.port.in.TeacherUseCase;
import com.horarios.horarios_unsis.data.teacher.domain.port.out.TeacherRepositoryPort;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService implements TeacherUseCase {
    
    private final TeacherRepositoryPort teacherRepositoryPort;

    // Inyectamos el puerto a través del constructor
    public TeacherService(TeacherRepositoryPort teacherRepositoryPort) {
        this.teacherRepositoryPort = teacherRepositoryPort;
    }
    
    @Override
    public TeacherResponseDTO createTeacher(TeacherRequestDTO request) {
        // DTO -> Dominio
        Teacher teacher = TeacherMapper.toDomain(request);
        // Persistencia
        Teacher savedTeacher = teacherRepositoryPort.save(teacher);
        // Dominio -> DTO
        return TeacherMapper.toDTO(savedTeacher);
    }

    @Override
    public TeacherResponseDTO getTeacher(Integer id) {
        return teacherRepositoryPort.findById(id)
                .map(TeacherMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con ID: " + id));
    }

    @Override
    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepositoryPort.findAll().stream()
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeacherResponseDTO updateTeacher(Integer id, TeacherRequestDTO request) {
        Teacher existingTeacher = teacherRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el profesor para actualizar"));
        
        existingTeacher.setNombre(request.getNombre());
        existingTeacher.setSabatico(request.getSabatico());
        
        Teacher updatedTeacher = teacherRepositoryPort.save(existingTeacher);
        return TeacherMapper.toDTO(updatedTeacher);
    }

    @Override
    public void deleteTeacher(Integer id) {
        if (!teacherRepositoryPort.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Profesor no encontrado");
        }
        teacherRepositoryPort.deleteById(id);
    }
}