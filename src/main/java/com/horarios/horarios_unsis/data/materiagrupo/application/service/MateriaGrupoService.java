package com.horarios.horarios_unsis.data.materiagrupo.application.service;

import com.horarios.horarios_unsis.data.materiagrupo.infrastructure.persistence.entity.MateriaGrupoEntity;
import com.horarios.horarios_unsis.data.materiagrupo.infrastructure.persistence.repository.MateriaGrupoRepository;
import com.horarios.horarios_unsis.data.grupo.infrastructure.persistence.entity.GrupoEntity;
import com.horarios.horarios_unsis.data.grupo.infrastructure.persistence.repository.GrupoRepository;
import com.horarios.horarios_unsis.integration.Consume.HorarioConsumeClient;
import com.horarios.horarios_unsis.integration.Consume.DTO.HorarioExternoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Servicio para gestionar las relaciones materia-grupo.
 * Sincroniza la información desde el endpoint de horarios de la API externa.
 */
@Service
public class MateriaGrupoService {

    private static final Logger logger = LoggerFactory.getLogger(MateriaGrupoService.class);

    private final MateriaGrupoRepository materiaGrupoRepository;
    private final GrupoRepository grupoRepository;
    private final HorarioConsumeClient horarioClient;

    public MateriaGrupoService(
            MateriaGrupoRepository materiaGrupoRepository,
            GrupoRepository grupoRepository,
            HorarioConsumeClient horarioClient) {
        this.materiaGrupoRepository = materiaGrupoRepository;
        this.grupoRepository = grupoRepository;
        this.horarioClient = horarioClient;
    }

    /**
     * Sincroniza todas las relaciones materia-grupo para un período,
     * consultando los horarios de todos los grupos del período.
     * 
     * @param clavePeriodo Clave del período a sincronizar
     * @return Número de relaciones sincronizadas
     */
    @Transactional
    public int sincronizarMateriasPorPeriodo(String clavePeriodo) {
        logger.info("Iniciando sincronización de materias-grupo para período: {}", clavePeriodo);

        // Obtener todos los grupos del período
        List<GrupoEntity> grupos = grupoRepository.findByClavePeriodo(clavePeriodo);
        logger.info("Se encontraron {} grupos en el período {}", grupos.size(), clavePeriodo);

        if (grupos.isEmpty()) {
            logger.warn("No hay grupos en el período {}. Verifique que los grupos estén sincronizados.", clavePeriodo);
            return 0;
        }

        // Usar un Map para agregar las horas por semana de cada materia-grupo
        // Clave: "claveMateria_claveGrupo_periodo"
        Map<String, MateriaGrupoEntity> materiasUnicas = new HashMap<>();

        // Para cada grupo, obtener sus horarios y extraer las materias
        for (GrupoEntity grupo : grupos) {
            try {
                List<HorarioExternoDTO> horarios = horarioClient.obtenerHorariosPorGrupo(clavePeriodo, grupo.getClave());
                logger.debug("Grupo {}: {} horarios obtenidos", grupo.getClave(), horarios.size());

                for (HorarioExternoDTO horario : horarios) {
                    // Crear clave única para agrupar
                    String claveUnica = String.format("%s_%s_%s",
                            horario.getCodigoAsignatura(),
                            horario.getIdGrupoStr() != null ? horario.getIdGrupoStr() : grupo.getClave(),
                            clavePeriodo);

                    if (materiasUnicas.containsKey(claveUnica)) {
                        // Incrementar horas si ya existe
                        MateriaGrupoEntity existing = materiasUnicas.get(claveUnica);
                        existing.setHorasSemana(existing.getHorasSemana() + 1);
                    } else {
                        // Crear nueva relación
                        MateriaGrupoEntity materiaGrupo = new MateriaGrupoEntity();
                        materiaGrupo.setClaveMateria(horario.getCodigoAsignatura());
                        materiaGrupo.setNombreMateria(horario.getNombreMateria());
                        materiaGrupo.setClaveGrupo(horario.getIdGrupoStr() != null ? horario.getIdGrupoStr() : grupo.getClave());
                        materiaGrupo.setClaveCarrera(horario.getCarrera());
                        materiaGrupo.setClavePeriodo(clavePeriodo);
                        materiaGrupo.setIdProfesor(horario.getIdProfesor());
                        materiaGrupo.setNombreProfesor(horario.getNombreProfesor());
                        materiaGrupo.setHorasSemana(1); // Primera hora
                        materiaGrupo.setActivo(true);
                        materiaGrupo.setFechaSincronizacion(LocalDateTime.now());

                        materiasUnicas.put(claveUnica, materiaGrupo);
                    }
                }
            } catch (Exception e) {
                logger.error("Error obteniendo horarios para grupo {}: {}", grupo.getClave(), e.getMessage());
            }
        }

        // Desactivar relaciones anteriores del período (conservar historial)
        materiaGrupoRepository.desactivarByPeriodo(clavePeriodo);
        logger.info("Relaciones materia-grupo anteriores del período {} desactivadas", clavePeriodo);

        // Guardar nuevas relaciones
        List<MateriaGrupoEntity> nuevasRelaciones = new ArrayList<>(materiasUnicas.values());
        if (!nuevasRelaciones.isEmpty()) {
            materiaGrupoRepository.saveAll(nuevasRelaciones);
            logger.info("Se guardaron {} relaciones materia-grupo para el período {}", 
                       nuevasRelaciones.size(), clavePeriodo);
        }

        return nuevasRelaciones.size();
    }

    /**
     * Sincroniza materias solo para una carrera específica
     * Realiza upsert: actualiza si existe, inserta si no existe
     */
    @Transactional
    public int sincronizarMateriasPorCarrera(String clavePeriodo, String claveCarrera) {
        logger.info("Sincronizando materias para carrera {} en período {}", claveCarrera, clavePeriodo);

        List<GrupoEntity> grupos = grupoRepository.findByClavePeriodoAndClaveCarrera(clavePeriodo, claveCarrera);
        logger.info("Se encontraron {} grupos de la carrera {}", grupos.size(), claveCarrera);

        Map<String, MateriaGrupoEntity> materiasUnicas = new HashMap<>();

        for (GrupoEntity grupo : grupos) {
            try {
                List<HorarioExternoDTO> horarios = horarioClient.obtenerHorariosPorGrupo(clavePeriodo, grupo.getClave());

                for (HorarioExternoDTO horario : horarios) {
                    String claveMateria = horario.getCodigoAsignatura();
                    String claveGrupo = horario.getIdGrupoStr() != null ? horario.getIdGrupoStr() : grupo.getClave();
                    String claveUnica = String.format("%s_%s_%s", claveMateria, claveGrupo, clavePeriodo);

                    if (materiasUnicas.containsKey(claveUnica)) {
                        MateriaGrupoEntity existing = materiasUnicas.get(claveUnica);
                        existing.setHorasSemana(existing.getHorasSemana() + 1);
                    } else {
                        // Buscar si ya existe en la BD (para hacer upsert)
                        MateriaGrupoEntity materiaGrupo = materiaGrupoRepository
                                .findByClaveMateriaAndClaveGrupoAndClavePeriodo(claveMateria, claveGrupo, clavePeriodo)
                                .orElse(new MateriaGrupoEntity());
                        
                        materiaGrupo.setClaveMateria(claveMateria);
                        materiaGrupo.setNombreMateria(horario.getNombreMateria());
                        materiaGrupo.setClaveGrupo(claveGrupo);
                        materiaGrupo.setClaveCarrera(horario.getCarrera());
                        materiaGrupo.setClavePeriodo(clavePeriodo);
                        materiaGrupo.setIdProfesor(horario.getIdProfesor());
                        materiaGrupo.setNombreProfesor(horario.getNombreProfesor());
                        // Si ya existía, mantenemos las horas previas + 1, sino comenzamos en 1
                        if (materiaGrupo.getIdMateriaGrupo() != null) {
                            materiaGrupo.setHorasSemana(1); // Reiniciamos contador para recálculo
                        } else {
                            materiaGrupo.setHorasSemana(1);
                        }
                        materiaGrupo.setActivo(true);
                        materiaGrupo.setFechaSincronizacion(LocalDateTime.now());

                        materiasUnicas.put(claveUnica, materiaGrupo);
                    }
                }
            } catch (Exception e) {
                logger.error("Error obteniendo horarios para grupo {}: {}", grupo.getClave(), e.getMessage());
            }
        }

        // Guardar relaciones (upsert: saveAll funciona para entidades nuevas y existentes)
        List<MateriaGrupoEntity> relaciones = new ArrayList<>(materiasUnicas.values());
        if (!relaciones.isEmpty()) {
            materiaGrupoRepository.saveAll(relaciones);
            logger.info("Se sincronizaron {} relaciones materia-grupo para carrera {} en período {}", 
                       relaciones.size(), claveCarrera, clavePeriodo);
        }

        return relaciones.size();
    }

    /**
     * Obtiene las materias de un grupo (con información del profesor)
     */
    public List<MateriaGrupoEntity> obtenerMateriasPorGrupo(String claveGrupo, String clavePeriodo) {
        return materiaGrupoRepository.findMateriasConProfesorByGrupo(claveGrupo, clavePeriodo);
    }

    /**
     * Obtiene las materias de una carrera
     */
    public List<MateriaGrupoEntity> obtenerMateriasPorCarrera(String claveCarrera, String clavePeriodo) {
        return materiaGrupoRepository.findByClaveCarreraAndClavePeriodoAndActivoTrue(claveCarrera, clavePeriodo);
    }

    /**
     * Obtiene todas las relaciones materia-grupo del período
     */
    public List<MateriaGrupoEntity> obtenerMateriasPorPeriodo(String clavePeriodo) {
        return materiaGrupoRepository.findByClavePeriodoAndActivoTrue(clavePeriodo);
    }

    /**
     * Obtiene los grupos que cursan una materia específica
     */
    public List<MateriaGrupoEntity> obtenerGruposPorMateria(String claveMateria, String clavePeriodo) {
        return materiaGrupoRepository.findByClaveMateriaAndClavePeriodoAndActivoTrue(claveMateria, clavePeriodo);
    }

    /**
     * Cuenta materias por período
     */
    public long contarMaterias(String clavePeriodo) {
        return materiaGrupoRepository.countByClavePeriodoAndActivoTrue(clavePeriodo);
    }

    /**
     * Obtiene lista de materias únicas del período
     */
    public List<Map<String, Object>> obtenerMateriasUnicasDelPeriodo(String clavePeriodo) {
        List<Object[]> results = materiaGrupoRepository.findDistinctMateriasByPeriodo(clavePeriodo);
        List<Map<String, Object>> materias = new ArrayList<>();
        
        for (Object[] row : results) {
            Map<String, Object> materia = new HashMap<>();
            materia.put("claveMateria", row[0]);
            materia.put("nombreMateria", row[1]);
            materias.add(materia);
        }
        
        return materias;
    }

    /**
     * Obtiene resumen de materias por grupo
     */
    public List<Map<String, Object>> obtenerResumenMateriasPorGrupo(String clavePeriodo) {
        List<Object[]> results = materiaGrupoRepository.countMateriasPorGrupo(clavePeriodo);
        List<Map<String, Object>> resumen = new ArrayList<>();
        
        for (Object[] row : results) {
            Map<String, Object> item = new HashMap<>();
            item.put("claveGrupo", row[0]);
            item.put("totalMaterias", row[1]);
            resumen.add(item);
        }
        
        return resumen;
    }
}
