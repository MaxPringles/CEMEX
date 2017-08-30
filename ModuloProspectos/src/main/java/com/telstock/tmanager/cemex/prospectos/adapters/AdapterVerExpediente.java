package com.telstock.tmanager.cemex.prospectos.adapters;

/**
 * Created by czamora on 11/22/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.telstock.tmanager.cemex.prospectos.R;
import com.telstock.tmanager.cemex.prospectos.interfaces.OnClickDescarga;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;

public class AdapterVerExpediente extends RecyclerView.Adapter<AdapterVerExpediente.ProspectosViewHolder> {

    List<ArchivosAltaDB> archivosAltaDBList;
    boolean showCheckBox;
    public static OnClickDescarga onClickDescarga;

    //Constructor de la clase.
    public AdapterVerExpediente(OnClickDescarga onClickDescarga, List<ArchivosAltaDB> archivosAltaDBList,
                                boolean showCheckBox){
        this.onClickDescarga = onClickDescarga;
        this.archivosAltaDBList = archivosAltaDBList;
        this.showCheckBox = showCheckBox;
    }

    //Método que inicializa la vista.
    @Override
    public ProspectosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_ver_expediente, parent, false);

        return new ProspectosViewHolder(v);
    }

    //Método que controla los elementos de cada columna (el contenido que tendrán y sus
    //eventos onClick)
    @Override
    public void onBindViewHolder(ProspectosViewHolder holder, int position) {

        //final PaintingProspectos prospectosArchivos = archivosAltaDBList.get(position);

        String nombre =  archivosAltaDBList.get(position).getNombre();
        holder.checkBox.setTag(R.id.checkbox_descarga, position);

        holder.txt_nombre_agregar.setText(nombre);

        if (showCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.GONE);
        }
    }

    //Método que devuelve el tamaño de la lista.
    @Override
    public int getItemCount() {
        return archivosAltaDBList.size();
    }

    //Clase interna que instancia todos los elementos de la vista.
    public static class ProspectosViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

        private TextView txt_nombre_agregar;
        private CheckBox checkBox;

        public ProspectosViewHolder(View v){
            super(v);

            txt_nombre_agregar = (TextView)v.findViewById(R.id.txt_nombre_descarga);
            checkBox = (CheckBox)v.findViewById(R.id.checkbox_descarga);

            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CheckBox checkBox = (CheckBox) v;

            int position = (int) checkBox.getTag(R.id.checkbox_descarga);
            if (checkBox.isChecked()) {
                onClickDescarga.onClickDescarga(position, true);
            } else {
                onClickDescarga.onClickDescarga(position, false);
            }
        }
    }
}

