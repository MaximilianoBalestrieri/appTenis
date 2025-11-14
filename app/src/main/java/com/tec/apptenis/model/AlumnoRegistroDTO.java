package com.tec.apptenis.model;

import com.google.gson.annotations.SerializedName;

public class AlumnoRegistroDTO {

    @SerializedName("UsuarioId")
    private int usuarioId;

    @SerializedName("Nombre")
    private String nombre;

    @SerializedName("Telefono")
    private String telefono;

    @SerializedName("Direccion")
    private String direccion;

    @SerializedName("Localidad")
    private String localidad;

    @SerializedName("Nivel")
    private String nivel;

    @SerializedName("ManoHabil")
    private String manoHabil;

    @SerializedName("Reves")
    private String reves;

    // Constructor completo
    public AlumnoRegistroDTO(int usuarioId, String nombre, String telefono, String direccion, String localidad, String nivel, String manoHabil, String reves) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.localidad = localidad;
        this.nivel = nivel;
        this.manoHabil = manoHabil;
        this.reves = reves;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getManoHabil() {
        return manoHabil;
    }

    public void setManoHabil(String manoHabil) {
        this.manoHabil = manoHabil;
    }

    public String getReves() {
        return reves;
    }

    public void setReves(String reves) {
        this.reves = reves;
    }
// Retrofit/Gson necesita Getters (no incluídos por brevedad, pero necesarios)
}