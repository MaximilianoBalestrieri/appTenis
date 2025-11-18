package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;

public class DevolucionRequest {
    // Nota: El ID de la devolución se generará en el backend (C#)

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("ejemplo")
    private String ejemplo;

    // Clave foránea que relaciona la devolución con el alumno inscrito en la clase.
    @SerializedName("idClaseAlumno")
    private int idClaseAlumno;

    public DevolucionRequest(String comentario, String ejemplo, int idClaseAlumno) {
        this.comentario = comentario;
        this.ejemplo = ejemplo;
        this.idClaseAlumno = idClaseAlumno;
    }

    // No se necesitan getters/setters si solo se usa para el POST
}