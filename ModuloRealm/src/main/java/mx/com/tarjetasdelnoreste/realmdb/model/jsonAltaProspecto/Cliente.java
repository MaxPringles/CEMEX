
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import com.google.gson.annotations.SerializedName;

public class Cliente {

    @SerializedName("campania")
    private Campania mCampania;
    @SerializedName("comentarios")
    private String mComentarios;
    @SerializedName("direccion")
    private Direccion mDireccion;
    @SerializedName("nombre")
    private String mNombre;
    @SerializedName("obra")
    private String mObra;
    @SerializedName("razonSocial")
    private String mRazonSocial;
    @SerializedName("rfc")
    private String mRfc;
    @SerializedName("telefono")
    private String mTelefono;
    @SerializedName("sitioweb")
    private String mSitioWeb;

    public Campania getCampania() {
        return mCampania;
    }

    public void setCampania(Campania campania) {
        mCampania = campania;
    }

    public String getComentarios() {
        return mComentarios;
    }

    public void setComentarios(String comentarios) {
        mComentarios = comentarios;
    }

    public Direccion getDireccion() {
        return mDireccion;
    }

    public void setDireccion(Direccion direccion) {
        mDireccion = direccion;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public String getObra() {
        return mObra;
    }

    public void setObra(String obra) {
        mObra = obra;
    }

    public String getRazonSocial() {
        return mRazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        mRazonSocial = razonSocial;
    }

    public String getRfc() {
        return mRfc;
    }

    public void setRfc(String rfc) {
        mRfc = rfc;
    }

    public String getTelefono() {
        return mTelefono;
    }

    public void setTelefono(String telefono) {
        mTelefono = telefono;
    }

    public String getmSitioWeb() {
        return mSitioWeb;
    }

    public void setmSitioWeb(String mSitioWeb) {
        this.mSitioWeb = mSitioWeb;
    }
}
