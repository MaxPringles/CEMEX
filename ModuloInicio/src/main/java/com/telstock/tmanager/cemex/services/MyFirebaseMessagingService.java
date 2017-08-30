package com.telstock.tmanager.cemex.services;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.NotificacionFirebaseDB;
import mx.com.tarjetasdelnoreste.realmdb.servicios.ServiceNotificaciones;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by usr_micro13 on 02/02/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";
    private Realm realmDB;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        realmDB = Realm.getDefaultInstance();

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message Data: " + remoteMessage.getData().get("idTipoNotificacion"));

        try {
            /** SE GUARDA LA NOTIFICACIÓN EN BD **/
            NotificacionFirebaseDB notificacionFirebaseDB = new NotificacionFirebaseDB();
            notificacionFirebaseDB.setIdNotificacion(remoteMessage.getData().get("idNotificacion"));
            notificacionFirebaseDB.setIdTipoNotificacion(Integer.parseInt(remoteMessage.getData().get("idTipoNotificacion")));
            notificacionFirebaseDB.setIdPrioridad(Integer.parseInt(remoteMessage.getData().get("idPrioridad")));
            notificacionFirebaseDB.setIdReferencia(remoteMessage.getData().get("idReferencia"));
            notificacionFirebaseDB.setTitle(remoteMessage.getNotification().getTitle());
            notificacionFirebaseDB.setMessage(remoteMessage.getNotification().getBody());
            notificacionFirebaseDB.setStatusEnvio(Valores.ESTATUS_NO_ENVIADO);

            realmDB.beginTransaction();
            realmDB.copyToRealmOrUpdate(notificacionFirebaseDB);
            realmDB.commitTransaction();

            realmDB.close();

            //Se manda a ejecutar el servicio que realiza las operaciones de acuerdo a las
            //notificaciones recibidas.
            Intent intentService = new Intent(getApplicationContext(), ServiceNotificaciones.class);
            getApplication().startService(intentService);

        } catch (Exception e) {
            Log.e("NOTIFICACION_FIREBASE", e.toString());
        }
    }
}
