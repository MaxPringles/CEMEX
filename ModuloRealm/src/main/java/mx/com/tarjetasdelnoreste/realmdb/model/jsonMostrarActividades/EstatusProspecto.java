
package mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

@SuppressWarnings("unused")
public class EstatusProspecto extends RealmObject {

    @SerializedName("descripcion")
    private String mDescripcion;
    @SerializedName("id")
    private int mId;
    @SerializedName("idCatalogo")
    private Long mIdCatalogo;
    @SerializedName("idPadre")
    private Long mIdPadre;
    @SerializedName("idPaso")
    private Long mIdPaso;
    @SerializedName("status")
    private Long mStatus;

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        mDescripcion = descripcion;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Long getIdCatalogo() {
        return mIdCatalogo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        mIdCatalogo = idCatalogo;
    }

    public Long getIdPadre() {
        return mIdPadre;
    }

    public void setIdPadre(Long idPadre) {
        mIdPadre = idPadre;
    }

    public Long getIdPaso() {
        return mIdPaso;
    }

    public void setIdPaso(Long idPaso) {
        mIdPaso = idPaso;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
