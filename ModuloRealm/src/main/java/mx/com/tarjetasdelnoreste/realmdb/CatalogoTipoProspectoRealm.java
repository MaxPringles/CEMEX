package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoProspectoDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoTipoProspectoRealm extends MenuRealm {

    public CatalogoTipoProspectoRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaTipoProspecto(List<CatalogoTipoProspectoDB> listaTipoProspecto) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaTipoProspecto); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoTipoProspectoDB> mostrarListaTipoProspecto() {

        return realm.where(CatalogoTipoProspectoDB.class).findAll();

    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA, EXCEPTO EL REGISTRO
     * DE NUEVO PROSPECTO (el id = 1) **/
    public static List<CatalogoTipoProspectoDB> mostrarListaTipoProspectoSinNuevo() {

        return realm.where(CatalogoTipoProspectoDB.class)
                .notEqualTo("id", 1)
                .findAll();

    }

    /** MÉTODO QUE DEVUELVE TODOS EL REGISTRO DE NUEVO PROSPECTO **/
    public static List<CatalogoTipoProspectoDB> mostrarListaProspectoNuevo() {

        return realm.where(CatalogoTipoProspectoDB.class)
                .equalTo("id", 1)
                .findAll();

    }
}
