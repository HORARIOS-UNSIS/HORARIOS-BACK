package com.horarios.horarios_unsis.data.teacherSubject.application.dto;

public class TeacherSubjectResponseDTO {
    private Integer idProfesor;
    private Integer idMateria;
    private String nombreProfesor;
    private String nombreMateria;

    public TeacherSubjectResponseDTO() {
    }

    public TeacherSubjectResponseDTO(Integer idProfesor, Integer idMateria, String nombreProfesor, String nombreMateria) {
        this.idProfesor = idProfesor;
        this.idMateria = idMateria;
        this.nombreProfesor = nombreProfesor;
        this.nombreMateria = nombreMateria;
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

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    public void setNombreProfesor(String nombreProfesor) {
        this.nombreProfesor = nombreProfesor;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }
}