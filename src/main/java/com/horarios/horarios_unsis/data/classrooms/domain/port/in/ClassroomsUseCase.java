package com.horarios.horarios_unsis.data.classrooms.domain.port.in;

import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsRequestDTO;
import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsResponseDTO;

import java.util.List;

public interface ClassroomsUseCase {
    ClassroomsResponseDTO createClassroom(ClassroomsRequestDTO request);
    ClassroomsResponseDTO getClassroom(Integer id);
    List<ClassroomsResponseDTO> getAllClassrooms();
    ClassroomsResponseDTO updateClassroom(Integer id, ClassroomsRequestDTO request);
    void deleteClassroom(Integer id);
}
