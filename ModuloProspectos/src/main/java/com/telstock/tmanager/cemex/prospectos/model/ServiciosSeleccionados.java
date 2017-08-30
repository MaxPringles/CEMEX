package com.telstock.tmanager.cemex.prospectos.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USRMICRO10 on 20/09/2016.
 */
public class ServiciosSeleccionados {

    @SerializedName("idProducto")
    private int idProducto;

    @SerializedName("idProspecto")
    private int idProspecto;

    @SerializedName("idTipoSorP")
    private int idTipoSorP;

    @SerializedName("idSubSegmento")
    private int idSubSegmento;

    @SerializedName("descProducto")
    private String descProducto;

    @SerializedName("cantidadvol")
    private int cantidadvol;

    @SerializedName("unidadVol")
    private String unidadVol;

    public int getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(int idProspecto) {
        this.idProspecto = idProspecto;
    }

    public int getIdTipoSorP() {
        return idTipoSorP;
    }

    public void setIdTipoSorP(int idTipoSorP) {
        this.idTipoSorP = idTipoSorP;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdSubSegmento() {
        return idSubSegmento;
    }

    public void setIdSubSegmento(int idSubSegmento) {
        this.idSubSegmento = idSubSegmento;
    }

    public String getDescProducto() {
        return descProducto;
    }

    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    public int getCantidadvol() {
        return cantidadvol;
    }

    public void setCantidadvol(int cantidadvol) {
        this.cantidadvol = cantidadvol;
    }

    public String getUnidadVol() {
        return unidadVol;
    }

    public void setUnidadVol(String unidadVol) {
        this.unidadVol = unidadVol;
    }

}
