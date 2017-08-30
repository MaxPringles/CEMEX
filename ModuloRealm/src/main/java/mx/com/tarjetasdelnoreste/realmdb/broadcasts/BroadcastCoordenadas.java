package mx.com.tarjetasdelnoreste.realmdb.broadcasts;

/**
 * Created by czamora on 11/7/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoAccionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CoordenadasRealm;
import mx.com.tarjetasdelnoreste.realmdb.MenuRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.LocationTracker;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoAccionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CoordenadasDB;
import mx.com.tarjetasdelnoreste.realmdb.servicios.ServiceEnvioDeCoordenadas;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

import static android.content.Context.MODE_PRIVATE;


public class BroadcastCoordenadas extends BroadcastReceiver {

    LocationTracker gps;
    Calendar calendarCoordenada;
    MenuRealm realmDB;
    Realm realm;


    @Override
    public void onReceive(Context context, Intent intent) {
        //Objeto que maneja Realml.
        realmDB = new MenuRealm();
        realm = Realm.getDefaultInstance();
        calendarCoordenada = Calendar.getInstance();




        int accion=1;
        List<CatalogoAccionDB> accionListAll = CatalogoAccionRealm.mostrarListaAccion();
        if(accionListAll!=null) {
            for (CatalogoAccionDB acciones : accionListAll) {

                if (acciones.getDescripcion().equals("Tracking")) {
                    accion = acciones.getId().intValue();
                }
            }
        }
        gps = new LocationTracker(context);

        if (gps.canGetLocation()) {

            Log.d("FUNCION_COORDENADA", gps.getLatitude() + "/" + gps.getLongitude());

            try {
                //Se abre una instancia propia de Realm, para que no haya problema en caso de que la
                //actividad principal de la aplicación sea destruida.
                realmDB.open();
                //Se guarda el registro con la hora, la latitud y longitud, y el status que indica si
                //ya se ha enviado o no.
                CoordenadasRealm.guardarListaCoordenadas(new CoordenadasDB(
                        calendarCoordenada.getTimeInMillis() / 1000,
                        gps.getLatitude(),
                        gps.getLongitude(),
                        Valores.ESTATUS_NO_ENVIADO,
                        accion));
                realmDB.close(); //Se cierra la instancia de Realm.
            } catch (Exception e) {
                Log.e("ERROR_COORDENADAS", e.toString());
            }
        } else {
            gps.showSettingsAlert();
        }

    }


}