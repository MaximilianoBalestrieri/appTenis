package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ClaseCreacionRequest {

    // Asegúrate que los nombres de las propiedades coincidan con el backend (case-sensitive)

    @SerializedName("fecha")
    private String fecha; // Formato yyyy-MM-dd

    @SerializedName("hora")
    private String hora;  // Formato HH:mm:ss o HH:mm

    @SerializedName("idProfesor")
    private int idProfesor;

    @SerializedName("estado")
    private String estado;

    @SerializedName("comentario")
    private String comentario;

    // Aquí enviamos la lista de IDs de alumnos (según el DTO del backend o la propiedad [NotMapped])
    @SerializedName("IdsAlumnosSeleccionados") // **IMPORTANTE**: Ajusta el nombre según la propiedad [NotMapped] de tu clase Clase.cs
    private List<Integer> idsAlumnos;

    // Constructor, Getters y Setters
    public ClaseCreacionRequest(String fecha, String hora, int idProfesor, String estado, String comentario, List<Integer> idsAlumnos) {
        this.fecha = fecha;
        this.hora = hora;
        this.idProfesor = idProfesor;
        this.estado = estado;
        this.comentario = comentario;
        this.idsAlumnos = idsAlumnos;
    }
}