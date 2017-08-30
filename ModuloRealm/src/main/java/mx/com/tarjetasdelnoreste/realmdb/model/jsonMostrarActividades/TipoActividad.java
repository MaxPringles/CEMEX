
package mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

@SuppressWarnings("unused")
public class TipoActividad extends RealmObject {

    @SerializedName("descripcion")
    private String mDescripcion;
    @SerializedName("id")
    private int mId;
    @SerializedName("idCatalogo")
    private int mIdCatalogo;
    @SerializedName("idPadre")
    private int mIdPadre;
    @SerializedName("status")
    private int mStatus;

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        mDescripcion = descripcion;
    }

    public int getId() {
        return mId;
    }

    public void setId(int _id) {
        mId = _id;
    }

    public int getIdCatalogo() {
        return mIdCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        mIdCatalogo = idCatalogo;
    }

    public int getIdPadre() {
        return mIdPadre;
    }

    public void setIdPadre(int idPadre) {
        mIdPadre = idPadre;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

}
