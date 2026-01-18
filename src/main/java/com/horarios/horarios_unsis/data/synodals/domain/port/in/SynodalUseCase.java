package com.horarios.horarios_unsis.data.synodals.domain.port.in;

import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalRequestDTO;
import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalResponseDTO;

import java.util.List;

public interface SynodalUseCase {
    SynodalResponseDTO createSynodal(SynodalRequestDTO request);
    SynodalResponseDTO getSynodal(Integer id);
    List<SynodalResponseDTO> getAllSynodals();
    SynodalResponseDTO updateSynodal(Integer id, SynodalRequestDTO request);
    void deleteSynodal(Integer id);
    List<SynodalResponseDTO> getSynodalsByMateria(Integer idMateria);
    List<SynodalResponseDTO> getSynodalsByProfesor(Integer idProfesor);
}
