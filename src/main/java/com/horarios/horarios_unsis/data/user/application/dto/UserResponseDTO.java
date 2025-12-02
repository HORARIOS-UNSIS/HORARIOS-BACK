package com.horarios.horarios_unsis.data.user.application.dto;

import com.horarios.horarios_unsis.data.user.domain.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de un usuario")
public class UserResponseDTO {
    
    @Schema(description = "ID único del usuario", example = "1")
    private Integer idUsuario;
    
    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez López")
    private String nombre;
    
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@unsis.edu")
    private String email;
    
    @Schema(description = "Nombre de usuario", example = "jperez")
    private String username;
    
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private Role rol;
    
    @Schema(description = "Estado del usuario", example = "true")
    private Boolean activo;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Integer idUsuario, String nombre, String email, String username, Role rol, Boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.rol = rol;
        this.activo = activo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}