package com.telstock.tmanager.cemex.model;

/**
 * Created by czamora on 9/7/16.
 */
public class GetUsuarioPOJO {

    public String Imagen;
    public int idUsuario;
    public String DescripcionRol;
    public String nombre;
    public String appPaterno;
    public String appMaterno;

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcionRol() {
        return DescripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        DescripcionRol = descripcionRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAppPaterno() {
        return appPaterno;
    }

    public void setAppPaterno(String appPaterno) {
        this.appPaterno = appPaterno;
    }

    public String getAppMaterno() {
        return appMaterno;
    }

    public void setAppMaterno(String appMaterno) {
        this.appMaterno = appMaterno;
    }
}
