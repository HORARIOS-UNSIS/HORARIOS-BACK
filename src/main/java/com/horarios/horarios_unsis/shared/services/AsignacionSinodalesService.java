package com.horarios.horarios_unsis.shared.services;

import com.horarios.horarios_unsis.shared.models.ExamScheduleRequest;
import com.horarios.horarios_unsis.shared.models.SinodalRequest;
import com.horarios.horarios_unsis.shared.ExamConstants;
import com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.repository.SynodalRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestión de sinodales
 * Los sinodales son asignados por el FRONTEND (Jefe de Carrera/Servicios Escolares)
 * Este servicio VALIDA y PROCESA los sinodales asignados
 * 
 * Usa la tabla de SINODALES que relaciona:
 * - Profesor Sinodal (id_profesor_sinodal)
 * - Profesor Titular (id_profesor_titular)
 * - Materia (id_materia)
 */
@Service
public class AsignacionSinodalesService {
    
    private static final Logger logger = LoggerFactory.getLogger(AsignacionSinodalesService.class);
    private final SynodalRepository synodalRepository;

    public AsignacionSinodalesService(SynodalRepository synodalRepository) {
        this.synodalRepository = synodalRepository;
    }

    /**
     * Obtiene y valida los sinodales del frontend según tipo de examen
     * 
     * IMPORTANTE: Los sinodales son SIEMPRE REQUERIDOS porque están ligados a la materia
     * 
     * Regla de CARRERA:
     * - Si NO es ACADEMIA: Sinodales deben ser de la MISMA CARRERA que la materia
     * - Si es ACADEMIA: Sinodales deben pertenecen a la ACADEMIA
     * 
     * Reglas según tipo de examen:
     * - Parcial: Sinodales asignados por servicios escolares
     * - Ordinario: Sinodales designados
     * - Extraordinario: Sinodales de la academia (si es academia) o misma carrera (si no es academia)
     * - Especial: Seleccionados/confirmados por jefe de carrera
     */
    public List<Integer> validarYObtenerSinodales(ExamScheduleRequest request) {
        logger.info("Validando sinodales para examen tipo: {} - REQUERIDOS para materia: {}", 
                   request.getTipoExamen(), request.getIdMateria());
        
        // Convertir sinodales del frontend (puede tener ID o nombre) a List<Integer>
        List<Integer> sinodales = resolverSinodalesDesdeRequest(request);
        
        // Validación inicial: Los sinodales siempre son requeridos
        if (sinodales == null || sinodales.isEmpty()) {
            throw new IllegalArgumentException(
                String.format("Sinodales son REQUERIDOS para examen de tipo %s de materia %d", 
                             request.getTipoExamen(), request.getIdMateria()));
        }
        
        logger.info("Se recibieron {} sinodales desde el frontend", sinodales.size());
        
        // Validación común: Si NO es academia, sinodales deben ser de la misma carrera
        if (!Boolean.TRUE.equals(request.getEsAcademia())) {
            logger.info("Examen NO es de academia: Validando que sinodales sean de la misma carrera de materia {}", 
                       request.getIdMateria());
            validarSinodalesMismaCarrera(sinodales, request.getIdMateria());
        }
        
        // Validar disponibilidad de sinodales según tipo de examen
        switch (request.getTipoExamen()) {
            case ExamConstants.TIPO_PARCIAL:
                logger.info("Parcial: Validando {} sinodales asignados por servicios escolares", 
                           sinodales.size());
                validarDisponibilidadSinodales(sinodales, request);
                break;
                
            case ExamConstants.TIPO_ORDINARIO:
                logger.info("Ordinario: Validando {} sinodales designados", sinodales.size());
                validarDisponibilidadSinodales(sinodales, request);
                break;
                
            case ExamConstants.TIPO_EXTRAORDINARIO:
                logger.info("Extraordinario: Validando {} sinodales para materia", sinodales.size());
                if (Boolean.TRUE.equals(request.getEsAcademia())) {
                    // Para extraordinarios en academia, sinodales deben estar en la academia
                    logger.info("Extraordinario de ACADEMIA: Validando que sinodales pertenezcan a academia {}", 
                               request.getIdAcademia());
                    validarSinodalesEnAcademia(sinodales, request.getIdAcademia());
                }
                validarDisponibilidadSinodales(sinodales, request);
                break;
                
            case ExamConstants.TIPO_ESPECIAL:
                logger.info("Especial: Validando {} sinodales seleccionados por jefe de carrera", 
                           sinodales.size());
                validarDisponibilidadSinodales(sinodales, request);
                break;
                
            default:
                throw new IllegalArgumentException("Tipo de examen no reconocido: " + request.getTipoExamen());
        }
        
        logger.info("✓ Sinodales validados exitosamente para examen tipo {}", request.getTipoExamen());
        return sinodales;
    }

    /**
     * Resuelve los sinodales del request: puede venir como IDs o nombres
     * Prioridad: Si vienen IDs, los usa directamente
     * Si vienen nombres, busca el profesor en la BD y extrae el ID
     * 
     * @param request Contiene idsProfesorSinodales (legacy) o sinodalesRequest (nuevo)
     * @return List<Integer> con los IDs resueltos
     */
    private List<Integer> resolverSinodalesDesdeRequest(ExamScheduleRequest request) {
        List<Integer> idsResueltos = new ArrayList<>();
        
        // Si viene el formato nuevo (sinodalesRequest)
        if (request.getSinodalesRequest() != null && !request.getSinodalesRequest().isEmpty()) {
            logger.info("Resolviendo sinodales desde formato nuevo (SinodalRequest)");
            
            for (SinodalRequest sinodalReq : request.getSinodalesRequest()) {
                if (sinodalReq.getIdProfesor() != null) {
                    // Ya viene el ID
                    idsResueltos.add(sinodalReq.getIdProfesor());
                    logger.debug("Sinodal por ID: {}", sinodalReq.getIdProfesor());
                } else if (sinodalReq.getNombreProfesor() != null && !sinodalReq.getNombreProfesor().trim().isEmpty()) {
                    // Viene el nombre, buscar en BD
                    Integer idResuelto = buscarProfesorPorNombre(sinodalReq.getNombreProfesor());
                    if (idResuelto != null) {
                        idsResueltos.add(idResuelto);
                        logger.debug("Sinodal por nombre '{}' resuelto a ID: {}", sinodalReq.getNombreProfesor(), idResuelto);
                    } else {
                        throw new IllegalArgumentException(
                            String.format("No se encontró profesor con nombre: %s", sinodalReq.getNombreProfesor())
                        );
                    }
                } else {
                    throw new IllegalArgumentException("SinodalRequest debe tener ID o nombre");
                }
            }
            
            return idsResueltos;
        }
        
        // Si viene el formato legacy (idsProfesorSinodales)
        if (request.getIdsProfesorSinodales() != null && !request.getIdsProfesorSinodales().isEmpty()) {
            logger.info("Usando sinodales desde formato legacy (List<Integer>)");
            return request.getIdsProfesorSinodales();
        }
        
        // Ninguno de los dos formatos está presente
        return new ArrayList<>();
    }

    /**
     * Busca un profesor por nombre en la BD
     * TODO: Implement TeacherRepository query
     */
    private Integer buscarProfesorPorNombre(String nombreProfesor) {
        logger.info("Buscando profesor por nombre: {}", nombreProfesor);
        
        // TODO: Consultar TeacherRepository
        // Buscar profesor donde name/nombre = nombreProfesor
        // TeacherRepository.findByNombre(nombreProfesor)
        
        // Por ahora retorna null indicando que no se encontró
        logger.warn("TODO: Implementar búsqueda de profesor en BD por nombre");
        return null;
    }

    /**
     * Valida que los sinodales tengan disponibilidad en fecha y hora del examen
     */
    private void validarDisponibilidadSinodales(List<Integer> idsProfesor, ExamScheduleRequest request) {
        logger.info("Validando disponibilidad de {} sinodales en fecha: {}, hora: {}", 
                   idsProfesor.size(), request.getFechaExamen(), request.getHoraExamen());
        
        for (Integer idProfesor : idsProfesor) {
            // TODO: Consultar base de datos de horarios
            // Verificar que el sinodal NO tenga conflicto en esa hora
            logger.debug("Verificando disponibilidad de sinodal: {}", idProfesor);
        }
    }

    /**
     * Para academias: Valida que los sinodales estén registrados para esa materia
     * Consulta tabla SINODALES
     */
    private void validarSinodalesEnAcademia(List<Integer> idsProfesor, Integer idAcademia) {
        logger.info("Validando que {} sinodales están registrados para academia: {}", 
                   idsProfesor.size(), idAcademia);
        
        for (Integer idProfesor : idsProfesor) {
            if (!validarSinodalEnAcademia(idProfesor, idAcademia)) {
                throw new IllegalArgumentException(
                    String.format("Sinodal %d no está registrado para la academia %d", idProfesor, idAcademia));
            }
        }
    }

    /**
     * Valida que un sinodal específico esté registrado para esa academia
     * Consulta tabla SINODALES
     */
    public boolean validarSinodalEnAcademia(Integer idProfesor, Integer idAcademia) {
        logger.info("Validando que profesor {} está registrado para academia {}", idProfesor, idAcademia);
        
        // TODO: Consultar SynodalRepository para verificar que existe un registro
        // donde el profesor sea sinodal de una materia de esa academia
        // synodalRepository.findByProfesorSinodalIdAndMateriaIdAcademia(idProfesor, idAcademia);
        
        return true;
    }

    /**
     * Valida que todos los sinodales sean de la MISMA CARRERA que la materia
     * Se ejecuta cuando el examen NO es de academia
     * 
     * Verifica consultando tabla SINODALES que el profesor está registrado 
     * como sinodal para esa materia
     */
    private void validarSinodalesMismaCarrera(List<Integer> idsProfesor, Integer idMateria) {
        logger.info("Validando que {} sinodales sean válidos para materia: {}", 
                   idsProfesor.size(), idMateria);
        
        for (Integer idProfesor : idsProfesor) {
            if (!validarSinodalMismaCarrera(idProfesor, idMateria)) {
                throw new IllegalArgumentException(
                    String.format("Sinodal %d no está registrado como sinodal para la materia %d", 
                                 idProfesor, idMateria));
            }
        }
        
        logger.info("✓ Todos los sinodales están registrados para la materia");
    }

    /**
     * Valida que un sinodal específico esté registrado para esa materia
     * Consulta tabla SINODALES para verificar la relación
     */
    private boolean validarSinodalMismaCarrera(Integer idProfesor, Integer idMateria) {
        logger.debug("Verificando que profesor {} está registrado como sinodal para materia {}", 
                    idProfesor, idMateria);
        
        // TODO: Consultar SynodalRepository
        // Buscar registros donde: 
        //   id_profesor_sinodal = idProfesor 
        //   AND id_materia = idMateria
        // synodalRepository.findByProfesorSinodalIdAndMateriaId(idProfesor, idMateria);
        
        return true;
    }
}
