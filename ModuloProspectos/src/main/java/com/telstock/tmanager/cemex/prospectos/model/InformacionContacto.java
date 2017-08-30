package com.telstock.tmanager.cemex.prospectos.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USRMICRO10 on 05/09/2016.
 */
public class InformacionContacto {
    @SerializedName("nombre")
    private String nombre;

    @SerializedName("appPaterno")
    private String apellidoPaterno;

    @SerializedName("appMaterno")
    private String apellidoMaterno;

    @SerializedName("Telefono")
    private String telefono;

    @SerializedName("Extension")
    private String extension;

    @SerializedName("email")
    private String email;

    @SerializedName("Cargo")
    private String cargo;

    @SerializedName("comentarios")
    private String comentarios;


//    private boolean contactoPrincipal;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

//    public boolean isContactoPrincipal() {
//        return contactoPrincipal;
//    }
//
//    public void setContactoPrincipal(boolean contactoPrincipal) {
//        this.contactoPrincipal = contactoPrincipal;
//    }
}
