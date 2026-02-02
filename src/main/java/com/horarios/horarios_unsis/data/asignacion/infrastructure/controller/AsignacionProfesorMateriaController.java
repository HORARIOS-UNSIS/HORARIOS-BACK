package com.horarios.horarios_unsis.data.asignacion.infrastructure.controller;

import com.horarios.horarios_unsis.data.asignacion.application.service.AsignacionProfesorMateriaService;
import com.horarios.horarios_unsis.data.asignacion.infrastructure.persistence.entity.AsignacionProfesorMateriaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las asignaciones de profesores a materias.
 */
@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "*")
public class AsignacionProfesorMateriaController {

    private static final Logger logger = LoggerFactory.getLogger(AsignacionProfesorMateriaController.class);

    private final AsignacionProfesorMateriaService asignacionService;

    public AsignacionProfesorMateriaController(AsignacionProfesorMateriaService asignacionService) {
        this.asignacionService = asignacionService;
    }

    /**
     * Sincroniza las asignaciones profesor-materia desde el endpoint de horarios
     * POST /api/asignaciones/sincronizar/{clavePeriodo}
     */
    @PostMapping("/sincronizar/{clavePeriodo}")
    public ResponseEntity<Map<String, Object>> sincronizarAsignaciones(
            @PathVariable String clavePeriodo,
            @RequestParam(required = false) String claveCarrera) {
        
        logger.info("Solicitud de sincronización de asignaciones para período: {}, carrera: {}", 
                    clavePeriodo, claveCarrera);

        try {
            int cantidad;
            if (claveCarrera != null && !claveCarrera.isEmpty()) {
                cantidad = asignacionService.sincronizarAsignacionesPorCarrera(clavePeriodo, claveCarrera);
            } else {
                cantidad = asignacionService.sincronizarAsignacionesPorPeriodo(clavePeriodo);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sincronización completada");
            response.put("asignacionesSincronizadas", cantidad);
            response.put("periodo", clavePeriodo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error sincronizando asignaciones: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en sincronización: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Obtiene todas las asignaciones de un período
     * GET /api/asignaciones/periodo/{clavePeriodo}
     */
    @GetMapping("/periodo/{clavePeriodo}")
    public ResponseEntity<List<AsignacionProfesorMateriaEntity>> obtenerAsignacionesPorPeriodo(
            @PathVariable String clavePeriodo) {
        
        List<AsignacionProfesorMateriaEntity> asignaciones = 
            asignacionService.obtenerAsignacionesPorPeriodo(clavePeriodo);
        
        return ResponseEntity.ok(asignaciones);
    }

    /**
     * Obtiene las asignaciones de un grupo específico
     * GET /api/asignaciones/grupo/{claveGrupo}/periodo/{clavePeriodo}
     */
    @GetMapping("/grupo/{claveGrupo}/periodo/{clavePeriodo}")
    public ResponseEntity<List<AsignacionProfesorMateriaEntity>> obtenerAsignacionesPorGrupo(
            @PathVariable String claveGrupo,
            @PathVariable String clavePeriodo) {
        
        List<AsignacionProfesorMateriaEntity> asignaciones = 
            asignacionService.obtenerAsignacionesPorGrupo(claveGrupo, clavePeriodo);
        
        return ResponseEntity.ok(asignaciones);
    }

    /**
     * Obtiene las asignaciones de una carrera
     * GET /api/asignaciones/carrera/{claveCarrera}/periodo/{clavePeriodo}
     */
    @GetMapping("/carrera/{claveCarrera}/periodo/{clavePeriodo}")
    public ResponseEntity<List<AsignacionProfesorMateriaEntity>> obtenerAsignacionesPorCarrera(
            @PathVariable String claveCarrera,
            @PathVariable String clavePeriodo) {
        
        List<AsignacionProfesorMateriaEntity> asignaciones = 
            asignacionService.obtenerAsignacionesPorCarrera(claveCarrera, clavePeriodo);
        
        return ResponseEntity.ok(asignaciones);
    }

    /**
     * Obtiene el profesor asignado a una materia en un grupo
     * GET /api/asignaciones/materia/{claveMateria}/grupo/{claveGrupo}/periodo/{clavePeriodo}
     */
    @GetMapping("/materia/{claveMateria}/grupo/{claveGrupo}/periodo/{clavePeriodo}")
    public ResponseEntity<AsignacionProfesorMateriaEntity> obtenerProfesorDeMateria(
            @PathVariable String claveMateria,
            @PathVariable String claveGrupo,
            @PathVariable String clavePeriodo) {
        
        return asignacionService.obtenerProfesorDeMateria(claveMateria, claveGrupo, clavePeriodo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene las materias que imparte un profesor
     * GET /api/asignaciones/profesor/{idProfesor}/materias/periodo/{clavePeriodo}
     */
    @GetMapping("/profesor/{idProfesor}/materias/periodo/{clavePeriodo}")
    public ResponseEntity<List<String>> obtenerMateriasPorProfesor(
            @PathVariable Integer idProfesor,
            @PathVariable String clavePeriodo) {
        
        List<String> materias = asignacionService.obtenerMateriasPorProfesor(idProfesor, clavePeriodo);
        return ResponseEntity.ok(materias);
    }

    /**
     * Obtiene los grupos donde imparte un profesor
     * GET /api/asignaciones/profesor/{idProfesor}/grupos/periodo/{clavePeriodo}
     */
    @GetMapping("/profesor/{idProfesor}/grupos/periodo/{clavePeriodo}")
    public ResponseEntity<List<String>> obtenerGruposPorProfesor(
            @PathVariable Integer idProfesor,
            @PathVariable String clavePeriodo) {
        
        List<String> grupos = asignacionService.obtenerGruposPorProfesor(idProfesor, clavePeriodo);
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene estadísticas de asignaciones del período
     * GET /api/asignaciones/estadisticas/periodo/{clavePeriodo}
     */
    @GetMapping("/estadisticas/periodo/{clavePeriodo}")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(
            @PathVariable String clavePeriodo) {
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalAsignaciones", asignacionService.contarAsignaciones(clavePeriodo));
        estadisticas.put("profesores", asignacionService.obtenerProfesoresDelPeriodo(clavePeriodo));
        estadisticas.put("materias", asignacionService.obtenerMateriasDelPeriodo(clavePeriodo));
        estadisticas.put("periodo", clavePeriodo);
        
        return ResponseEntity.ok(estadisticas);
    }
}
