package com.horarios.horarios_unsis.data.carrera.infrastructure.controller;

import com.horarios.horarios_unsis.data.carrera.infrastructure.persistence.entity.CarreraEntity;
import com.horarios.horarios_unsis.data.carrera.infrastructure.persistence.repository.CarreraRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@Tag(name = "Carreras", description = "Gestión de carreras académicas")
public class CarreraController {

    private final CarreraRepository carreraRepository;

    public CarreraController(CarreraRepository carreraRepository) {
        this.carreraRepository = carreraRepository;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las carreras")
    public ResponseEntity<List<CarreraEntity>> getAllCareers() {
        return ResponseEntity.ok(carreraRepository.findAll());
    }

    @GetMapping("/vigentes")
    @Operation(summary = "Obtener carreras vigentes")
    public ResponseEntity<List<CarreraEntity>> getActiveCareers() {
        return ResponseEntity.ok(carreraRepository.findByVigente(true));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener carrera por ID")
    public ResponseEntity<CarreraEntity> getCareerById(@PathVariable Integer id) {
        return carreraRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/clave/{clave}")
    @Operation(summary = "Obtener carrera por clave")
    public ResponseEntity<CarreraEntity> getCareerByClave(@PathVariable String clave) {
        return carreraRepository.findByClave(clave)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva carrera")
    public ResponseEntity<CarreraEntity> createCareer(@RequestBody CarreraEntity carrera) {
        return ResponseEntity.ok(carreraRepository.save(carrera));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar carrera")
    public ResponseEntity<CarreraEntity> updateCareer(@PathVariable Integer id, @RequestBody CarreraEntity carrera) {
        return carreraRepository.findById(id)
                .map(existing -> {
                    existing.setClave(carrera.getClave());
                    existing.setNombre(carrera.getNombre());
                    existing.setVigente(carrera.getVigente());
                    return ResponseEntity.ok(carreraRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar carrera")
    public ResponseEntity<Void> deleteCareer(@PathVariable Integer id) {
        if (carreraRepository.existsById(id)) {
            carreraRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
