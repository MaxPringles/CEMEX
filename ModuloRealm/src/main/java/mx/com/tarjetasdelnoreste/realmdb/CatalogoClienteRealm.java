package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoClienteRealm extends MenuRealm {

    public CatalogoClienteRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaCliente(List<ClienteDB> listaCliente) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaCliente); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<ClienteDB> mostrarListaCliente() {

        return realm.where(ClienteDB.class).findAll();

    }

    /**
     * MÉTODO QUE ELIMINA LA TABLA
     **/
    public static void eliminarTablaCliente() {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.delete(ClienteDB.class); //Borra los registros de la tabla.
        realm.commitTransaction(); //Cierra la transacción.
    }
}
