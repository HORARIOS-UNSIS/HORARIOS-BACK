package com.horarios.horarios_unsis.data.user.domain.model;

public enum Role {
    ADMIN("Administrador"),
    SERV("Servicios escolares"),
    JEFE("Jefe de carrera");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}