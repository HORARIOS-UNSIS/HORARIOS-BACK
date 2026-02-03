package com.horarios.horarios_unsis.data.synodals.application.mapper;

import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalRequestDTO;
import com.horarios.horarios_unsis.data.synodals.application.dto.SynodalResponseDTO;
import com.horarios.horarios_unsis.data.synodals.domain.model.Synodal;
import com.horarios.horarios_unsis.data.subject.application.mapper.SubjectMapper;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;
import com.horarios.horarios_unsis.data.teacher.application.mapper.TeacherMapper;
import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.entity.SynodalEntity;

public class SynodalMapper {
    
    // De DTO de entrada a Modelo de Dominio
    public static Synodal toDomain(SynodalRequestDTO dto) {
        if (dto == null) return null;

        Synodal synodal = new Synodal();
        
        Teacher sinodal = new Teacher();
        sinodal.setIdProfesor(dto.getIdProfesorSinodal());
        
        Teacher titular = new Teacher();
        titular.setIdProfesor(dto.getIdProfesorTitular());
        
        Subject materia = new Subject();
        materia.setIdMateria(dto.getIdMateria());

        synodal.setProfesorSinodal(sinodal);
        synodal.setProfesorTitular(titular);
        synodal.setMateria(materia);
        
        return synodal;
    }
    
    // De Modelo de Dominio a DTO de respuesta
    public static SynodalResponseDTO toDTO(Synodal synodal) {
        if (synodal == null) return null;

        SynodalResponseDTO dto = new SynodalResponseDTO();
        dto.setIdSynodal(synodal.getId());
        
        if (synodal.getProfesorSinodal() != null) {
            dto.setIdProfesorSinodal(synodal.getProfesorSinodal().getIdProfesor());
        }
        
        if (synodal.getProfesorTitular() != null) {
            dto.setIdProfesorTitular(synodal.getProfesorTitular().getIdProfesor());
        }
        
        if (synodal.getMateria() != null) {
            dto.setIdMateria(synodal.getMateria().getIdMateria());
        }
        
        return dto;
    }

    public static Synodal entityToDomain(SynodalEntity entity) {
        if (entity == null) return null;
        Synodal domain = new Synodal();
        domain.setId(entity.getId());

        if (entity.getMateria() != null) {
        domain.setMateria(SubjectMapper.entityToDomain(entity.getMateria()));
        }
    
         if (entity.getProfesorSinodal() != null) {
        domain.setProfesorSinodal(TeacherMapper.entityToDomain(entity.getProfesorSinodal()));
         }
        
        return domain;
    }
}