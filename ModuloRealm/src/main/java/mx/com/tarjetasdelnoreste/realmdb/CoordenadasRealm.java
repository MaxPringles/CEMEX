package mx.com.tarjetasdelnoreste.realmdb;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMunicipiosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CoordenadasDB;

/**
 * Created by czamora on 11/7/16.
 */
public class CoordenadasRealm extends MenuRealm {

    public CoordenadasRealm(Realm realm) {
        super(realm);
    }

    public static void guardarListaCoordenadas(CoordenadasDB coordenadasDB) {

        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(coordenadasDB);
            realm.commitTransaction();
        } catch (Exception e) {
            Log.e("ERROR_COORDENADAS", e.toString());
        }
    }

    /** MÉTODO QUE DEVUELVE LA LISTA DE MUNICIPIOS DE ACUERDO AL idPadre **/
    public static List<CoordenadasDB> mostrarListaCoordenadas(int estatus) {

        return realm.where(CoordenadasDB.class)
                .equalTo("enviado", estatus)
                .findAll();

    }

    public static void cambiarEstatus (CoordenadasDB coordenadasDB) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(coordenadasDB);
        realm.commitTransaction();
    }
}
