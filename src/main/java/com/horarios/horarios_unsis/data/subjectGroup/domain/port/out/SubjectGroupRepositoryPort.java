package com.horarios.horarios_unsis.data.subjectGroup.domain.port.out;

import com.horarios.horarios_unsis.data.subjectGroup.domain.model.SubjectGroup;
import java.util.List;
import java.util.Optional;

public interface SubjectGroupRepositoryPort {
    SubjectGroup save(SubjectGroup subjectGroup);
    Optional<SubjectGroup> findById(Integer id);
    List<SubjectGroup> findAll();
    List<SubjectGroup> findByCareer(String careerCode);
    List<SubjectGroup> findByPeriod(String periodCode);
    List<SubjectGroup> findByGroup(String groupCode);
    List<SubjectGroup> findByTeacherId(Integer teacherId);
}
