package com.telstock.tmanager.cemex.prospectos.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.telstock.tmanager.cemex.prospectos.R;
import com.telstock.tmanager.cemex.prospectos.interfaces.OnClickContacto;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.ContactosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;


/**
 * Created by czamora on 9/9/16.
 */
public class AdapterContactos extends RecyclerView.Adapter<AdapterContactos.ViewHolder> {

    private Context context;

    private List<Contacto> arrayContactos; //Lista de contactos de alta prospecto.
    private int statusLista; //Status que indica si la vista está vacía o no.
    private String nombreObra;
    private static OnClickContacto itemListener;

    /**
     * CONSTRUCTOR QUE SE EJECUTA AL MOMENTO DE DAR DE ALTA UN PROSPECTO
     **/
    public AdapterContactos(Context context, List<Contacto> arrayContactos,
                            int statusLista, String nombreObra, OnClickContacto itemListener) {
        this.context = context;
        this.arrayContactos = arrayContactos;
        this.statusLista = statusLista;
        this.nombreObra = nombreObra;
        this.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if (statusLista == 0) { //La lista está vacía.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_row_contacto_vacio, parent, false);
        } else { //La lista tiene registros.
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_row_alta_contacto, parent, false);
        }

        return new ViewHolder(view, context, statusLista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (statusLista == 0) { //La lista está vacía.

        } else { //La lista tiene registros.

            Contacto contacto = arrayContactos.get(position);

            holder.img_contacto_llamar.setTag(R.id.img_contacto_llamar, arrayContactos.get(position).getTelefono());
            holder.txt_contacto_nombre.setText(contacto.getNombres() + " " +
                    contacto.getApellidoPaterno() + " " + contacto.getApellidoMaterno());
            holder.txt_contacto_cargo.setText(contacto.getCargo().getCargo());
        //si es contacto principal muestra el cambio
            if(contacto.getPrincipal())
            {
                holder.txt_contacto_pricipal.setVisibility(View.VISIBLE);
            }

            //Coloca el nombre de la obra (esto sólo para el alta de contactos individuales).
            if (!nombreObra.equals("")) {
                holder.txt_contacto_obra.setText(nombreObra);
            } else {
                holder.txt_contacto_obra.setText("");
            }

        }
    }

    @Override
    public int getItemCount() {

        return arrayContactos.size();
    }

    /**
     * CLASE QUE ENLAZA LOS OBJETOS CON LOS ELEMENTOS UI
     **/
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        //Elementos de lista vacía.
        LinearLayout layout_row;

        //Elementos de lista con registros.
        private ImageView img_contacto;
        private TextView txt_contacto_obra;
        private TextView txt_contacto_nombre;
        private TextView txt_contacto_cargo;
        private TextView txt_contacto_pricipal;
        private LinearLayout img_contacto_llamar;

        public ViewHolder(View view, Context context, int statusLista) {
            super(view);

            if (statusLista == 0) { //La lista está vacía.
                layout_row = (LinearLayout) view.findViewById(R.id.layout_row);

                layout_row.setOnClickListener(this);

            } else { //La lista tiene registros.
                img_contacto = (ImageView) view.findViewById(R.id.img_contacto);
                txt_contacto_obra = (TextView) view.findViewById(R.id.txt_contacto_obra);
                txt_contacto_nombre = (TextView) view.findViewById(R.id.txt_contacto_nombre);
                txt_contacto_cargo = (TextView) view.findViewById(R.id.txt_contacto_cargo);
                txt_contacto_pricipal= (TextView) view.findViewById(R.id.txt_contacto_pricipal);
                img_contacto_llamar = (LinearLayout) view.findViewById(R.id.img_contacto_llamar);

                img_contacto_llamar.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.img_contacto_llamar) {
                String telefono = (String) v.getTag(R.id.img_contacto_llamar);
                itemListener.onClickLlamar(telefono);

            } else if (v.getId() == R.id.layout_row) { //Sólo se activa si se muestra el ítem de lista vacía.
                itemListener.onClickProspectos(v);
            }
        }
    }
}
