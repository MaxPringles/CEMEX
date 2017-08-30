
package mx.com.tarjetasdelnoreste.realmdb.model.json;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Ubicacion {

    @SerializedName("latitud")
    private String mLatitud;
    @SerializedName("longitud")
    private String mLongitud;

    public String getLatitud() {
        return mLatitud;
    }

    public void setLatitud(String latitud) {
        mLatitud = latitud;
    }

    public String getLongitud() {
        return mLongitud;
    }

    public void setLongitud(String longitud) {
        mLongitud = longitud;
    }

}
