package mx.com.tarjetasdelnoreste.realmdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;

/**
 * Created by czamora on 9/20/16.
 */
public class PlanSemanalRealm extends MenuRealm {

    public PlanSemanalRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaPlanSemanal(List<PlanSemanalDB> listaPlanSemanal) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaPlanSemanal); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA
     **/
    public static List<PlanSemanalDB> mostrarListaPlanSemanal() {

        //return realm.where(PlanSemanalDB.class).findAll();
        return realm.where(PlanSemanalDB.class).findAllSorted(
                "HoraInicio", Sort.ASCENDING);

    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LAS ÚLTIMAS CITAS A REALIZAR
     **/
    public static List<PlanSemanalDB> mostrarListaPlanSemanalUltimasCitas() {

        //Se recuperan todos los prospectos, los cuales de antemano se sabe que contienen el
        //id de la última actividad agregada, esto dentro del atributo "idActividadAnterior".
        List<ProspectosDB> prospectosDBList = realm.where(ProspectosDB.class).findAll();
        PlanSemanalDB planSemanalDB;
        List<PlanSemanalDB> planSemanalDBList = new ArrayList<>();

        //Se buscan los datos de las actividades que correspondan con la última actividad de
        //cada prospecto y se guardan en una lista.
        for (ProspectosDB prospectosDB : prospectosDBList) {

            planSemanalDB = realm.where(PlanSemanalDB.class)
                    .equalTo("idActividadAnterior", prospectosDB.getIdActividadAnterior()).findFirst();

            //En caso de que sí se obtengan los datos de la actividad, éstos se guardan en una lista.
            if (planSemanalDB != null) {
                planSemanalDBList.add(planSemanalDB);
            }
        }

        //Se acomoda el arreglo de acuerdo a la fecha de inicio.
        Collections.sort(planSemanalDBList, new Comparator<PlanSemanalDB>() {
            @Override
            public int compare(PlanSemanalDB o1, PlanSemanalDB o2) {
                return o1.getHoraInicio().compareTo(o2.getHoraFin());
            }
        });

        return planSemanalDBList;


        /*RealmResults<PlanSemanalDB> citasOrdenadas = realm.where(PlanSemanalDB.class)
                .findAllSorted("HoraInicio", Sort.DESCENDING);

        citasOrdenadas = citasOrdenadas.where().distinct("idProspecto");

        return citasOrdenadas.where().findAllSorted("HoraInicio", Sort.ASCENDING);*/

    }

    /**
     * MÉTODO QUE DEVUELVE LOS REGISTROS CON LA FECHA SEÑALADA
     **/
    public static List<PlanSemanalDB> mostrarListaPlanSemanalFecha(String fecha) {

        return realm.where(PlanSemanalDB.class)
                .beginsWith("HoraInicio", fecha)
                .findAllSorted(
                        "HoraInicio", Sort.ASCENDING
                );

    }

    /**
     * MÉTODO QUE DEVUELVE LA PRIMERA FECHA (para el header de Citas/Visitas).
     **/
    public static PlanSemanalDB mostrarPrimeraFecha() {

        return realm.where(PlanSemanalDB.class).findAllSorted(
                "HoraInicio", Sort.ASCENDING
        ).first();

    }
}
