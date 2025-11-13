package com.tec.apptenis.model;

public class IdUsuarioRequest {
    private final int idUsuario;

    public IdUsuarioRequest(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
}