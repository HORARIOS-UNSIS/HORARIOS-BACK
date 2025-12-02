package com.horarios.horarios_unsis.data.teacherSubject.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "profesor_materia")
@IdClass(TeacherSubjectId.class)
public class TeacherSubjectEntity {
    
    @Id
    @Column(name = "id_profesor")
    private Integer idProfesor;

    @Id
    @Column(name = "id_materia")
    private Integer idMateria;

    public TeacherSubjectEntity() {
    }

    public TeacherSubjectEntity(Integer idProfesor, Integer idMateria) {
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