package com.horarios.horarios_unsis.data.teacher.application.dto;

public class TeacherRequestDTO {
    private String nombre;
    private Boolean sabatico;

    public TeacherRequestDTO() {
    }

    public TeacherRequestDTO(String nombre, Boolean sabatico) {
        this.nombre = nombre;
        this.sabatico = sabatico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getSabatico() {
        return sabatico;
    }

    public void setSabatico(Boolean sabatico) {
        this.sabatico = sabatico;
    }
}