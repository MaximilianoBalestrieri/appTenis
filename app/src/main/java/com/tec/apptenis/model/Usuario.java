package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Usuario implements Serializable {

    // Nota: Aunque el backend lo use como clave, aquí se mapea
    @SerializedName("idUsuario")
    private int idUsuario;

    @SerializedName("email")
    private String email;

    @SerializedName("clave")
    private String clave;

    @SerializedName("rol")
    private String rol;

    // --- Getters y Setters ---

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}