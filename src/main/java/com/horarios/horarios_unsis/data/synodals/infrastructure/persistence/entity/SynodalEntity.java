package com.horarios.horarios_unsis.data.synodals.infrastructure.persistence.entity;
import com.horarios.horarios_unsis.data.subject.infrastructure.persistence.entity.SubjectEntity;
import com.horarios.horarios_unsis.data.teacher.infrastructure.persistence.entity.TeacherEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "sinodales")
public class SynodalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // PROFESOR QUE SERÁ EL SINODAL
    @ManyToOne
    @JoinColumn(
        name = "id_profesor_sinodal", 
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_sinodal_hacia_profesor_sinodal")
    )
    private TeacherEntity profesorSinodal;

    // PROFESOR TITULAR DE LA MATERIA
    @ManyToOne
    @JoinColumn(
        name = "id_profesor_titular", 
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_sinodal_hacia_profesor_titular")
    )
    private TeacherEntity profesorTitular;

    @ManyToOne
    @JoinColumn(
        name = "id_materia", 
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_sinodal_hacia_materia")
    )
    private SubjectEntity materia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TeacherEntity getProfesorSinodal() {
        return profesorSinodal;
    }

    public void setProfesorSinodal(TeacherEntity profesorSinodal) {
        this.profesorSinodal = profesorSinodal;
    }

    public TeacherEntity getProfesorTitular() {
        return profesorTitular;
    }

    public void setProfesorTitular(TeacherEntity profesorTitular) {
        this.profesorTitular = profesorTitular;
    }

    public SubjectEntity getMateria() {
        return materia;
    }

    public void setMateria(SubjectEntity materia) {
        this.materia = materia;
    }
}
