package mx.com.tarjetasdelnoreste.realmdb;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.NotificacionFirebaseDB;

/**
 * Created by usr_micro13 on 07/02/2017.
 */

public class NotificacionFirebaseRealm extends MenuRealm {

    public NotificacionFirebaseRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarNotificacionesFirebase(NotificacionFirebaseDB notificacionFirebaseDB) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(notificacionFirebaseDB); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }
}
