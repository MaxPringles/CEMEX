
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import com.google.gson.annotations.SerializedName;

public class Pais {

    @SerializedName("id")
    private String mId;
    @SerializedName("nombre")
    private String mNombre;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

}
