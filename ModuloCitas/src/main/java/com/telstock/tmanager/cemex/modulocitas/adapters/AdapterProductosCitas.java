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

import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Producto;


/**
 * Created by czamora on 10/13/16.
 */
public class AdapterProductosCitas extends RecyclerView.Adapter<AdapterProductosCitas.ViewHolder> {

    private Context context;
    private List<Producto> productos;
    public static OnCheckedCheckBox onCheckedCheckBox;

    public AdapterProductosCitas(Context context, List<Producto> productos,
                                 OnCheckedCheckBox onCheckedCheckBox) {
        this.context = context;
        this.productos = productos;
        this.onCheckedCheckBox = onCheckedCheckBox;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_row_productos_servicios, parent, false);

        return new ViewHolder(view, context);
    }

    public List<Producto> obtenerProductosSeleccionados() {
        return productos;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.checkBox.setTag(R.id.checkbox, position);
        holder.txtNombre.setText(productos.get(position).getNombre());

        //En caso de que el producto ya venga seleccionado.
        if (productos.get(position).getSeleccionado()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                productos.get(position).setSeleccionado(b);

                if(productos.get(position).getSeleccionado() != b) {
                    notifyDataSetChanged();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return productos.size();
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

            if (v.getId() == R.id.checkbox) {
                CheckBox checkBox = (CheckBox) v;
                int positionChecked = (int) checkBox.getTag(R.id.checkbox);

                if (checkBox.isChecked()) {
                    onCheckedCheckBox.onCheckedCheckBoxProductos(positionChecked, true);
                } else {
                    onCheckedCheckBox.onCheckedCheckBoxProductos(positionChecked, false);
                }
            } else {
                onCheckedCheckBox.onCheckedCheckBoxProductos(getLayoutPosition(), true);
//                if (checkBox.isChecked()) {
//                    onCheckedCheckBox.onCheckedCheckBoxProductos(getLayoutPosition(), true);
//                } else {
//                    onCheckedCheckBox.onCheckedCheckBoxProductos(getLayoutPosition(), true);
//                }
            }
        }
    }
}
