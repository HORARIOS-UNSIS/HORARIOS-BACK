package com.horarios.horarios_unsis.data.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para autenticación de usuario")
public class LoginRequestDTO {
    
    @Schema(description = "Nombre de usuario", example = "admin", required = true)
    @NotBlank(message = "El username es obligatorio")
    private String username;
    
    @Schema(description = "Contraseña", example = "password123", required = true)
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}