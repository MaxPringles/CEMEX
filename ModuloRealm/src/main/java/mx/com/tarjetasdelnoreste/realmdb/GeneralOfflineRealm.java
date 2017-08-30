package mx.com.tarjetasdelnoreste.realmdb;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.GeneralOfflineDB;

/**
 * Created by usr_micro13 on 18/01/2017.
 */

public class GeneralOfflineRealm extends MenuRealm {

    public GeneralOfflineRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarGeneralOffline(GeneralOfflineDB generalOfflineDB) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(generalOfflineDB); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

}
