package com.horarios.horarios_unsis.data.user.application.mapper;

import com.horarios.horarios_unsis.data.user.application.dto.UserRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.UserResponseDTO;
import com.horarios.horarios_unsis.data.user.domain.model.User;
import com.horarios.horarios_unsis.data.user.infrastructure.persistence.entity.UserEntity;

public class UserMapper {
    
    public static User requestToModel(UserRequestDTO dto) {
        User user = new User();
        user.setNombre(dto.getNombre());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRol(dto.getRol());
        user.setActivo(true);
        return user;
    }
    
    public static UserEntity modelToEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setIdUsuario(user.getIdUsuario());
        entity.setNombre(user.getNombre());
        entity.setEmail(user.getEmail());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRol(user.getRol());
        entity.setActivo(user.getActivo());
        return entity;
    }
    
    public static User entityToModel(UserEntity entity) {
        return new User(
            entity.getIdUsuario(),
            entity.getNombre(),
            entity.getEmail(),
            entity.getUsername(),
            entity.getPassword(),
            entity.getRol(),
            entity.getActivo()
        );
    }
    
    public static UserResponseDTO modelToResponse(User user) {
        return new UserResponseDTO(
            user.getIdUsuario(),
            user.getNombre(),
            user.getEmail(),
            user.getUsername(),
            user.getRol(),
            user.getActivo()
        );
    }
    
    public static UserResponseDTO entityToDTO(UserEntity entity) {
        return new UserResponseDTO(
            entity.getIdUsuario(),
            entity.getNombre(),
            entity.getEmail(),
            entity.getUsername(),
            entity.getRol(),
            entity.getActivo()
        );
    }
}