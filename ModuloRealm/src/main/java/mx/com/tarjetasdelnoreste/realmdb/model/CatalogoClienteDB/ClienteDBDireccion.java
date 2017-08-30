package mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB;

import io.realm.RealmObject;

public class ClienteDBDireccion  extends RealmObject {
    private ClienteDBDireccionEstado estado;
    private ClienteDBDireccionUbicacion ubicacion;
    private String numero;
    private String numeroInterior;
    private String codigoPostal;
    private ClienteDBDireccionMunicipio municipio;
    private String calle;
    private String comentarios;
    private String colonia;
    private ClienteDBDireccionPais pais;

    public ClienteDBDireccionEstado getEstado() {
        return this.estado;
    }

    public void setEstado(ClienteDBDireccionEstado estado) {
        this.estado = estado;
    }

    public ClienteDBDireccionUbicacion getUbicacion() {
        return this.ubicacion;
    }

    public void setUbicacion(ClienteDBDireccionUbicacion ubicacion) {
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

    public ClienteDBDireccionMunicipio getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(ClienteDBDireccionMunicipio municipio) {
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

    public ClienteDBDireccionPais getPais() {
        return this.pais;
    }

    public void setPais(ClienteDBDireccionPais pais) {
        this.pais = pais;
    }
}
