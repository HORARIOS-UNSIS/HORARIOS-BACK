package com.horarios.horarios_unsis.data.teacherSubject.application.dto;

public class TeacherSubjectRequestDTO {
    private Integer idProfesor;
    private Integer idMateria;

    public TeacherSubjectRequestDTO() {
    }

    public TeacherSubjectRequestDTO(Integer idProfesor, Integer idMateria) {
        this.idProfesor = idProfesor;
        this.idMateria = idMateria;
    }

    public Integer getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(Integer idProfesor) {
        this.idProfesor = idProfesor;
    }

    public Integer getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }
}