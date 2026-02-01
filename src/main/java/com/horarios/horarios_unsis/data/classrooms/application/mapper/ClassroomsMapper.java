package com.horarios.horarios_unsis.data.classrooms.application.mapper;

import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsRequestDTO;
import com.horarios.horarios_unsis.data.classrooms.application.dto.ClassroomsResponseDTO;
import com.horarios.horarios_unsis.data.classrooms.domain.model.Classrooms;
import com.horarios.horarios_unsis.data.classrooms.infrastructure.persistence.entity.ClassroomsEntity;
import com.horarios.horarios_unsis.integration.Consume.DTO.AulaExternaDTO;

public class ClassroomsMapper {
    
    /**
     * Convierte RequestDTO → Modelo de dominio
     */
    public static Classrooms toEntity(ClassroomsRequestDTO dto) {
        Classrooms classrooms = new Classrooms();
        classrooms.setNombre(dto.getNombre());
        classrooms.setCapacidad(dto.getCapacidad());
        return classrooms;
    }
    
    /**
     * Convierte Modelo de dominio → Entity JPA
     */
    public static ClassroomsEntity domainToEntity(Classrooms model) {
        if (model == null) return null;
        
        ClassroomsEntity entity = new ClassroomsEntity();
        entity.setIdAula(model.getIdAula());
        entity.setNombre(model.getNombre());
        entity.setCapacidad(model.getCapacidad());
        entity.setClave(model.getClave());
        entity.setStatusProyector(model.getStatusProyector());
        entity.setTipo(model.getTipo());
        
        return entity;
    }
    
    public static ClassroomsResponseDTO toDTO(Classrooms classrooms) {
        return new ClassroomsResponseDTO(
            classrooms.getIdAula(),
            classrooms.getNombre(),
            classrooms.getCapacidad(),
            classrooms.getClave(),
            classrooms.getStatusProyector(),
            classrooms.getTipo()
        );
    }
    
    /**
     * Convierte Entity JPA → Modelo de dominio
     */
    public static Classrooms toModel(ClassroomsEntity entity) {
        if (entity == null) return null;
        
        Classrooms classrooms = new Classrooms();
        classrooms.setIdAula(entity.getIdAula());
        classrooms.setNombre(entity.getNombre());
        classrooms.setCapacidad(entity.getCapacidad());
        classrooms.setClave(entity.getClave());
        classrooms.setStatusProyector(entity.getStatusProyector());
        classrooms.setTipo(entity.getTipo());
        
        return classrooms;
    }
    
    /**
     * Convierte DTO externo → Modelo de dominio
     */
    public static Classrooms toModelFromExternal(AulaExternaDTO dto) {
        if (dto == null) return null;
        
        Classrooms classrooms = new Classrooms();
        classrooms.setIdAula(dto.getIdAula());
        classrooms.setNombre(dto.getNombre());
        classrooms.setCapacidad(dto.getCapacidad());
        
        return classrooms;
    }
}

