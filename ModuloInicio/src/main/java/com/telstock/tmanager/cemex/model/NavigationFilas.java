package com.telstock.tmanager.cemex.model;

/**
 * Created by czamora on 8/9/16.
 */
public class NavigationFilas {

    private String nombreFila;
    private int imagenFila;
    private int notificacionFila;

    public NavigationFilas(String nombreFila, int imagenFila, int notificacionFila) {
        this.nombreFila = nombreFila;
        this.imagenFila = imagenFila;
        this.notificacionFila = notificacionFila;
    }

    public String getNombreFila() {
        return nombreFila;
    }

    public void setNombreFila(String nombreFila) {
        this.nombreFila = nombreFila;
    }

    public int getImagenFila() {
        return imagenFila;
    }

    public void setImagenFila(int imagenFila) {
        this.imagenFila = imagenFila;
    }

    public int getNotificacionFila() {
        return notificacionFila;
    }

    public void setNotificacionFila(int notificacionFila) {
        this.notificacionFila = notificacionFila;
    }
}
