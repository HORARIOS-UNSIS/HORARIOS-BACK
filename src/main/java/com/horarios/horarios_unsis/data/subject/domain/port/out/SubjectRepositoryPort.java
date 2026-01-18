package com.horarios.horarios_unsis.data.subject.domain.port.out;

import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import java.util.List;
import java.util.Optional;

public interface SubjectRepositoryPort {
    Subject save(Subject subject);
    Optional<Subject> findById(Integer id);
    List<Subject> findAll();
    void deleteById(Integer id);
}