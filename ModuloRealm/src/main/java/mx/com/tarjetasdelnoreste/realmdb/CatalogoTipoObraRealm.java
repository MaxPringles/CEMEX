package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstatusPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoObraDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoTipoObraRealm extends MenuRealm {

    public CatalogoTipoObraRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaTipoObra(List<CatalogoTipoObraDB> listaTipoObra) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaTipoObra); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoTipoObraDB> mostrarListaTipoObra() {

        return realm.where(CatalogoTipoObraDB.class).findAll();

    }

    public static String mostrarNombreTipoObra(int idTipoObra) {
        return realm.where(CatalogoTipoObraDB.class)
                .equalTo("id", idTipoObra)
                .findFirst().getDescripcion();
    }
}
