package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ClaseAlumno implements Serializable {

    // 1. IDs de la tabla de unión (deben ser 'int')
    private int idClaseAlumno;

    // Estos IDs son clave foránea, pero Gson los mapea como int simples.
    private int idClase;
    private int idAlumno;

    // 2. Propiedad de Navegación del Alumno
    // Mapea el objeto 'alumno' anidado que viene en la respuesta del JSON.
    @SerializedName("alumno")
    private Alumno alumno;

    // ⚠️ NO incluyas la propiedad 'clase' aquí para evitar bucles de Gson.

    // --- CONSTRUCTOR (Opcional, pero buena práctica) ---

    public ClaseAlumno() {}

    public ClaseAlumno(int idClaseAlumno, int idClase, int idAlumno, Alumno alumno) {
        this.idClaseAlumno = idClaseAlumno;
        this.idClase = idClase;
        this.idAlumno = idAlumno;
        this.alumno = alumno;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdClaseAlumno() {
        return idClaseAlumno;
    }

    public void setIdClaseAlumno(int idClaseAlumno) {
        this.idClaseAlumno = idClaseAlumno;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
}