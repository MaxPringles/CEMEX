package com.telstock.tmanager.cemex.prospectos.model;

import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USRMICRO10 on 05/09/2016.
 */
public class InformacionGeneral implements Serializable {

    @SerializedName("Obra")
    private String Obra;

    @SerializedName("Cliente")
    private String Cliente;

    @SerializedName("idEstatusObra")
    private int idEstatusObra;

    @SerializedName("descripcionObra")
    private String descripcionObra;

    @SerializedName("numeroDeRegistro")
    private String numeroDeRegistro;

    @SerializedName("idTipoProspecto")
    private int idTipoProspecto;

    @SerializedName("descripcionTipoP")
    private String descripcionTipoP;

    @SerializedName("idStatus")
    private int idStatus;

    @SerializedName("descriptcionStatus")
    private String descriptcionStatus;

    @SerializedName("campania")
    private String campania;

    @SerializedName("comentarios")
    private String comentarios;

    @SerializedName("RazonSocial")
    private String RazonSocial;

    @SerializedName("RFC")
    private String RFC;

    @SerializedName("Telefono")
    private String Telefono;

    @SerializedName("calle")
    private String calle;

    @SerializedName("Numero")
    private String Numero;

    @SerializedName("Colonia")
    private String Colonia;

    @SerializedName("codigoPostal")
    private String codigoPostal;

    @SerializedName("idPais")
    private int idPais;

    @SerializedName("idEstado")
    private int idEstado;

    @SerializedName("idMunicipio")
    private int idMunicipio;

    @SerializedName("Latitud")
    private String Latitud;

    @SerializedName("Longitud")
    private String Longitud;

    @SerializedName("ComentariosUbicacion")
    private String ComentariosUbicacion;

    @SerializedName("imagen")
    private String imagen;

    @SerializedName("status")
    private int status;

    @SerializedName("idPlataforma")
    private int idPlataforma;

    @SerializedName("idSubsegmento")
    private int idSubsegmento;

    @SerializedName("prdAdqr")
    private ArrayList<ProductosSeleccionados> prdAdqr;

    @SerializedName("srvAdqr")
    private ArrayList<ServiciosSeleccionados> srvAdqr;

    @SerializedName("contactos")
    private ArrayList<InformacionContacto> contactos;

    public InformacionGeneral() {

    }

    public InformacionGeneral(String Obra, String Cliente, int idEstatusObra, String descripcionObra,
                              String numeroDeRegistro, int idTipoProspecto, String descripcionTipoP,
                              int idStatus, String descriptcionStatus, String campania, String comentarios,
                              String RazonSocial, String RFC, String Telefono, String calle, String Numero,
                              String Colonia, String codigoPostal, int idPais, int idEstado, int idMunicipio,
                              String Latitud, String Longitud, String ComentariosUbicacion, String imagen,
                              int status, int idPlataforma, int idSubsegmento) {

        this.Obra = Obra;
        this.Cliente = Cliente;
        this.idEstatusObra = idEstatusObra;
        this.descripcionObra = descripcionObra;
        this.numeroDeRegistro = numeroDeRegistro;
        this.idTipoProspecto = idTipoProspecto;
        this.descripcionObra = descripcionTipoP;
        this.idStatus = idStatus;
        this.descriptcionStatus = descriptcionStatus;
        this.campania = campania;
        this.comentarios = comentarios;
        this.RazonSocial = RazonSocial;
        this.RFC = RFC;
        this.Telefono = Telefono;
        this.calle = calle;
        this.Numero = Numero;
        this.Colonia = Colonia;
        this.codigoPostal = codigoPostal;
        this.idPais = idPais;
        this.idEstado = idEstado;
        this.idMunicipio = idMunicipio;
        this.Latitud = Latitud;
        this.Longitud = Longitud;
        this.ComentariosUbicacion = ComentariosUbicacion;
        this.imagen = imagen;
        this.status = status;
        this.idPlataforma = idPlataforma;
        this.idSubsegmento = idSubsegmento;

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

    public int getIdEstatusObra() {
        return idEstatusObra;
    }

    public void setIdEstatusObra(int idEstatusObra) {
        this.idEstatusObra = idEstatusObra;
    }

    public String getDescripcionObra() {
        return descripcionObra;
    }

    public void setDescripcionObra(String descripcionObra) {
        this.descripcionObra = descripcionObra;
    }

    public String getNumeroDeRegistro() {
        return numeroDeRegistro;
    }

    public void setNumeroDeRegistro(String numeroDeRegistro) {
        this.numeroDeRegistro = numeroDeRegistro;
    }

    public int getIdTipoProspecto() {
        return idTipoProspecto;
    }

    public void setIdTipoProspecto(int idTipoProspecto) {
        this.idTipoProspecto = idTipoProspecto;
    }

    public String getDescripcionTipoP() {
        return descripcionTipoP;
    }

    public void setDescripcionTipoP(String descripcionTipoP) {
        this.descripcionTipoP = descripcionTipoP;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public String getDescriptcionStatus() {
        return descriptcionStatus;
    }

    public void setDescriptcionStatus(String descriptcionStatus) {
        this.descriptcionStatus = descriptcionStatus;
    }

    public String getCampania() {
        return campania;
    }

    public void setCampania(String campania) {
        this.campania = campania;
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

    public int getIdPais() {
        return idPais;
    }

    public void setIdPais(int idPais) {
        this.idPais = idPais;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.idMunicipio = idMunicipio;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIdPlataforma() {
        return idPlataforma;
    }

    public void setIdPlataforma(int idPlataforma) {
        this.idPlataforma = idPlataforma;
    }

    public int getIdSubsegmento() {
        return idSubsegmento;
    }

    public void setIdSubsegmento(int idSubsegmento) {
        this.idSubsegmento = idSubsegmento;
    }

    public ArrayList<ProductosSeleccionados> getPrdAdqr() {
        return prdAdqr;
    }

    public void setPrdAdqr(ArrayList<ProductosSeleccionados> prdAdqr) {
        this.prdAdqr = prdAdqr;
    }

    public ArrayList<ServiciosSeleccionados> getSrvAdqr() {
        return srvAdqr;
    }

    public void setSrvAdqr(ArrayList<ServiciosSeleccionados> srvAdqr) {
        this.srvAdqr = srvAdqr;
    }

    public ArrayList<InformacionContacto> getContactos() {
        return contactos;
    }

    public void setContactos(ArrayList<InformacionContacto> contactos) {
        this.contactos = contactos;
    }
}
