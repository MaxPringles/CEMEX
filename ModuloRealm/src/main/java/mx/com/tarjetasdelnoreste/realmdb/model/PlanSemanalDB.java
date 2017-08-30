package mx.com.tarjetasdelnoreste.realmdb.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by czamora on 9/20/16.
 */
public class PlanSemanalDB extends RealmObject {

    @PrimaryKey
    private String id;
    @Index //Se indexa para poder usar el método "distinct" de Realm.
    private String idProspecto;

    private boolean estaDescartado;
    private int idUsuario;
    private int idActividad;
    private String HoraInicio;
    private String HoraFin;
    private String Descripcion;
    private int idStatus;
    private String comentario;
    private int idTipoProspecto;
    private int idSubSegmentoProspecto;
    private int status;
    //Variable que sólo se usa para manejar las fechas (no es parte del WS).
    private Date HoraInicioSupport;

    //Variables que provienen de la tabla de ProspectosDB.
    private String Obra;
    private String Cliente;
    private String descripcionTipoP;
    @Index //Se indexa para poder usar el método "distinct" de Realm.
    private String descripcionObra;
    private String calle;
    private String Numero;
    private String Colonia;
    private String codigoPostal;
    private String idPais;
    private String idEstado;
    private String idMunicipio;
    private String ComentariosUbicacion;
    private String imagen;
    //Variables de ProspectosDB que se usan para la visualización del mapa.
    private String Latitud;
    private String Longitud;
    private long idEstatusProspecto;
    private String idActividadAnterior;
    private int estatusAgenda;
    private int idPadreActividad;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEstaDescartado() {
        return estaDescartado;
    }

    public void setEstaDescartado(boolean estaDescartado) {
        this.estaDescartado = estaDescartado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getHoraInicio() {
        return HoraInicio;
    }

    public void setHoraInicio(String horaInicio) {
        HoraInicio = horaInicio;
    }

    public String getHoraFin() {
        return HoraFin;
    }

    public void setHoraFin(String horaFin) {
        HoraFin = horaFin;
    }

    public String getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        this.idProspecto = idProspecto;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getIdTipoProspecto() {
        return idTipoProspecto;
    }

    public void setIdTipoProspecto(int idTipoProspecto) {
        this.idTipoProspecto = idTipoProspecto;
    }

    public int getIdSubSegmentoProspecto() {
        return idSubSegmentoProspecto;
    }

    public void setIdSubSegmentoProspecto(int idSubSegmentoProspecto) {
        this.idSubSegmentoProspecto = idSubSegmentoProspecto;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getHoraInicioSupport() {
        return HoraInicioSupport;
    }

    public void setHoraInicioSupport(Date horaInicioSupport) {
        HoraInicioSupport = horaInicioSupport;
    }

    public String getObra() {
        return Obra;
    }

    public void setObra(String obra) {
        Obra = obra;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getDescripcionTipoP() {
        return descripcionTipoP;
    }

    public void setDescripcionTipoP(String descripcionTipoP) {
        this.descripcionTipoP = descripcionTipoP;
    }

    public String getDescripcionObra() {
        return descripcionObra;
    }

    public void setDescripcionObra(String descripcionObra) {
        this.descripcionObra = descripcionObra;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public String getColonia() {
        return Colonia;
    }

    public void setColonia(String colonia) {
        Colonia = colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    public String getComentariosUbicacion() {
        return ComentariosUbicacion;
    }

    public void setComentariosUbicacion(String comentariosUbicacion) {
        ComentariosUbicacion = comentariosUbicacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public long getIdEstatusProspecto() {
        return idEstatusProspecto;
    }

    public void setIdEstatusProspecto(long idEstatusProspecto) {
        this.idEstatusProspecto = idEstatusProspecto;
    }

    public String getIdActividadAnterior() {
        return idActividadAnterior;
    }

    public void setIdActividadAnterior(String idActividadAnterior) {
        this.idActividadAnterior = idActividadAnterior;
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
}
