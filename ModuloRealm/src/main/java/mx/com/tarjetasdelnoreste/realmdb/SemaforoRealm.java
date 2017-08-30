package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.SemaforoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonSemaforo.Reporte;

/**
 * Created by usr_micro13 on 25/01/2017.
 */

public class SemaforoRealm extends MenuRealm {

    public SemaforoRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaSemaforo(List<SemaforoDB> semaforoDBList) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(semaforoDBList); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    public static List<SemaforoDB> mostrarListaSemaforo() {

        return realm.where(SemaforoDB.class).findAll();
    }

    public static SemaforoDB mostrarSemaforoIdPaso(int idPaso) {

        return realm.where(SemaforoDB.class)
                .equalTo("idPaso", idPaso)
                .findFirst();

    }


}
