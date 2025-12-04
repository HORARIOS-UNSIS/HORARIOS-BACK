package com.horarios.horarios_unsis.data.subject.domain.port.in;

import com.horarios.horarios_unsis.data.subject.application.dto.SubjectRequestDTO;
import com.horarios.horarios_unsis.data.subject.application.dto.SubjectResponseDTO;

import java.util.List;

public interface SubjectUseCase {
    SubjectResponseDTO createSubject(SubjectRequestDTO request);
    SubjectResponseDTO getSubject(Integer id);
    List<SubjectResponseDTO> getAllSubjects();
    SubjectResponseDTO updateSubject(Integer id, SubjectRequestDTO request);
    void deleteSubject(Integer id);
}