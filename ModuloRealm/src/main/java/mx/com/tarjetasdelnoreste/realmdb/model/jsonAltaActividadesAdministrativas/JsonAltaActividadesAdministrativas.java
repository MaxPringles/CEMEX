
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;

@SuppressWarnings("unused")
public class JsonAltaActividadesAdministrativas {

    @SerializedName("fechaHoraCitaFin")
    private String mFechaHoraCitaFin;
    @SerializedName("fechaHoraCitaInicio")
    private String mFechaHoraCitaInicio;
    @SerializedName("tipoActividad")
    private CatalogoActividadesPGVDB mTipoActividad;
    @SerializedName("comentario")
    private String mComentarios;
    @SerializedName("idAltaOffline")
    private String idAltaOffline;
    //Variable que sólo se usa para manejar las fechas (no es parte del WS).
    private Date HoraInicioSupport;

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

    public CatalogoActividadesPGVDB getTipoActividad() {
        return mTipoActividad;
    }

    public String getComentarios() {
        return mComentarios;
    }

    public void setComentarios(String comentarios) {
        this.mComentarios = comentarios;
    }

    public void setTipoActividad(CatalogoActividadesPGVDB tipoActividad) {
        mTipoActividad = tipoActividad;
    }

    public Date getHoraInicioSupport() {
        return HoraInicioSupport;
    }

    public void setHoraInicioSupport(Date horaInicioSupport) {
        HoraInicioSupport = horaInicioSupport;
    }

    public String getIdAltaOffline() {
        return idAltaOffline;
    }

    public void setIdAltaOffline(String idAltaOffline) {
        this.idAltaOffline = idAltaOffline;
    }
}
