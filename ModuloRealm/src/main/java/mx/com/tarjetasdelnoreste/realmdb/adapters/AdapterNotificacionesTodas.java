package mx.com.tarjetasdelnoreste.realmdb.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.model.BuzonNotificacionesDB;

/**
 * Created by usr_micro13 on 01/02/2017.
 */

public class AdapterNotificacionesTodas extends RecyclerView.Adapter<AdapterNotificacionesTodas.ViewHolder> {

    private Context context;
    private List<BuzonNotificacionesDB> buzonNotificacionesDBList;
    Calendar calendar;
    SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");

    public AdapterNotificacionesTodas(Context context, List<BuzonNotificacionesDB> buzonNotificacionesDBList) {
        this.context = context;
        this.buzonNotificacionesDBList = buzonNotificacionesDBList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_notificaciones_cardview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        BuzonNotificacionesDB buzonNotificacionesDB = buzonNotificacionesDBList.get(position);

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(buzonNotificacionesDB.getFecha() * 1000);

        holder.txtFecha.setText(formatFecha.format(calendar.getTime()));
        holder.txtHora.setText(formatHora.format(calendar.getTime()));
        holder.txtRemitenteNotificacion.setText(buzonNotificacionesDB.getNombreRemitente());
        holder.txtTextoNotificacion.setText(buzonNotificacionesDB.getMensaje());

        //Si la notificación ya está leída, colocar la franja en color azul.
        if (buzonNotificacionesDB.getEstaLeido()) {
            holder.viewFranja.setBackgroundColor(context.getResources().getColor(R.color.colorAzulElectrico));
        }

        switch (buzonNotificacionesDB.getIdPrioridad()) {
            case 1:
                holder.imageView.setImageResource(R.drawable.bell_verde);
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.bell_azul);
                break;
            case 3:
                holder.imageView.setImageResource(R.drawable.bell_ambar);
                break;
            case 4:
                holder.imageView.setImageResource(R.drawable.bell_roja);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.bell_azul);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return buzonNotificacionesDBList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View viewFranja;
        private ImageView imageView;
        private TextView txtFecha;
        private TextView txtHora;
        private TextView txtRemitenteNotificacion;
        private TextView txtTextoNotificacion;

        public ViewHolder(View view) {
            super(view);

            viewFranja = view.findViewById(R.id.viewFranja);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            txtFecha = (TextView) view.findViewById(R.id.txtFecha);
            txtHora = (TextView) view.findViewById(R.id.txtHora);
            txtRemitenteNotificacion = (TextView) view.findViewById(R.id.txtRemitenteNotificacion);
            txtTextoNotificacion = (TextView) view.findViewById(R.id.txtTextoNotificacion);
        }
    }
}
