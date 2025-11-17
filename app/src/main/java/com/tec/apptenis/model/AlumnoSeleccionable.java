package com.tec.apptenis.model;

import java.io.Serializable;


public class AlumnoSeleccionable implements Serializable {
    private final Alumno alumno;
    private boolean isSelected;

    public AlumnoSeleccionable(Alumno alumno, boolean isSelected) {
        this.alumno = alumno;
        this.isSelected = isSelected;
    }

    // Getters y Setters
    public Alumno getAlumno() {
        return alumno;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // Delegación para obtener el nombre (útil para el Adapter)
    public String getNombreCompleto() {
        // Asumiendo que Alumno tiene getNombre() y getApellido()
        return alumno.getNombre() ;
    }
}