package mx.com.tarjetasdelnoreste.realmdb.model;

/**
 * Created by czamora on 10/27/16.
 */
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ActividadesDB extends RealmObject {

    @PrimaryKey
    private String compoundId;

    private long idTipoActividad;
    private String descripcionActividad;
    private String fechaHoraCitaInicio;
    private String fechaHoraCitaFin;
    private String comentarios;
    private String fechaInicioObra;
    private String confirmarVolumenObra;
    private String idArchivoAdjunto;
    private String idActividad;
    private int estatusAgenda;
    private int idPadreActividad;
    private String idProspecto;

    public String getCompoundId() {
        return compoundId;
    }

    public void setCompoundId(String compoundId) {
        this.compoundId = compoundId;
    }

    public long getIdTipoActividad() {
        return idTipoActividad;
    }

    public void setIdTipoActividad(long idTipoActividad) {
        this.idTipoActividad = idTipoActividad;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public String getFechaHoraCitaInicio() {
        return fechaHoraCitaInicio;
    }

    public void setFechaHoraCitaInicio(String fechaHoraCitaInicio) {
        this.fechaHoraCitaInicio = fechaHoraCitaInicio;
    }

    public String getFechaHoraCitaFin() {
        return fechaHoraCitaFin;
    }

    public void setFechaHoraCitaFin(String fechaHoraCitaFin) {
        this.fechaHoraCitaFin = fechaHoraCitaFin;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getFechaInicioObra() {
        return fechaInicioObra;
    }

    public void setFechaInicioObra(String fechaInicioObra) {
        this.fechaInicioObra = fechaInicioObra;
    }

    public String getConfirmarVolumenObra() {
        return confirmarVolumenObra;
    }

    public void setConfirmarVolumenObra(String confirmarVolumenObra) {
        this.confirmarVolumenObra = confirmarVolumenObra;
    }

    public String getIdArchivoAdjunto() {
        return idArchivoAdjunto;
    }

    public void setIdArchivoAdjunto(String idArchivoAdjunto) {
        this.idArchivoAdjunto = idArchivoAdjunto;
    }

    public String getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(String idActividad) {
        this.idActividad = idActividad;
    }

    public int getEstatusAgenda() {
        return estatusAgenda;
    }

    public void setEstatusAgenda(int estatusAgenda) {
        this.estatusAgenda = estatusAgenda;
    }

    public int getIdPadreActividad() {
        return idPadreActividad;
    }

    public void setIdPadreActividad(int idPadreActividad) {
        this.idPadreActividad = idPadreActividad;
    }

    public String getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        this.idProspecto = idProspecto;
    }
}
