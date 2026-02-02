package com.horarios.horarios_unsis.data.asignacion.application.service;

import com.horarios.horarios_unsis.data.asignacion.infrastructure.persistence.entity.AsignacionProfesorMateriaEntity;
import com.horarios.horarios_unsis.data.asignacion.infrastructure.persistence.repository.AsignacionProfesorMateriaRepository;
import com.horarios.horarios_unsis.data.group.infrastructure.persistence.entity.GroupEntity;
import com.horarios.horarios_unsis.data.group.infrastructure.persistence.repository.GroupRepository;
import com.horarios.horarios_unsis.integration.Consume.HorarioConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Servicio para gestionar las asignaciones de profesores a materias.
 * Sincroniza la información desde el endpoint de horarios de la API externa.
 */
@Service
public class AsignacionProfesorMateriaService {

    private static final Logger logger = LoggerFactory.getLogger(AsignacionProfesorMateriaService.class);

    private final AsignacionProfesorMateriaRepository asignacionRepository;
    private final GroupRepository grupoRepository;
    private final HorarioConsumeClient horarioClient;

    public AsignacionProfesorMateriaService(
            AsignacionProfesorMateriaRepository asignacionRepository,
            GroupRepository grupoRepository,
            HorarioConsumeClient horarioClient) {
        this.asignacionRepository = asignacionRepository;
        this.grupoRepository = grupoRepository;
        this.horarioClient = horarioClient;
    }

    /**
     * Sincroniza todas las asignaciones de profesor-materia para un período,
     * consultando los horarios de todos los grupos del período.
     * 
     * @param clavePeriodo Clave del período a sincronizar
     * @return Número de asignaciones sincronizadas
     */
    @Transactional
    public int sincronizarAsignacionesPorPeriodo(String clavePeriodo) {
        logger.info("Iniciando sincronización de asignaciones para período: {}", clavePeriodo);

        // Obtener todos los grupos del período
        List<GroupEntity> grupos = grupoRepository.findByClavePeriodo(clavePeriodo);
        logger.info("Se encontraron {} grupos en el período {}", grupos.size(), clavePeriodo);

        if (grupos.isEmpty()) {
            logger.warn("No hay grupos en el período {}. Verifique que los grupos estén sincronizados.", clavePeriodo);
            return 0;
        }

        // Usar un Set para evitar duplicados (clave: idProfesor_claveMateria_claveGrupo)
        Set<String> asignacionesUnicas = new HashSet<>();
        List<AsignacionProfesorMateriaEntity> nuevasAsignaciones = new ArrayList<>();

        // Para cada grupo, obtener sus horarios y extraer las asignaciones
        for (GroupEntity grupo : grupos) {
            try {
                List<HorarioExternoDTO> horarios = horarioClient.obtenerHorariosPorGrupo(clavePeriodo, grupo.getClave());
                logger.debug("Grupo {}: {} horarios obtenidos", grupo.getClave(), horarios.size());

                for (HorarioExternoDTO horario : horarios) {
                    // Crear clave única para evitar duplicados
                    String claveUnica = String.format("%s_%s_%s_%s",
                            horario.getIdProfesor(),
                            horario.getCodigoAsignatura(),
                            horario.getIdGrupoStr(),
                            clavePeriodo);

                    if (!asignacionesUnicas.contains(claveUnica) && horario.getIdProfesor() != null) {
                        asignacionesUnicas.add(claveUnica);

                        AsignacionProfesorMateriaEntity asignacion = new AsignacionProfesorMateriaEntity();
                        asignacion.setIdProfesor(horario.getIdProfesor());
                        asignacion.setNombreProfesor(horario.getNombreProfesor());
                        asignacion.setClaveMateria(horario.getCodigoAsignatura());
                        asignacion.setNombreMateria(horario.getNombreMateria());
                        asignacion.setClaveGrupo(horario.getIdGrupoStr() != null ? horario.getIdGrupoStr() : grupo.getClave());
                        asignacion.setClaveCarrera(horario.getCarrera());
                        asignacion.setClavePeriodo(clavePeriodo);
                        asignacion.setDia(horario.getDia());
                        asignacion.setHora(horario.getHora());
                        // Guardar nombreAula para hacer match con aulas locales por nombre
                        asignacion.setClaveAula(horario.getNombreAula() != null ? 
                            horario.getNombreAula() : horario.getIdAulaStr());
                        asignacion.setActivo(true);
                        asignacion.setFechaSincronizacion(java.time.LocalDateTime.now());

                        nuevasAsignaciones.add(asignacion);
                    }
                }
            } catch (Exception e) {
                logger.error("Error obteniendo horarios para grupo {}: {}", grupo.getClave(), e.getMessage());
            }
        }

        // Desactivar asignaciones anteriores del período (conservar historial)
        asignacionRepository.desactivarByPeriodo(clavePeriodo);
        logger.info("Asignaciones anteriores del período {} desactivadas", clavePeriodo);

        // Guardar nuevas asignaciones usando UPSERT para evitar violaciones de clave única
        if (!nuevasAsignaciones.isEmpty()) {
            int guardadas = 0;
            for (AsignacionProfesorMateriaEntity asignacion : nuevasAsignaciones) {
                try {
                    asignacionRepository.upsertAsignacion(
                        asignacion.getIdProfesor(),
                        asignacion.getClaveMateria(),
                        asignacion.getClaveGrupo(),
                        asignacion.getClavePeriodo(),
                        asignacion.getNombreProfesor(),
                        asignacion.getNombreMateria(),
                        asignacion.getClaveCarrera(),
                        asignacion.getDia(),
                        asignacion.getHora(),
                        asignacion.getClaveAula(),
                        asignacion.getActivo(),
                        asignacion.getFechaSincronizacion()
                    );
                    guardadas++;
                } catch (Exception e) {
                    logger.error("Error guardando asignación {}-{}-{}: {}", 
                        asignacion.getIdProfesor(), asignacion.getClaveMateria(), 
                        asignacion.getClaveGrupo(), e.getMessage());
                }
            }
            logger.info("Se guardaron {} asignaciones profesor-materia para el período {}", 
                       guardadas, clavePeriodo);
        }

        return nuevasAsignaciones.size();
    }

    /**
     * Sincroniza asignaciones solo para una carrera específica
     * Realiza upsert: actualiza si existe, inserta si no existe
     */
    @Transactional
    public int sincronizarAsignacionesPorCarrera(String clavePeriodo, String claveCarrera) {
        logger.info("Sincronizando asignaciones para carrera {} en período {}", claveCarrera, clavePeriodo);

        List<GroupEntity> grupos = grupoRepository.findByClaveCarreraAndClavePeriodo(claveCarrera, clavePeriodo);
        logger.info("Se encontraron {} grupos de la carrera {}", grupos.size(), claveCarrera);

        Set<String> asignacionesUnicas = new HashSet<>();
        List<AsignacionProfesorMateriaEntity> asignaciones = new ArrayList<>();

        for (GroupEntity grupo : grupos) {
            try {
                List<HorarioExternoDTO> horarios = horarioClient.obtenerHorariosPorGrupo(clavePeriodo, grupo.getClave());

                for (HorarioExternoDTO horario : horarios) {
                    Integer idProfesor = horario.getIdProfesor();
                    String claveMateria = horario.getCodigoAsignatura();
                    String claveGrupo = horario.getIdGrupoStr() != null ? horario.getIdGrupoStr() : grupo.getClave();
                    String claveUnica = String.format("%s_%s_%s_%s", idProfesor, claveMateria, claveGrupo, clavePeriodo);

                    if (!asignacionesUnicas.contains(claveUnica) && idProfesor != null) {
                        asignacionesUnicas.add(claveUnica);

                        // Buscar si ya existe en la BD (para hacer upsert)
                        AsignacionProfesorMateriaEntity asignacion = asignacionRepository
                                .findByIdProfesorAndClaveMateriaAndClaveGrupoAndClavePeriodo(
                                    idProfesor, claveMateria, claveGrupo, clavePeriodo)
                                .orElse(new AsignacionProfesorMateriaEntity());

                        asignacion.setIdProfesor(idProfesor);
                        asignacion.setNombreProfesor(horario.getNombreProfesor());
                        asignacion.setClaveMateria(claveMateria);
                        asignacion.setNombreMateria(horario.getNombreMateria());
                        asignacion.setClaveGrupo(claveGrupo);
                        asignacion.setClaveCarrera(horario.getCarrera());
                        asignacion.setClavePeriodo(clavePeriodo);
                        asignacion.setDia(horario.getDia());
                        asignacion.setHora(horario.getHora());
                        // Guardar nombreAula para hacer match con aulas locales por nombre
                        asignacion.setClaveAula(horario.getNombreAula() != null ? 
                            horario.getNombreAula() : horario.getIdAulaStr());
                        asignacion.setActivo(true);
                        asignacion.setFechaSincronizacion(java.time.LocalDateTime.now());

                        asignaciones.add(asignacion);
                    }
                }
            } catch (Exception e) {
                logger.error("Error obteniendo horarios para grupo {}: {}", grupo.getClave(), e.getMessage());
            }
        }

        if (!asignaciones.isEmpty()) {
            int guardadas = 0;
            for (AsignacionProfesorMateriaEntity asignacion : asignaciones) {
                try {
                    asignacionRepository.upsertAsignacion(
                        asignacion.getIdProfesor(),
                        asignacion.getClaveMateria(),
                        asignacion.getClaveGrupo(),
                        asignacion.getClavePeriodo(),
                        asignacion.getNombreProfesor(),
                        asignacion.getNombreMateria(),
                        asignacion.getClaveCarrera(),
                        asignacion.getDia(),
                        asignacion.getHora(),
                        asignacion.getClaveAula(),
                        asignacion.getActivo(),
                        asignacion.getFechaSincronizacion()
                    );
                    guardadas++;
                } catch (Exception e) {
                    logger.error("Error guardando asignación {}-{}-{}: {}", 
                        asignacion.getIdProfesor(), asignacion.getClaveMateria(), 
                        asignacion.getClaveGrupo(), e.getMessage());
                }
            }
            logger.info("Se sincronizaron {} asignaciones para carrera {} en período {}", 
                       guardadas, claveCarrera, clavePeriodo);
        }

        return asignaciones.size();
    }

    /**
     * Obtiene las asignaciones de un período (solo activas por defecto)
     */
    public List<AsignacionProfesorMateriaEntity> obtenerAsignacionesPorPeriodo(String clavePeriodo) {
        return asignacionRepository.findByClavePeriodoAndActivoTrue(clavePeriodo);
    }

    /**
     * Obtiene las asignaciones de un período (incluyendo inactivas para historial)
     */
    public List<AsignacionProfesorMateriaEntity> obtenerTodasAsignacionesPorPeriodo(String clavePeriodo) {
        return asignacionRepository.findByClavePeriodo(clavePeriodo);
    }

    /**
     * Obtiene las asignaciones de un grupo en un período
     */
    public List<AsignacionProfesorMateriaEntity> obtenerAsignacionesPorGrupo(String claveGrupo, String clavePeriodo) {
        return asignacionRepository.findByClaveGrupoAndClavePeriodo(claveGrupo, clavePeriodo);
    }

    /**
     * Obtiene las asignaciones de una carrera en un período
     */
    public List<AsignacionProfesorMateriaEntity> obtenerAsignacionesPorCarrera(String claveCarrera, String clavePeriodo) {
        return asignacionRepository.findByClaveCarreraAndClavePeriodo(claveCarrera, clavePeriodo);
    }

    /**
     * Obtiene el profesor asignado a una materia en un grupo específico
     */
    public Optional<AsignacionProfesorMateriaEntity> obtenerProfesorDeMateria(
            String claveMateria, String claveGrupo, String clavePeriodo) {
        return asignacionRepository.findProfesorByMateriaAndGrupo(claveMateria, claveGrupo, clavePeriodo);
    }

    /**
     * Obtiene las materias que imparte un profesor
     */
    public List<String> obtenerMateriasPorProfesor(Integer idProfesor, String clavePeriodo) {
        return asignacionRepository.findMateriasByProfesor(idProfesor, clavePeriodo);
    }

    /**
     * Obtiene los grupos donde un profesor imparte clases
     */
    public List<String> obtenerGruposPorProfesor(Integer idProfesor, String clavePeriodo) {
        return asignacionRepository.findGruposByProfesor(idProfesor, clavePeriodo);
    }

    /**
     * Cuenta las asignaciones de un período
     */
    public long contarAsignaciones(String clavePeriodo) {
        return asignacionRepository.countByClavePeriodo(clavePeriodo);
    }

    /**
     * Obtiene lista de profesores únicos del período
     */
    public List<Map<String, Object>> obtenerProfesoresDelPeriodo(String clavePeriodo) {
        List<Object[]> results = asignacionRepository.findDistinctProfesoresByPeriodo(clavePeriodo);
        List<Map<String, Object>> profesores = new ArrayList<>();
        
        for (Object[] row : results) {
            Map<String, Object> profesor = new HashMap<>();
            profesor.put("idProfesor", row[0]);
            profesor.put("nombreProfesor", row[1]);
            profesores.add(profesor);
        }
        
        return profesores;
    }

    /**
     * Obtiene lista de materias únicas del período
     */
    public List<Map<String, Object>> obtenerMateriasDelPeriodo(String clavePeriodo) {
        List<Object[]> results = asignacionRepository.findDistinctMateriasByPeriodo(clavePeriodo);
        List<Map<String, Object>> materias = new ArrayList<>();
        
        for (Object[] row : results) {
            Map<String, Object> materia = new HashMap<>();
            materia.put("claveMateria", row[0]);
            materia.put("nombreMateria", row[1]);
            materias.add(materia);
        }
        
        return materias;
    }
}
