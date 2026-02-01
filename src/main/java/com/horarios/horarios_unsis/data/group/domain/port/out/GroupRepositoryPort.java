package com.horarios.horarios_unsis.data.group.domain.port.out;

import com.horarios.horarios_unsis.data.group.domain.model.Group;
import java.util.List;
import java.util.Optional;

public interface GroupRepositoryPort {
    Group save(Group group);
    Optional<Group> findById(Integer id);
    List<Group> findAll();
    List<Group> findByCareer(String careerCode);
    List<Group> findByPeriod(String periodCode);
    List<Group> findByCareerAndPeriod(String careerCode, String periodCode);
}
