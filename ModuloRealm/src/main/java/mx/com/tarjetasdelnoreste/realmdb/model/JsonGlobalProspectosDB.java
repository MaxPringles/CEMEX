package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by czamora on 11/17/16.
 */

public class JsonGlobalProspectosDB extends RealmObject {

    @PrimaryKey
    private String idProspecto;

    private String jsonGlobalProspectos;

    public String getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        this.idProspecto = idProspecto;
    }

    public String getJsonGlobalProspectos() {
        return jsonGlobalProspectos;
    }

    public void setJsonGlobalProspectos(String jsonGlobalProspectos) {
        this.jsonGlobalProspectos = jsonGlobalProspectos;
    }
}
