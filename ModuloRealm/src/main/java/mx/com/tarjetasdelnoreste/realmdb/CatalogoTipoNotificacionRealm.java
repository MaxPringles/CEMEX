package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCargoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoNotificacionDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoTipoNotificacionRealm extends MenuRealm {

    public CatalogoTipoNotificacionRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaTipoNotificacion(List<CatalogoTipoNotificacionDB> listaTipoNotificacion) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaTipoNotificacion); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoTipoNotificacionDB> mostrarListaTipoNotificacion() {
        return realm.where(CatalogoTipoNotificacionDB.class).findAll();

    }
}
