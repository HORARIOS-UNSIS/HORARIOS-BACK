package com.horarios.horarios_unsis.data.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticación exitosa")
public class LoginResponseDTO {
    
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;
    
    @Schema(description = "Tipo de token", example = "Bearer")
    private String type = "Bearer";
    
    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;
    
    @Schema(description = "Rol del usuario", example = "ADMIN")
    private String role;
    
    @Schema(description = "ID del usuario", example = "1")
    private Integer idUsuario;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(String token, String username, String role, Integer idUsuario) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.idUsuario = idUsuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}