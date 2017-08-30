package mx.com.tarjetasdelnoreste.realmdb;

import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.MenuDB;

/**
 * Created by czamora on 9/13/16.
 */
public class MenuRealm {

    public static Realm realm;

    public MenuRealm() {

        // Reset Realm (se comenta porque borra la base).
        //Realm.deleteRealm(realmConfig);
    }

    public MenuRealm(Realm realm) {
        this.realm = realm;
    }

    public static Realm getRealm() {
        return realm;
    }

    public static void setRealm(Realm realm) {
        MenuRealm.realm = realm;
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaMenu(List<MenuDB> listaMenu) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.delete(MenuDB.class); //Borra lo que hay en la tabla.
        realm.copyToRealm(listaMenu); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.

    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA
     **/
    public static List<MenuDB> mostrarListaMenu() {

        return realm.where(MenuDB.class).findAll();

    }

    public static void open() {
        // Don't use Realm.setDefaultInstance() in library projects. It is unsafe as app developers can override the
        // default configuration. So always use explicit configurations in library projects.

        realm = Realm.getDefaultInstance();

    }

    public static boolean isClosed() {
        return realm.isClosed();
    }

    public static void close() {
        realm.close();

    }
}
