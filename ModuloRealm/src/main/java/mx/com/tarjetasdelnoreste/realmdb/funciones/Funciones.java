package mx.com.tarjetasdelnoreste.realmdb.funciones;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarFragments;

/**
 * Created by czamora on 9/27/16.
 */
public class Funciones {

    public static void onBackPressedFunction(Context context, boolean flujoNormal){
        try {
            ((ComunicarFragments) context).onBackPressedFragment(flujoNormal);
        } catch (Exception e) {
            Log.e("INTERFACE_BACK_PRESS", e.toString());
        }
    }

    public static final ProgressDialog customProgressDialogConMensaje(
            Context context, String mensaje) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(mensaje);
        return progressDialog;
    }

    public static File archivoTemporal(Context context, String Prefijo, String ID, String Sufijo,
                                String Album) throws IOException {
        File f = nombreAleatorio(context, Prefijo, ID, Sufijo, Album);
        return f;
    }

    private static File nombreAleatorio(Context context, String Prefijo, String ID, String Sufijo,
                                 String Album) throws IOException {
        // Guarda el Archivo con un Nombre Aleatorio
        String imageFileName = Prefijo + "_" + ID + "_";
        File image = File.createTempFile(imageFileName, "_photo" + Sufijo,
                obtenerPath(context, Album));
        return image;
    }

    private static File obtenerPath(Context context, String album) {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {

            storageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    album);

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }
        } else {
            Log.v(context.getString(R.string.app_name),
                    "External storage is not mounted READ/WRITE.");
            Toast.makeText(context,
                    "Tarjeta SD no disponible, guardando en memoria interna",
                    Toast.LENGTH_SHORT).show();
            storageDir = new File(context.getFilesDir(), album);
        }

        return storageDir;
    }
}
