package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCargoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoObraDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoCargoRealm extends MenuRealm {

    public CatalogoCargoRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaCargo(List<CatalogoCargoDB> listaCargo) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaCargo); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoCargoDB> mostrarListaCargo() {

        return realm.where(CatalogoCargoDB.class).findAll();

    }
}
