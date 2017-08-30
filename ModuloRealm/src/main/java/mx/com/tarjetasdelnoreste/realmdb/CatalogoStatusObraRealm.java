package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMunicipiosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoStatusObraRealm extends MenuRealm {

    public CatalogoStatusObraRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaStatusObra(List<CatalogoStatusObraDB> listaStatusObra) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaStatusObra); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoStatusObraDB> mostrarListaStatusObra() {

        return realm.where(CatalogoStatusObraDB.class).findAll();

    }

    public static String mostrarNombreEstatusObra(int idEstatusObra) {
        return realm.where(CatalogoStatusObraDB.class)
                .equalTo("id", idEstatusObra)
                .findFirst().getDescripcion();
    }
}
