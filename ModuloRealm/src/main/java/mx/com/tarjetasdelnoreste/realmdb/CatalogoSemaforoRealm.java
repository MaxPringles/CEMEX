package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSemaforoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoSemaforoRealm extends MenuRealm {

    public CatalogoSemaforoRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaSemaforo(List<CatalogoSemaforoDB> listaSemaforo) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaSemaforo); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoSemaforoDB> mostrarListaSemaforo() {

        return realm.where(CatalogoSemaforoDB.class).findAll();

    }
}
