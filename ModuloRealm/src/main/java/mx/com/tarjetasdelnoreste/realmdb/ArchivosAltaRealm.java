package mx.com.tarjetasdelnoreste.realmdb;

/**
 * Created by czamora on 11/22/16.
 */

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;

public class ArchivosAltaRealm extends MenuRealm {

    public ArchivosAltaRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaEstados(List<ArchivosAltaDB> listaArchivos) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaArchivos); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<ArchivosAltaDB> mostrarListaArchivos(String idProspecto) {

        return realm.where(ArchivosAltaDB.class)
                .equalTo("idProspecto", idProspecto)
                .findAll();
    }
}