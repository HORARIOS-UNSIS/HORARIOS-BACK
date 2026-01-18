package com.horarios.horarios_unsis.data.synodals.domain.model;

import com.horarios.horarios_unsis.data.teacher.domain.model.Teacher;
import com.horarios.horarios_unsis.data.subject.domain.model.Subject;

public class Synodal {
    private Integer id;
    private Teacher profesorSinodal;
    private Teacher profesorTitular;
    private Subject materia;

    public Synodal() {
    }

    public Synodal(Integer id, Teacher profesorSinodal, Teacher profesorTitular, Subject materia) {
        this.id = id;
        this.profesorSinodal = profesorSinodal;
        this.profesorTitular = profesorTitular;
        this.materia = materia;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Teacher getProfesorSinodal() { return profesorSinodal; }
    public void setProfesorSinodal(Teacher profesorSinodal) { this.profesorSinodal = profesorSinodal; }

    public Teacher getProfesorTitular() { return profesorTitular; }
    public void setProfesorTitular(Teacher profesorTitular) { this.profesorTitular = profesorTitular; }

    public Subject getMateria() { return materia; }
    public void setMateria(Subject materia) { this.materia = materia; }

    // --- Métodos de Conveniencia 

    public Integer getIdProfesorTitular() {
        return (profesorTitular != null) ? profesorTitular.getIdProfesor() : null;
    }

}