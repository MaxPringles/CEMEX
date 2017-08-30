package mx.com.tarjetasdelnoreste.realmdb.model;


/**
 * Created by czamora on 10/25/16.
 */
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProspectosDB extends RealmObject {

    @PrimaryKey
    private String id;

    private long numeroRegistro;
    private boolean estaDescartado;
    private String fotografia;
    private long idEstatusObraProspecto;
    private String descripcionEstatusObraProspecto;
    private long idSubsegmento;
    private String descripcionIdSubsegmento;
    private long idTipoProspecto;
    private String descripcionTipoProspecto;
    private String descripcionActividad;
    private long idActividad;
    private String idActividadAnterior;
    private int estatusAgenda;

    private long idEstatusProspecto;
    private String descripcionEstatusProspecto;

    private String Obra;
    private String Cliente;

    private long idCampania;
    private String descripcionCampania;

    private String comentarios;
    private String RazonSocial;
    private String RFC;
    private String Telefono;
    private String calle;
    private String Numero;
    private String Colonia;
    private String codigoPostal;
    private String nombrePais;
    private String nombreEstado;
    private String nombreMunicipio;
    private String comentariosUbicacion;
    private String Latitud;
    private String Longitud;
    private int fechaAltaProspecto;
    /*****************************/
    private RealmList<ActividadesDB> actividadesDBRealmList;
    private RealmList<ArchivosAltaDB> archivosAltaDBRealmList;

    public ProspectosDB() {

    }

    public ProspectosDB(String id, String descripcionTipoProspecto, String Obra, String Cliente, String descripcionActividad,
                           int estatusAgenda, String fotografia, boolean estaDescartado, String calle, String Numero,
                           String Colonia, String codigoPostal, String nombrePais,
                           String nombreEstado, String nombreMunicipio, String comentariosUbicacion,
                           String Latitud, String Longitud) {
        this.id = id;
        this.descripcionTipoProspecto = descripcionTipoProspecto;
        this.Obra = Obra;
        this.Cliente = Cliente;
        this.descripcionActividad = descripcionActividad;
        this.estatusAgenda = estatusAgenda;
        this.fotografia = fotografia;
        this.estaDescartado = estaDescartado;
        this.calle = calle;
        this.Numero = Numero;
        this.Colonia = Colonia;
        this.codigoPostal = codigoPostal;
        this.nombrePais = nombrePais;
        this.nombreEstado = nombreEstado;
        this.nombreMunicipio = nombreMunicipio;
        this.comentariosUbicacion = comentariosUbicacion;
        this.Latitud = Latitud;
        this.Longitud = Longitud;
    }

    //Método que devuelve los textos e imágenes.
    public static ProspectosDB[] getAllPaintings(List<ProspectosDB> items) {

        int size = items.size();
        ProspectosDB[] paintings = new ProspectosDB[size];

        for (int i = 0; i < size; i++) {
            paintings[i] = new ProspectosDB(
                    items.get(i).getId(),
                    items.get(i).getDescripcionTipoProspecto(),
                    items.get(i).getObra(),
                    items.get(i).getCliente(),
                    items.get(i).getDescripcionActividad(),
                    items.get(i).getEstatusAgenda(),
                    items.get(i).getFotografia(),
                    items.get(i).isEstaDescartado(),
                    items.get(i).getCalle(),
                    items.get(i).getNumero(),
                    items.get(i).getColonia(),
                    items.get(i).getCodigoPostal(),
                    items.get(i).getNombrePais(),
                    items.get(i).getNombreEstado(),
                    items.get(i).getNombreMunicipio(),
                    items.get(i).getComentariosUbicacion(),
                    items.get(i).getLatitud(),
                    items.get(i).getLongitud());
        }

        return paintings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(long numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public boolean isEstaDescartado() {
        return estaDescartado;
    }

    public void setEstaDescartado(boolean estaDescartado) {
        this.estaDescartado = estaDescartado;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public long getIdEstatusObraProspecto() {
        return idEstatusObraProspecto;
    }

    public void setIdEstatusObraProspecto(long idEstatusObraProspecto) {
        this.idEstatusObraProspecto = idEstatusObraProspecto;
    }

    public String getDescripcionEstatusObraProspecto() {
        return descripcionEstatusObraProspecto;
    }

    public void setDescripcionEstatusObraProspecto(String descripcionEstatusObraProspecto) {
        this.descripcionEstatusObraProspecto = descripcionEstatusObraProspecto;
    }

    public long getIdSubsegmento() {
        return idSubsegmento;
    }

    public void setIdSubsegmento(long idSubsegmento) {
        this.idSubsegmento = idSubsegmento;
    }

    public String getDescripcionIdSubsegmento() {
        return descripcionIdSubsegmento;
    }

    public void setDescripcionIdSubsegmento(String descripcionIdSubsegmento) {
        this.descripcionIdSubsegmento = descripcionIdSubsegmento;
    }

    public long getIdTipoProspecto() {
        return idTipoProspecto;
    }

    public void setIdTipoProspecto(long idTipoProspecto) {
        this.idTipoProspecto = idTipoProspecto;
    }

    public String getDescripcionTipoProspecto() {
        return descripcionTipoProspecto;
    }

    public void setDescripcionTipoProspecto(String descripcionTipoProspecto) {
        this.descripcionTipoProspecto = descripcionTipoProspecto;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }

    public long getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(long idActividad) {
        this.idActividad = idActividad;
    }

    public int getEstatusAgenda() {
        return estatusAgenda;
    }

    public void setEstatusAgenda(int estatusAgenda) {
        this.estatusAgenda = estatusAgenda;
    }

    public long getIdEstatusProspecto() {
        return idEstatusProspecto;
    }

    public void setIdEstatusProspecto(long idEstatusProspecto) {
        this.idEstatusProspecto = idEstatusProspecto;
    }

    public String getDescripcionEstatusProspecto() {
        return descripcionEstatusProspecto;
    }

    public void setDescripcionEstatusProspecto(String descripcionEstatusProspecto) {
        this.descripcionEstatusProspecto = descripcionEstatusProspecto;
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

    public long getIdCampania() {
        return idCampania;
    }

    public void setIdCampania(long idCampania) {
        this.idCampania = idCampania;
    }

    public String getDescripcionCampania() {
        return descripcionCampania;
    }

    public void setDescripcionCampania(String descripcionCampania) {
        this.descripcionCampania = descripcionCampania;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getRazonSocial() {
        return RazonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        RazonSocial = razonSocial;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
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

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getComentariosUbicacion() {
        return comentariosUbicacion;
    }

    public void setComentariosUbicacion(String comentariosUbicacion) {
        this.comentariosUbicacion = comentariosUbicacion;
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

    public RealmList<ActividadesDB> getActividadesDBRealmList() {
        return actividadesDBRealmList;
    }

    public void setActividadesDBRealmList(RealmList<ActividadesDB> actividadesDBRealmList) {
        this.actividadesDBRealmList = actividadesDBRealmList;
    }

    public RealmList<ArchivosAltaDB> getArchivosAltaDBRealmList() {
        return archivosAltaDBRealmList;
    }

    public void setArchivosAltaDBRealmList(RealmList<ArchivosAltaDB> archivosAltaDBRealmList) {
        this.archivosAltaDBRealmList = archivosAltaDBRealmList;
    }

    public String getIdActividadAnterior() {
        return idActividadAnterior;
    }

    public void setIdActividadAnterior(String idActividadAnterior) {
        this.idActividadAnterior = idActividadAnterior;
    }

    public int getFechaAltaProspecto() {
        return fechaAltaProspecto;
    }

    public void setFechaAltaProspecto(int fechaAltaProspecto) {
        this.fechaAltaProspecto = fechaAltaProspecto;
    }
}
