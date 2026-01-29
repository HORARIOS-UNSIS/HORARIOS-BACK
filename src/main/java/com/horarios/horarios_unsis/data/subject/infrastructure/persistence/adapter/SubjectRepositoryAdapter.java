package com.horarios.horarios_unsis.data.subject.infrastructure.persistence.adapter;

import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.subject.domain.port.out.SubjectRepositoryPort;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.repository.SubjectRepository;
import com.horarios.horarios_unsis.data.subject.application.mapper.SubjectMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SubjectRepositoryAdapter implements SubjectRepositoryPort {

    private final SubjectRepository subjectRepository;

    public SubjectRepositoryAdapter(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Subject save(Subject subject) {
        SubjectEntity entity = SubjectMapper.toEntityJPA(subject);
        SubjectEntity saved = subjectRepository.save(entity);
        return SubjectMapper.entityToDomain(saved);
    }

    @Override
    public Optional<Subject> findById(Integer id) {
        return subjectRepository.findById(id)
                .map(SubjectMapper::entityToDomain);
    }

    @Override
    public List<Subject> findAll() {
        return subjectRepository.findAll().stream()
                .map(SubjectMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        subjectRepository.deleteById(id);
    }
}
