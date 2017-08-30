package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCompetidorDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMotivoExclusionDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoCompetidorRealm extends MenuRealm {

    public CatalogoCompetidorRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaCompetidor(List<CatalogoCompetidorDB> listaCompetidor) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaCompetidor); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoCompetidorDB> mostrarListaCompetidor() {

        return realm.where(CatalogoCompetidorDB.class).findAll();

    }
}
