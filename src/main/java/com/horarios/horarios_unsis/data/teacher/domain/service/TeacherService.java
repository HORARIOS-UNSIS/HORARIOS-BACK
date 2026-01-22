package com.horarios.horarios_unsis.data.teacher.domain.service;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.application.mapper.TeacherMapper;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.teacher.domain.port.in.TeacherUseCase;
import com.horarios.horarios_unsis.data.teacher.domain.port.out.TeacherRepositoryPort;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.entity.TeacherEntity;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.repository.TeacherRepository;
import com.horarios.horarios_unsis.integration.Consume.DTO.ProfesorExternoDTO;
import com.horarios.horarios_unsis.integration.Consume.TeacherConsumeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService implements TeacherUseCase {

    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepositoryPort teacherRepositoryPort;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherConsumeClient teacherConsumeClient;

    // Constructor para inyección del puerto
    public TeacherService(TeacherRepositoryPort teacherRepositoryPort) {
        this.teacherRepositoryPort = teacherRepositoryPort;
    }
    
    @Override
    public TeacherResponseDTO createTeacher(TeacherRequestDTO request) {
        // DTO -> Dominio
        Teacher teacher = TeacherMapper.toModel(request);
        // Persistencia usando el puerto
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

    /**
     * Importa profesores desde la API externa
     * GET /api/profesores
     */
    @Transactional
    public List<TeacherResponseDTO> importarProfesoresDelAPI() {
        try {
            logger.info("Iniciando importación de profesores desde API externa...");
            
            // 1. Obtener datos de la API externa
            ProfesorExternoDTO[] profesoresArray = teacherConsumeClient.obtenerProfesoresDelAPI();
            
            if (profesoresArray == null || profesoresArray.length == 0) {
                logger.warn("No se obtuvieron profesores de la API externa");
                return List.of();
            }
            
            logger.info("Se obtuvieron {} profesores de la API", profesoresArray.length);
            
            // 2. Convertir a modelos de dominio y persistir
            List<TeacherResponseDTO> profesoresImportados = List.of(profesoresArray)
                .stream()
                .map(TeacherMapper::toModelFromExternal)
                .map(teacher -> teacherRepositoryPort.save(teacher))
                .map(TeacherMapper::toDTO)
                .collect(Collectors.toList());
            
            logger.info("Se importaron exitosamente {} profesores", profesoresImportados.size());
            return profesoresImportados;
            
        } catch (Exception e) {
            logger.error("Error al importar profesores desde API", e);
            throw new RuntimeException("Error al importar profesores: " + e.getMessage(), e);
        }
    }
}