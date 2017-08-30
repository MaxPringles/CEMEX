package com.telstock.tmanager.cemex.prospectos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexvasilkov.android.commons.adapters.ItemsAdapter;
import com.bumptech.glide.Glide;
import com.telstock.tmanager.cemex.prospectos.R;
import com.telstock.tmanager.cemex.prospectos.interfaces.OnClickProspectos;

import java.util.Arrays;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;

/**
 * Created by czamora on 8/25/16.
 */
public class AdapterProspectos extends ItemsAdapter<ProspectosDB>  {

    private Context context;

    private static OnClickProspectos itemListener;
    private List<ProspectosDB> arrayListProspectos;

    public AdapterProspectos(Context context, OnClickProspectos itemListener,
                             List<ProspectosDB> arrayListProspectos) {
        super(context);

        this.context = context;
        this.itemListener = itemListener;
        this.arrayListProspectos = arrayListProspectos;

        //Se mandan a llamar a todas las imágenes y sus títulos.
        setItemsList(Arrays.asList(ProspectosDB.getAllPaintings(arrayListProspectos)));
    }

    //Se define la vista a utilizar para cada folder cerrado.
    @Override
    protected View createView(ProspectosDB item, int pos, ViewGroup parent, LayoutInflater inflater) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_prospectos, parent, false);
        ViewHolder vh = new ViewHolder(view);

        vh.layout_row = (LinearLayout)view.findViewById(R.id.layout_row);
        vh.txt_clasificacion_prospecto = (TextView)view.findViewById(R.id.txt_clasificacion_prospecto);
        vh.txt_nombre_prospecto = (TextView)view.findViewById(R.id.txt_nombre_prospecto);
        vh.txt_tarea_prospecto = (TextView)view.findViewById(R.id.txt_tarea_prospecto);
        vh.img_prospecto = (ImageView) view.findViewById(R.id.img_prospecto);
        vh.img_icono = (ImageView) view.findViewById(R.id.img_icono);

        view.setTag(vh);

        return view;
    }

    //Se especifica lo que contendrá cada elemento del folder cerrado.
    @Override
    protected void bindView(ProspectosDB item, int pos, View convertView) {

        ViewHolder vh = (ViewHolder)convertView.getTag();

        vh.layout_row.setTag(R.id.layout_row, item);

        vh.txt_clasificacion_prospecto.setText(item.getDescripcionTipoProspecto());
        vh.txt_nombre_prospecto.setText(item.getObra() + " - " + item.getCliente());
        vh.txt_tarea_prospecto.setText(item.getDescripcionActividad());

        Glide.with(context)
                .load(Url.URL_WEBSERVICE + Url.getArchivoProspecto + item.getFotografia())
                .placeholder(R.drawable.avatar_prospecto)
                .error(R.drawable.avatar_prospecto)
                .fitCenter()
                .into(vh.img_prospecto);

        //Revisa si está descartado para poner el ícono correspondiente.
        if (item.isEstaDescartado()) {
            Log.d("", "");
            Glide.with(context)
                    .load(R.drawable.status_descarted)
                    .into(vh.img_icono);
        } else { //Es necesario poner la excepción para que el ListView no coloque los íconos en posiciones indebidas.
            if (item.getEstatusAgenda() == Valores.ID_ACTIVIDAD_REAGENDADA) { //En caso de no ser descartada, colocar ícono de reagendado si es necesario.
                Glide.with(context)
                        .load(R.drawable.status_reschedule)
                        .into(vh.img_icono);
            } else {
                Glide.with(context)
                        .load(R.drawable.btn_add_photo) //Ícono en blanco para indicar que no tiene ningún estatus particular.
                        .into(vh.img_icono);
            }
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layout_row;
        TextView txt_clasificacion_prospecto;
        TextView txt_nombre_prospecto;
        TextView txt_tarea_prospecto;
        ImageView img_prospecto;
        ImageView img_icono;

        public ViewHolder(View convertView) {
            super(convertView);

            layout_row = (LinearLayout)convertView.findViewById(R.id.layout_row);
            layout_row.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //No se usa Switch, debido a que necesita que las constantes sean final,
            //cosa que no es posible en los módulos.
            if (v.getId() == R.id.layout_row) {
                ProspectosDB item = (ProspectosDB) v.getTag(R.id.layout_row);
                itemListener.onClickProspectos(v, item);
            }
        }
    }
}
