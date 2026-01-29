package com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.adapter;

import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.teacher.domain.port.out.TeacherRepositoryPort;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.entity.TeacherEntity;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.repository.TeacherRepository;
import com.horarios.horarios_unsis.data.teacher.application.mapper.TeacherMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TeacherRepositoryAdapter implements TeacherRepositoryPort {

    private final TeacherRepository teacherRepository;

    public TeacherRepositoryAdapter(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Teacher save(Teacher teacher) {
        TeacherEntity entity = TeacherMapper.toEntity(teacher);
        TeacherEntity saved = teacherRepository.save(entity);
        return TeacherMapper.entityToDomain(saved);
    }

    @Override
    public Optional<Teacher> findById(Integer id) {
        return teacherRepository.findById(id)
                .map(TeacherMapper::entityToDomain);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherRepository.findAll().stream()
                .map(TeacherMapper::entityToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return teacherRepository.existsById(id);
    }
}
