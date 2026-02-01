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

    @Schema(description = "Clave del aula", example = "A-101")
    private String clave;

    @Schema(description = "Estado del proyector", example = "Funcional")
    private String statusProyector;

    @Schema(description = "Tipo de aula", example = "Laboratorio")
    private String tipo;

    public ClassroomsResponseDTO() {
    }

    public ClassroomsResponseDTO(Integer idAula, String nombre, Integer capacidad) {
        this.idAula = idAula;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public ClassroomsResponseDTO(Integer idAula, String nombre, Integer capacidad, String clave, String statusProyector, String tipo) {
        this.idAula = idAula;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.clave = clave;
        this.statusProyector = statusProyector;
        this.tipo = tipo;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getStatusProyector() {
        return statusProyector;
    }

    public void setStatusProyector(String statusProyector) {
        this.statusProyector = statusProyector;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
}
