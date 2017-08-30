package mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ClienteDB extends RealmObject{

    @PrimaryKey
    private String id;

    private String razonSocial;
    private String sitioWeb;
    private String telefono;
    private String nombre;
    private String comentarios;
    private String rfc;
    private int status;

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getSitioweb() {
        return this.sitioWeb;
    }

    public void setSitioweb(String sitioweb) {
        this.sitioWeb = sitioweb;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public String getRfc() {
        return this.rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
