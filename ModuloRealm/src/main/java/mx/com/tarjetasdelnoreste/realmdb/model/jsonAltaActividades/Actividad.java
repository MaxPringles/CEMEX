package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;

public class Actividad {

    @SerializedName("comentario")
    private String mComentarios;
    @SerializedName("confirmarVolumenObra")
    private String mConfirmarVolumenObra;
    @SerializedName("fechaHoraCitaFin")
    private String mFechaHoraCitaFin;
    @SerializedName("fechaHoraCitaInicio")
    private String mFechaHoraCitaInicio;
    @SerializedName("fechaInicioObra")
    private String mFechaInicioObra;
    @SerializedName("idArchivoAdjunto")
    private String mIdArchivoAdjunto;
    @SerializedName("tipoActividad")
    private TipoActividad mTipoActividad;
    @SerializedName("estatusAgenda")
    private int mEstatusAgenda;
    @SerializedName("idAltaOffline")
    private String idAltaOffline;
    //Variable que sólo se usa para manejar las fechas (no es parte del WS).
    private Date HoraInicioSupport;

    public String getComentarios() {
        return mComentarios;
    }

    public void setComentarios(String comentarios) {
        mComentarios = comentarios;
    }

    public String getConfirmarVolumenObra() {
        return mConfirmarVolumenObra;
    }

    public void setConfirmarVolumenObra(String confirmarVolumenObra) {
        mConfirmarVolumenObra = confirmarVolumenObra;
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

    public TipoActividad getTipoActividad() {
        return mTipoActividad;
    }

    public void setTipoActividad(TipoActividad mTipoActividad) {
        this.mTipoActividad = mTipoActividad;
    }

    public Date getHoraInicioSupport() {
        return HoraInicioSupport;
    }

    public void setHoraInicioSupport(Date horaInicioSupport) {
        HoraInicioSupport = horaInicioSupport;
    }

    public int getmEstatusAgenda() {
        return mEstatusAgenda;
    }

    public void setmEstatusAgenda(int mEstatusAgenda) {
        this.mEstatusAgenda = mEstatusAgenda;
    }

    public String getIdAltaOffline() {
        return idAltaOffline;
    }

    public void setIdAltaOffline(String idAltaOffline) {
        this.idAltaOffline = idAltaOffline;
    }
}
