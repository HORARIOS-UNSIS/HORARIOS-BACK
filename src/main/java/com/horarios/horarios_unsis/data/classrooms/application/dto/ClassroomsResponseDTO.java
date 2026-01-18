package com.horarios.horarios_unsis.data.classrooms.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta de un aula")
public class ClassroomsResponseDTO {
    
    @Schema(description = "ID único del aula", example = "1")
    private Integer idAula;
    
    @Schema(description = "Nombre del aula", example = "Aula 101")
    private String nombre;
    
    @Schema(description = "Capacidad del aula", example = "40")
    private Integer capacidad;

    public ClassroomsResponseDTO() {
    }

    public ClassroomsResponseDTO(Integer idAula, String nombre, Integer capacidad) {
        this.idAula = idAula;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public Integer getIdAula() {
        return idAula;
    }

    public void setIdAula(Integer idAula) {
        this.idAula = idAula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
}
