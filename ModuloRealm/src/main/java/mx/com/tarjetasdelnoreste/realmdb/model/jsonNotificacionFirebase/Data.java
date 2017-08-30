
package mx.com.tarjetasdelnoreste.realmdb.model.jsonNotificacionFirebase;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Data {

    @SerializedName("idNotificacion")
    private String mIdNotificacion;
    @SerializedName("idPrioridad")
    private Long mIdPrioridad;
    @SerializedName("idReferencia")
    private String mIdReferencia;
    @SerializedName("idTipoNotificacion")
    private Long mIdTipoNotificacion;

    public String getIdNotificacion() {
        return mIdNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        mIdNotificacion = idNotificacion;
    }

    public Long getIdPrioridad() {
        return mIdPrioridad;
    }

    public void setIdPrioridad(Long idPrioridad) {
        mIdPrioridad = idPrioridad;
    }

    public String getIdReferencia() {
        return mIdReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        mIdReferencia = idReferencia;
    }

    public Long getIdTipoNotificacion() {
        return mIdTipoNotificacion;
    }

    public void setIdTipoNotificacion(Long idTipoNotificacion) {
        mIdTipoNotificacion = idTipoNotificacion;
    }

}
