package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Profesor implements Serializable {
    private int idProfesor;

    private String apellido;
    private String telefono;

    // 🔥 CAMBIO CRÍTICO: Mapeamos el objeto anidado 'usuario' 🔥
    @SerializedName("usuario")
    private Usuario usuario;

    public Profesor() {
    }

    // Nota: El constructor completo debe actualizarse para usar el objeto Usuario
    // Aquí lo simplificamos, pero debes actualizar donde lo uses:
    // public Profesor(int idProfesor, String apellido, String telefono, Usuario usuario) {
    //     this.idProfesor = idProfesor;
    //     this.apellido = apellido;
    //     this.telefono = telefono;
    //     this.usuario = usuario;
    // }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public String getapellido() {
        return apellido;
    }

    public void setapellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // 🔥 NUEVOS GETTERS/SETTERS para acceder al objeto anidado 'usuario' 🔥
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    // IMPORTANTE: Los antiguos getEmail() y getClave() deben eliminarse o no usarse,
    // ya que ahora se accede a los datos a través de getUsuario().
}