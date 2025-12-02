package com.horarios.horarios_unsis.data.user.domain.model;

public class User {
    private Integer idUsuario;
    private String nombre;
    private String email;
    private String username;
    private String password;
    private Role rol;
    private Boolean activo;

    public User() {
    }

    public User(Integer idUsuario, String nombre, String email, String username, String password, Role rol, Boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRol() {
        return rol;
    }

    public void setRol(Role rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}