
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import com.google.gson.annotations.SerializedName;

public class TipoProspecto {

    @SerializedName("descripcion")
    private String mDescripcion;
    @SerializedName("id")
    private Long mId;
    @SerializedName("idCatalogo")
    private int mIdCatalogo;
    @SerializedName("idPadre")
    private int mIdPadre;

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

}
