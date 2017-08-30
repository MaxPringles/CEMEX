
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import com.google.gson.annotations.SerializedName;

public class EstatusObra {

    @SerializedName("descripcion")
    private String mDescripcion = "";
    @SerializedName("id")
    private Long mId = (long) 0;
    @SerializedName("idCatalogo")
    private int mIdCatalogo =  0;
    @SerializedName("idPadre")
    private int mIdPadre =  0;
    @SerializedName("status")
    private int mStatus =  0;

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
