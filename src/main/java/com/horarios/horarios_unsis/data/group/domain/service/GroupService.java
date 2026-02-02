package com.horarios.horarios_unsis.data.group.domain.service;

import com.horarios.horarios_unsis.data.group.application.dto.GroupResponseDTO;
import com.horarios.horarios_unsis.data.group.application.mapper.GroupMapper;
import com.horarios.horarios_unsis.data.group.domain.model.Group;
import com.horarios.horarios_unsis.data.group.domain.port.in.GroupUseCase;
import com.horarios.horarios_unsis.data.group.domain.port.out.GroupRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService implements GroupUseCase {

    private final GroupRepositoryPort groupRepositoryPort;

    public GroupService(GroupRepositoryPort groupRepositoryPort) {
        this.groupRepositoryPort = groupRepositoryPort;
    }

    @Override
    public List<GroupResponseDTO> getAllGroups() {
        return groupRepositoryPort.findAll().stream()
                .map(GroupMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GroupResponseDTO getGroupById(Integer id) {
        Group group = groupRepositoryPort.findById(id)
                .orElseThrow(() -> new RuntimeException("Grupo no encontrado con id: " + id));
        return GroupMapper.toDTO(group);
    }

    @Override
    public List<GroupResponseDTO> getGroupsByCareer(String careerCode) {
        return groupRepositoryPort.findByCareer(careerCode).stream()
                .map(GroupMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponseDTO> getGroupsByPeriod(String periodCode) {
        return groupRepositoryPort.findByPeriod(periodCode).stream()
                .map(GroupMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponseDTO> getGroupsByCareerAndPeriod(String careerCode, String periodCode) {
        return groupRepositoryPort.findByCareerAndPeriod(careerCode, periodCode).stream()
                .map(GroupMapper::toDTO)
                .collect(Collectors.toList());
    }
}
