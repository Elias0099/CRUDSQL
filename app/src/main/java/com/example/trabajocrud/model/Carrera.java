package com.example.trabajocrud.model;

public class Carrera {
    private int id;
    private String nombre;
    private String estado;

    public Carrera() {
        // Constructor vacío requerido para SQLite
    }

    public Carrera(int id, String nombre, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.estado = estado;
    }


    // Getters y setters para los atributos

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre; // Devuelve el nombre de la carrera como representación de cadena
    }
}

