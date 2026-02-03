package com.horarios.horarios_unsis.data.asignacion.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.asignacion.infrastructure.persistence.entity.AsignacionProfesorMateriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionProfesorMateriaRepository extends JpaRepository<AsignacionProfesorMateriaEntity, Integer> {

    /**
     * UPSERT: Inserta o actualiza una asignación profesor-materia
     * Si ya existe (por clave única), actualiza los campos
     */
    @Modifying
    @Query(value = "INSERT INTO asignacion_profesor_materia " +
                   "(id_profesor, clave_materia, clave_grupo, clave_periodo, " +
                   "nombre_profesor, nombre_materia, clave_carrera, dia, hora, clave_aula, " +
                   "activo, fecha_sincronizacion) " +
                   "VALUES (:idProfesor, :claveMateria, :claveGrupo, :clavePeriodo, " +
                   ":nombreProfesor, :nombreMateria, :claveCarrera, :dia, :hora, :claveAula, " +
                   ":activo, :fechaSincronizacion) " +
                   "ON CONFLICT (id_profesor, clave_materia, clave_grupo, clave_periodo) " +
                   "DO UPDATE SET " +
                   "nombre_profesor = :nombreProfesor, " +
                   "nombre_materia = :nombreMateria, " +
                   "clave_carrera = :claveCarrera, " +
                   "dia = :dia, " +
                   "hora = :hora, " +
                   "clave_aula = :claveAula, " +
                   "activo = :activo, " +
                   "fecha_sincronizacion = :fechaSincronizacion", 
           nativeQuery = true)
    void upsertAsignacion(
            @Param("idProfesor") Integer idProfesor,
            @Param("claveMateria") String claveMateria,
            @Param("claveGrupo") String claveGrupo,
            @Param("clavePeriodo") String clavePeriodo,
            @Param("nombreProfesor") String nombreProfesor,
            @Param("nombreMateria") String nombreMateria,
            @Param("claveCarrera") String claveCarrera,
            @Param("dia") Integer dia,
            @Param("hora") Integer hora,
            @Param("claveAula") String claveAula,
            @Param("activo") Boolean activo,
            @Param("fechaSincronizacion") LocalDateTime fechaSincronizacion);

    /**
     * Busca asignaciones por período
     */
    List<AsignacionProfesorMateriaEntity> findByClavePeriodo(String clavePeriodo);

    /**
     * Busca asignaciones activas por período
     */
    List<AsignacionProfesorMateriaEntity> findByClavePeriodoAndActivoTrue(String clavePeriodo);

    /**
     * Busca asignaciones por grupo y período
     */
    List<AsignacionProfesorMateriaEntity> findByClaveGrupoAndClavePeriodo(String claveGrupo, String clavePeriodo);

    /**
     * Busca asignaciones por carrera y período
     */
    List<AsignacionProfesorMateriaEntity> findByClaveCarreraAndClavePeriodo(String claveCarrera, String clavePeriodo);

    /**
     * Busca asignaciones por profesor y período
     */
    List<AsignacionProfesorMateriaEntity> findByIdProfesorAndClavePeriodo(Integer idProfesor, String clavePeriodo);

    /**
     * Busca el profesor asignado a una materia específica en un grupo
     */
    @Query("SELECT DISTINCT a FROM AsignacionProfesorMateriaEntity a " +
           "WHERE a.claveMateria = :claveMateria " +
           "AND a.claveGrupo = :claveGrupo " +
           "AND a.clavePeriodo = :clavePeriodo")
    Optional<AsignacionProfesorMateriaEntity> findProfesorByMateriaAndGrupo(
            @Param("claveMateria") String claveMateria,
            @Param("claveGrupo") String claveGrupo,
            @Param("clavePeriodo") String clavePeriodo);

    /**
     * Busca todas las materias que imparte un profesor en un período
     */
    @Query("SELECT DISTINCT a.claveMateria FROM AsignacionProfesorMateriaEntity a " +
           "WHERE a.idProfesor = :idProfesor AND a.clavePeriodo = :clavePeriodo")
    List<String> findMateriasByProfesor(
            @Param("idProfesor") Integer idProfesor,
            @Param("clavePeriodo") String clavePeriodo);

    /**
     * Busca todos los grupos donde un profesor imparte clases
     */
    @Query("SELECT DISTINCT a.claveGrupo FROM AsignacionProfesorMateriaEntity a " +
           "WHERE a.idProfesor = :idProfesor AND a.clavePeriodo = :clavePeriodo")
    List<String> findGruposByProfesor(
            @Param("idProfesor") Integer idProfesor,
            @Param("clavePeriodo") String clavePeriodo);

    /**
     * Verifica si existe una asignación
     */
    boolean existsByIdProfesorAndClaveMateriaAndClaveGrupoAndClavePeriodo(
            Integer idProfesor, String claveMateria, String claveGrupo, String clavePeriodo);

    /**
     * Busca una asignación específica (sin filtro de activo)
     */
    Optional<AsignacionProfesorMateriaEntity> findByIdProfesorAndClaveMateriaAndClaveGrupoAndClavePeriodo(
            Integer idProfesor, String claveMateria, String claveGrupo, String clavePeriodo);

    /**
     * Marca todas las asignaciones de un período como inactivas (para resincronización)
     */
    @Modifying
    @Query("UPDATE AsignacionProfesorMateriaEntity a SET a.activo = false WHERE a.clavePeriodo = :clavePeriodo")
    void desactivarByPeriodo(@Param("clavePeriodo") String clavePeriodo);

    /**
     * Elimina todas las asignaciones de un período (para resincronización completa)
     * DEPRECATED: Usar desactivarByPeriodo() para conservar historial
     */
    @Modifying
    @Query("DELETE FROM AsignacionProfesorMateriaEntity a WHERE a.clavePeriodo = :clavePeriodo")
    void deleteByPeriodo(@Param("clavePeriodo") String clavePeriodo);

    /**
     * Cuenta asignaciones por período
     */
    long countByClavePeriodo(String clavePeriodo);

    /**
     * Obtiene una lista de profesores únicos por período
     */
    @Query("SELECT DISTINCT a.idProfesor, a.nombreProfesor FROM AsignacionProfesorMateriaEntity a " +
           "WHERE a.clavePeriodo = :clavePeriodo ORDER BY a.nombreProfesor")
    List<Object[]> findDistinctProfesoresByPeriodo(@Param("clavePeriodo") String clavePeriodo);

    /**
     * Obtiene una lista de materias únicas por período
     */
    @Query("SELECT DISTINCT a.claveMateria, a.nombreMateria FROM AsignacionProfesorMateriaEntity a " +
           "WHERE a.clavePeriodo = :clavePeriodo ORDER BY a.nombreMateria")
    List<Object[]> findDistinctMateriasByPeriodo(@Param("clavePeriodo") String clavePeriodo);
}
