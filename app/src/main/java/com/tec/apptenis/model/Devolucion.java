package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
// import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder; // Esto no es necesario si usas java.io.Serializable

public class Devolucion implements Serializable {
    private int idDevolucion;
    private String comentario;
    private String ejemplo;

    // 🚨 CORRECCIÓN CLAVE: Debe ser int IdClaseAlumno para coincidir con la FK de C#
    @SerializedName("idClaseAlumno") // <-- Mapeo explícito
    private int idClaseAlumno;

    public Devolucion() {
    }

    public int getIdDevolucion() {
        return idDevolucion;
    }

    public void setIdDevolucion(int idDevolucion) {
        this.idDevolucion = idDevolucion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getEjemplo() {
        return ejemplo;
    }

    public void setEjemplo(String ejemplo) {
        this.ejemplo = ejemplo;
    }

    // 🚨 Nuevo Getter y Setter para la FK correcta
    public int getIdClaseAlumno() {
        return idClaseAlumno;
    }

    public void setIdClaseAlumno(int idClaseAlumno) {
        this.idClaseAlumno = idClaseAlumno;
    }

    // 🚨 Constructor Completo Corregido
    public Devolucion(int idDevolucion, String comentario, String ejemplo, int idClaseAlumno) {
        this.idDevolucion = idDevolucion;
        this.comentario = comentario;
        this.ejemplo = ejemplo;
        this.idClaseAlumno = idClaseAlumno;
    }
}