package com.tec.apptenis.model;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class Alumno implements Serializable {

    private  int idAlumno;
    private String nombre;
    private String telefono;
    private String direccion;
    private String localidad;
    private String nivel;
    @SerializedName("manoHabil")
    private String manohabil;
    private String reves;

    public Alumno() {

    }

    public Alumno(int idAlumno, String nombre, String telefono, String direccion, String localidad, String nivel, String manohabil, String reves) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.localidad = localidad;
        this.nivel = nivel;
        this.manohabil = manohabil;
        this.reves = reves;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getManohabil() {
        return manohabil;
    }

    public void setManohabil(String manohabil) {
        this.manohabil = manohabil;
    }

    public String getReves() {
        return reves;
    }

    public void setReves(String reves) {
        this.reves = reves;
    }
}

