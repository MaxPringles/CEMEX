package mx.com.tarjetasdelnoreste.realmdb.funciones;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import mx.com.tarjetasdelnoreste.realmdb.AlarmasActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonMostrarActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.model.AlarmasActividadesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades.JsonMostrarActividades;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by usr_micro13 on 14/12/2016.
 */

public class AlarmasActividades {

    Context context;

    public AlarmasActividades(Context context) {
        this.context = context;
    }

    /** Adds Events and Reminders in Calendar. */
    public void configurarAlarmasActividades() {

        //Se recuperan las últimas actividades que tiene el prospecto.
        List<PlanSemanalDB> planSemanalDB;
        planSemanalDB = PlanSemanalRealm.mostrarListaPlanSemanalUltimasCitas();

        //Variables para poner la fecha de la actividad en el calendario.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar calendarInicio;
        Calendar calendarFin;

        //Lista con las uri's de alarmas guardadas previamente.
        List<AlarmasActividadesDB> alarmasGuardadasAnteriormente = AlarmasActividadesRealm.mostrarListaAlarmasActividades();

        List<AlarmasActividadesDB> alarmasActividadesDBList = new ArrayList<>();
        AlarmasActividadesDB alarmasActividadesDB;

        //Uri's que guardan un evento nuevo, o actualizan uno ya existente.
        Uri event;

        for (PlanSemanalDB fechas : planSemanalDB) {
            if (fechas.getHoraInicio() != null && fechas.getHoraFin() != null) {

                alarmasActividadesDB = new AlarmasActividadesDB();

                calendarInicio = Calendar.getInstance();
                calendarFin = Calendar.getInstance();

                try {
                    calendarInicio.setTime(format.parse(fechas.getHoraInicio()));
                    calendarFin.setTime(format.parse(fechas.getHoraFin()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events");
                ContentResolver cr = context.getContentResolver();
                TimeZone timeZone = TimeZone.getDefault();

                /** Inserting an event in calendar. */
                ContentValues values = new ContentValues();
                values.put(CalendarContract.Events.CALENDAR_ID, 1);
                values.put(CalendarContract.Events.TITLE, fechas.getObra() + " - " + fechas.getCliente());
                values.put(CalendarContract.Events.DESCRIPTION, fechas.getDescripcionObra());
                values.put(CalendarContract.Events.ALL_DAY, 0);
                //Inicio del evento.
                values.put(CalendarContract.Events.DTSTART, calendarInicio.getTimeInMillis());
                //Fin del evento.
                values.put(CalendarContract.Events.DTEND, calendarFin.getTimeInMillis());
                values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                values.put(CalendarContract.Events.HAS_ALARM, 1);

                event = revisarUriExistente(fechas.getIdProspecto(), alarmasGuardadasAnteriormente);

                if (event != null) {
                    //En caso de que ya exista alguna alarma de ese prospecto, entonces se actualiza.
                    context.getContentResolver().update(event, values, null, null);
                } else {
                    //En caso de que no haya alarmas previas del prospecto, se crea una nueva.
                    event = cr.insert(EVENTS_URI, values);

                    // Display event id.
                    //Toast.makeText(context, "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT).show();

                    /** Adding reminder for event added. */
                    Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
                    values = new ContentValues();
                    values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
                    values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                    values.put(CalendarContract.Reminders.MINUTES, 15);
                    cr.insert(REMINDERS_URI, values);

                    //Se guardan los parámetros de la alarma en el objeto AlarmasActividadesDB.
                    alarmasActividadesDB.setIdProspectoAlarma(fechas.getIdProspecto());
                    alarmasActividadesDB.setUriAlarma(event.toString()); //Transforma Uri en String para poder guardar en Realm.
                    alarmasActividadesDBList.add(alarmasActividadesDB);
                }
            }
        }

        //Se guardan los parámetros de las alarmas en Realm.
        if (alarmasActividadesDBList.size() > 0) {
            AlarmasActividadesRealm.guardarListaAlarmasActividades(alarmasActividadesDBList);
        }

        //Se programan las notificaciones de recordatorio de las Actividades Administrativas.
        configurarAlarmasActividadesAdministrativas();
    }

    /** Adds Events and Reminders in Calendar. */
    public void configurarAlarmasActividadesAdministrativas() {

        //Se recuperan las últimas actividades que tiene el prospecto.
        List<JsonMostrarActividades> planSemanalDB = JsonMostrarActividadesRealm.mostrarListaActividadesTodo();

        //Variables para poner la fecha de la actividad en el calendario.
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar calendarInicio;
        Calendar calendarFin;

        //Lista con las uri's de alarmas guardadas previamente.
        List<AlarmasActividadesDB> alarmasGuardadasAnteriormente = AlarmasActividadesRealm.mostrarListaAlarmasActividades();

        List<AlarmasActividadesDB> alarmasActividadesDBList = new ArrayList<>();
        AlarmasActividadesDB alarmasActividadesDB;

        //Uri's que guardan un evento nuevo, o actualizan uno ya existente.
        Uri event;

        for (JsonMostrarActividades fechas : planSemanalDB) {

            if (fechas.getTipoActividad().getIdPadre() != Valores.ID_PADRE_ACTIVIDADES_VENTA) {

                if (fechas.getFechaHoraCitaInicio() != null && fechas.getFechaHoraCitaFin() != null) {

                    alarmasActividadesDB = new AlarmasActividadesDB();

                    calendarInicio = Calendar.getInstance();
                    calendarFin = Calendar.getInstance();

                    try {
                        calendarInicio.setTime(format.parse(fechas.getFechaHoraCitaInicio()));
                        calendarFin.setTime(format.parse(fechas.getFechaHoraCitaFin()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Uri EVENTS_URI = Uri.parse(getCalendarUriBase(true) + "events");
                    ContentResolver cr = context.getContentResolver();
                    TimeZone timeZone = TimeZone.getDefault();

                    /** Inserting an event in calendar. */
                    ContentValues values = new ContentValues();
                    values.put(CalendarContract.Events.CALENDAR_ID, 1);
                    values.put(CalendarContract.Events.TITLE, fechas.getTipoActividad().getDescripcion());
                    values.put(CalendarContract.Events.DESCRIPTION, fechas.getComentario());
                    values.put(CalendarContract.Events.ALL_DAY, 0);
                    //Inicio del evento.
                    values.put(CalendarContract.Events.DTSTART, calendarInicio.getTimeInMillis());
                    //Fin del evento.
                    values.put(CalendarContract.Events.DTEND, calendarFin.getTimeInMillis());
                    values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
                    values.put(CalendarContract.Events.HAS_ALARM, 1);

                    event = revisarUriExistente(fechas.getId(), alarmasGuardadasAnteriormente);

                    if (event != null) {
                        //En caso de que ya exista alguna alarma de ese prospecto, entonces se actualiza.
                        context.getContentResolver().update(event, values, null, null);
                    } else {
                        //En caso de que no haya alarmas previas del prospecto, se crea una nueva.
                        event = cr.insert(EVENTS_URI, values);

                        // Display event id.
                        //Toast.makeText(context, "Event added :: ID :: " + event.getLastPathSegment(), Toast.LENGTH_SHORT).show();

                        /** Adding reminder for event added. */
                        Uri REMINDERS_URI = Uri.parse(getCalendarUriBase(true) + "reminders");
                        values = new ContentValues();
                        values.put(CalendarContract.Reminders.EVENT_ID, Long.parseLong(event.getLastPathSegment()));
                        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                        values.put(CalendarContract.Reminders.MINUTES, 15);
                        cr.insert(REMINDERS_URI, values);

                        //Se guardan los parámetros de la alarma en el objeto AlarmasActividadesDB.
                        alarmasActividadesDB.setIdProspectoAlarma(fechas.getId());
                        alarmasActividadesDB.setUriAlarma(event.toString()); //Transforma Uri en String para poder guardar en Realm.
                        alarmasActividadesDBList.add(alarmasActividadesDB);
                    }
                }
            }
        }

        //Se guardan los parámetros de las alarmas en Realm.
        if (alarmasActividadesDBList.size() > 0) {
            AlarmasActividadesRealm.guardarListaAlarmasActividades(alarmasActividadesDBList);
        }
    }


    /** MÉTODO QUE REVISA SI YA EXISTE UNA URI DE ALARMA PARA UN PROSPECTO ESPECÍFICO **/
    public Uri revisarUriExistente(String idProspecto, List<AlarmasActividadesDB> alarmasGuardadasAnteriormente) {

        //Revisa si el prospecto ya tenía guardada alguna cita en el calendario.
        for (AlarmasActividadesDB alarmas : alarmasGuardadasAnteriormente) {
            if (alarmas.getIdProspectoAlarma().equals(idProspecto)) {
                //Devuelve la uri de la actividad del prospecto ya existente.
                return Uri.parse(alarmas.getUriAlarma()); //Transforma el String en Uri.
            }
        }

        //Si no existen uri's del prospecto previamente, entonces se devuelve null.
        return null;
    }

    /** Returns Calendar Base URI, supports both new and old OS. */
    private String getCalendarUriBase(boolean eventUri) {
        Uri calendarURI = null;
        try {
            if (android.os.Build.VERSION.SDK_INT <= 7) {
                calendarURI = (eventUri) ? Uri.parse("content://calendar/") : Uri.parse("content://calendar/calendars");
            } else {
                calendarURI = (eventUri) ? Uri.parse("content://com.android.calendar/") : Uri
                        .parse("content://com.android.calendar/calendars");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendarURI.toString();
    }
}
