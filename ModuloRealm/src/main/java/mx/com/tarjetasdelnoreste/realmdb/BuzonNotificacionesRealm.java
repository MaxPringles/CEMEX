package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.BuzonNotificacionesDB;

/**
 * Created by usr_micro13 on 03/02/2017.
 */

public class BuzonNotificacionesRealm extends MenuRealm {

    public BuzonNotificacionesRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaBuzonNotificaciones(List<BuzonNotificacionesDB> buzonNotificacionesDBList) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(buzonNotificacionesDBList); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    public static List<BuzonNotificacionesDB> mostrarListaBuzonNotificacionesTodas() {

        return realm.where(BuzonNotificacionesDB.class)
                .findAll();
    }

    public static List<BuzonNotificacionesDB> mostrarListaBuzonNotificacionesLeidas() {

        return realm.where(BuzonNotificacionesDB.class)
                .equalTo("mEstaLeido", true)
                .findAll();
    }
}
