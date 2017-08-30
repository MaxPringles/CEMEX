package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstadosDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoEstadosRealm extends MenuRealm {

    public CatalogoEstadosRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaEstados(List<CatalogoEstadosDB> listaEstados) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaEstados); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE GUARDA DATOS INDIVIDUALMENTE **/
    public static void guardarObjetoEstado(final Long id, final int idCatalogo, final String Descripcion, final int idPadre){

        //CatalogoEstadosDB catalogoEstadosDB = realm.createObject(CatalogoEstadosDB.class);
        CatalogoEstadosDB catalogoEstadosDB = new CatalogoEstadosDB();
        catalogoEstadosDB.setId(id);
        catalogoEstadosDB.setIdCatalogo(idCatalogo);
        catalogoEstadosDB.setDescripcion(Descripcion);
        catalogoEstadosDB.setIdPadre(idPadre);

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(catalogoEstadosDB); //Inserta el objeto en la DB.
        realm.commitTransaction(); //Cierra la transacción.

    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoEstadosDB> mostrarListaEstados() {

        return realm.where(CatalogoEstadosDB.class).findAll();

    }
}
