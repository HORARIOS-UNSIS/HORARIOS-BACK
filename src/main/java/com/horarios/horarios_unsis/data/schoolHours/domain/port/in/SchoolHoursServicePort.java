package com.horarios.horarios_unsis.data.schoolHours.domain.port.in;

import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursRequestDTO;
import com.horarios.horarios_unsis.data.schoolHours.application.dto.SchoolHoursResponseDTO;
import java.util.List;

/**
 * Puerto de entrada (UseCase) para operaciones con horarios escolares
 */
public interface SchoolHoursServicePort {
    
    /**
     * Crea un nuevo horario escolar
     */
    SchoolHoursResponseDTO createSchoolHours(SchoolHoursRequestDTO request);
    
    /**
     * Obtiene un horario escolar por ID
     */
    SchoolHoursResponseDTO getSchoolHours(Long id);
    
    /**
     * Obtiene todos los horarios escolares
     */
    List<SchoolHoursResponseDTO> getAllSchoolHours();
    
    /**
     * Actualiza un horario escolar
     */
    SchoolHoursResponseDTO updateSchoolHours(Long id, SchoolHoursRequestDTO request);
    
    /**
     * Elimina un horario escolar
     */
    void deleteSchoolHours(Long id);
    
    /**
     * CONSUMO DE API: Importa horarios desde API externa
     */
    List<SchoolHoursResponseDTO> importarHorariosDelAPI();
}
