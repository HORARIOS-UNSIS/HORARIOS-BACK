package com.horarios.horarios_unsis.data.schoolHours.domain.service;

import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursRequestDTO;
import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursResponseDTO;
import com.horarios.horarios_unsis.data.schoolHours.application.mapper.SchoolHorsMapper;
import com.horarios.horarios_unsis.data.schoolHours.domain.model.SchoollHors;
import com.horarios.horarios_unsis.data.schoolHours.domain.port.in.SchoolHoursServicePort;
import com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.entity.SchoolHoursEntity;
import com.horarios.horarios_unsis.data.schoolHours.infrastructure.persistence.repository.SchoolHoursRepository;
import com.horarios.horarios_unsis.integration.Consume.SchoolHoursConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.DTO.SchoolHoursExternoDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para operaciones con horarios escolares.
 * Implementa la lógica de negocio y consumo de APIs externas.
 */
@Service
@Transactional
public class SchoolHoursService implements SchoolHoursServicePort {
    
    private static final Logger logger = LoggerFactory.getLogger(SchoolHoursService.class);
    
    private final SchoolHoursRepository schoolHoursRepository;
    private final SchoolHoursConsumeClient consumeClient;

    public SchoolHoursService(SchoolHoursRepository schoolHoursRepository, SchoolHoursConsumeClient consumeClient) {
        this.schoolHoursRepository = schoolHoursRepository;
        this.consumeClient = consumeClient;
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
        
        return schoolHoursRepository.findById(id)
                .map(SchoolHorsMapper::toDomain)
                .map(SchoolHorsMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Horario escolar no encontrado con ID: {}", id);
                    return new RuntimeException("Horario escolar no encontrado con ID: " + id);
                });
    }

    @Override
    public List<SchoolHoursResponseDTO> getAllSchoolHours() {
        logger.info("Obteniendo todos los horarios escolares");
        
        return schoolHoursRepository.findAll().stream()
                .map(SchoolHorsMapper::toDomain)
                .map(SchoolHorsMapper::toDTO)
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
     * IMPLEMENTACIÓN CLAVE: Consume API externa y guarda horarios en BD
     * Este es el flujo completo de consumo de API:
     * 1. Llamar al cliente HTTP para obtener datos de API externa
     * 2. Convertir DTOs externos a modelos de dominio
     * 3. Guardar en la base de datos
     * 4. Retornar DTOs de respuesta
     */
    @Override
    public List<SchoolHoursResponseDTO> importarHorariosDelAPI() {
        logger.info("Iniciando importación de horarios desde API externa");
        
        try {
            // 1. Obtener datos de la API
            List<SchoolHoursExternoDTO> horariosDelAPI = consumeClient.obtenerHorariosDelAPI();
            logger.info("Se obtuvieron {} horarios de la API", horariosDelAPI.size());
            
            // 2. Convertir DTOs externos → Modelos de dominio
            List<SchoollHors> horarios = horariosDelAPI.stream()
                    .map(this::convertirDTOExternoAModelo)
                    .collect(Collectors.toList());
            
            // 3. Guardar en BD
            List<SchoolHoursEntity> entities = horarios.stream()
                    .map(SchoolHorsMapper::toEntity)
                    .collect(Collectors.toList());
            
            List<SchoolHoursEntity> guardados = entities.stream()
                    .map(schoolHoursRepository::save)
                    .collect(Collectors.toList());
            
            logger.info("Se guardaron {} horarios en la BD", guardados.size());
            
            // 4. Convertir respuesta
            return guardados.stream()
                    .map(SchoolHorsMapper::toDomain)
                    .map(SchoolHorsMapper::toDTO)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            logger.error("Error al importar horarios desde API: {}", e.getMessage(), e);
            throw new RuntimeException("Error al importar horarios: " + e.getMessage(), e);
        }
    }

    /**
     * Método auxiliar para convertir DTO externo a modelo de dominio
     */
    private SchoollHors convertirDTOExternoAModelo(SchoolHoursExternoDTO dto) {
        return new SchoollHors(
            null,  // El ID será generado por la BD
            dto.getPeriodNumber(),
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getIsBreak(),
            dto.getDescription()
        );
    }
}
