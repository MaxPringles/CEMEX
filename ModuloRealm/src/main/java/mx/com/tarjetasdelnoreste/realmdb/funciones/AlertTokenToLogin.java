package mx.com.tarjetasdelnoreste.realmdb.funciones;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by usr_micro13 on 09/02/2017.
 */

public class AlertTokenToLogin {

    public static void showAlertDialog(final Context context) {

        SharedPreferences prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        final Dialog dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_alert_one_button);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialogTxtTitle = (TextView) dialogModal.findViewById(R.id.dialog_txt_title);
        TextView dialogTxtMessage = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        TextView dialogBtnAceptar = (TextView) dialogModal.findViewById(R.id.dialog_btn_aceptar);

        dialogTxtTitle.setText(context.getString(R.string.token_expirado_title));
        dialogTxtMessage.setText(context.getString(R.string.token_expirado_message));
        dialogBtnAceptar.setText(context.getString(R.string.token_expirado_aceptar));

        dialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModal.dismiss();

                Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.LoginActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

        //Indica que se cerrará sesión, para que al abrir la pantalla de Login, no se haga el
        //redireccionamiento.
        editor.putBoolean(Valores.SHARED_PREFERENCES_SESION_INICIADA, false);
        //Se indica que ya no hay ningún proceso de notificación en ejecución.
        editor.putBoolean(Valores.SHARED_PREFERENCES_NOTIFICACIONES, false);
        editor.commit();

        dialogModal.show();
    }

    public static void showAlertService(Context context) {

        SharedPreferences prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //Indica que se cerrará sesión, para que al abrir la pantalla de Login, no se haga el
        //redireccionamiento.
        editor.putBoolean(Valores.SHARED_PREFERENCES_SESION_INICIADA, false);
        //Se indica que ya no hay ningún proceso de notificación en ejecución.
        editor.putBoolean(Valores.SHARED_PREFERENCES_NOTIFICACIONES, false);
        editor.commit();

        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.simplifiedcoding.net"));
        //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
        Intent intent = new Intent();
        intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.LoginActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(context.getString(R.string.token_expirado_title));
        builder.setContentText(context.getString(R.string.token_expirado_message));
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(2, builder.build());

        Intent intentLogin = new Intent();
        intentLogin.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.LoginActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intentLogin);
    }

}
