package com.horarios.horarios_unsis.data.subject.application.dto;

public class SubjectRequestDTO {
    private String nombre;

    public SubjectRequestDTO() {
    }

    public SubjectRequestDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}