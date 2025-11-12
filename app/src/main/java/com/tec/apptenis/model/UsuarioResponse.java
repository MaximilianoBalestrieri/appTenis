package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;

public class UsuarioResponse {
    @SerializedName("Token")
    private String token;
    private String mensaje;
    private Usuario usuario;
    public UsuarioResponse() {
    }
    public UsuarioResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    // Setters (Necesarios para que el código sea completo y flexible)
    public void setToken(String token) {
        this.token = token;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}

