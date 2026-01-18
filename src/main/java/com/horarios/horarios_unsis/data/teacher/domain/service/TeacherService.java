package com.horarios.horarios_unsis.data.teacher.domain.service;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;
import com.horarios.horarios_unsis.data.teacher.application.mapper.TeacherMapper;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.teacher.domain.port.in.TeacherUseCase;
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

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private TeacherConsumeClient teacherConsumeClient;

    @Override
    public TeacherResponseDTO createTeacher(TeacherRequestDTO request) {
        Teacher teacher = TeacherMapper.toEntity(request);
        TeacherEntity entity = TeacherMapper.toEntity(teacher);
        TeacherEntity saved = teacherRepository.save(entity);
        return TeacherMapper.toDTO(TeacherMapper.toDomain(saved));
    }

    @Override
    public TeacherResponseDTO getTeacher(Integer id) {
        return teacherRepository.findById(Long.valueOf(id))
            .map(TeacherMapper::toDomain)
            .map(TeacherMapper::toDTO)
            .orElse(null);
    }

    @Override
    public List<TeacherResponseDTO> getAllTeachers() {
        return teacherRepository.findAll()
            .stream()
            .map(TeacherMapper::toDomain)
            .map(TeacherMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public TeacherResponseDTO updateTeacher(Integer id, TeacherRequestDTO request) {
        return teacherRepository.findById(Long.valueOf(id))
            .map(entity -> {
                entity.setNombre(request.getNombre());
                entity.setSabatico(request.getSabatico());
                return teacherRepository.save(entity);
            })
            .map(TeacherMapper::toDomain)
            .map(TeacherMapper::toDTO)
            .orElse(null);
    }

    @Override
    public void deleteTeacher(Integer id) {
        teacherRepository.deleteById(Long.valueOf(id));
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
                .map(TeacherMapper::toEntity)
                .map(teacherRepository::save)
                .map(TeacherMapper::toDomain)
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