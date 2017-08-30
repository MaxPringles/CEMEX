package mx.com.tarjetasdelnoreste.realmdb;



import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoServiciosRealm extends MenuRealm {

    public CatalogoServiciosRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaServicios(List<CatalogoServiciosDB> listaServicios) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaServicios); //Inserta la lista en la DB.
        realm.commitTransaction();
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoServiciosDB> mostrarListaServicios() {

        return realm.where(CatalogoServiciosDB.class).findAll();
    }

    public static void cambiarEstatusSeleccionado(CatalogoServiciosDB catalogoServiciosDB) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(catalogoServiciosDB);
        realm.commitTransaction();
    }

}
