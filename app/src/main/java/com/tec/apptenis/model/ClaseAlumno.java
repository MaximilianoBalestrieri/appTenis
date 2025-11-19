package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class ClaseAlumno implements Serializable {

    private int idClaseAlumno;
    private int idClase;
    private int idAlumno;

    @SerializedName("alumno")
    private Alumno alumno;

    // 🔥🔥 ESTA ES LA PROPIEDAD QUE FALTABA 🔥🔥
    @SerializedName("devoluciones")
    private List<Devolucion> devoluciones;

    public ClaseAlumno() {}

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

    // 🔥🔥 GETTERS Y SETTERS PARA LAS DEVOLUCIONES 🔥🔥
    public List<Devolucion> getDevoluciones() {
        return devoluciones;
    }

    public void setDevoluciones(List<Devolucion> devoluciones) {
        this.devoluciones = devoluciones;
    }
}
