package com.tec.apptenis.model;

import java.io.Serializable;

public class Clase implements Serializable {

    private int idClase;
    private Profesor idProfesor;
    private Alumno idAlumno;
    private String fecha;
    private String hora;
    private String comentario;
    private String estado;

    public Clase() {

    }

    public Clase(int idClase, Profesor idProfesor, Alumno idAlumno, String fecha, String hora, String comentario, String estado) {
        this.idClase = idClase;
        this.idProfesor = idProfesor;
        this.idAlumno = idAlumno;
        this.fecha = fecha;
        this.hora = hora;
        this.comentario = comentario;
        this.estado = estado;
    }
}

