package com.horarios.horarios_unsis.data.subject.domain.service;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;
import com.horarios.horarios_unsis.data.subject.application.mapper.SubjectMapper;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.domain.port.in.SubjectUseCase;
import com.horarios.horarios_unsis.data.subject.domain.port.out.SubjectRepositoryPort;
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

import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.projection.SubjectDetailsProjection;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectDetailsDTO;
import java.util.ArrayList;

@Service
public class SubjectService implements SubjectUseCase {
    
    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepositoryPort subjectRepositoryPort;

    @SuppressWarnings("unused")
    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private SubjectConsumeClient subjectConsumeClient;

    public SubjectService(SubjectRepositoryPort subjectRepositoryPort) {
        this.subjectRepositoryPort = subjectRepositoryPort;
    }

    @Override
    public SubjectResponseDTO createSubject(SubjectRequestDTO request) {
        Subject subject = SubjectMapper.toDomain(request);
        Subject savedSubject = subjectRepositoryPort.save(subject);
        return SubjectMapper.toDTO(savedSubject);
    }

    @Override
    public SubjectResponseDTO getSubject(Integer id) {
        return subjectRepositoryPort.findById(id)
                .map(SubjectMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Materia con ID " + id + " no encontrada"));
    }

    @Override
    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepositoryPort.findAll().stream()
                .map(SubjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SubjectResponseDTO updateSubject(Integer id, SubjectRequestDTO request) {
        Subject existingSubject = subjectRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Materia no encontrada"));
        
        existingSubject.setNombre(request.getNombre());
        
        Subject updatedSubject = subjectRepositoryPort.save(existingSubject);
        return SubjectMapper.toDTO(updatedSubject);
    }

    @Override
    public void deleteSubject(Integer id) {
        if (!subjectRepositoryPort.findById(id).isPresent()) {
            throw new RuntimeException("No se puede eliminar: Materia no encontrada");
        }
        subjectRepositoryPort.deleteById(id);
    }

    @Override
    public List<SubjectDetailsDTO> getSubjectsByCareerAndPeriod(String claveCarrera, String clavePeriodo) {
        List<SubjectDetailsProjection> projections = subjectRepository.findSubjectsByCareerAndPeriod(claveCarrera, clavePeriodo);
        return projections.stream()
            .map(p -> new SubjectDetailsDTO(p.getNombre(), p.getEsAcademia(), p.getClaveGrupo(), p.getNombreProfesor()))
            .collect(Collectors.toList());
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
                .map(subject -> subjectRepositoryPort.save(subject))
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