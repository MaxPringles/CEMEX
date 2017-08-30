
package mx.com.tarjetasdelnoreste.realmdb.model.jsonObra;

import com.google.gson.annotations.SerializedName;

public class Direccion {

    @SerializedName("calle")
    private String mCalle;
    @SerializedName("codigoPostal")
    private String mCodigoPostal;
    @SerializedName("colonia")
    private String mColonia;
    @SerializedName("comentarios")
    private String mComentarios;
    @SerializedName("estado")
    private Estado mEstado;
    @SerializedName("municipio")
    private Municipio mMunicipio;
    @SerializedName("numero")
    private String mNumero;
    @SerializedName("pais")
    private Pais mPais;
    @SerializedName("ubicacion")
    private Ubicacion mUbicacion;

    public String getCalle() {
        return mCalle;
    }

    public void setCalle(String calle) {
        mCalle = calle;
    }

    public String getCodigoPostal() {
        return mCodigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        mCodigoPostal = codigoPostal;
    }

    public String getColonia() {
        return mColonia;
    }

    public void setColonia(String colonia) {
        mColonia = colonia;
    }

    public String getComentarios() {
        return mComentarios;
    }

    public void setComentarios(String comentarios) {
        mComentarios = comentarios;
    }

    public Estado getEstado() {
        return mEstado;
    }

    public void setEstado(Estado estado) {
        mEstado = estado;
    }

    public Municipio getMunicipio() {
        return mMunicipio;
    }

    public void setMunicipio(Municipio municipio) {
        mMunicipio = municipio;
    }

    public String getNumero() {
        return mNumero;
    }

    public void setNumero(String numero) {
        mNumero = numero;
    }

    public Pais getPais() {
        return mPais;
    }

    public void setPais(Pais pais) {
        mPais = pais;
    }

    public Ubicacion getUbicacion() {
        return mUbicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        mUbicacion = ubicacion;
    }

}
