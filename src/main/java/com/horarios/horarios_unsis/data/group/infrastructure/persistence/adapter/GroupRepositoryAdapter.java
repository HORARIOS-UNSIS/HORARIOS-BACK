package com.horarios.horarios_unsis.data.group.infrastructure.persistence.adapter;

import com.horarios.horarios_unsis.data.group.domain.model.Group;
import com.horarios.horarios_unsis.data.group.domain.port.out.GroupRepositoryPort;
import com.horarios.horarios_unsis.data.group.infrastructure.persistence.entity.GroupEntity;
import com.horarios.horarios_unsis.data.group.infrastructure.persistence.repository.GroupRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GroupRepositoryAdapter implements GroupRepositoryPort {

    private final GroupRepository groupRepository;

    public GroupRepositoryAdapter(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Group save(Group group) {
        GroupEntity entity = toEntity(group);
        return toDomain(groupRepository.save(entity));
    }

    @Override
    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> findByCareer(String careerCode) {
        return groupRepository.findByClaveCarreraContaining(careerCode).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> findByPeriod(String periodCode) {
        return groupRepository.findByClavePeriodo(periodCode).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Group> findByCareerAndPeriod(String careerCode, String periodCode) {
        return groupRepository.findByClaveCarreraContainingAndClavePeriodo(careerCode, periodCode).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private GroupEntity toEntity(Group domain) {
        if (domain == null) return null;
        GroupEntity entity = new GroupEntity();
        entity.setIdGrupo(domain.getIdGrupo());
        entity.setAlumnos(domain.getAlumnos());
        entity.setClave(domain.getClave());
        entity.setClaveCarrera(domain.getClaveCarrera());
        entity.setClavePeriodo(domain.getClavePeriodo());
        entity.setNombre(domain.getNombre());
        entity.setSemestre(domain.getSemestre());
        return entity;
    }

    private Group toDomain(GroupEntity entity) {
        if (entity == null) return null;
        return new Group(
                entity.getIdGrupo(),
                entity.getAlumnos(),
                entity.getClave(),
                entity.getClaveCarrera(),
                entity.getClavePeriodo(),
                entity.getNombre(),
                entity.getSemestre()
        );
    }
}
