package com.horarios.horarios_unsis.data.group.application.mapper;

import com.horarios.horarios_unsis.data.group.application.dto.GroupResponseDTO;
import com.horarios.horarios_unsis.data.group.domain.model.Group;
import com.horarios.horarios_unsis.data.group.infrastructure.persistence.entity.GroupEntity;

public class GroupMapper {

    public static GroupResponseDTO toDTO(Group domain) {
        if (domain == null) return null;
        return new GroupResponseDTO(
            domain.getIdGrupo(),
            domain.getAlumnos(),
            domain.getClave(),
            domain.getClaveCarrera(),
            domain.getClavePeriodo(),
            domain.getNombre(),
            domain.getSemestre()
        );
    }
    
    // Additional mapping methods can be added if needed, e.g. Entity to Domain is handled by Adapter, 
    // but Domain to ResponseDTO is handled here.
}
