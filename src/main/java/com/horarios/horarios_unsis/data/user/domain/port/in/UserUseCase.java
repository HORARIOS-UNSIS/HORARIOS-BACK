package com.horarios.horarios_unsis.data.user.domain.port.in;

import com.horarios.horarios_unsis.data.user.application.dto.LoginRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.LoginResponseDTO;
import com.horarios.horarios_unsis.data.user.application.dto.UserRequestDTO;
import com.horarios.horarios_unsis.data.user.application.dto.UserResponseDTO;

import java.util.List;

public interface UserUseCase {
    UserResponseDTO createUser(UserRequestDTO request);
    UserResponseDTO getUser(Integer id);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Integer id, UserRequestDTO request);
    void deleteUser(Integer id);
    LoginResponseDTO login(LoginRequestDTO request);
    UserResponseDTO getUserByUsername(String username);
}