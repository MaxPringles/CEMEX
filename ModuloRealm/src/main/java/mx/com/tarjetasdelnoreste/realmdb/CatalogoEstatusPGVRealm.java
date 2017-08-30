package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstatusPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoOportunidadVentaDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoEstatusPGVRealm extends MenuRealm {

    public CatalogoEstatusPGVRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaEstatusPGV(List<CatalogoEstatusPGVDB> listaEstatusPGV) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaEstatusPGV); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoEstatusPGVDB> mostrarListaEstatusPGV() {

        return realm.where(CatalogoEstatusPGVDB.class).findAll();

    }
}
