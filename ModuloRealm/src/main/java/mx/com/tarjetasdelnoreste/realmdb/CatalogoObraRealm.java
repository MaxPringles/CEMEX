package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoObraRealm extends MenuRealm {

    public CatalogoObraRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaObra(List<ObraDB> listaObra) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaObra); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<ObraDB> mostrarListaObraIdCliente(String idCliente) {

        return realm.where(ObraDB.class)
                .equalTo("idCliente", idCliente)
                .findAll();
    }

    /**
     * MÉTODO QUE ELIMINA LA TABLA
     **/
    public static void eliminarTablaObra() {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.delete(ObraDB.class); //Borra los registros de la tabla.
        realm.commitTransaction(); //Cierra la transacción.
    }
}
