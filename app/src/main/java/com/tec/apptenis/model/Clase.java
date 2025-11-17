package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
// Se eliminan los imports de java.sql.Time, java.time.LocalDate y java.time.LocalTime que ya no se usan.

public class Clase implements Serializable {

    private int idClase;

    @SerializedName("idProfesor")
    private int idProfesor;

    // 🟢 CORRECCIÓN 1: Mapeo y Tipo para la lista de alumnos inscritos
    // Usa 'claseAlumnos' para coincidir con la respuesta JSON y el tipo 'ClaseAlumno' anidado.
    @SerializedName("claseAlumnos")
    private List<ClaseAlumno> claseAlumnos;

    private Date fecha;

    // 🟢 CORRECCIÓN 2: Usar String para la hora para evitar fallos de parsing de 'java.sql.Time'
    private String hora;

    private String comentario;
    private String estado;

    public Clase() {
        this.claseAlumnos = new ArrayList<>();
    }

    // --- CONSTRUCTOR AJUSTADO ---
    public Clase(int idClase, int idProfesor, List<ClaseAlumno> claseAlumnos,
                 Date fecha, String hora, String comentario, String estado) {
        this.idClase = idClase;
        this.idProfesor = idProfesor;
        this.claseAlumnos = claseAlumnos; // Usar la lista corregida
        this.fecha = fecha;
        this.hora = hora;                 // Usar String para la hora
        this.comentario = comentario;
        this.estado = estado;
    }

    // --- GETTERS Y SETTERS ---

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    // 🟢 Getters/Setters para la lista corregida
    public List<ClaseAlumno> getClaseAlumnos() {
        return claseAlumnos;
    }

    public void setClaseAlumnos(List<ClaseAlumno> claseAlumnos) {
        this.claseAlumnos = claseAlumnos;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    // 🟢 Getters/Setters para la hora como String
    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}