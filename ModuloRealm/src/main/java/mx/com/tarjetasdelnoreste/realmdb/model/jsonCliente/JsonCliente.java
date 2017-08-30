
package mx.com.tarjetasdelnoreste.realmdb.model.jsonCliente;

import com.google.gson.annotations.SerializedName;


public class JsonCliente{

    @SerializedName("comentarios")
    private String mComentarios;
    @SerializedName("id")
    private String mId;
    @SerializedName("nombre")
    private String mNombre;
    @SerializedName("razonSocial")
    private String mRazonSocial;
    @SerializedName("rfc")
    private String mRfc;
    @SerializedName("sitioweb")
    private String mSitioweb;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("telefono")
    private String mTelefono;

    public String getComentarios() {
        return mComentarios;
    }

    public void setComentarios(String comentarios) {
        mComentarios = comentarios;
    }

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

    public String getSitioweb() {
        return mSitioweb;
    }

    public void setSitioweb(String sitioweb) {
        mSitioweb = sitioweb;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public String getTelefono() {
        return mTelefono;
    }

    public void setTelefono(String telefono) {
        mTelefono = telefono;
    }

}
