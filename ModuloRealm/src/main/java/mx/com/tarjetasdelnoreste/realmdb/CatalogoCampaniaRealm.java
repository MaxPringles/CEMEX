package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCampaniaDB;

/**
 * Created by czamora on 11/11/16.
 */

public class CatalogoCampaniaRealm extends MenuRealm {

    public CatalogoCampaniaRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaCampania(List<CatalogoCampaniaDB> listaTipoProspecto) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaTipoProspecto); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoCampaniaDB> mostrarListaCampania() {

        return realm.where(CatalogoCampaniaDB.class).findAll();

    }

    public static String mostrarNombreCampania(int idCampania) {
        return realm.where(CatalogoCampaniaDB.class)
                .equalTo("id", idCampania)
                .findFirst().getDescripcion();
    }
}
