package cemex.tmanager.telstock.com.moduloplansemanal;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.crash.FirebaseCrash;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cemex.tmanager.telstock.com.moduloplansemanal.funciones.TinyDB;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiClient;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiInterface;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Actividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas.JsonAltaActividadesAdministrativas;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by Cesar on 18/09/2016.
 */
public class TimePickerFragment extends Fragment {

    private Context context;

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
    Calendar calendar;
    String datetime;

    Dialog dialogModal; //Modal que muestra mensaje de traslape de evento.

    ApiInterface apiInterface;
    private String actividadAdministrativa;

    View view;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_time_picker, container, false);

        context = getActivity();
        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        //Se inicializa la interface de Retrofit.
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se recibe el tipo de actividad a dar de alta (en caso de que sea administrativa).
        actividadAdministrativa = getArguments().getString(Valores.BUNDLE_CALENDAR, "");

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
                + " " + getString(R.string.plan_time_picker_title_inicio));

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
        calendar = Calendar.getInstance();
        //Indica que NO es actividadAdministrativa.
        if (actividadAdministrativa.equals("")) {
            calendar.setTime(actividad.getHoraInicioSupport());
        } else { //Indica que SÍ es actividadAdministrativa.
            calendar.setTime(jsonAltaActividadesAdministrativas.getHoraInicioSupport());
        }

        //Formato en que se mostrará la fecha.
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM yyyy");

        txt_numero_dia.setText(calendar.get(Calendar.DAY_OF_MONTH) + "");
        txt_fecha_dia.setText(dateFormat.format(calendar.getTime()));

        btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Indica que se muestre el calendario normalmente (sin cargar desde cero).
                //tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_CALENDARIO_NORMAL, true);
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
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

                    try {
                        datetime = dateFormat.format(calendar.getTime());

                        if (revisarTraslapes(calendar)) {
                            if (validarHoraAtrasada(calendar)) {

                                avanzarHoraFin();
                            } else {
                                Snackbar.make(view, getString(R.string.plan_calendar_hora_atrasada), Snackbar.LENGTH_LONG).show();
                            }
                        }

                    } catch (Exception e) {
                        Log.e("TIMEPICKERFRAGMENT", e.toString());
                        //Significa que no existe esa llave de SharedPreference aún.
                        Snackbar.make(v, getString(R.string.plan_time_picker_fail), Snackbar.LENGTH_LONG).show();
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

    public void avanzarHoraFin() {

        //Indica que NO es actividadAdministrativa.
        if (actividadAdministrativa.equals("")) {
            //Se agrega la fecha de inicio en el objeto de PlanSemanalDB.
            actividad.setFechaHoraCitaInicio(datetime); //Se guarda la fecha de inicio.
            jsonAltaActividades.setActividad(actividad);

            //El objeto modificado se inserta nuevamente en TinyDB.
            tinyDB.putJsonAltaActividades(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES, jsonAltaActividades);

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_TIMEPICKER_FIN);
            startActivity(intent);

        } else { //Indica que SÍ es actividadAdministrativa.
            jsonAltaActividadesAdministrativas.setFechaHoraCitaInicio(datetime);

            tinyDB.putJsonAltaActividadesAdministrativas(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS,
                    jsonAltaActividadesAdministrativas);

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_TIMEPICKER_FIN);
            intent.putExtra(Valores.BUNDLE_CALENDAR, Valores.BUNDLE_ACTIVIDADES_ADMINISTRATIVAS);
            startActivity(intent);
        }
    }

    public boolean revisarTraslapes(Calendar calendarOriginal) {

        List<PlanSemanalDB> planSemanalDB = PlanSemanalRealm.mostrarListaPlanSemanal();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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

                        //Se colocan segundos y milisegundos en 0 para hacer una correcta comparación.
                        calendarInicioDB.set(Calendar.SECOND, 0);
                        calendarInicioDB.set(Calendar.MILLISECOND, 0);
                        calendarFinDB.set(Calendar.SECOND, 0);
                        calendarFinDB.set(Calendar.MILLISECOND, 0);

                        if (calendarOriginal.getTimeInMillis() >= calendarInicioDB.getTimeInMillis()
                                && calendarOriginal.getTimeInMillis() < calendarFinDB.getTimeInMillis()) {

                            String mensajeModal = "Lo sentimos, la hora de inicio seleccionada se cruza con el evento: \n" +
                                    planSemanalDB.get(i).getObra() + " - " + planSemanalDB.get(i).getCliente() + "\n\n" +
                                    "\u2022 Horario: " + agregarCero(calendarInicioDB.get(Calendar.HOUR_OF_DAY)) + ":" + agregarCero(calendarInicioDB.get(Calendar.MINUTE)) +
                                    " - " + agregarCero(calendarFinDB.get(Calendar.HOUR_OF_DAY)) + ":" + agregarCero(calendarFinDB.get(Calendar.MINUTE));

                            showModalTraslape(mensajeModal);

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

    public boolean validarHoraAtrasada(Calendar calendarSeleccionado) {

        Calendar calendarActual = Calendar.getInstance();
        calendarActual.setTimeInMillis(System.currentTimeMillis());

        calendarActual.set(Calendar.SECOND, 0);
        calendarActual.set(Calendar.MILLISECOND, 0);

        if (calendarSeleccionado.getTimeInMillis() >= calendarActual.getTimeInMillis()) {
            return true;
        } else {
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

    public void showModalTraslape(String mensaje) {

        dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_plan_traslape);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        //dialogModal.getWindow().getAttributes().windowAnimations = com.telstock.tmanager.cemex.prospectos.R.style.DialogAnimation;
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_txt_message = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        TextView dialog_btn_aceptar = (TextView) dialogModal.findViewById(R.id.dialog_btn_aceptar);

        dialog_txt_message.setText(mensaje);

        dialog_btn_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModal.dismiss();
            }
        });

        dialogModal.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            //Indica que se muestre el calendario normalmente (sin cargar desde cero).
            //tinyDB.putBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_CALENDARIO_NORMAL, true);
            //Ejecuta el método onBackPressed() de la actividad madre.
            Funciones.onBackPressedFunction(context, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
