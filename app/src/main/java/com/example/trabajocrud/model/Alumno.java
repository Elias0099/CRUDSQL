package com.example.trabajocrud.model;

public class Alumno {

    private int id;
    private String nombres;
    private String apellidos;
    private String correo; // Nuevo campo
    private int carreraId;

    public Alumno(int id, String nombres, String apellidos, String correo, int carreraId) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.carreraId = carreraId;
    }

    public int getId() {
        return id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(int carreraId) {
        this.carreraId = carreraId;
    }
}