package com.horarios.horarios_unsis.data.materiagrupo.infrastructure.controller;

import com.horarios.horarios_unsis.data.materiagrupo.application.service.MateriaGrupoService;
import com.horarios.horarios_unsis.data.materiagrupo.infrastructure.persistence.entity.MateriaGrupoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar las relaciones materia-grupo.
 */
@RestController
@RequestMapping("/api/materias-grupo")
@CrossOrigin(origins = "*")
public class MateriaGrupoController {

    private static final Logger logger = LoggerFactory.getLogger(MateriaGrupoController.class);

    private final MateriaGrupoService materiaGrupoService;

    public MateriaGrupoController(MateriaGrupoService materiaGrupoService) {
        this.materiaGrupoService = materiaGrupoService;
    }

    /**
     * Sincroniza las relaciones materia-grupo desde el endpoint de horarios
     * POST /api/materias-grupo/sincronizar/{clavePeriodo}
     */
    @PostMapping("/sincronizar/{clavePeriodo}")
    public ResponseEntity<Map<String, Object>> sincronizarMaterias(
            @PathVariable String clavePeriodo,
            @RequestParam(required = false) String claveCarrera) {
        
        logger.info("Solicitud de sincronización de materias-grupo para período: {}, carrera: {}", 
                    clavePeriodo, claveCarrera);

        try {
            int cantidad;
            if (claveCarrera != null && !claveCarrera.isEmpty()) {
                cantidad = materiaGrupoService.sincronizarMateriasPorCarrera(clavePeriodo, claveCarrera);
            } else {
                cantidad = materiaGrupoService.sincronizarMateriasPorPeriodo(clavePeriodo);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Sincronización completada");
            response.put("materiasSincronizadas", cantidad);
            response.put("periodo", clavePeriodo);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error sincronizando materias-grupo: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error en sincronización: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * Obtiene las materias de un grupo específico
     * GET /api/materias-grupo/grupo/{claveGrupo}/periodo/{clavePeriodo}
     */
    @GetMapping("/grupo/{claveGrupo}/periodo/{clavePeriodo}")
    public ResponseEntity<List<MateriaGrupoEntity>> obtenerMateriasPorGrupo(
            @PathVariable String claveGrupo,
            @PathVariable String clavePeriodo) {
        
        List<MateriaGrupoEntity> materias = 
            materiaGrupoService.obtenerMateriasPorGrupo(claveGrupo, clavePeriodo);
        
        return ResponseEntity.ok(materias);
    }

    /**
     * Obtiene las materias de una carrera
     * GET /api/materias-grupo/carrera/{claveCarrera}/periodo/{clavePeriodo}
     */
    @GetMapping("/carrera/{claveCarrera}/periodo/{clavePeriodo}")
    public ResponseEntity<List<MateriaGrupoEntity>> obtenerMateriasPorCarrera(
            @PathVariable String claveCarrera,
            @PathVariable String clavePeriodo) {
        
        List<MateriaGrupoEntity> materias = 
            materiaGrupoService.obtenerMateriasPorCarrera(claveCarrera, clavePeriodo);
        
        return ResponseEntity.ok(materias);
    }

    /**
     * Obtiene todas las relaciones materia-grupo del período
     * GET /api/materias-grupo/periodo/{clavePeriodo}
     */
    @GetMapping("/periodo/{clavePeriodo}")
    public ResponseEntity<List<MateriaGrupoEntity>> obtenerMateriasPorPeriodo(
            @PathVariable String clavePeriodo) {
        
        List<MateriaGrupoEntity> materias = 
            materiaGrupoService.obtenerMateriasPorPeriodo(clavePeriodo);
        
        return ResponseEntity.ok(materias);
    }

    /**
     * Obtiene los grupos que cursan una materia
     * GET /api/materias-grupo/materia/{claveMateria}/periodo/{clavePeriodo}
     */
    @GetMapping("/materia/{claveMateria}/periodo/{clavePeriodo}")
    public ResponseEntity<List<MateriaGrupoEntity>> obtenerGruposPorMateria(
            @PathVariable String claveMateria,
            @PathVariable String clavePeriodo) {
        
        List<MateriaGrupoEntity> grupos = 
            materiaGrupoService.obtenerGruposPorMateria(claveMateria, clavePeriodo);
        
        return ResponseEntity.ok(grupos);
    }

    /**
     * Obtiene lista de materias únicas del período
     * GET /api/materias-grupo/unicas/periodo/{clavePeriodo}
     */
    @GetMapping("/unicas/periodo/{clavePeriodo}")
    public ResponseEntity<List<Map<String, Object>>> obtenerMateriasUnicas(
            @PathVariable String clavePeriodo) {
        
        List<Map<String, Object>> materias = 
            materiaGrupoService.obtenerMateriasUnicasDelPeriodo(clavePeriodo);
        
        return ResponseEntity.ok(materias);
    }

    /**
     * Obtiene resumen de materias por grupo
     * GET /api/materias-grupo/resumen/periodo/{clavePeriodo}
     */
    @GetMapping("/resumen/periodo/{clavePeriodo}")
    public ResponseEntity<Map<String, Object>> obtenerResumen(
            @PathVariable String clavePeriodo) {
        
        Map<String, Object> resumen = new HashMap<>();
        resumen.put("totalMaterias", materiaGrupoService.contarMaterias(clavePeriodo));
        resumen.put("materiasPorGrupo", materiaGrupoService.obtenerResumenMateriasPorGrupo(clavePeriodo));
        resumen.put("materiasUnicas", materiaGrupoService.obtenerMateriasUnicasDelPeriodo(clavePeriodo));
        resumen.put("periodo", clavePeriodo);
        
        return ResponseEntity.ok(resumen);
    }
}
