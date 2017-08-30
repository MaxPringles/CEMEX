
package mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@SuppressWarnings("unused")
public class JsonMostrarActividades extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String mId;

    @SerializedName("comentario")
    private String mComentario;
    @SerializedName("confirmarVolumenObra")
    private String mConfirmarVolumenObra;
    @SerializedName("estatusAgenda")
    private int mEstatusAgenda;
    @SerializedName("fechaAlta")
    private Long mFechaAlta;
    @SerializedName("fechaHoraCitaFin")
    private String mFechaHoraCitaFin;
    @SerializedName("fechaHoraCitaInicio")
    private String mFechaHoraCitaInicio;
    @SerializedName("fechaInicioObra")
    private String mFechaInicioObra;
    @SerializedName("idArchivoAdjunto")
    private String mIdArchivoAdjunto;
    @SerializedName("idReferencia")
    private String mIdReferencia;
    @SerializedName("idTipoAlta")
    private Long mIdTipoAlta;
    @SerializedName("idTipoAsignacion")
    private int mIdTipoAsignacion;
    @SerializedName("prospecto")
    private Prospecto mProspecto;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("tipoActividad")
    private TipoActividad mTipoActividad;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getComentario() {
        return mComentario;
    }

    public void setComentario(String comentario) {
        mComentario = comentario;
    }

    public String getConfirmarVolumenObra() {
        return mConfirmarVolumenObra;
    }

    public void setConfirmarVolumenObra(String confirmarVolumenObra) {
        mConfirmarVolumenObra = confirmarVolumenObra;
    }

    public int getEstatusAgenda() {
        return mEstatusAgenda;
    }

    public void setEstatusAgenda(int estatusAgenda) {
        mEstatusAgenda = estatusAgenda;
    }

    public Long getFechaAlta() {
        return mFechaAlta;
    }

    public void setFechaAlta(Long fechaAlta) {
        mFechaAlta = fechaAlta;
    }

    public String getFechaHoraCitaFin() {
        return mFechaHoraCitaFin;
    }

    public void setFechaHoraCitaFin(String fechaHoraCitaFin) {
        mFechaHoraCitaFin = fechaHoraCitaFin;
    }

    public String getFechaHoraCitaInicio() {
        return mFechaHoraCitaInicio;
    }

    public void setFechaHoraCitaInicio(String fechaHoraCitaInicio) {
        mFechaHoraCitaInicio = fechaHoraCitaInicio;
    }

    public String getFechaInicioObra() {
        return mFechaInicioObra;
    }

    public void setFechaInicioObra(String fechaInicioObra) {
        mFechaInicioObra = fechaInicioObra;
    }

    public String getIdArchivoAdjunto() {
        return mIdArchivoAdjunto;
    }

    public void setIdArchivoAdjunto(String idArchivoAdjunto) {
        mIdArchivoAdjunto = idArchivoAdjunto;
    }

    public String getIdReferencia() {
        return mIdReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        mIdReferencia = idReferencia;
    }

    public Long getIdTipoAlta() {
        return mIdTipoAlta;
    }

    public void setIdTipoAlta(Long idTipoAlta) {
        mIdTipoAlta = idTipoAlta;
    }

    public int getIdTipoAsignacion() {
        return mIdTipoAsignacion;
    }

    public void setIdTipoAsignacion(int idTipoAsignacion) {
        mIdTipoAsignacion = idTipoAsignacion;
    }

    public Prospecto getProspecto() {
        return mProspecto;
    }

    public void setProspecto(Prospecto prospecto) {
        mProspecto = prospecto;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public TipoActividad getTipoActividad() {
        return mTipoActividad;
    }

    public void setTipoActividad(TipoActividad tipoActividad) {
        mTipoActividad = tipoActividad;
    }

}
