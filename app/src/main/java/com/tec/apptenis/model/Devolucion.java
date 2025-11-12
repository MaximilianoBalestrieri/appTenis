package com.tec.apptenis.model;

import java.io.Serializable;

import kotlinx.serialization.descriptors.ClassSerialDescriptorBuilder;

public class Devolucion implements Serializable {
    private int idDevolucion;
    private String comentario;
    private String ejemplo;
    private Clase idClase;

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

    public Clase getIdClase() {
        return idClase;
    }

    public void setIdClase(Clase idClase) {
        this.idClase = idClase;
    }

    public Devolucion(int idDevolucion, String comentario, String ejemplo, Clase idClase) {
        this.idDevolucion = idDevolucion;
        this.comentario = comentario;
        this.ejemplo = ejemplo;
        this.idClase = idClase;


    }
}
