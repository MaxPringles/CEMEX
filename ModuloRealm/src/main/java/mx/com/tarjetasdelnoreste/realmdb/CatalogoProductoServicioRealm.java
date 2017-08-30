package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoServicioDB;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoProductoServicioRealm extends MenuRealm {

    public CatalogoProductoServicioRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaProductoServicio(List<CatalogoProductoServicioDB> listaProductoServicio) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaProductoServicio); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoProductoServicioDB> mostrarListaProductoServicio(String tipo) {

        if(tipo.equals(Valores.producto)) {
            return realm.where(CatalogoProductoServicioDB.class)
                    .equalTo("idTipoSorP", Integer.parseInt(tipo))
                    .findAll();
        } else {
            return realm.where(CatalogoProductoServicioDB.class)
                    .equalTo("idTipoSorP", Integer.parseInt(tipo))
                    .findAll();
        }
    }

    public static List<CatalogoProductoServicioDB> mostrarListaProducto(String idSubsegmento) {
        return realm.where(CatalogoProductoServicioDB.class)
                .equalTo("idSubSegmento", Integer.parseInt(idSubsegmento))
                .findAll();
    }
}
