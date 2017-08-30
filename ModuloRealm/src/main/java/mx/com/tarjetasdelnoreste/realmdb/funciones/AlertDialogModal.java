package mx.com.tarjetasdelnoreste.realmdb.funciones;

/**
 * Created by usr_micro13 on 26/12/2016.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarAlertDialog;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;


public class AlertDialogModal {

    //Variables para recuperar el idProspecto.
    public static SharedPreferences prefs;
    public static SharedPreferences.Editor editor;

    public static Dialog showModalOneButton(final Context context, final ComunicarAlertDialog comunicarAlertDialog,
                                            String title, String message, String stringButton, final String idDialog) {

        final Dialog dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_alert_one_button);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialogTxtTitle = (TextView) dialogModal.findViewById(R.id.dialog_txt_title);
        TextView dialogTxtMessage = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        TextView dialogBtnAceptar = (TextView) dialogModal.findViewById(R.id.dialog_btn_aceptar);

        dialogTxtTitle.setText(title);
        dialogTxtMessage.setText(message);
        dialogBtnAceptar.setText(stringButton);

        dialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicarAlertDialog.alertDialogPositive(idDialog);
                dialogModal.dismiss();
            }
        });

        dialogModal.show();

        return dialogModal;
    }

    public static Dialog showModalTwoButtons(final Context context, final ComunicarAlertDialog comunicarAlertDialog,
                                             String title, String message, String stringButtonAceptar,
                                             String stringButtonCancelar, final String idDialog) {

        final Dialog dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_alert_two_buttons);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialogTxtTitle = (TextView) dialogModal.findViewById(R.id.dialog_txt_title);
        TextView dialogTxtMessage = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        TextView dialogBtnAceptar = (TextView) dialogModal.findViewById(R.id.dialog_btn_aceptar);
        TextView dialogBtnCancelar = (TextView) dialogModal.findViewById(R.id.dialog_btn_cancelar);

        dialogTxtTitle.setText(title);
        dialogTxtMessage.setText(message);
        dialogBtnAceptar.setText(stringButtonAceptar);
        dialogBtnCancelar.setText(stringButtonCancelar);

        dialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicarAlertDialog.alertDialogPositive(idDialog);
                dialogModal.dismiss();
            }
        });

        dialogBtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicarAlertDialog.alertDialogNegative(idDialog);
                dialogModal.dismiss();
            }
        });

        dialogModal.show();

        return dialogModal;
    }

    public static Dialog showModalTwoButtonsNoTitle(final Context context, final ComunicarAlertDialog comunicarAlertDialog,
                                             String message, String stringButtonAceptar,
                                             String stringButtonCancelar, final String idDialog) {

        final Dialog dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_alert_two_buttons_no_title);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialogTxtMessage = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        TextView dialogBtnAceptar = (TextView) dialogModal.findViewById(R.id.dialog_btn_aceptar);
        TextView dialogBtnCancelar = (TextView) dialogModal.findViewById(R.id.dialog_btn_cancelar);

        dialogTxtMessage.setText(message);
        dialogBtnAceptar.setText(stringButtonAceptar);
        dialogBtnCancelar.setText(stringButtonCancelar);

        dialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicarAlertDialog.alertDialogPositive(idDialog);
                dialogModal.dismiss();
            }
        });

        dialogBtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comunicarAlertDialog.alertDialogNegative(idDialog);
                dialogModal.dismiss();
            }
        });

        dialogModal.show();

        return dialogModal;
    }

    public static Dialog showModalTwoButtonsEditText(final Context context, final ComunicarAlertDialog comunicarAlertDialog,
                                                    String title, String stringButtonAceptar, String stringButtonCancelar,
                                                     final String idDialog) {

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = prefs.edit();

        final Dialog dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_alert_two_buttons_edittext);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final TextView dialogTxtTitle = (TextView) dialogModal.findViewById(R.id.dialog_txt_title);
        final EditText etxNombreArchivo = (EditText) dialogModal.findViewById(R.id.etx_nombre_archivo);

        dialogTxtTitle.setText(title);
        //Se elimina el botón de emojis
        etxNombreArchivo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        TextView dialogBtnAceptar = (TextView) dialogModal.findViewById(R.id.dialog_btn_aceptar);
        TextView dialogBtnCancelar = (TextView) dialogModal.findViewById(R.id.dialog_btn_cancelar);

        dialogBtnAceptar.setText(stringButtonAceptar);
        dialogBtnCancelar.setText(stringButtonCancelar);

        dialogBtnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Verifica si el cuadro de diálogo apareció como petición de colocar el comentario
                //de una Actividad Administrativa.
                if (idDialog == Valores.ID_COLOCAR_COMENTARIO) {

                    String comentarios = etxNombreArchivo.getText().toString();

                    //Se guarda el nombre del archivo en una sharedPreference.
                    editor.putString(Valores.SHARED_PREFERENCES_COLOCAR_COMENTARIOS, comentarios);
                    editor.commit();

                } else {

                    String nombreArchivo = etxNombreArchivo.getText().toString();

                    if (nombreArchivo.equals("")) {
                        Toast.makeText(context, context.getString(R.string.dialog_alert_edittext_fail),
                                Toast.LENGTH_LONG).show();
                    } else {

                        //Se guarda el nombre del archivo en una sharedPreference.
                        editor.putString(Valores.SHARED_PREFERENCES_NOMBRE_ARCHIVO, nombreArchivo);
                        editor.commit();

                        comunicarAlertDialog.alertDialogPositive(idDialog);

                        cerrarTeclado(context, v);
                        dialogModal.dismiss();
                    }
                }


            }
        });

        dialogBtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Verifica si el cuadro de diálogo apareció como petición de colocar el comentario
                //de una Actividad Administrativa.
                if (idDialog == Valores.ID_COLOCAR_COMENTARIO) {

                    //Se guarda el nombre del archivo en una sharedPreference.
                    editor.putString(Valores.SHARED_PREFERENCES_COLOCAR_COMENTARIOS, "");
                    editor.commit();
                }

                comunicarAlertDialog.alertDialogNegative(idDialog);

                cerrarTeclado(context, v);
                dialogModal.dismiss();
            }
        });

        dialogModal.show();

        return dialogModal;
    }

    /**
     * MÉTODO QUE CIERRA EL TECLADO
     **/
    public static void cerrarTeclado(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

