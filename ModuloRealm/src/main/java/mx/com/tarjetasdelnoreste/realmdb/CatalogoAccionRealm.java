package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoAccionDB;

import static mx.com.tarjetasdelnoreste.realmdb.MenuRealm.realm;


/**
 * Created by usr_micro13 on 26/04/2017.
 */

public class CatalogoAccionRealm  extends MenuRealm{

    public CatalogoAccionRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaAccion(List<CatalogoAccionDB> listaAccion) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaAccion); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE GUARDA DATOS INDIVIDUALMENTE **/
    public static void guardarObjetoAccion(final Long id, final int idCatalogo, final String Descripcion, final int idPadre){


        CatalogoAccionDB catalogoAccionDB = new CatalogoAccionDB();
        catalogoAccionDB.setId(id);
        catalogoAccionDB.setIdCatalogo(idCatalogo);
        catalogoAccionDB.setDescripcion(Descripcion);
        catalogoAccionDB.setIdPadre(idPadre);

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(catalogoAccionDB); //Inserta el objeto en la DB.
        realm.commitTransaction(); //Cierra la transacción.

    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoAccionDB> mostrarListaAccion() {

        return realm.where(CatalogoAccionDB.class).findAll();

    }
}
