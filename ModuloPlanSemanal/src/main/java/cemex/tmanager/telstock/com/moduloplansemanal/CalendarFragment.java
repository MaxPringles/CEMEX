package cemex.tmanager.telstock.com.moduloplansemanal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.tibolte.agendacalendarview.AgendaCalendarView;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cemex.tmanager.telstock.com.moduloplansemanal.funciones.TinyDB;
import cemex.tmanager.telstock.com.moduloplansemanal.model.JSONfiltro;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiClient;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiInterface;
import cemex.tmanager.telstock.com.moduloplansemanal.util.DrawableCalendarEvent;
import cemex.tmanager.telstock.com.moduloplansemanal.util.DrawableEventRenderer;

import mx.com.tarjetasdelnoreste.realmdb.JsonMostrarActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonFiltroVendedorAsignado;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Actividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas.JsonAltaActividadesAdministrativas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades.JsonMostrarActividades;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Cesar on 17/09/2016.
 */
public class CalendarFragment extends Fragment
        implements CalendarPickerController {

    Context context;

    AgendaCalendarView agendaCalendarView;
    ApiInterface apiInterface;

    ProgressBar progressBar;
    TinyDB tinyDB;

    boolean flagAltaActividad = false;
    private String actividadAdministrativa;
    private String verCalendarioSoloVisualizacion;

    //Se declara la SharedPreferences.
    SharedPreferences prefs;

    View view;
    Toolbar toolbar;
int cargar=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar, container, false);

        context = getActivity();
        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

        //Se recupera el objeto llenado previamente en PlanSemanalFragment.
        tinyDB = new TinyDB(context);

        //Se inicializa la interface de Retrofit.
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se recibe el tipo de actividad a dar de alta (en caso de que sea administrativa).
        actividadAdministrativa = getArguments().getString(Valores.BUNDLE_CALENDAR, "");
        verCalendarioSoloVisualizacion = getArguments().getString(Valores.BUNDLE_IR_A_CALENDARIO, "");

        //Se coloca el Toolbar.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.plan_titulo));

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        agendaCalendarView = (AgendaCalendarView) view.findViewById(R.id.agenda_calendar_view);

        //obtenerActividadProspectos();
        obtenerTodasActividades();

        return view;
    }

    public void obtenerTodasActividades() {

        //Se muestra el Spinner mientras se cargan las actividades.
        visibilidad(View.VISIBLE);

        JsonFiltroVendedorAsignado jsonfiltro = new JsonFiltroVendedorAsignado();

        jsonfiltro.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));

        Call<List<JsonMostrarActividades>> jsonMostrarActividadesTodoCall = apiInterface.getActividadesTodo(jsonfiltro);

        String json = new Gson().toJson(jsonfiltro);

        jsonMostrarActividadesTodoCall.enqueue(new Callback<List<JsonMostrarActividades>>() {
            @Override
            public void onResponse(Call<List<JsonMostrarActividades>> call, Response<List<JsonMostrarActividades>> response) {
                if (response.body() != null && response.code() == 200) {

                    JsonMostrarActividadesRealm.guardarListaActividadestodo(response.body());

                    prepararCalendario();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    prepararCalendario();
                }
            }

            @Override
            public void onFailure(Call<List<JsonMostrarActividades>> call, Throwable t) {
                Log.d("", "");
                prepararCalendario();
            }
        });

    }

    public void prepararCalendario() {

        //Se coloca el rango del calendario.
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -1);
        maxDate.add(Calendar.MONTH, 1);

        List<CalendarEvent> eventList = new ArrayList<>();

        mockList(eventList);

        agendaCalendarView.init(eventList, minDate, maxDate, new Locale("es", "MX"), this);
        agendaCalendarView.addEventRenderer(new DrawableEventRenderer());
    }

    public void mockList(List<CalendarEvent> eventList) {

        //List<ProspectosDB> prospectosDBList = ProspectosRealm.mostrarListaProspectosConActividad();
        Calendar fechaInicio;
        Calendar fechaFin;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        List<DrawableCalendarEvent> listDrawableCalendar = new ArrayList<>();

        //Recupera todas las citas por orden de fecha.
        List<JsonMostrarActividades> planSemanalDBList = JsonMostrarActividadesRealm.mostrarListaActividadesTodo();
        String nombreObra;
        String tipoProspecto;
        boolean estaDescartado;

        for (JsonMostrarActividades planSemanalDB : planSemanalDBList) {
            try {

                fechaInicio = Calendar.getInstance();
                fechaFin = Calendar.getInstance();

                fechaInicio.setTime(format.parse(planSemanalDB.getFechaHoraCitaInicio()));
                fechaFin.setTime(format.parse(planSemanalDB.getFechaHoraCitaFin()));

                if (planSemanalDB.getProspecto() == null) {
                    nombreObra = "";
                    tipoProspecto = "";
                    estaDescartado = false;
                } else {
                    nombreObra = planSemanalDB.getProspecto().getObra() + " - " + planSemanalDB.getProspecto().getNombre();
                    tipoProspecto = planSemanalDB.getProspecto().getTipoProspecto().getDescripcion();
                    estaDescartado = planSemanalDB.getProspecto().getEstaDescartado();
                }

                //Indica que NO es actividadAdministrativa.
                if (planSemanalDB.getTipoActividad().getIdPadre() == Valores.ID_PADRE_ACTIVIDADES_VENTA) {

                    listDrawableCalendar.add(new DrawableCalendarEvent(
                            "", "", "", //Cadenas por default que usa la librería.
                            Color.CYAN,
                            fechaInicio,
                            fechaFin,
                            true,
                            tipoProspecto,
                            colocarHora(fechaInicio, fechaFin),
                            planSemanalDB.getTipoActividad().getDescripcion(),
                            nombreObra,
                            estaDescartado,
                            planSemanalDB.getEstatusAgenda(),
                            planSemanalDB.getTipoActividad().getIdPadre()));

                } else { //Indica que SÍ es actividadAdministrativa.
                    listDrawableCalendar.add(new DrawableCalendarEvent(
                            "", "", "", //Cadenas por default que usa la librería.
                            Color.CYAN,
                            fechaInicio,
                            fechaFin,
                            true,
                            planSemanalDB.getTipoActividad().getDescripcion(),
                            colocarHora(fechaInicio, fechaFin),
                            planSemanalDB.getComentario(),
                            nombreObra,
                            estaDescartado,
                            planSemanalDB.getEstatusAgenda(),
                            planSemanalDB.getTipoActividad().getIdPadre()));
                }

            } catch (Exception e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
            }
        }

        eventList.addAll(listDrawableCalendar);

        //se quita el Spinner y se muestra el calendario.
        visibilidad(View.GONE);
    }

    public String colocarHora(Calendar calendarInicio, Calendar calendarFin) {

        String horaInicio;
        String minutoInicio;
        String horaFin;
        String minutoFin;

        if (calendarInicio.get(Calendar.HOUR_OF_DAY) >= 10) {
            horaInicio = calendarInicio.get(Calendar.HOUR_OF_DAY) + "";
        } else {
            horaInicio = "0" + calendarInicio.get(Calendar.HOUR_OF_DAY);
        }

        if (calendarInicio.get(Calendar.MINUTE) >= 10) {
            minutoInicio = calendarInicio.get(Calendar.MINUTE) + "";
        } else {
            minutoInicio = "0" + calendarInicio.get(Calendar.MINUTE);
        }

        if (calendarFin.get(Calendar.HOUR_OF_DAY) >= 10) {
            horaFin = calendarFin.get(Calendar.HOUR_OF_DAY) + "";
        } else {
            horaFin = "0" + calendarFin.get(Calendar.HOUR_OF_DAY);
        }

        if (calendarFin.get(Calendar.MINUTE) >= 10) {
            minutoFin = calendarFin.get(Calendar.MINUTE) + "";
        } else {
            minutoFin = "0" + calendarFin.get(Calendar.MINUTE);
        }

        return horaInicio + ":" + minutoInicio + " - " + horaFin + ":" + minutoFin;
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("CalendarFragment", String.format("Selected day: %s", dayItem));

        //Revisa si el calendario es sólo para visualización o se quiere dar de alta una actividad.
        if (verCalendarioSoloVisualizacion.equals("")) {
            try {

                if (validarFechaAtrasada(dayItem.getDate())) {

                    //Indica que NO es actividadAdministrativa.
                    if (actividadAdministrativa.equals("")) {
                        //se muestra cargando para avitar abri 2 actividades
                        visibilidad(View.VISIBLE);
                        cargar=1;
                        JsonAltaActividades jsonAltaActividades = tinyDB.getJsonAltaActividades(
                                Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES, JsonAltaActividades.class);
                        Actividad actividad = jsonAltaActividades.getActividad();
                        actividad.setHoraInicioSupport(dayItem.getDate()); //Se guarda el atributo de soporte para fechas.

                        jsonAltaActividades.setActividad(actividad);

                        tinyDB.putJsonAltaActividades(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES, jsonAltaActividades);

                        //Revisa si el fragmento está añadido a la actividad madre.
                        if (isAdded()) {
                            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                            Intent intent = new Intent();
                            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_TIMEPICKER_INICIO);
                            startActivity(intent);

                        }

                    } else { //Indica que SÍ es actividadAdministrativa.

                        JsonAltaActividadesAdministrativas jsonAltaActividadesAdministrativas
                                = tinyDB.getJsonAltaActividadesAdministrativas(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS,
                                JsonAltaActividadesAdministrativas.class);

                        //Se guarda el atributo de soporte para fechas.
                        jsonAltaActividadesAdministrativas.setHoraInicioSupport(dayItem.getDate());

                        tinyDB.putJsonAltaActividadesAdministrativas(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS,
                                jsonAltaActividadesAdministrativas);

                        //Revisa si el fragmento está añadido a la actividad madre.
                        if (isAdded()) {
                            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                            //Nótese que se manda el bundle extra, para saber que se sigue manejando una
                            //actividad administrativa.
                            Intent intent = new Intent();
                            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_TIMEPICKER_INICIO);
                            intent.putExtra(Valores.BUNDLE_CALENDAR, Valores.BUNDLE_ACTIVIDADES_ADMINISTRATIVAS);
                            startActivity(intent);
                        }
                    }

                } else {
                    Snackbar.make(view, getString(R.string.plan_calendar_fecha_atrasada), Snackbar.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                //Significa que no existe esa llave de SharedPreference aún.
                FirebaseCrash.report(e);
                Snackbar.make(view, getString(R.string.plan_calendar_fecha_fail), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public boolean validarFechaAtrasada(Date dateItem) {

        Calendar calendarActual = Calendar.getInstance();
        Calendar calendarSeleccionado = Calendar.getInstance();

        calendarActual.setTimeInMillis(System.currentTimeMillis());
        calendarSeleccionado.setTime(dateItem);

        calendarActual.set(Calendar.HOUR_OF_DAY, 0);
        calendarActual.set(Calendar.MINUTE, 0);
        calendarActual.set(Calendar.SECOND, 0);
        calendarActual.set(Calendar.MILLISECOND, 0);

        calendarSeleccionado.set(Calendar.HOUR_OF_DAY, 0);
        calendarSeleccionado.set(Calendar.MINUTE, 0);
        calendarSeleccionado.set(Calendar.SECOND, 0);
        calendarSeleccionado.set(Calendar.MILLISECOND, 0);

        if (calendarSeleccionado.getTimeInMillis() >= calendarActual.getTimeInMillis()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        Log.d("CalendarFragment", String.format("Selected event: %s", event));
    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
        }*/
    }

    /**
     * MÉTODO QUE ESCONDE EL PROGRESSBAR Y MUESTRA EL LAYOUT CON CONTENIDO
     **/
    public void visibilidad(int visibility) {

        try {
            progressBar.setVisibility(visibility);
            agendaCalendarView.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            //En caso de que algún proceso ya haya escondido el progressBar antes.
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            //Se forza el regreso a Citas/Visitas.
            if (flagAltaActividad) {
                Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                startActivity(intent);
            } else {
                //Ejecuta el método onBackPressed() de la actividad madre.
                Funciones.onBackPressedFunction(context, true);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override

    public void onStart() {
        super.onStart();

        if (tinyDB.getBoolean(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK)) {

            //Indica que se hará el regreso forzado a Citas/Visitas.
            flagAltaActividad = true;
        }
        //Se elminia la SharedPreference, para que seguir el flujo de modo normal.
        tinyDB.remove(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK);
    }

    @Override
    public void onResume() {
        super.onResume();

//validacion para ocultar el sinbolo de cargando
        if(cargar==1)
        {
            visibilidad(View.GONE);
        }
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        if (flagAltaActividad) {

                            //Se forza el regreso a Citas/Visitas.
                            Intent intent = new Intent();
                            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                            startActivity(intent);
                        } else {
                            Funciones.onBackPressedFunction(context, true);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
    }
}
