package mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB;

import io.realm.RealmObject;

public class ClienteDBDireccionUbicacion  extends RealmObject {
    private String latitud;
    private String longitud;

    public String getLatitud() {
        return this.latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return this.longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
