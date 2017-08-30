package mx.com.tarjetasdelnoreste.realmdb.broadcasts;

/**
 * Created by czamora on 11/24/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Calendar;

import mx.com.tarjetasdelnoreste.realmdb.servicios.ServiceEnvioDeCoordenadas;
import mx.com.tarjetasdelnoreste.realmdb.servicios.ServiceGeneralOffline;
import mx.com.tarjetasdelnoreste.realmdb.servicios.ServiceNotificaciones;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;


public class VerificarRed extends BroadcastReceiver {

    public static final int TYPE_NOT_CONNECTED = 2;
    Calendar calendar;
    Calendar calendarFin;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentServiceGeneral;
        Intent intentNotificaciones;
        switch(getConnectivityStatus(context)){
            case ConnectivityManager.TYPE_MOBILE:
            case ConnectivityManager.TYPE_WIFI:

                //Se inicia el servicio de envío Offline.
                intentServiceGeneral = new Intent(context, ServiceGeneralOffline.class);
                context.startService(intentServiceGeneral);

                //Se inicia el servicio de recepción de Notificaciones.
                intentNotificaciones = new Intent(context, ServiceNotificaciones.class);
                context.startService(intentNotificaciones);

                //Se obtienen la hora límite de recepción de coordenadas y la hora actual.
                calendarFin = Calendar.getInstance();
                calendarFin.setTimeInMillis(System.currentTimeMillis());
                calendarFin.set(Calendar.HOUR_OF_DAY, Valores.VALOR_ALARMA_HORA_FIN);
                calendarFin.set(Calendar.MINUTE, 0);

                calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                //En caso de que la hora actual sea mayor a la hora límite, se revisa
                //si existen coordenadas por enviar.
                if (calendar.getTimeInMillis() - calendarFin.getTimeInMillis() >= 0) {
                    intentServiceGeneral = new Intent(context,
                            ServiceEnvioDeCoordenadas.class);
                    context.startService(intentServiceGeneral);
                }
                break;
            default:
        }
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return ConnectivityManager.TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return ConnectivityManager.TYPE_MOBILE;
        }

        return TYPE_NOT_CONNECTED;
    }
}