package mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB;

import io.realm.RealmObject;

public class ObraDBDireccion  extends RealmObject {
    private ObraDBDireccionEstado estado;
    private ObraDBDireccionUbicacion ubicacion;
    private String numero;
    private String numeroInterior;
    private String codigoPostal;
    private ObraDBDireccionMunicipio municipio;
    private String calle;
    private String comentarios;
    private String colonia;
    private ObraDBDireccionPais pais;

    public ObraDBDireccionEstado getEstado() {
        return this.estado;
    }

    public void setEstado(ObraDBDireccionEstado estado) {
        this.estado = estado;
    }

    public ObraDBDireccionUbicacion getUbicacion() {
        return this.ubicacion;
    }

    public void setUbicacion(ObraDBDireccionUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getCodigoPostal() {
        return this.codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public ObraDBDireccionMunicipio getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(ObraDBDireccionMunicipio municipio) {
        this.municipio = municipio;
    }

    public String getCalle() {
        return this.calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getColonia() {
        return this.colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public ObraDBDireccionPais getPais() {
        return this.pais;
    }

    public void setPais(ObraDBDireccionPais pais) {
        this.pais = pais;
    }
}
