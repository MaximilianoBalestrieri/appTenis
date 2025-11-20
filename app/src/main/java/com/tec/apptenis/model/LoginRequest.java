package com.tec.apptenis.model;

public class LoginRequest {
    private String email;
    private String clave;

    public LoginRequest(String email, String clave) {
        this.email = email;
        this.clave = clave;
    }
}
