package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMunicipiosDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoMunicipiosRealm extends MenuRealm {

    public CatalogoMunicipiosRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaMunicipios(List<CatalogoMunicipiosDB> listaMunicipios) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaMunicipios); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoMunicipiosDB> mostrarListaMunicipios() {

        return realm.where(CatalogoMunicipiosDB.class).findAll();

    }

    /** MÉTODO QUE DEVUELVE LA LISTA DE MUNICIPIOS DE ACUERDO AL idPadre **/
    public static List<CatalogoMunicipiosDB> mostrarListaMunicipiosidPadre(Long idPadre) {

        return realm.where(CatalogoMunicipiosDB.class)
                .equalTo("idPadre", idPadre)
                .findAll();

    }
}
