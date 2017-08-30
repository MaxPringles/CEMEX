package cemex.tmanager.telstock.com.moduloplansemanal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cemex.tmanager.telstock.com.moduloplansemanal.funciones.TinyDB;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiClient;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiInterface;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoAccionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CoordenadasRealm;
import mx.com.tarjetasdelnoreste.realmdb.GeneralOfflineRealm;
import mx.com.tarjetasdelnoreste.realmdb.MenuRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertDialogModal;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.funciones.LocationTracker;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarAlertDialog;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoAccionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CoordenadasDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GeneralOfflineDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Actividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividadesNuevas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas.JsonAltaActividadesAdministrativas;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cesar on 18/09/2016.
 */
public class TimePickerFragmentFin extends Fragment implements ComunicarAlertDialog {

    private Context context;

    private ProgressDialog progressDialog;
    private LinearLayout layout_time_picker; //Layout que envuelve al timePicker.
    private TimePicker timePicker;
    private TextView btn_cancelar;
    private TextView btn_guardar;
    private TextView txt_numero_dia;
    private TextView txt_fecha_dia;

    TinyDB tinyDB;
    JsonAltaActividades jsonAltaActividades = new JsonAltaActividades();
    JsonAltaActividadesAdministrativas jsonAltaActividadesAdministrativas = new JsonAltaActividadesAdministrativas();
    Actividad actividad;
    Calendar calendarInicio;
    Calendar calendarFin;
    String datetimeFin;

    Dialog dialogModal; //Modal que muestra mensaje de traslape de evento.

    //Se declara la SharedPreferences.
    SharedPreferences prefs;

    ApiInterface apiInterface;
    private String actividadAdministrativa;

    //Modal que muestra mensaje de alerta para colocar el comentario.
    Dialog dialogModalComentario;
    ComunicarAlertDialog comunicarAlertDialog;

    View view;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_picker_fin, container, false);

        context = getActivity();
        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.
        comunicarAlertDialog = this;

        //Se inicializa la interface de Retrofit.
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se recibe el tipo de actividad a dar de alta (en caso de que sea administrativa).
        actividadAdministrativa = getArguments().getString(Valores.BUNDLE_CALENDAR, "");

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

        //Indica que NO es actividadAdministrativa.
        if (actividadAdministrativa.equals("")) {

            try {
                //Se recupera el objeto llenado previamente en PlanSemanalFragment.
                tinyDB = new TinyDB(context);
                jsonAltaActividades = tinyDB.getJsonAltaActividades(
                        Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES, JsonAltaActividades.class);

                actividad = jsonAltaActividades.getActividad();
            } catch (Exception e) {
                Log.e("ERROR_TIME_TINYDB", e.toString());
            }

        } else { //Indica que SÍ es actividadAdministrativa.

            try {
                //Se recupera el objeto llenado previamente en PlanSemanalFragment.
                tinyDB = new TinyDB(context);
                jsonAltaActividadesAdministrativas = tinyDB.getJsonAltaActividadesAdministrativas(
                        Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS, JsonAltaActividadesAdministrativas.class);

            } catch (Exception e) {
                Log.e("ERROR_TIME_TINYDB", e.toString());
            }
        }

        //Se coloca el Toolbar.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.plan_titulo)
                + " " + getString(R.string.plan_time_picker_title_fin));

        layout_time_picker = (LinearLayout) view.findViewById(R.id.layout_time_picker);
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        btn_cancelar = (TextView) view.findViewById(R.id.txt_btn_cancelar);
        btn_guardar = (TextView) view.findViewById(R.id.txt_btn_guardar);
        txt_numero_dia = (TextView) view.findViewById(R.id.txt_numero_dia);
        txt_fecha_dia = (TextView) view.findViewById(R.id.txt_fecha_dia);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            ViewGroup.LayoutParams params = layout_time_picker.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layout_time_picker.setLayoutParams(params);
        }

        //Se obtiene la fecha para colocar la cabecera.
        calendarInicio = Calendar.getInstance();
        calendarFin = Calendar.getInstance();

        //Indica que NO es actividadAdministrativa.
        if (actividadAdministrativa.equals("")) {
            calendarFin.setTime(actividad.getHoraInicioSupport());
        } else { //Indica que SÍ es actividadAdministrativa.
            calendarFin.setTime(jsonAltaActividadesAdministrativas.getHoraInicioSupport());
        }

        //Formato en que se mostrará la fecha.
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM yyyy");

        txt_numero_dia.setText(calendarFin.get(Calendar.DAY_OF_MONTH) + "");
        txt_fecha_dia.setText(dateFormat.format(calendarFin.getTime()));

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ejecuta el método onBackPressed() de la actividad madre.
                Funciones.onBackPressedFunction(context, true);

            }
        });

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("HORA", timePicker.getCurrentHour() + ":"
                        + timePicker.getCurrentMinute());

                try {

                    //Se obtiene la hora seleccionado por el usuario.
                    calendarFin.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendarFin.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    calendarFin.set(Calendar.SECOND, 0);
                    calendarFin.set(Calendar.MILLISECOND, 0);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                    try {

                        Date datetimeInicio;

                        //Indica que NO es actividadAdministrativa.
                        if (actividadAdministrativa.equals("")) {
                            datetimeInicio = dateFormat.parse(actividad.getFechaHoraCitaInicio());
                        } else { //Indica que SÍ es actividadAdministrativa.
                            datetimeInicio = dateFormat.parse(jsonAltaActividadesAdministrativas.getFechaHoraCitaInicio());
                        }

                        calendarInicio.setTime(datetimeInicio);

                        datetimeFin = dateFormat.format(calendarFin.getTime());

                        if (horaFinalMayorAInicial(calendarInicio, calendarFin)) {
                            if (revisarTraslapes(calendarInicio, calendarFin)) {
                                if (revisarMinimoTiempo(calendarInicio, calendarFin)) {

                                    //Indica que NO es actividadAdministrativa.
                                    if (actividadAdministrativa.equals("")) {

                                        //Se hardcodea la "Z" en los horarios y se guardan en el objeto.
                                        if (!actividad.getFechaHoraCitaInicio().contains("Z")) {
                                            actividad.setFechaHoraCitaInicio(actividad.getFechaHoraCitaInicio() + "Z");
                                        }

                                        if (!datetimeFin.contains("Z")) {
                                            actividad.setFechaHoraCitaFin(datetimeFin + "Z");
                                        }

                                        if (!actividad.getFechaHoraCitaFin().contains("Z")) {
                                            actividad.setFechaHoraCitaFin(datetimeFin + "Z");
                                        }

                                        actividad.setmEstatusAgenda(1);
                                        actividad.setIdAltaOffline(UUID.randomUUID().toString());

                                        jsonAltaActividades.setActividad(actividad);

                                        //El objeto modificado se inserta nuevamente en TinyDB.
                                        tinyDB.putJsonAltaActividades(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES, jsonAltaActividades);

                                        if (!jsonAltaActividades.getmActividadAnterior().getIdActividad().equals("")) {
                                            mandarInformacion(v, jsonAltaActividades);
                                        } else {
                                            JsonAltaActividadesNuevas jsonAltaActividadesNuevas = new JsonAltaActividadesNuevas();
                                            jsonAltaActividadesNuevas.setActividad(jsonAltaActividades.getActividad());
                                            jsonAltaActividadesNuevas.setIdPaso(jsonAltaActividades.getIdPaso());
                                            jsonAltaActividadesNuevas.setIdStatusProspecto(jsonAltaActividades.getIdStatusProspecto());
                                            jsonAltaActividadesNuevas.setServicios(jsonAltaActividades.getServicios());
                                            jsonAltaActividadesNuevas.setSubsegmentoProductos(jsonAltaActividades.getSubsegmentoProductos());
                                            jsonAltaActividadesNuevas.setEsOfertaIntegral(jsonAltaActividades.isEsOfertaIntegral());

                                            mandarInformacionSinActividadAnterior(v, jsonAltaActividadesNuevas);
                                        }
                                    } else { //Indica que SÍ es actividadAdministrativa.
                                        //El envío se hace al momento de presionar CANCELAR o CONFIRMAR
                                        //en el cuadro de diálogo de comentarios.

                                        dialogModalComentario = AlertDialogModal.showModalTwoButtonsEditText(context, comunicarAlertDialog,
                                                getString(R.string.dialog_alert_edittext_comentario), getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar),
                                                Valores.ID_COLOCAR_COMENTARIO);

                                        jsonAltaActividadesAdministrativas.setIdAltaOffline(UUID.randomUUID().toString());

                                        //Se hardcodea la "Z" en los horarios y se guardan en el objeto.
                                        if (!jsonAltaActividadesAdministrativas.getFechaHoraCitaInicio().contains("Z")) {
                                            jsonAltaActividadesAdministrativas.setFechaHoraCitaInicio(
                                                    jsonAltaActividadesAdministrativas.getFechaHoraCitaInicio() + "Z");
                                        }

                                        if (!datetimeFin.contains("Z")) {
                                            jsonAltaActividadesAdministrativas.setFechaHoraCitaFin(datetimeFin + "Z");
                                        }

                                        if (!jsonAltaActividadesAdministrativas.getFechaHoraCitaFin().contains("Z")) {
                                            jsonAltaActividadesAdministrativas.setFechaHoraCitaFin(datetimeFin + "Z");
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {
                        Log.e("TIMEPICKERFRAGMENT", e.toString());
                        FirebaseCrash.report(e);
                    }
                } catch (Exception e) {
                    //Significa que no existe esa llave de SharedPreference aún.
                    Snackbar.make(v, getString(R.string.plan_time_picker_fail), Snackbar.LENGTH_LONG).show();
                    FirebaseCrash.report(e);
                }
            }
        });

        return view;
    }

    public boolean revisarTraslapes(Calendar calendarInicioOriginal, Calendar calendarFinOriginal) {

        List<PlanSemanalDB> planSemanalDB = PlanSemanalRealm.mostrarListaPlanSemanal();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        calendarFinOriginal.set(Calendar.SECOND, 0);

        Calendar calendarInicioDB;
        Calendar calendarFinDB;

        if (planSemanalDB.size() > 0) {

            for (int i = 0; i < planSemanalDB.size(); i++) {

                if (planSemanalDB.get(i).getHoraInicio() != null && planSemanalDB.get(i).getHoraFin() != null) {


                    calendarInicioDB = Calendar.getInstance();
                    calendarFinDB = Calendar.getInstance();

                    try {
                        calendarInicioDB.setTime(format.parse(planSemanalDB.get(i).getHoraInicio()));
                        calendarFinDB.setTime(format.parse(planSemanalDB.get(i).getHoraFin()));

                        //Se resetean los segundos para que no haya problemas de cálculo.
                        calendarInicioDB.set(Calendar.SECOND, 0);
                        calendarFinDB.set(Calendar.SECOND, 0);

                        if (calendarInicioOriginal.getTimeInMillis() - calendarFinDB.getTimeInMillis() >= 0
                                || calendarInicioDB.getTimeInMillis() - calendarFinOriginal.getTimeInMillis() >= 0) {
                        } else {

                            String mensajeModal = "Lo sentimos, la hora de fin seleccionada se cruza con el evento: \n" +
                                    planSemanalDB.get(i).getObra() + " - " + planSemanalDB.get(i).getCliente() + "\n\n" +
                                    "\u2022 Horario: " + agregarCero(calendarInicioDB.get(Calendar.HOUR_OF_DAY)) + ":" + agregarCero(calendarInicioDB.get(Calendar.MINUTE)) +
                                    " - " + agregarCero(calendarFinDB.get(Calendar.HOUR_OF_DAY)) + ":" + agregarCero(calendarFinDB.get(Calendar.MINUTE));

                            showModalTraslape(getString(R.string.plan_time_picker_traslape_title), mensajeModal);

                            return false;
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                        FirebaseCrash.report(e);
                    }
                }
            }
        }

        return true;
    }

    public boolean horaFinalMayorAInicial(Calendar calendarInicio, Calendar calendarFin) {

        if (calendarFin.getTimeInMillis() - calendarInicio.getTimeInMillis() > 0) {
            return true;
        } else {

            String horaInicio = agregarCero(calendarInicio.get(Calendar.HOUR_OF_DAY)) + ":" +
                    agregarCero(calendarInicio.get(Calendar.MINUTE));

            String mensajeModal = "La hora final debe ser mayor a la hora de inicio " +
                    "(" + horaInicio + ")";

            showModalTraslape(getString(R.string.plan_time_picker_fin_mayor_inicio_title), mensajeModal);

            return false;
        }
    }

    public boolean revisarMinimoTiempo(Calendar calendarInicio, Calendar calendarFin) {

        if (calendarFin.getTimeInMillis() - calendarInicio.getTimeInMillis() >= (60*1000*15)) {
            return true;
        } else {

            String horaInicio = agregarCero(calendarInicio.get(Calendar.HOUR_OF_DAY)) + ":" +
                    agregarCero(calendarInicio.get(Calendar.MINUTE));

            String mensajeModal = "La hora de fin debe tener al menos 15min. de diferencia con hora inicial " +
                    "(" + horaInicio + ")";

            showModalTraslape(getString(R.string.plan_time_picker_fin_15_min), mensajeModal);


            return false;
        }
    }

    public String agregarCero(int numero) {

        if (numero < 10) {
            return "0" + numero;
        } else {
            return numero + "";
        }
    }

    public void showModalTraslape(String title, String mensaje) {

        dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_plan_traslape);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        //dialogModal.getWindow().getAttributes().windowAnimations = com.telstock.tmanager.cemex.prospectos.R.style.DialogAnimation;
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_txt_title = (TextView) dialogModal.findViewById(R.id.dialog_txt_title);
        TextView dialog_txt_message = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        TextView dialog_btn_aceptar = (TextView) dialogModal.findViewById(R.id.dialog_btn_aceptar);

        dialog_txt_title.setText(title);
        dialog_txt_message.setText(mensaje);

        dialog_btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModal.dismiss();
            }
        });

        dialogModal.show();

    }

    public void mandarInformacionSinActividadAnterior(final View view, JsonAltaActividadesNuevas jsonAltaActividades) {

        //Se muestra el ProgressDialog.
        mostrarProgressDialog();

        Call<ResponseBody> sendActividadAlta = apiInterface.setActividadAltaSinActividadAnterior(
                prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""), jsonAltaActividades);

        final String json = new Gson().toJson(jsonAltaActividades);

        sendActividadAlta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    Toast.makeText(context, getString(R.string.actividad_alta_success), Toast.LENGTH_LONG).show();

                    //Indica que se regresará dos veces (hasta la pantalla del Calendario).
                    tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK, true);

                    enviarCheckIn();

                    progressDialog.dismiss();

                    //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                    //Se regresa a la pantalla del Calendario.
                    Intent intent = new Intent();
                    intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                    startActivity(intent);

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else if (response.code() == 400) {
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Snackbar.make(view, jObjError.getString("message"), Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    } else {
                        Snackbar.make(view, getString(R.string.actividad_alta_fail), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    FirebaseCrash.log("Error al mandar la información plan semanal " + response.code());
                    Snackbar.make(view, getString(R.string.actividad_alta_fail), Snackbar.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR_TIMEPICKERDIALOG", t.toString());
                FirebaseCrash.log("Error al mandar la información plan semanal");
                FirebaseCrash.report(t);

                /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                Toast.makeText(context, getString(R.string.actividad_alta_offline), Toast.LENGTH_LONG).show();

                GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                        System.currentTimeMillis(),
                        Valores.ID_ENVIO_ALTA_ACTIVIDAD_SIN_ANTERIOR,
                        prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""),
                        json,
                        Valores.ESTATUS_NO_ENVIADO
                );
                GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                /**********************************************************************/

                progressDialog.dismiss();

                //Indica que se regresará dos veces (hasta la pantalla del Calendario).
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK, true);

                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                //Se regresa a la pantalla del Calendario.
                Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                startActivity(intent);
            }
        });

    }

    public void mandarInformacion(final View view, JsonAltaActividades jsonAltaActividades) {

        //Se muestra el ProgressDialog.
        mostrarProgressDialog();

        Call<ResponseBody> sendActividadAlta = apiInterface.setActividadAlta(
                prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""), jsonAltaActividades);

        final String json = new Gson().toJson(jsonAltaActividades);

        sendActividadAlta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    Toast.makeText(context, getString(R.string.actividad_alta_success), Toast.LENGTH_LONG).show();

                    //Indica que se regresará dos veces (hasta la pantalla del Calendario).
                    tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK, true);

                    enviarCheckIn();

                    progressDialog.dismiss();

                    //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                    //Se regresa a la pantalla del Calendario.
                    Intent intent = new Intent();
                    intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                    startActivity(intent);

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else if (response.code() == 400) {
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Snackbar.make(view, jObjError.getString("message"), Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    } else {
                        Snackbar.make(view, getString(R.string.actividad_alta_fail), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    FirebaseCrash.log("Error al mandar la información plan semanal " + response.code());
                    Snackbar.make(view, getString(R.string.actividad_alta_fail), Snackbar.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR_TIMEPICKERDIALOG", t.toString());
                FirebaseCrash.log("Error al mandar la información plan semanal");
                FirebaseCrash.report(t);

                /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                Toast.makeText(context, getString(R.string.actividad_alta_offline), Toast.LENGTH_LONG).show();

                GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                        System.currentTimeMillis(),
                        Valores.ID_ENVIO_ALTA_ACTIVIDAD,
                        prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""),
                        json,
                        Valores.ESTATUS_NO_ENVIADO
                );
                GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                /**********************************************************************/

                progressDialog.dismiss();

                //Indica que se regresará dos veces (hasta la pantalla del Calendario).
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK, true);

                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                //Se regresa a la pantalla del Calendario.
                Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                startActivity(intent);
            }
        });
    }

    public void mandarInformacionActividadAdministrativa(JsonAltaActividadesAdministrativas jsonAltaActividadesAdministrativas) {

        //Se muestra el ProgressDialog.
        mostrarProgressDialog();

        //El ID_TIPO_ASIGNACION_ADMINISTRATIVAS indica que la actividad que se está dando de alta es
        //Administrativa. El valor de esta variable es 2 y nunca cambia.
        Call<ResponseBody> sendActividadAdministrativaAlta = apiInterface.setActividadAdministrativa(Valores.ID_TIPO_ASIGNACION_ADMINISTRATIVAS,
                prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""), jsonAltaActividadesAdministrativas);

        final String json = new Gson().toJson(jsonAltaActividadesAdministrativas);

        sendActividadAdministrativaAlta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    Toast.makeText(context, getString(R.string.actividad_alta_success), Toast.LENGTH_LONG).show();

                    //Indica que se regresará dos veces (hasta la pantalla del Calendario).
                    tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK, true);

                    enviarCheckIn();

                    progressDialog.dismiss();

                    //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                    //Se regresa a la pantalla del Calendario.
                    Intent intent = new Intent();
                    intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                    startActivity(intent);

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else if (response.code() == 400) {
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Snackbar.make(view, jObjError.getString("message"), Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    } else {
                        Snackbar.make(view, getString(R.string.actividad_alta_fail), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    FirebaseCrash.log("Error al mandar la información plan semanal " + response.code());
                    Snackbar.make(view, getString(R.string.actividad_alta_fail), Snackbar.LENGTH_LONG).show();

                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR_TIMEPICKERDIALOG", t.toString());
                FirebaseCrash.log("Error al mandar la información plan semanal");
                FirebaseCrash.report(t);

                /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                Toast.makeText(context, getString(R.string.actividad_alta_offline), Toast.LENGTH_LONG).show();

                GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                        System.currentTimeMillis(),
                        Valores.ID_ENVIO_ALTA_ACTIVIDAD_ADMINISTRATIVA,
                        prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""),
                        json,
                        Valores.ESTATUS_NO_ENVIADO
                );
                GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                /**********************************************************************/

                progressDialog.dismiss();

                //Indica que se regresará dos veces (hasta la pantalla del Calendario).
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK, true);

                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                //Se regresa a la pantalla del Calendario.
                Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                startActivity(intent);
            }
        });

    }

    public void mostrarProgressDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Espere un momento por favor...");
        progressDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            //Ejecuta el método onBackPressed() de la actividad madre.
            Funciones.onBackPressedFunction(context, true);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void alertDialogPositive(String idDialog) {

        switch (idDialog) {
            case Valores.ID_COLOCAR_COMENTARIO:

                //El comentario colocado se guarda en jsonAltaActividadesAdministrativas;
                jsonAltaActividadesAdministrativas.setComentarios(prefs.getString(Valores.SHARED_PREFERENCES_COLOCAR_COMENTARIOS, ""));

                tinyDB.putJsonAltaActividadesAdministrativas(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS,
                        jsonAltaActividadesAdministrativas);

                mandarInformacionActividadAdministrativa(jsonAltaActividadesAdministrativas);

                break;
        }

    }

    @Override
    public void alertDialogNegative(String idDialog) {

        switch (idDialog) {
            case Valores.ID_COLOCAR_COMENTARIO:

                //El comentario colocado se guarda en jsonAltaActividadesAdministrativas;
                jsonAltaActividadesAdministrativas.setComentarios(prefs.getString(Valores.SHARED_PREFERENCES_COLOCAR_COMENTARIOS, ""));

                tinyDB.putJsonAltaActividadesAdministrativas(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS,
                        jsonAltaActividadesAdministrativas);

                mandarInformacionActividadAdministrativa(jsonAltaActividadesAdministrativas);

                break;
        }
    }

    @Override
    public void alertDialogNeutral(String idDialog) {

    }

    public void enviarCheckIn(){

        LocationTracker gps;
        Calendar calendarCoordenada;
        MenuRealm realmDB;
        //Objeto que maneja Realml.
        realmDB = new MenuRealm();

        calendarCoordenada = Calendar.getInstance();

        int accion=3;
        List<CatalogoAccionDB> accionListAll = CatalogoAccionRealm.mostrarListaAccion();
        if(accionListAll!=null) {
            for (CatalogoAccionDB acciones : accionListAll) {
                Log.d("catoloaccion", acciones.getDescripcion());
                if (acciones.getDescripcion().equals("Agenda Actividades")) {
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
