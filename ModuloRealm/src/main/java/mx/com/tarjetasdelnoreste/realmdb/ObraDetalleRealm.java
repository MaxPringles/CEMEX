package mx.com.tarjetasdelnoreste.realmdb;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.ObraDetalleDB;

/**
 * Created by movil4 on 30/12/16.
 */

public class ObraDetalleRealm extends MenuRealm {

    public ObraDetalleRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UN OBJETO DENTRO DE LA TABLA
     **/
    public static void guardarObraDetalle(ObraDetalleDB obraDetalleDB) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(obraDetalleDB); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE GUARDA UN OBJETO DENTRO DE LA TABLA
     **/
    public static ObraDetalleDB mostrarObraDetalle(String idObra) {

        return realm.where(ObraDetalleDB.class)
                .equalTo("mId", idObra).findFirst();
    }


}
