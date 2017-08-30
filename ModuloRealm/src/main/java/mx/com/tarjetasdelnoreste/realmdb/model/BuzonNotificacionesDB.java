
package mx.com.tarjetasdelnoreste.realmdb.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@SuppressWarnings("unused")
public class BuzonNotificacionesDB extends RealmObject{

    @PrimaryKey
    @SerializedName("id")
    private String mId;
    @SerializedName("estaLeido")
    private Boolean mEstaLeido;
    @SerializedName("fecha")
    private Long mFecha;
    @SerializedName("idAccion")
    private Long mIdAccion;
    @SerializedName("idPrioridad")
    private int mIdPrioridad;
    @SerializedName("idUsuario")
    private String mIdUsuario;
    @SerializedName("idUsuarioRemitente")
    private String mIdUsuarioRemitente;
    @SerializedName("mensaje")
    private String mMensaje;
    @SerializedName("nombreRemitente")
    private String mNombreRemitente;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("title")
    private String mTitle;

    public Boolean getEstaLeido() {
        return mEstaLeido;
    }

    public void setEstaLeido(Boolean estaLeido) {
        mEstaLeido = estaLeido;
    }

    public Long getFecha() {
        return mFecha;
    }

    public void setFecha(Long fecha) {
        mFecha = fecha;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Long getIdAccion() {
        return mIdAccion;
    }

    public void setIdAccion(Long idAccion) {
        mIdAccion = idAccion;
    }

    public int getIdPrioridad() {
        return mIdPrioridad;
    }

    public void setIdPrioridad(int idPrioridad) {
        mIdPrioridad = idPrioridad;
    }

    public String getIdUsuario() {
        return mIdUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        mIdUsuario = idUsuario;
    }

    public String getIdUsuarioRemitente() {
        return mIdUsuarioRemitente;
    }

    public void setIdUsuarioRemitente(String idUsuarioRemitente) {
        mIdUsuarioRemitente = idUsuarioRemitente;
    }

    public String getMensaje() {
        return mMensaje;
    }

    public void setMensaje(String mensaje) {
        mMensaje = mensaje;
    }

    public String getNombreRemitente() {
        return mNombreRemitente;
    }

    public void setNombreRemitente(String nombreRemitente) {
        mNombreRemitente = nombreRemitente;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
