
package com.telstock.tmanager.cemex.modulocitas.model;

import com.google.gson.annotations.SerializedName;

public class JSONdescartar {

    @SerializedName("idProspecto")
    private String mIdProspecto;
    @SerializedName("motivoExclusion")
    private MotivoExclusion mMotivoExclusion;

    public String getIdProspecto() {
        return mIdProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        mIdProspecto = idProspecto;
    }

    public MotivoExclusion getMotivoExclusion() {
        return mMotivoExclusion;
    }

    public void setMotivoExclusion(MotivoExclusion motivoExclusion) {
        mMotivoExclusion = motivoExclusion;
    }

}
