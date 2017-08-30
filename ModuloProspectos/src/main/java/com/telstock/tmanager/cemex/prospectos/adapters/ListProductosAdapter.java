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

import mx.com.tarjetasdelnoreste.realmdb.CatalogoProductosRealm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;

/**
 * Created by USRMICRO10 on 22/09/2016.
 */
public class ListProductosAdapter extends RecyclerView.Adapter<ListProductosAdapter.MyViewHolder> {

    private ArrayList<Producto> listaProductos;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox chkbox;
        TextView nombreProducto;

        MyViewHolder(View itemView) {
            super(itemView);
            chkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            nombreProducto = (TextView)itemView.findViewById(R.id.etNombre);
        }
    }

    public ListProductosAdapter(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    @Override
    public ListProductosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_productos, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListProductosAdapter.MyViewHolder holder, final int position) {

        holder.chkbox.setOnCheckedChangeListener(null);

        //Se inicializan las variables de la vista
        Producto producto = listaProductos.get(position);
        holder.chkbox.setChecked(producto.getSeleccionado());
        holder.nombreProducto.setText(producto.getNombre());

        holder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            //Cambia la variable del checkbox que se cambió en la lista
            CatalogoProductoDB catalogoProductosDB = new CatalogoProductoDB();

                listaProductos.get(position).setSeleccionado(b);
//            catalogoProductosDB.setId(listaProductos.get(position).getId());
//            catalogoProductosDB.setDescripcion(listaProductos.get(position).getDescripcion());
//            catalogoProductosDB.setIdCatalogo(listaProductos.get(position).getIdCatalogo());
//            catalogoProductosDB.setIdPadre(listaProductos.get(position).getIdPadre());
//            catalogoProductosDB.setChecked(b);
//            CatalogoProductosRealm.cambiarEstatusSeleccionado(catalogoProductosDB);

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
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
//            CatalogoProductoDB catalogoProductosDB = new CatalogoProductoDB();
//
//            listaProductos.get(position).setChecked(b);
//            notifyDataSetChanged();
//
//        }
//    }

    //Método que devuelve la lista de servicios seleccionados cambiándolo de CatalogoProductoProductoDB -> ProductosSeleccionados
    public ArrayList<Producto> traerProductosSeleccionados () {

//        ArrayList<Producto> listaProductosSeleciconados = new ArrayList<>();
//        Producto serviciosSeleccionados;
//
//        for(CatalogoProductoDB c : listaProductos) {
//            serviciosSeleccionados = new Producto();
//
//            serviciosSeleccionados.setId(c.getId() + "");
//            serviciosSeleccionados.setNombre(c.getDescripcion());
//            serviciosSeleccionados.setSeleccionado(c.isChecked());
//
//            listaProductosSeleciconados.add(serviciosSeleccionados);
//        }

        return listaProductos;
    }
}
