package com.telstock.tmanager.cemex.modulocitas.adapters;

/**
 * Created by czamora on 11/22/16.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.telstock.tmanager.cemex.modulocitas.R;

import java.util.ArrayList;

public class AdapterAgregarExpediente extends RecyclerView.Adapter<AdapterAgregarExpediente.ProspectosViewHolder>{

    private ArrayList<String> listPersona = new ArrayList<>();

    //Constructor de la clase.
    public AdapterAgregarExpediente(ArrayList<String> listPersona){
        this.listPersona = listPersona;
    }

    //Método que inicializa la vista.
    @Override
    public ProspectosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_agregar_expediente, parent, false);

        return new ProspectosViewHolder(v);
    }

    //Método que controla los elementos de cada columna (el contenido que tendrán y sus
    //eventos onClick)
    @Override
    public void onBindViewHolder(ProspectosViewHolder holder, int position) {

        //final PaintingProspectos prospectosArchivos = listPersona.get(position);

        String nombre =  listPersona.get(position);
        holder.txt_nombre_agregar.setText(nombre);
    }

    //Método que devuelve el tamaño de la lista.
    @Override
    public int getItemCount() {
        return listPersona.size();
    }

    //Clase interna que instancia todos los elementos de la vista.
    public static class ProspectosViewHolder extends RecyclerView.ViewHolder{

        private TextView txt_nombre_agregar;

        public ProspectosViewHolder(View v){
            super(v);

            txt_nombre_agregar = (TextView)v.findViewById(R.id.txt_nombre_agregar);
        }
    }
}
