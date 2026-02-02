package com.horarios.horarios_unsis.data.periodo.infrastructure.controller;

import com.horarios.horarios_unsis.data.periodo.infrastructure.persistence.entity.PeriodoEntity;
import com.horarios.horarios_unsis.data.periodo.infrastructure.persistence.repository.PeriodoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/periods")
@Tag(name = "Períodos", description = "Gestión de períodos académicos")
public class PeriodoController {

    private final PeriodoRepository periodoRepository;

    public PeriodoController(PeriodoRepository periodoRepository) {
        this.periodoRepository = periodoRepository;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los períodos")
    public ResponseEntity<List<PeriodoEntity>> getAllPeriods() {
        return ResponseEntity.ok(periodoRepository.findAll());
    }

    @GetMapping("/actual")
    @Operation(summary = "Obtener el período actual (basado en la fecha de hoy)")
    public ResponseEntity<PeriodoEntity> getCurrentPeriod() {
        LocalDate hoy = LocalDate.now();
        List<PeriodoEntity> periodos = periodoRepository.findByFechaActual(hoy);
        
        if (!periodos.isEmpty()) {
            return ResponseEntity.ok(periodos.get(0));
        }
        
        // Si no hay período actual por fecha, retornar el más reciente
        List<PeriodoEntity> todosOrdenados = periodoRepository.findAllOrderByFechaInicioDesc();
        if (!todosOrdenados.isEmpty()) {
            return ResponseEntity.ok(todosOrdenados.get(0));
        }
        
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener períodos activos")
    public ResponseEntity<List<PeriodoEntity>> getActivePeriods() {
        return ResponseEntity.ok(periodoRepository.findByActivo(true));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener período por ID")
    public ResponseEntity<PeriodoEntity> getPeriodById(@PathVariable Integer id) {
        return periodoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/clave/{clave}")
    @Operation(summary = "Obtener período por clave")
    public ResponseEntity<PeriodoEntity> getPeriodByClave(@PathVariable String clave) {
        return periodoRepository.findByClave(clave)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Obtener períodos por tipo")
    public ResponseEntity<List<PeriodoEntity>> getPeriodsByType(@PathVariable String tipo) {
        return ResponseEntity.ok(periodoRepository.findByTipo(tipo));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo período")
    public ResponseEntity<PeriodoEntity> createPeriod(@RequestBody PeriodoEntity periodo) {
        return ResponseEntity.ok(periodoRepository.save(periodo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar período")
    public ResponseEntity<PeriodoEntity> updatePeriod(@PathVariable Integer id, @RequestBody PeriodoEntity periodo) {
        return periodoRepository.findById(id)
                .map(existing -> {
                    existing.setClave(periodo.getClave());
                    existing.setNombre(periodo.getNombre());
                    existing.setTipo(periodo.getTipo());
                    existing.setFechaInicio(periodo.getFechaInicio());
                    existing.setFechaFin(periodo.getFechaFin());
                    existing.setActivo(periodo.getActivo());
                    return ResponseEntity.ok(periodoRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar período")
    public ResponseEntity<Void> deletePeriod(@PathVariable Integer id) {
        if (periodoRepository.existsById(id)) {
            periodoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
