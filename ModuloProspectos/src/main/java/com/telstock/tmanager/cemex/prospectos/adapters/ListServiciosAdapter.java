package com.telstock.tmanager.cemex.prospectos.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.telstock.tmanager.cemex.prospectos.R;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoServiciosRealm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Servicio;

/**
 * Created by USRMICRO10 on 22/09/2016.
 */
public class ListServiciosAdapter extends RecyclerView.Adapter<ListServiciosAdapter.MyViewHolder> {

    private ArrayList<Servicio> listaServicios;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox chkbox;
        TextView nombreServicio;

        MyViewHolder(View itemView) {
            super(itemView);
            chkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            nombreServicio = (TextView)itemView.findViewById(R.id.etNombre);
        }
    }

    public ListServiciosAdapter(ArrayList<Servicio> listaServicios) {
        this.listaServicios = listaServicios;
    }

    @Override
    public ListServiciosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_productos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListServiciosAdapter.MyViewHolder holder, final int position) {

        holder.chkbox.setOnCheckedChangeListener(null);

        //Se inicializan las variables de la vista
        Servicio servicio = listaServicios.get(position);
        holder.chkbox.setChecked(servicio.getSeleccionado());
        holder.nombreServicio.setText(servicio.getNombre());

        holder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            //Cambia la variable del checkbox que se cambió en la lista
            CatalogoServiciosDB catalogoServiciosDB = new CatalogoServiciosDB();

                listaServicios.get(position).setSeleccionado(b);
//            catalogoServiciosDB.setId(listaServicios.get(position).getId());
//            catalogoServiciosDB.setDescripcion(listaServicios.get(position).getDescripcion());
//            catalogoServiciosDB.setIdCatalogo(listaServicios.get(position).getIdCatalogo());
//            catalogoServiciosDB.setIdPadre(listaServicios.get(position).getIdPadre());
//            catalogoServiciosDB.setChecked(b);
//            CatalogoServiciosRealm.cambiarEstatusSeleccionado(catalogoServiciosDB);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaServicios.size();
    }



//    private class GroupCheckChangedListener implements CheckBox.OnCheckedChangeListener{
//
//        private final int position;
//
//        private GroupCheckChangedListener(int position)
//        {
//            this.position = position;
//        }
//
//
//
//        @Override
//        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//            //Cambia la variable del checkbox que se cambió en la lista
//            CatalogoServiciosDB catalogoServiciosDB = new CatalogoServiciosDB();
//
//            listaServicios.get(position).setChecked(b);
//            notifyDataSetChanged();
//
//        }
//    }

    //Método que devuelve la lista de servicios seleccionados cambiándolo de CatalogoProductoServicioDB -> ServiciosSeleccionados
    public ArrayList<Servicio> traerServiciosSeleccionados () {

//        ArrayList<Servicio> listaServiciosSeleciconados = new ArrayList<>();
//        Servicio serviciosSeleccionados;
//
//        for(CatalogoServiciosDB c : listaServicios) {
//            serviciosSeleccionados = new Servicio();
//
//            serviciosSeleccionados.setId(c.getId() + "");
//            serviciosSeleccionados.setNombre(c.getDescripcion());
//            serviciosSeleccionados.setSeleccionado(c.isChecked());
//
//            listaServiciosSeleciconados.add(serviciosSeleccionados);
//        }

        return listaServicios;
    }
}
