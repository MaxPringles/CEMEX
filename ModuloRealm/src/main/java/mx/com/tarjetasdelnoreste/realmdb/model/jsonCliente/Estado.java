
package mx.com.tarjetasdelnoreste.realmdb.model.jsonCliente;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Estado{

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
