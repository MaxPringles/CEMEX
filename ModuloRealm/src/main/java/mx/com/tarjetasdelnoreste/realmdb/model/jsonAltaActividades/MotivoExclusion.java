
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades;

import com.google.gson.annotations.SerializedName;

public class MotivoExclusion {

    @SerializedName("descripcion")
    private String mDescripcion;
    @SerializedName("id")
    private Long mId;
    @SerializedName("idCatalogo")
    private Long mIdCatalogo;
    @SerializedName("idPadre")
    private Long mIdPadre;
    @SerializedName("status")
    private Long mStatus;

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        mDescripcion = descripcion;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
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

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

}
