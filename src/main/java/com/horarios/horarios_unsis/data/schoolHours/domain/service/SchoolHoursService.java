package com.horarios.horarios_unsis.data.schoolHours.domain.service;

import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursRequestDTO;
import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursResponseDTO;
import com.horarios.horarios_unsis.data.schoolHours.application.mapper.SchoolHorsMapper;
import com.horarios.horarios_unsis.data.schoolHours.domain.model.SchoollHors;
import com.horarios.horarios_unsis.data.schoolHours.domain.port.in.SchoolHoursServicePort;
import com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.entity.SchoolHoursEntity;
import com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.repository.SchoolHoursRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para operaciones con horarios escolares.
 * Implementa la lógica de negocio para gestión de horarios.
 */
@Service
@Transactional
public class SchoolHoursService implements SchoolHoursServicePort {
    
    private static final Logger logger = LoggerFactory.getLogger(SchoolHoursService.class);
    
    private final SchoolHoursRepository schoolHoursRepository;

    public SchoolHoursService(SchoolHoursRepository schoolHoursRepository) {
        this.schoolHoursRepository = schoolHoursRepository;
    }

    @Override
    public SchoolHoursResponseDTO createSchoolHours(SchoolHoursRequestDTO request) {
        logger.info("Creando nuevo horario escolar: {}", request.getDescription());
        
        SchoollHors model = SchoolHorsMapper.toModel(request);
        SchoolHoursEntity entity = SchoolHorsMapper.toEntity(model);
        SchoolHoursEntity saved = schoolHoursRepository.save(entity);
        
        SchoollHors savedModel = SchoolHorsMapper.toDomain(saved);
        return SchoolHorsMapper.toDTO(savedModel);
    }

    @Override
    public SchoolHoursResponseDTO getSchoolHours(Long id) {
        logger.info("Obteniendo horario escolar con ID: {}", id);
        
        SchoolHoursEntity entity = schoolHoursRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Horario escolar no encontrado con ID: {}", id);
                    return new RuntimeException("Horario escolar no encontrado con ID: " + id);
                });
        
        SchoollHors model = SchoolHorsMapper.toDomain(entity);
        SchoolHoursResponseDTO response = SchoolHorsMapper.toDTO(model);
        return response;
    }

    @Override
    public List<SchoolHoursResponseDTO> getAllSchoolHours() {
        logger.info("Obteniendo todos los horarios escolares");
        
        List<SchoolHoursEntity> entities = schoolHoursRepository.findAll();
        return entities.stream()
                .map(entity -> {
                    SchoollHors model = SchoolHorsMapper.toDomain(entity);
                    return SchoolHorsMapper.toDTO(model);
                })
                .collect(Collectors.toList());
    }

    @Override
    public SchoolHoursResponseDTO updateSchoolHours(Long id, SchoolHoursRequestDTO request) {
        logger.info("Actualizando horario escolar con ID: {}", id);
        
        SchoolHoursEntity entity = schoolHoursRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Horario escolar no encontrado con ID: " + id));
        
        entity.setPeriodNumber(request.getPeriodNumber());
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        entity.setIsBreak(request.getIsBreak());
        entity.setDescription(request.getDescription());
        
        SchoolHoursEntity updated = schoolHoursRepository.save(entity);
        SchoollHors updatedModel = SchoolHorsMapper.toDomain(updated);
        return SchoolHorsMapper.toDTO(updatedModel);
    }

    @Override
    public void deleteSchoolHours(Long id) {
        logger.info("Eliminando horario escolar con ID: {}", id);
        
        if (!schoolHoursRepository.existsById(id)) {
            throw new RuntimeException("Horario escolar no encontrado con ID: " + id);
        }
        schoolHoursRepository.deleteById(id);
    }

    /**
     * NOTA: Los horarios escolares vienen embebidos en HorarioExternoDTO
     * No hay un endpoint separado para horarios, por lo que este método
     * retorna una lista vacía. El consumo de horarios se realiza en ScheduleService.
     */
    @Override
    public List<SchoolHoursResponseDTO> importarHorariosDelAPI() {
        logger.info("Los horarios escolares se consumen desde ScheduleService (embebidos en HorarioExternoDTO)");
        return List.of();
    }
}
