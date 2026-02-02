package com.horarios.horarios_unsis.data.grupo.infrastructure.controller;

import com.horarios.horarios_unsis.data.grupo.infrastructure.persistence.entity.GrupoEntity;
import com.horarios.horarios_unsis.data.grupo.infrastructure.persistence.repository.GrupoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@Tag(name = "Grupos", description = "Gestión de grupos académicos")
public class GrupoController {

    private final GrupoRepository grupoRepository;

    public GrupoController(GrupoRepository grupoRepository) {
        this.grupoRepository = grupoRepository;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los grupos")
    public ResponseEntity<List<GrupoEntity>> getAllGroups() {
        return ResponseEntity.ok(grupoRepository.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener grupo por ID")
    public ResponseEntity<GrupoEntity> getGroupById(@PathVariable Integer id) {
        return grupoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/carrera/{claveCarrera}")
    @Operation(summary = "Obtener grupos por carrera")
    public ResponseEntity<List<GrupoEntity>> getGroupsByCareer(@PathVariable String claveCarrera) {
        return ResponseEntity.ok(grupoRepository.findByClaveCarrera(claveCarrera));
    }

    @GetMapping("/periodo/{clavePeriodo}")
    @Operation(summary = "Obtener grupos por período")
    public ResponseEntity<List<GrupoEntity>> getGroupsByPeriod(@PathVariable String clavePeriodo) {
        return ResponseEntity.ok(grupoRepository.findByClavePeriodo(clavePeriodo));
    }

    @GetMapping("/semestre/{semestre}")
    @Operation(summary = "Obtener grupos por semestre")
    public ResponseEntity<List<GrupoEntity>> getGroupsBySemester(@PathVariable Integer semestre) {
        return ResponseEntity.ok(grupoRepository.findBySemestre(semestre));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo grupo")
    public ResponseEntity<GrupoEntity> createGroup(@RequestBody GrupoEntity grupo) {
        return ResponseEntity.ok(grupoRepository.save(grupo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar grupo")
    public ResponseEntity<GrupoEntity> updateGroup(@PathVariable Integer id, @RequestBody GrupoEntity grupo) {
        return grupoRepository.findById(id)
                .map(existing -> {
                    existing.setClave(grupo.getClave());
                    existing.setNombre(grupo.getNombre());
                    existing.setClaveCarrera(grupo.getClaveCarrera());
                    existing.setSemestre(grupo.getSemestre());
                    existing.setAlumnos(grupo.getAlumnos());
                    existing.setClavePeriodo(grupo.getClavePeriodo());
                    return ResponseEntity.ok(grupoRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar grupo")
    public ResponseEntity<Void> deleteGroup(@PathVariable Integer id) {
        if (grupoRepository.existsById(id)) {
            grupoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
