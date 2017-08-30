package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades;

import com.google.gson.annotations.SerializedName;

/**

 * Created by usr_micro13 on 13/12/2016.
 */

public class ActividadAnterior {

    @SerializedName("idActividad")

    private String mIdActividad;
    @SerializedName("estatusAgenda")
    private int mEstatusAgenda;

    public String getIdActividad() {
        return mIdActividad;
    }

    public void setIdActividad(String mIdActividad) {
        this.mIdActividad = mIdActividad;
    }

    public int getEstatusAgenda() {
        return mEstatusAgenda;
    }

    public void setEstatusAgenda(int mEstatusAgenda) {
        this.mEstatusAgenda = mEstatusAgenda;

    }
}
