package com.horarios.horarios_unsis.data.user.domain.service;

import com.horarios.horarios_unsis.config.JwtTokenUtil;
import com.horarios.horarios_unsis.data.user.application.dto.LoginRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.LoginResponseDTO;
import com.horarios.horarios_unsis.data.user.application.dto.UserRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.UserResponseDTO;
import com.horarios.horarios_unsis.data.user.application.mapper.UserMapper;
import com.horarios.horarios_unsis.data.user.domain.port.in.UserUseCase;
import com.horarios.horarios_unsis.data.user.infrastructure.persistence.entity.UserEntity;
import com.horarios.horarios_unsis.data.user.infrastructure.persistence.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserUseCase {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        // Verificar si el usuario ya existe
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya existe");
        }
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya existe");
        }
        
        // Crear nueva entidad de usuario
        UserEntity userEntity = new UserEntity();
        userEntity.setNombre(request.getNombre());
        userEntity.setEmail(request.getEmail());
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRol(request.getRol());
        userEntity.setActivo(true);
        
        UserEntity savedUser = userRepository.save(userEntity);
        return UserMapper.entityToDTO(savedUser);
    }

    @Override
    public UserResponseDTO getUser(Integer id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UserMapper.entityToDTO(userEntity);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(Integer id, UserRequestDTO request) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        userEntity.setNombre(request.getNombre());
        userEntity.setEmail(request.getEmail());
        
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        if (request.getRol() != null) {
            userEntity.setRol(request.getRol());
        }
        
        UserEntity savedUser = userRepository.save(userEntity);
        return UserMapper.entityToDTO(savedUser);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );

        // Cargar detalles del usuario
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        
        // Obtener información del usuario desde la BD
        UserEntity userEntity = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Generar token JWT
        final String token = jwtTokenUtil.generateToken(userDetails);

        return new LoginResponseDTO(
                token,
                userDetails.getUsername(),
                userEntity.getRol().name(),
                userEntity.getIdUsuario()
        );
    }

    @Override
    public UserResponseDTO getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return UserMapper.entityToDTO(userEntity);
    }
}