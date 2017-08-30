
package mx.com.tarjetasdelnoreste.realmdb.model.jsonObra;

import com.google.gson.annotations.SerializedName;

public class Pais {

    @SerializedName("id")
    private Long mId;
    @SerializedName("nombre")
    private String mNombre;

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

}
