package mx.com.tarjetasdelnoreste.realmdb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by usr_micro13 on 07/02/2017.
 */

public class JSONFiltroProspecto {

    @SerializedName("idVendedorAsignado")
    private String mIdVendedorAsignado;
    @SerializedName("id")
    private String mId;

    public String getIdVendedorAsignado() {
        return mIdVendedorAsignado;
    }

    public void setIdVendedorAsignado(String idVendedorAsignado) {
        mIdVendedorAsignado = idVendedorAsignado;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }
}

