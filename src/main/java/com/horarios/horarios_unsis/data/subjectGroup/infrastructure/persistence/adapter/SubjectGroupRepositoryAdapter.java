package com.horarios.horarios_unsis.data.subjectGroup.infrastructure.persistence.adapter;

import com.horarios.horarios_unsis.data.subjectGroup.domain.model.SubjectGroup;
import com.horarios.horarios_unsis.data.subjectGroup.domain.port.out.SubjectGroupRepositoryPort;
import com.horarios.horarios_unsis.data.subjectGroup.infrastructure.persistence.entity.SubjectGroupEntity;
import com.horarios.horarios_unsis.data.subjectGroup.infrastructure.persistence.repository.SubjectGroupRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class SubjectGroupRepositoryAdapter implements SubjectGroupRepositoryPort {

    private final SubjectGroupRepository subjectGroupRepository;

    public SubjectGroupRepositoryAdapter(SubjectGroupRepository subjectGroupRepository) {
        this.subjectGroupRepository = subjectGroupRepository;
    }

    @Override
    public SubjectGroup save(SubjectGroup subjectGroup) {
        SubjectGroupEntity entity = toEntity(subjectGroup);
        return toDomain(subjectGroupRepository.save(entity));
    }

    @Override
    public Optional<SubjectGroup> findById(Integer id) {
        return subjectGroupRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<SubjectGroup> findAll() {
        return subjectGroupRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectGroup> findByCareer(String careerCode) {
        return subjectGroupRepository.findByClaveCarrera(careerCode).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectGroup> findByPeriod(String periodCode) {
        return subjectGroupRepository.findByClavePeriodo(periodCode).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectGroup> findByGroup(String groupCode) {
        return subjectGroupRepository.findByClaveGrupo(groupCode).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectGroup> findByTeacherId(Integer teacherId) {
        return subjectGroupRepository.findByIdProfesor(teacherId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private SubjectGroupEntity toEntity(SubjectGroup domain) {
        if (domain == null) return null;
        SubjectGroupEntity entity = new SubjectGroupEntity();
        entity.setIdMateriaGrupo(domain.getIdMateriaGrupo());
        entity.setActivo(domain.getActivo());
        entity.setClaveCarrera(domain.getClaveCarrera());
        entity.setClaveGrupo(domain.getClaveGrupo());
        entity.setClaveMateria(domain.getClaveMateria());
        entity.setClavePeriodo(domain.getClavePeriodo());
        entity.setFechaSincronizacion(domain.getFechaSincronizacion());
        entity.setHorasSemana(domain.getHorasSemana());
        entity.setIdProfesor(domain.getIdProfesor());
        entity.setNombreMateria(domain.getNombreMateria());
        entity.setNombreProfesor(domain.getNombreProfesor());
        return entity;
    }

    private SubjectGroup toDomain(SubjectGroupEntity entity) {
        if (entity == null) return null;
        return new SubjectGroup(
                entity.getIdMateriaGrupo(),
                entity.getActivo(),
                entity.getClaveCarrera(),
                entity.getClaveGrupo(),
                entity.getClaveMateria(),
                entity.getClavePeriodo(),
                entity.getFechaSincronizacion(),
                entity.getHorasSemana(),
                entity.getIdProfesor(),
                entity.getNombreMateria(),
                entity.getNombreProfesor()
        );
    }
}
