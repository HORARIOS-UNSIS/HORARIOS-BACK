package com.horarios.horarios_unsis.data.materiagrupo.infrastructure.persistence.repository;

import com.horarios.horarios_unsis.data.materiagrupo.infrastructure.persistence.entity.MateriaGrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MateriaGrupoRepository extends JpaRepository<MateriaGrupoEntity, Integer> {

    /**
     * Obtiene todas las materias de un grupo en un período (solo activas)
     */
    List<MateriaGrupoEntity> findByClaveGrupoAndClavePeriodoAndActivoTrue(String claveGrupo, String clavePeriodo);

    /**
     * Obtiene todas las materias de un grupo en un período (incluyendo inactivas)
     */
    List<MateriaGrupoEntity> findByClaveGrupoAndClavePeriodo(String claveGrupo, String clavePeriodo);

    /**
     * Obtiene todas las materias de una carrera en un período (solo activas)
     */
    List<MateriaGrupoEntity> findByClaveCarreraAndClavePeriodoAndActivoTrue(String claveCarrera, String clavePeriodo);

    /**
     * Obtiene todos los grupos que cursan una materia en un período
     */
    List<MateriaGrupoEntity> findByClaveMateriaAndClavePeriodoAndActivoTrue(String claveMateria, String clavePeriodo);

    /**
     * Obtiene la relación materia-grupo específica
     */
    Optional<MateriaGrupoEntity> findByClaveMateriaAndClaveGrupoAndClavePeriodoAndActivoTrue(
            String claveMateria, String claveGrupo, String clavePeriodo);

    /**
     * Busca por período (solo activas)
     */
    List<MateriaGrupoEntity> findByClavePeriodoAndActivoTrue(String clavePeriodo);

    /**
     * Busca por período (todas)
     */
    List<MateriaGrupoEntity> findByClavePeriodo(String clavePeriodo);

    /**
     * Verifica si existe la relación
     */
    boolean existsByClaveMateriaAndClaveGrupoAndClavePeriodo(String claveMateria, String claveGrupo, String clavePeriodo);

    /**
     * Busca una relación materia-grupo específica (sin filtro de activo)
     */
    Optional<MateriaGrupoEntity> findByClaveMateriaAndClaveGrupoAndClavePeriodo(
            String claveMateria, String claveGrupo, String clavePeriodo);

    /**
     * Desactiva todas las relaciones de un período (para resincronización)
     */
    @Modifying
    @Query("UPDATE MateriaGrupoEntity m SET m.activo = false WHERE m.clavePeriodo = :clavePeriodo")
    void desactivarByPeriodo(@Param("clavePeriodo") String clavePeriodo);

    /**
     * Cuenta materias activas por período
     */
    long countByClavePeriodoAndActivoTrue(String clavePeriodo);

    /**
     * Obtiene lista de materias únicas de un período
     */
    @Query("SELECT DISTINCT m.claveMateria, m.nombreMateria FROM MateriaGrupoEntity m " +
           "WHERE m.clavePeriodo = :clavePeriodo AND m.activo = true ORDER BY m.nombreMateria")
    List<Object[]> findDistinctMateriasByPeriodo(@Param("clavePeriodo") String clavePeriodo);

    /**
     * Obtiene cantidad de materias por grupo
     */
    @Query("SELECT m.claveGrupo, COUNT(DISTINCT m.claveMateria) FROM MateriaGrupoEntity m " +
           "WHERE m.clavePeriodo = :clavePeriodo AND m.activo = true " +
           "GROUP BY m.claveGrupo ORDER BY m.claveGrupo")
    List<Object[]> countMateriasPorGrupo(@Param("clavePeriodo") String clavePeriodo);

    /**
     * Busca materias de un grupo con información del profesor
     */
    @Query("SELECT m FROM MateriaGrupoEntity m " +
           "WHERE m.claveGrupo = :claveGrupo AND m.clavePeriodo = :clavePeriodo AND m.activo = true " +
           "ORDER BY m.nombreMateria")
    List<MateriaGrupoEntity> findMateriasConProfesorByGrupo(
            @Param("claveGrupo") String claveGrupo, 
            @Param("clavePeriodo") String clavePeriodo);
}
