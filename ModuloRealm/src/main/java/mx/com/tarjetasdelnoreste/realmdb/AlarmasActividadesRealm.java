package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlarmasActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.AlarmasActividadesDB;

/**
 * Created by usr_micro13 on 14/12/2016.
 */

public class AlarmasActividadesRealm extends MenuRealm {

    public AlarmasActividadesRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaAlarmasActividades(List<AlarmasActividadesDB> alarmasActividadesList) {

        realm.beginTransaction();
        realm.copyToRealmOrUpdate(alarmasActividadesList);
        realm.commitTransaction();
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA
     **/
    public static List<AlarmasActividadesDB> mostrarListaAlarmasActividades() {

        return realm.where(AlarmasActividadesDB.class).findAll();
    }
}
