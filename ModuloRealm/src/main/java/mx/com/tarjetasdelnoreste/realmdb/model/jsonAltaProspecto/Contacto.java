
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import com.google.gson.annotations.SerializedName;

public class Contacto {

    @SerializedName("apellidoMaterno")
    private String mApellidoMaterno;
    @SerializedName("apellidoPaterno")
    private String mApellidoPaterno;
    @SerializedName("cargo")
    private Cargo mCargo;
    @SerializedName("comentarios")
    private String mComentarios;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("extension")
    private String mExtension;
    @SerializedName("fotografia")
    private String mFotografia;
    @SerializedName("id")
    private int mId;
    @SerializedName("nombres")
    private String mNombres;
    @SerializedName("principal")
    private Boolean mPrincipal;
    @SerializedName("telefono")
    private String mTelefono;
    @SerializedName("idAltaOffline")
    private String idAltaOffline;

    public String getApellidoMaterno() {
        return mApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        mApellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return mApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        mApellidoPaterno = apellidoPaterno;
    }

    public Cargo getCargo() {
        return mCargo;
    }

    public void setCargo(Cargo mCargo) {
        this.mCargo = mCargo;
    }

    public String getComentarios() {
        return mComentarios;
    }

    public void setComentarios(String comentarios) {
        mComentarios = comentarios;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getExtension() {
        return mExtension;
    }

    public void setExtension(String extension) {
        mExtension = extension;
    }

    public String getFotografia() {
        return mFotografia;
    }

    public void setFotografia(String fotografia) {
        mFotografia = fotografia;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getNombres() {
        return mNombres;
    }

    public void setNombres(String nombres) {
        mNombres = nombres;
    }

    public Boolean getPrincipal() {
        return mPrincipal;
    }

    public void setPrincipal(Boolean principal) {
        mPrincipal = principal;
    }

    public String getTelefono() {
        return mTelefono;
    }

    public void setTelefono(String telefono) {
        mTelefono = telefono;
    }

    public String getIdAltaOffline() {
        return idAltaOffline;
    }

    public void setIdAltaOffline(String idAltaOffline) {
        this.idAltaOffline = idAltaOffline;
    }
}
