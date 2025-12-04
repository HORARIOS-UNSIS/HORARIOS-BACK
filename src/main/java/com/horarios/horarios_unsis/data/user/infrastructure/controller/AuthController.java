package com.horarios.horarios_unsis.data.user.infrastructure.controller;

import com.horarios.horarios_unsis.data.user.application.dto.LoginRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.LoginResponseDTO;
import com.horarios.horarios_unsis.data.user.infrastructure.persistence.repository.UserRepository;
import com.horarios.horarios_unsis.config.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Endpoints para autenticación de usuarios")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Login de usuario", description = "Autentica un usuario y retorna un JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Obtener información completa del usuario
            var userEntity = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            // Generar token JWT
            String token = jwtTokenUtil.generateToken(userDetails);
            
            // Extraer el rol del usuario
            String role = userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
            
            LoginResponseDTO response = new LoginResponseDTO(
                    token,
                    userDetails.getUsername(),
                    role,
                    userEntity.getIdUsuario()
            );
            response.setType("Bearer");

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
}