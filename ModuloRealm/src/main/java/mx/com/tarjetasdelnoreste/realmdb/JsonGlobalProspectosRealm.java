package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonGlobalProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.MenuDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;

/**
 * Created by czamora on 11/17/16.
 */

public class JsonGlobalProspectosRealm extends MenuRealm {

    public JsonGlobalProspectosRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA EL JSON GLOBAL DEL PROSPECTO DENTRO DE LA TABLA
     **/
    public static void guardarJsonGlobalProspectos(List<JsonGlobalProspectosDB> listaGlobalProspectos) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaGlobalProspectos); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    public static JsonGlobalProspectosDB mostrarJsonGlobalProspecto(String idProspecto) {

        return realm.where(JsonGlobalProspectosDB.class)
                .equalTo("idProspecto", idProspecto)
                .findFirst();
    }
}
