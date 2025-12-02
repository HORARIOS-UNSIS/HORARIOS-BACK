package com.horarios.horarios_unsis.data.user.infrastructure.controller;

import com.horarios.horarios_unsis.data.user.application.dto.LoginRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.LoginResponseDTO;
import com.horarios.horarios_unsis.data.user.application.dto.UserRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.UserResponseDTO;
import com.horarios.horarios_unsis.data.user.domain.port.in.UserUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Users", description = "Gestión de usuarios del sistema")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    
    @Autowired
    private UserUseCase userUseCase;

    @PostMapping
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - Solo administradores")
    })
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO request) {
        try {
            UserResponseDTO response = userUseCase.createUser(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario específico por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<UserResponseDTO> getUser(
            @PathVariable @Parameter(description = "ID del usuario") Integer id) {
        try {
            UserResponseDTO response = userUseCase.getUser(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene una lista de todos los usuarios del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> response = userUseCase.getAllUsers();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Integer id, 
            @RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO response = userUseCase.updateUser(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        try {
            userUseCase.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        try {
            UserResponseDTO response = userUseCase.getUserByUsername(username);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Autenticación de usuarios")
class AuthController {
    
    @Autowired
    private UserUseCase userUseCase;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y retorna un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            LoginResponseDTO response = userUseCase.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
}