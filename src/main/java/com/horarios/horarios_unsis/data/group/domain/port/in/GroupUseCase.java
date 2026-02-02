package com.horarios.horarios_unsis.data.group.domain.port.in;

import com.horarios.horarios_unsis.data.group.application.dto.GroupResponseDTO;
import java.util.List;

public interface GroupUseCase {
    List<GroupResponseDTO> getAllGroups();
    GroupResponseDTO getGroupById(Integer id);
    List<GroupResponseDTO> getGroupsByCareer(String careerCode);
    List<GroupResponseDTO> getGroupsByPeriod(String periodCode);
    List<GroupResponseDTO> getGroupsByCareerAndPeriod(String careerCode, String periodCode);
}
