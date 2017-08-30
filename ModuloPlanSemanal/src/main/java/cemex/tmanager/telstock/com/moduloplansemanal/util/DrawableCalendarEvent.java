package cemex.tmanager.telstock.com.moduloplansemanal.util;

/**
 * Created by Cesar on 17/09/2016.
 */

import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;

import java.util.Calendar;

public class DrawableCalendarEvent extends BaseCalendarEvent {
    private int mDrawableId;

    private String txt_calendar_tarea;
    private String txt_calendar_hora;
    private String txt_calendar_tipo_prospecto;
    private String txt_calendar_nombre_obra;
    private boolean estaDescartado;
    private int estatusAgenda;
    private int tipoVenta;

    // region Constructors

    public DrawableCalendarEvent(long id, int color, String title, String description, String location, long dateStart, long dateEnd, int allDay, String duration, int drawableId) {
        super(id, color, title, description, location, dateStart, dateEnd, allDay, duration);
        this.mDrawableId = drawableId;
    }

    public DrawableCalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay, int drawableId) {
        super(title, description, location, color, startTime, endTime, allDay);
        this.mDrawableId = drawableId;
    }

    public DrawableCalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay) {
        super(title, description, location, color, startTime, endTime, allDay);
    }

    public DrawableCalendarEvent(String title, String description, String location, int color, Calendar startTime, Calendar endTime, boolean allDay,
                                 String txt_calendar_tarea, String txt_calendar_hora, String txt_calendar_tipo_prospecto, String txt_calendar_nombre_obra,
                                 boolean estaDescartado, int estatusAgenda, int tipoVenta) {
        super(title, description, location, color, startTime, endTime, allDay);

        this.txt_calendar_tarea = txt_calendar_tarea;
        this.txt_calendar_hora = txt_calendar_hora;
        this.txt_calendar_tipo_prospecto = txt_calendar_tipo_prospecto;
        this.txt_calendar_nombre_obra = txt_calendar_nombre_obra;
        this.estaDescartado = estaDescartado;
        this.estatusAgenda = estatusAgenda;
        this.tipoVenta = tipoVenta;
    }

    public DrawableCalendarEvent(DrawableCalendarEvent calendarEvent) {
        super(calendarEvent);
        this.mDrawableId = calendarEvent.getDrawableId();
        this.txt_calendar_tarea = calendarEvent.getTxt_calendar_tarea();
        this.txt_calendar_nombre_obra = calendarEvent.getTxt_calendar_nombre_obra();
        this.txt_calendar_tipo_prospecto = calendarEvent.getTxt_calendar_tipo_prospecto();
        this.txt_calendar_hora = calendarEvent.getTxt_calendar_hora();
        this.estaDescartado = calendarEvent.isEstaDescartado();
        this.estatusAgenda = calendarEvent.getEstatusAgenda();
        this.tipoVenta = calendarEvent.getTipoVenta();
    }

    // endregion

    // region Public methods


    public String getTxt_calendar_tarea() {
        return txt_calendar_tarea;
    }

    public void setTxt_calendar_tarea(String txt_calendar_tarea) {
        this.txt_calendar_tarea = txt_calendar_tarea;
    }

    public String getTxt_calendar_hora() {
        return txt_calendar_hora;
    }

    public void setTxt_calendar_hora(String txt_calendar_hora) {
        this.txt_calendar_hora = txt_calendar_hora;
    }

    public String getTxt_calendar_tipo_prospecto() {
        return txt_calendar_tipo_prospecto;
    }

    public void setTxt_calendar_tipo_prospecto(String txt_calendar_tipo_prospecto) {
        this.txt_calendar_tipo_prospecto = txt_calendar_tipo_prospecto;
    }

    public String getTxt_calendar_nombre_obra() {
        return txt_calendar_nombre_obra;
    }

    public void setTxt_calendar_nombre_obra(String txt_calendar_nombre_obra) {
        this.txt_calendar_nombre_obra = txt_calendar_nombre_obra;
    }

    public boolean isEstaDescartado() {
        return estaDescartado;
    }

    public void setEstaDescartado(boolean estaDescartado) {
        this.estaDescartado = estaDescartado;
    }

    public int getEstatusAgenda() {
        return estatusAgenda;
    }

    public void setEstatusAgenda(int estatusAgenda) {
        this.estatusAgenda = estatusAgenda;
    }

    public int getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(int tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public int getDrawableId() {
        return mDrawableId;
    }

    public void setDrawableId(int drawableId) {
        this.mDrawableId = drawableId;
    }

    // endregion

    // region Class - BaseCalendarEvent

    @Override
    public CalendarEvent copy() {
        return new DrawableCalendarEvent(this);
    }

    // endregion
}