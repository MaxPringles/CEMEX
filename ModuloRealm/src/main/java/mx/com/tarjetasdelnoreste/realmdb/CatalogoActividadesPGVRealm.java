package mx.com.tarjetasdelnoreste.realmdb;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoServicioDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoActividadesPGVRealm extends MenuRealm {

    public CatalogoActividadesPGVRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaActividadesPGV(List<CatalogoActividadesPGVDB> listaActividadesPGV) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaActividadesPGV); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA **/
    public static List<CatalogoActividadesPGVDB> mostrarListaActividadesPGV() {

        return realm.where(CatalogoActividadesPGVDB.class).findAll();

    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA (ACTIVIDADES QUE SE HAYAN REALIZADO) **/
    public static List<CatalogoActividadesPGVDB> mostrarListaActividadesExistentes() {

        List<PlanSemanalDB> planSemanalDBList = realm.where(PlanSemanalDB.class)
                .distinct("descripcionObra");
        CatalogoActividadesPGVDB catalogoActividadesPGVDB;
        List<CatalogoActividadesPGVDB> catalogoActividadesPGVDBList = new ArrayList<>();

        for (PlanSemanalDB planSemanalDB : planSemanalDBList) {
            catalogoActividadesPGVDB = realm.where(CatalogoActividadesPGVDB.class)
                    .equalTo("Descripcion", planSemanalDB.getDescripcionObra()).findFirst();

            if (catalogoActividadesPGVDB != null) {
                catalogoActividadesPGVDBList.add(catalogoActividadesPGVDB);
            }
        }

        return catalogoActividadesPGVDBList;
    }

    /** MÉTODO QUE DEVUELVE EL ID DE LA ACTIVIDAD **/
    public static CatalogoActividadesPGVDB mostrarActividad(int id) {

        CatalogoActividadesPGVDB actividad;

        actividad = realm.where(CatalogoActividadesPGVDB.class).equalTo("id", id)
                .findFirst();

        return actividad;

    }

    /** MÉTODO QUE DEVUELVE EL ID DE LA ACTIVIDAD **/
    public static int mostrarIdActividad(String nombreActividad) {

        int actividad = 0;

        actividad = realm.where(CatalogoActividadesPGVDB.class).equalTo("Descripcion", nombreActividad)
                .findFirst()
                .getId().intValue();

        return actividad;
    }

    /** MÉTODO QUE DEVUELVE LA INFORMACIÓN DE UNA ACTIVIDAD EN ESPECÍFICO **/
    public static CatalogoActividadesPGVDB mostrarInformacionActividad(long idActividad) {

        return realm.where(CatalogoActividadesPGVDB.class).equalTo("id", idActividad)
                .findFirst();

    }

    /** MÉTODO QUE DEVUELVE LA INFORMACIÓN DE UNA ACTIVIDAD EN ESPECÍFICO **/
    public static List<CatalogoActividadesPGVDB> mostrarActividadesIdPadre(int idPadre) {

        List<CatalogoActividadesPGVDB> catalogoActividadesPGVDBList = new ArrayList<>();
        List<CatalogoActividadesPGVDB> catalogoActividadesPGVDBListRealm = new ArrayList<>();

        catalogoActividadesPGVDBListRealm = realm.where(CatalogoActividadesPGVDB.class)
                .equalTo("idPadre", idPadre)
                .findAll();
        if (catalogoActividadesPGVDBListRealm.size() > 0) {

            for (CatalogoActividadesPGVDB catalogoActividadesPGVDB : catalogoActividadesPGVDBListRealm) {
                catalogoActividadesPGVDBList.add(new CatalogoActividadesPGVDB(
                        catalogoActividadesPGVDB.getId(),
                        catalogoActividadesPGVDB.getIdCatalogo(),
                        catalogoActividadesPGVDB.getDescripcion(),
                        catalogoActividadesPGVDB.getIdPadre(),
                        false
                ));
            }
        }

        return catalogoActividadesPGVDBList;

    }

}
