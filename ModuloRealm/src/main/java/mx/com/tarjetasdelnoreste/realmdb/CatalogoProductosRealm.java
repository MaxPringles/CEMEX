package mx.com.tarjetasdelnoreste.realmdb;



import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMunicipiosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoProductosRealm extends MenuRealm {

    public CatalogoProductosRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaProductos(List<CatalogoProductoDB> listaProductos) {

        for (int i = 0; i < listaProductos.size(); i++) {
            listaProductos.get(i).setCompoundId("" + listaProductos.get(i).getId() + listaProductos.get(i).getIdPadre());
        }

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaProductos); //Inserta la lista en la DB.
        realm.commitTransaction();
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoProductoDB> mostrarListaProductoPorId(Long idPadre) {

        return realm.where(CatalogoProductoDB.class)
                .equalTo("idPadre", idPadre)
                .findAll();
    }

    public static void cambiarEstatusSeleccionado(CatalogoProductoDB catalogoProductoDB) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(catalogoProductoDB);
        realm.commitTransaction();
    }

}
