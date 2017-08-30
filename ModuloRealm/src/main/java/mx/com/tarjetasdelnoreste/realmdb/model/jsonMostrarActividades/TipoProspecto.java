package mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by movil4 on 30/12/16.
 */

public class TipoProspecto extends RealmObject {

    @SerializedName("descripcion")
    private String mDescripcion;
    @SerializedName("id")
    private int mId;
    @SerializedName("idCatalogo")
    private Long mIdCatalogo;
    @SerializedName("idPadre")
    private Long mIdPadre;
    @SerializedName("status")
    private Long mStatus;

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String mDescripcion) {
        this.mDescripcion = mDescripcion;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public Long getIdCatalogo() {
        return mIdCatalogo;
    }

    public void setIdCatalogo(Long mIdCatalogo) {
        this.mIdCatalogo = mIdCatalogo;
    }

    public Long getIdPadre() {
        return mIdPadre;
    }

    public void setIdPadre(Long mIdPadre) {
        this.mIdPadre = mIdPadre;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long mStatus) {
        this.mStatus = mStatus;
    }
}
