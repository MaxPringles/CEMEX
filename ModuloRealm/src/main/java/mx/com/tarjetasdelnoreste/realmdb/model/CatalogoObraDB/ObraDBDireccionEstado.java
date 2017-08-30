package mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB;

import io.realm.RealmObject;

public class ObraDBDireccionEstado  extends RealmObject {
    private int id;
    private String nombre;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
