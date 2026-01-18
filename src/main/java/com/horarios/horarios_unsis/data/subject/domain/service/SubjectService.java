package com.horarios.horarios_unsis.data.subject.domain.service;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.application.mapper.SubjectMapper;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.domain.port.in.SubjectUseCase;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository.SubjectRepository;
import com.horarios.horarios_unsis.integration.Consume.DTO.MateriaExternaDTO;
import com.horarios.horarios_unsis.integration.Consume.SubjectConsumeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService implements SubjectUseCase {

    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectConsumeClient subjectConsumeClient;

    @Override
    public SubjectResponseDTO createSubject(SubjectRequestDTO request) {
        Subject subject = SubjectMapper.toEntity(request);
        SubjectEntity entity = SubjectMapper.toEntityJPA(subject);
        SubjectEntity saved = subjectRepository.save(entity);
        return SubjectMapper.toDTO(SubjectMapper.toDomain(saved));
    }

    @Override
    public SubjectResponseDTO getSubject(Integer id) {
        return subjectRepository.findById(Long.valueOf(id))
            .map(SubjectMapper::toDomain)
            .map(SubjectMapper::toDTO)
            .orElse(null);
    }

    @Override
    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepository.findAll()
            .stream()
            .map(SubjectMapper::toDomain)
            .map(SubjectMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public SubjectResponseDTO updateSubject(Integer id, SubjectRequestDTO request) {
        return subjectRepository.findById(Long.valueOf(id))
            .map(entity -> {
                entity.setNombre(request.getNombre());
                return subjectRepository.save(entity);
            })
            .map(SubjectMapper::toDomain)
            .map(SubjectMapper::toDTO)
            .orElse(null);
    }

    @Override
    public void deleteSubject(Integer id) {
        subjectRepository.deleteById(Long.valueOf(id));
    }

    /**
     * Importa materias desde la API externa
     * GET /api/materias
     */
    @Transactional
    public List<SubjectResponseDTO> importarMateriasDelAPI() {
        try {
            logger.info("Iniciando importación de materias desde API externa...");
            
            // 1. Obtener datos de la API externa
            MateriaExternaDTO[] materiasArray = subjectConsumeClient.obtenerMateriasDelAPI();
            
            if (materiasArray == null || materiasArray.length == 0) {
                logger.warn("No se obtuvieron materias de la API externa");
                return List.of();
            }
            
            logger.info("Se obtuvieron {} materias de la API", materiasArray.length);
            
            // 2. Convertir a modelos de dominio y persistir
            List<SubjectResponseDTO> materiasImportadas = List.of(materiasArray)
                .stream()
                .map(SubjectMapper::toModelFromExternal)
                .map(SubjectMapper::toEntityJPA)
                .map(subjectRepository::save)
                .map(SubjectMapper::toDomain)
                .map(SubjectMapper::toDTO)
                .collect(Collectors.toList());
            
            logger.info("Se importaron exitosamente {} materias", materiasImportadas.size());
            return materiasImportadas;
            
        } catch (Exception e) {
            logger.error("Error al importar materias desde API", e);
            throw new RuntimeException("Error al importar materias: " + e.getMessage(), e);
        }
    }
}