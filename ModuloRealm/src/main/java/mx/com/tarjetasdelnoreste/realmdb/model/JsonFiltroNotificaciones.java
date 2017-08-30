package mx.com.tarjetasdelnoreste.realmdb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by usr_micro13 on 03/02/2017.
 */

public class JsonFiltroNotificaciones {

    @SerializedName("idUsuario")
    private String mIdUsuario;
    @SerializedName("status")
    private String mStatus;

    public String getIdUsuario() {
        return mIdUsuario;
    }

    public void setIdUsuario(String mIdUsuario) {
        this.mIdUsuario = mIdUsuario;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
