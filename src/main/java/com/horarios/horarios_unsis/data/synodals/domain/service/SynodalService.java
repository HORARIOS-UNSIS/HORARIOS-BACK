package com.horarios.horarios_unsis.data.synodals.domain.service;

import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalRequestDTO;
import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalResponseDTO;
import com.horarios.horarios_unsis.data.synodals.application.mapper.SynodalMapper;
import com.horarios.horarios_unsis.data.synodals.domain.model.Synodal;
import com.horarios.horarios_unsis.data.synodals.domain.port.in.SynodalUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SynodalService implements SynodalUseCase {
    
    @Override
    public SynodalResponseDTO createSynodal(SynodalRequestDTO request) {
        Synodal synodal = SynodalMapper.toDomain(request);
        return SynodalMapper.toDTO(synodal);
    }

    @Override
    public SynodalResponseDTO getSynodal(Integer id) {
        return null;
    }

    @Override
    public List<SynodalResponseDTO> getAllSynodals() {
        return null;
    }

    @Override
    public SynodalResponseDTO updateSynodal(Integer id, SynodalRequestDTO request) {
        return null;
    }

    @Override
    public void deleteSynodal(Integer id) {
    }

    @Override
    public List<SynodalResponseDTO> getSynodalsByMateria(Integer idMateria) {
        return null;
    }

    @Override
    public List<SynodalResponseDTO> getSynodalsByProfesor(Integer idProfesor) {
        return null;
    }
}
