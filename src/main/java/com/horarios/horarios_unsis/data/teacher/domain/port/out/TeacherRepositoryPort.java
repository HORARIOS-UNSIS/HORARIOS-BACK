package com.horarios.horarios_unsis.data.teacher.domain.port.out;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import java.util.List;
import java.util.Optional;

public interface TeacherRepositoryPort {
    Teacher save(Teacher teacher);
    Optional<Teacher> findById(Integer id);
    List<Teacher> findAll();
    void deleteById(Integer id);
    boolean existsById(Integer id);
}
