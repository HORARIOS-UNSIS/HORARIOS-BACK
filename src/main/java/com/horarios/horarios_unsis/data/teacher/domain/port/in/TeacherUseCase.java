package com.horarios.horarios_unsis.data.teacher.domain.port.in;

import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherRequestDTO;
import com.horarios.horarios_unsis.data.teacher.application.dto.TeacherResponseDTO;

import java.util.List;

public interface TeacherUseCase {
    TeacherResponseDTO createTeacher(TeacherRequestDTO request);
    TeacherResponseDTO getTeacher(Integer id);
    List<TeacherResponseDTO> getAllTeachers();
    TeacherResponseDTO updateTeacher(Integer id, TeacherRequestDTO request);
    void deleteTeacher(Integer id);
}