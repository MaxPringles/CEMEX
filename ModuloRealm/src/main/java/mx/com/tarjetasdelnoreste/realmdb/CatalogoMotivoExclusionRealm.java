package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMotivoExclusionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSemaforoDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoMotivoExclusionRealm extends MenuRealm {

    public CatalogoMotivoExclusionRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaMotivoExclusion(List<CatalogoMotivoExclusionDB> listaMotivoExclusion) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaMotivoExclusion); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoMotivoExclusionDB> mostrarListaMotivoExclusion() {

        return realm.where(CatalogoMotivoExclusionDB.class).findAll();

    }
}
