
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class TipoActividad {

    @SerializedName("Descripcion")
    private String mDescripcion;
    @SerializedName("id")
    private Long mId;
    @SerializedName("idCatalogo")
    private Long mIdCatalogo;
    @SerializedName("idPadre")
    private Long mIdPadre;
    @SerializedName("nombre")
    private String mNombre;

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

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

}
