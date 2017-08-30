
package com.telstock.tmanager.cemex.prospectos.model;

import com.google.gson.annotations.SerializedName;

public class ProductosServiciosUpCross {

    @SerializedName("idObra")
    private String mIdObra;
    @SerializedName("idTipoProspecto")
    private Long mIdTipoProspecto;

    public String getIdObra() {
        return mIdObra;
    }

    public void setIdObra(String idObra) {
        mIdObra = idObra;
    }

    public Long getIdTipoProspecto() {
        return mIdTipoProspecto;
    }

    public void setIdTipoProspecto(Long idTipoProspecto) {
        mIdTipoProspecto = idTipoProspecto;
    }

}
