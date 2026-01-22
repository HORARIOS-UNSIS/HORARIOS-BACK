package com.horarios.horarios_unsis.data.classrooms.domain.service;

import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsRequestDTO;
import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsResponseDTO;
import com.horarios.horarios_unsis.data.classrooms.application.mapper.ClassroomsMapper;
import com.horarios.horarios_unsis.data.classrooms.domain.model.Classrooms;
import com.horarios.horarios_unsis.data.classrooms.domain.port.in.ClassroomsUseCase;
import com.horarios.horarios_unsis.data.classrooms.infrastructure.persistence.entity.ClassroomsEntity;
import com.horarios.horarios_unsis.data.classrooms.infrastructure.persistence.repository.ClassroomsRepository;
import com.horarios.horarios_unsis.integration.Consume.AulaConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.DTO.AulaExternaDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClassroomsService implements ClassroomsUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(ClassroomsService.class);
    
    @Autowired
    private ClassroomsRepository classroomsRepository;
    
    @Autowired
    private AulaConsumeClient aulaConsumeClient;

    @Override
    public ClassroomsResponseDTO createClassroom(ClassroomsRequestDTO request) {
        logger.info("Creando nuevo aula: {}", request.getNombre());
        
        Classrooms model = ClassroomsMapper.toEntity(request);
        ClassroomsEntity entity = ClassroomsMapper.domainToEntity(model);
        ClassroomsEntity saved = classroomsRepository.save(entity);
        Classrooms savedModel = ClassroomsMapper.toModel(saved);
        
        return ClassroomsMapper.toDTO(savedModel);
    }

    @Override
    public ClassroomsResponseDTO getClassroom(Integer id) {
        logger.info("Obteniendo aula con ID: {}", id);
        
        return classroomsRepository.findById(id)
            .map(ClassroomsMapper::toModel)
            .map(ClassroomsMapper::toDTO)
            .orElseThrow(() -> {
                logger.warn("Aula no encontrada con ID: {}", id);
                return new RuntimeException("Aula no encontrada con ID: " + id);
            });
    }

    @Override
    public List<ClassroomsResponseDTO> getAllClassrooms() {
        logger.info("Obteniendo todas las aulas");
        
        return classroomsRepository.findAll().stream()
            .map(ClassroomsMapper::toModel)
            .map(ClassroomsMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public ClassroomsResponseDTO updateClassroom(Integer id, ClassroomsRequestDTO request) {
        logger.info("Actualizando aula con ID: {}", id);
        
        ClassroomsEntity entity = classroomsRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aula no encontrada con ID: " + id));
        
        entity.setNombre(request.getNombre());
        entity.setCapacidad(request.getCapacidad());
        
        ClassroomsEntity updated = classroomsRepository.save(entity);
        Classrooms model = ClassroomsMapper.toModel(updated);
        
        return ClassroomsMapper.toDTO(model);
    }

    @Override
    public void deleteClassroom(Integer id) {
        logger.info("Eliminando aula con ID: {}", id);
        
        if (!classroomsRepository.existsById(id)) {
            throw new RuntimeException("Aula no encontrada con ID: " + id);
        }
        
        classroomsRepository.deleteById(id);
        logger.info("Aula eliminada: {}", id);
    }

    /**
     * Importa aulas desde API externa
     * 
     * GET /api/aulas → AulaExternaDTO[]
     * → ClassroomsMapper.toModelFromExternal()
     * → repository.save()
     * → BD tabla "aulas"
     */
    @Transactional
    public List<ClassroomsResponseDTO> importarAulasDelAPI() {
        logger.info("=== INICIANDO IMPORTACIÓN DE AULAS DESDE API ===");
        
        try {
            // 1. Consumir del API
            List<AulaExternaDTO> aulasDelAPI = aulaConsumeClient.obtenerAulasDelAPI();
            logger.info("Se obtuvieron {} aulas de la API", aulasDelAPI.size());
            
            // 2. Convertir, persistir y retornar
            List<ClassroomsResponseDTO> guardadas = aulasDelAPI.stream()
                .map(dto -> {
                    // Convertir DTO Externo → Model de Dominio
                    Classrooms model = ClassroomsMapper.toModelFromExternal(dto);
                    
                    // Convertir Model → Entity JPA
                    ClassroomsEntity entity = ClassroomsMapper.domainToEntity(model);
                    
                    // Persistir en BD
                    ClassroomsEntity saved = classroomsRepository.save(entity);
                    logger.debug("Aula guardada: {} (ID: {})", 
                        saved.getNombre(), saved.getIdAula());
                    
                    // Convertir Entity → Model para respuesta
                    Classrooms savedModel = ClassroomsMapper.toModel(saved);
                    
                    // Convertir Model → DTO de respuesta
                    return ClassroomsMapper.toDTO(savedModel);
                })
                .collect(Collectors.toList());
            
            logger.info("✅ Se importaron {} aulas exitosamente", guardadas.size());
            return guardadas;
            
        } catch (Exception e) {
            logger.error("❌ Error importando aulas desde API: {}", e.getMessage(), e);
            throw new RuntimeException("Error en importación de aulas: " + e.getMessage(), e);
        }
    }
}
