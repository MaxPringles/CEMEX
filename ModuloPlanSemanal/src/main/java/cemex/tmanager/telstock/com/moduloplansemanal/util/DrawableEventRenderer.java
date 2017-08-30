package cemex.tmanager.telstock.com.moduloplansemanal.util;

/**
 * Created by Cesar on 17/09/2016.
 */
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.tibolte.agendacalendarview.render.EventRenderer;

import cemex.tmanager.telstock.com.moduloplansemanal.R;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

public class DrawableEventRenderer extends EventRenderer<DrawableCalendarEvent> {

    // region Class - EventRenderer

    TextView txt_calendar_tarea;
    TextView txt_calendar_tipo_prospecto;
    TextView txt_calendar_nombre_obra;
    TextView txt_calendar_hora;
    TextView txt_calendar_estatus;
    View view_calendar_tipo_venta;

    @Override
    public void render(View view, DrawableCalendarEvent event) {
        txt_calendar_tarea = (TextView) view.findViewById(R.id.txt_calendar_tarea);
        txt_calendar_tipo_prospecto = (TextView) view.findViewById(R.id.txt_calendar_tipo_prospecto);
        txt_calendar_nombre_obra = (TextView) view.findViewById(R.id.txt_calendar_nombre_obra);
        txt_calendar_hora = (TextView) view.findViewById(R.id.txt_calendar_hora);
        txt_calendar_estatus = (TextView) view.findViewById(R.id.txt_calendar_estatus);
        view_calendar_tipo_venta = view.findViewById(R.id.view_calendar_tipo_venta);

        txt_calendar_tarea.setText(event.getTxt_calendar_tarea());
        txt_calendar_tipo_prospecto.setText(event.getTxt_calendar_tipo_prospecto());
        txt_calendar_nombre_obra.setText(event.getTxt_calendar_nombre_obra());
        txt_calendar_hora.setText(event.getTxt_calendar_hora());

        //Revisa si está descartado para poner el texto correspondiente.
        if (event.isEstaDescartado()) {
            txt_calendar_estatus.setText("Descartado");
        } else {
            //En caso de no estar descartado, colocar ícono de reagendado si es necesario.
            if (event.getEstatusAgenda() == Valores.ID_ACTIVIDAD_REAGENDADA) {
                txt_calendar_estatus.setText("Reagendado");
            } else {
                txt_calendar_estatus.setText("");
            }
        }

        if (event.getTipoVenta() == 1) {
            view_calendar_tipo_venta.setBackgroundResource(R.color.color_actividades_ventas);
        } else if (event.getTipoVenta() == 2) {
            view_calendar_tipo_venta.setBackgroundResource(R.color.color_actividades_adminitrativas_ventas);
        } else if (event.getTipoVenta() == 3) {
            view_calendar_tipo_venta.setBackgroundResource(R.color.color_actividades_administrativas);
        }
    }

    @Override
    public int getEventLayout() {
        return R.layout.custom_row_calendar;
    }

    @Override
    public Class<DrawableCalendarEvent> getRenderType() {
        return DrawableCalendarEvent.class;
    }

    // endregion
}