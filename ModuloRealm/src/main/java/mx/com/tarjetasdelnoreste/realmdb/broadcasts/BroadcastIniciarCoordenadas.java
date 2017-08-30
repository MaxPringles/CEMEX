package mx.com.tarjetasdelnoreste.realmdb.broadcasts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mx.com.tarjetasdelnoreste.realmdb.servicios.ServiceEnvioDeCoordenadas;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by czamora on 11/23/16.
 */

public class BroadcastIniciarCoordenadas extends BroadcastReceiver {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm = new Intent(context, BroadcastCoordenadas.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intentAlarm, 0);

        if (intent.getIntExtra(Valores.ALARMA_ENVIO_DE_COORDENADAS, 0) == Valores.VALOR_ALARMA_INICIO_COORDENADAS) {
            Log.d("FUNCION_COORDENADA", "INICIO");

            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    0, 60 * 1000, pendingIntent);

        } else if (intent.getIntExtra(Valores.ALARMA_ENVIO_DE_COORDENADAS, 0) == Valores.VALOR_ALARMA_FIN_COORDENADAS) {
            Log.d("FUNCION_COORDENADA", "FIN");

            //Se detiene la alarma que obtiene las coordenadas cada minuto.
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }

            //Una vez detenida la alarma, se mandan todas las coordenadas obtenidas durante
            //el día.
            Intent intentServicio = new Intent(context, ServiceEnvioDeCoordenadas.class);
            context.startService(intentServicio);
        }
    }
}
