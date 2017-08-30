package mx.com.tarjetasdelnoreste.realmdb.broadcasts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mx.com.tarjetasdelnoreste.realmdb.MainActivity;
import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by czamora on 12/1/16.
 */

public class BroadcastAlarmaActividades extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String titulo = intent.getStringExtra(Valores.VALOR_ALARMA_ACTIVIDADES_TITULO);
        String mensaje = intent.getStringExtra(Valores.VALOR_ALARMA_ACTIVIDADES_MENSAJE);

        Log.d("BROADCAST_PRUEBA", titulo + "/" + mensaje);

        //showNotification(titulo, mensaje, context, MainActivity.class);

    }

    //Método para mostrar notificaciones en la barra de tareas del dispositivo.
    public void showNotification(String title, String message, Context context, Class clase) {

        Intent notifyIntent = new Intent(context, clase);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(context, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.action_add_photo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .build();

        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}
