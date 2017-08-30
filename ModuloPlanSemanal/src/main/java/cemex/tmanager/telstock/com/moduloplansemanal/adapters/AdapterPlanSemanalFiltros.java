package cemex.tmanager.telstock.com.moduloplansemanal.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cemex.tmanager.telstock.com.moduloplansemanal.R;
import cemex.tmanager.telstock.com.moduloplansemanal.interfaces.OnClickProspectos;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;

/**
 * Created by czamora on 9/12/16.
 */
public class AdapterPlanSemanalFiltros extends RecyclerView.Adapter<AdapterPlanSemanalFiltros.ViewHolder> {

    private Context context;
    private static OnClickProspectos itemListener;
    private List<ProspectosDB> arrayBusqueda;

    public AdapterPlanSemanalFiltros(Context context, OnClickProspectos itemListener,
                                     List<ProspectosDB> arrayBusqueda) {
        this.context = context;
        this.itemListener = itemListener;
        this.arrayBusqueda = arrayBusqueda;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_plan_filtro, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int positionProspecto = position;

        holder.layout_row.setTag(R.id.layout_row, positionProspecto);
        holder.txt_busqueda_nombre.setText(arrayBusqueda.get(position).getObra() + " - "
                + arrayBusqueda.get(position).getCliente());
    }

    @Override
    public int getItemCount() {
        return arrayBusqueda.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        Context context;
        LinearLayout layout_row;
        TextView txt_busqueda_nombre;

        public ViewHolder(View view, Context context) {
            super(view);

            this.context = context;

            layout_row = (LinearLayout) view.findViewById(R.id.layout_row);
            txt_busqueda_nombre = (TextView) view.findViewById(R.id.txt_busqueda_nombre);

            layout_row.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.layout_row) {
                int prospectosDB = (int) v.getTag(R.id.layout_row);
                itemListener.onClickProspectos(v, prospectosDB);
            }
        }
    }
}
