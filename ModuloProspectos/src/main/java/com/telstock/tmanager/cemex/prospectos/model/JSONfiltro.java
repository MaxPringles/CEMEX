
package com.telstock.tmanager.cemex.prospectos.model;

import com.google.gson.annotations.SerializedName;

public class JSONfiltro {

    @SerializedName("idVendedorAsignado")
    private String mIdVendedorAsignado;

    @SerializedName("fechaAltaProspecto")
    private int mFechaAltaProspecto;

    public String getIdVendedorAsignado() {
        return mIdVendedorAsignado;
    }

    public void setIdVendedorAsignado(String idVendedorAsignado) {
        mIdVendedorAsignado = idVendedorAsignado;
    }

    public int getFechaAltaProspecto() {
        return mFechaAltaProspecto;
    }

    public void setFechaAltaProspecto(int mFechaAltaProspecto) {
        this.mFechaAltaProspecto = mFechaAltaProspecto;
    }
}
