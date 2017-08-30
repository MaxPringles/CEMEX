package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.ActividadesDB;

/**
 * Created by czamora on 10/26/16.
 */
public class ActividadesRealm extends MenuRealm {

    public ActividadesRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaActividades(List<ActividadesDB> listaActividades) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaActividades); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE ELIMINAR LA TABLA
     **/
    public static void eliminarTabla() {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.delete(ActividadesDB.class); //Borra los registros de la tabla.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA
     **/
    public static List<ActividadesDB> mostrarListaActividades() {

        return realm.where(ActividadesDB.class).findAll();
    }

    public static int mostrarNumeroActividades(int id, String idProspecto) {
        return realm.where(ActividadesDB.class)
                .equalTo("idTipoActividad", id)
                .equalTo("idProspecto", idProspecto)
                .findAll().size();
    }
}
