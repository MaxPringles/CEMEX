package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades.JsonMostrarActividades;

/**
 * Created by movil4 on 29/12/16.
 */

public class JsonMostrarActividadesRealm extends MenuRealm {

    public JsonMostrarActividadesRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaActividadestodo(List<JsonMostrarActividades> jsonMostrarActividadesList) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(jsonMostrarActividadesList); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA
     **/
    public static List<JsonMostrarActividades> mostrarListaActividadesTodo() {

        //return realm.where(PlanSemanalDB.class).findAll();
        return realm.where(JsonMostrarActividades.class).findAllSorted(
                "mFechaHoraCitaInicio", Sort.ASCENDING);

    }


}
