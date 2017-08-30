package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;

import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoSubsegmentosRealm extends MenuRealm {

    public CatalogoSubsegmentosRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaSubsegmentos(List<CatalogoSubsegmentosDB> listaSubsegmentos) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaSubsegmentos); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoSubsegmentosDB> mostrarListaSubsegmentos() {

        return realm.where(CatalogoSubsegmentosDB.class).findAll();

    }

    public static void cambiarEstatusSeleccionado(CatalogoSubsegmentosDB catalogoSubsegmentosDB) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(catalogoSubsegmentosDB);
        realm.commitTransaction();
    }

    public static String obtenerNombreSubsegmento(String id) {

        return realm.where(CatalogoSubsegmentosDB.class).
                equalTo("id", Integer.parseInt(id)).findFirst().getDescripcion();
    }
}
