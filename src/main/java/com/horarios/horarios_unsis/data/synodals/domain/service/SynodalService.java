package com.horarios.horarios_unsis.data.synodals.domain.service;

import com.horarios.horarios_unsis.data.synodals.application.dto.SinodalAssignmentDTO;
import com.horarios.horarios_unsis.data.synodals.application.dto.SinodalDTO;
import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalRequestDTO;
import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalResponseDTO;
import com.horarios.horarios_unsis.data.synodals.application.mapper.SynodalMapper;
import com.horarios.horarios_unsis.data.synodals.domain.model.Synodal;
import com.horarios.horarios_unsis.data.synodals.domain.port.in.SynodalUseCase;
import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.entity.SynodalEntity;
import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.projection.SinodalAssignmentProjection;
import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.repository.SynodalRepository;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository.SubjectRepository;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.entity.TeacherEntity;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.repository.TeacherRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SynodalService implements SynodalUseCase {

    private final SynodalRepository synodalRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public SynodalService(SynodalRepository synodalRepository, 
                          SubjectRepository subjectRepository, 
                          TeacherRepository teacherRepository) {
        this.synodalRepository = synodalRepository;
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
    }
    
    @Override
    @Transactional
    public SynodalResponseDTO createSynodal(SynodalRequestDTO request) {
        SubjectEntity materia = subjectRepository.findById(request.getIdMateria())
                .orElseThrow(() -> new RuntimeException("Materia no encontrada"));
        
        TeacherEntity profesorTitular = teacherRepository.findById(request.getIdProfesorTitular())
                .orElseThrow(() -> new RuntimeException("Profesor titular no encontrado"));

        TeacherEntity profesorSinodal = teacherRepository.findById(request.getIdProfesorSinodal())
                .orElseThrow(() -> new RuntimeException("Profesor sinodal no encontrado"));

        SynodalEntity entity = new SynodalEntity();
        entity.setMateria(materia);
        entity.setProfesorTitular(profesorTitular);
        entity.setProfesorSinodal(profesorSinodal);

        SynodalEntity saved = synodalRepository.save(entity);
        
        // Simple mapping manually or we could use the Mapper if it supports Entity -> DTO
        SynodalResponseDTO response = new SynodalResponseDTO();
        response.setIdSynodal(saved.getId());
        response.setIdMateria(saved.getMateria().getIdMateria());
        response.setNombreMateria(saved.getMateria().getNombre());
        response.setIdProfesorTitular(saved.getProfesorTitular().getIdProfesor());
        response.setNombreProfesorTitular(saved.getProfesorTitular().getNombre());
        response.setIdProfesorSinodal(saved.getProfesorSinodal().getIdProfesor());
        response.setNombreProfesorSinodal(saved.getProfesorSinodal().getNombre());
        
        return response;
    }

    @Override
    public SynodalResponseDTO getSynodal(Integer id) {
        // Implementación básica, se puede mejorar
        return null; 
    }

    @Override
    public List<SynodalResponseDTO> getAllSynodals() {
        return null;
    }

    @Override
    public SynodalResponseDTO updateSynodal(Integer id, SynodalRequestDTO request) {
        return null;
    }

    @Override
    @Transactional
    public void deleteSynodal(Integer id) {
        synodalRepository.deleteById(id);
    }

    @Override
    public List<SynodalResponseDTO> getSynodalsByMateria(Integer idMateria) {
        return null;
    }

    @Override
    public List<SynodalResponseDTO> getSynodalsByProfesor(Integer idProfesor) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SinodalAssignmentDTO> getAssignments(String carrera, String periodo) {
        List<SinodalAssignmentProjection> projections = synodalRepository.findAssignmentsByCareerAndPeriod(carrera, periodo);
        List<SinodalAssignmentDTO> result = new ArrayList<>();

        for (SinodalAssignmentProjection p : projections) {
            List<SynodalEntity> synodals = synodalRepository.findByMateria_IdMateriaAndProfesorTitular_IdProfesor(p.getIdMateria(), p.getIdProfesorTitular());
            
            List<SinodalDTO> sinodalDTOs = synodals.stream()
                .map(s -> new SinodalDTO(s.getId(), s.getProfesorSinodal().getIdProfesor(), s.getProfesorSinodal().getNombre()))
                .collect(Collectors.toList());

            SinodalAssignmentDTO dto = new SinodalAssignmentDTO(
                p.getNombreMateria(),
                p.getNombreProfesorTitular(),
                p.getIdMateria(),
                p.getIdProfesorTitular(),
                sinodalDTOs
            );
            dto.setSemestre(p.getSemestre());
            result.add(dto);
        }
        return result;
    }
}
