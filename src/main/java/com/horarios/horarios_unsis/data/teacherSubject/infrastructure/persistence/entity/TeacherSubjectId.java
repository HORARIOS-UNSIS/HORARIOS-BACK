package com.horarios.horarios_unsis.data.teacherSubject.infrastructure.persistence.entity;

import java.io.Serializable;
import java.util.Objects;

public class TeacherSubjectId implements Serializable {
    private Integer idProfesor;
    private Integer idMateria;

    public TeacherSubjectId() {
    }

    public TeacherSubjectId(Integer idProfesor, Integer idMateria) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherSubjectId that = (TeacherSubjectId) o;
        return Objects.equals(idProfesor, that.idProfesor) && Objects.equals(idMateria, that.idMateria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProfesor, idMateria);
    }
}