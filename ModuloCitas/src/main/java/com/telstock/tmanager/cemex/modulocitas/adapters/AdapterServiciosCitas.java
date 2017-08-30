package com.telstock.tmanager.cemex.modulocitas.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.telstock.tmanager.cemex.modulocitas.R;
import com.telstock.tmanager.cemex.modulocitas.interfaces.OnCheckedCheckBox;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio;


/**
 * Created by czamora on 10/13/16.
 */
public class AdapterServiciosCitas extends RecyclerView.Adapter<AdapterServiciosCitas.ViewHolder> {

    private Context context;
    private List<Servicio> servicios;
    public static OnCheckedCheckBox onCheckedCheckBox;

    public AdapterServiciosCitas(Context context, List<Servicio> servicios,
                                 OnCheckedCheckBox onCheckedCheckBox) {
        this.context = context;
        this.servicios = servicios;
        this.onCheckedCheckBox = onCheckedCheckBox;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_productos_servicios, parent, false);

        return new ViewHolder(view, context);
    }

    public List<Servicio> obtenerServiciosSeleccionados() {
        return servicios;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.checkBox.setTag(R.id.checkbox, position);
        holder.txtNombre.setText(servicios.get(position).getNombre());

        //En caso de que el servicio ya venga seleccionado.
        if (servicios.get(position).getSeleccionado()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                servicios.get(position).setSeleccionado(b);

                if(servicios.get(position).getSeleccionado() != b) {
                    notifyDataSetChanged();
                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return servicios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        private CheckBox checkBox;
        private TextView txtNombre;

        public ViewHolder(View view, Context context) {
            super(view);

            this.context = context;
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            txtNombre = (TextView) view.findViewById(R.id.txtNombre);

            checkBox.setOnClickListener(this);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.checkbox) {
                CheckBox checkBox = (CheckBox) v;
                int positionChecked = (int) checkBox.getTag(R.id.checkbox);

                if (checkBox.isChecked()) {
                    onCheckedCheckBox.onCheckedCheckBoxServicios(positionChecked, true);
                } else {
                    onCheckedCheckBox.onCheckedCheckBoxServicios(positionChecked, false);
                }
            } else {
                onCheckedCheckBox.onCheckedCheckBoxServicios(getLayoutPosition(), true);
//                if (checkBox.isChecked()) {
//                    onCheckedCheckBox.onCheckedCheckBoxServicios(getLayoutPosition(), false);
//                } else {
//                    onCheckedCheckBox.onCheckedCheckBoxServicios(getLayoutPosition(), true);
//                }
            }

        }
    }
}
