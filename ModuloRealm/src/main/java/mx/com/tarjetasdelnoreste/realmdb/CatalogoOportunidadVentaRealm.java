package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMotivoExclusionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoOportunidadVentaDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoOportunidadVentaRealm extends MenuRealm {

    public CatalogoOportunidadVentaRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaOportunidadVenta(List<CatalogoOportunidadVentaDB> listaOportunidadVenta) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaOportunidadVenta); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoOportunidadVentaDB> mostrarListaOportunidadVenta() {

        return realm.where(CatalogoOportunidadVentaDB.class).findAll();

    }
}
