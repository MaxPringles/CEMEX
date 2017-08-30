
package mx.com.tarjetasdelnoreste.realmdb.model;

import com.google.gson.annotations.SerializedName;

public class Ruta {

    @SerializedName("fechaHoraAlta")
    private Long mFecha;
    @SerializedName("latitud")
    private double mLatitud;
    @SerializedName("longitud")
    private double mLongitud;
    @SerializedName("idVendedor")
    private String mIdVendedor;
    @SerializedName("idAccion")
    private int mIdAccion;
    @SerializedName("idAltaOffline")
    private String mIdAltaOffline;
    @SerializedName("status")
    private int mStatus;

    public Long getmFecha() {
        return mFecha;
    }

    public void setmFecha(Long mFecha) {
        this.mFecha = mFecha;
    }

    public double getmLatitud() {
        return mLatitud;
    }

    public void setmLatitud(double mLatitud) {
        this.mLatitud = mLatitud;
    }

    public double getmLongitud() {
        return mLongitud;
    }

    public void setmLongitud(double mLongitud) {
        this.mLongitud = mLongitud;
    }

    public String getmIdVendedor() {
        return mIdVendedor;
    }

    public void setmIdVendedor(String mIdVendedor) {
        this.mIdVendedor = mIdVendedor;
    }

    public int getmIdAccion() {
        return mIdAccion;
    }

    public void setmIdAccion(int mIdAccion) {
        this.mIdAccion = mIdAccion;
    }

    public String getmIdAltaOffline() {
        return mIdAltaOffline;
    }

    public void setmIdAltaOffline(String mIdAltaOffline) {
        this.mIdAltaOffline = mIdAltaOffline;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }
}
