package com.telstock.tmanager.cemex.prospectos.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.telstock.tmanager.cemex.prospectos.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoSubsegmentosRealm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubSegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubsegmentosProducto;

/**
 * Created by USRMICRO10 on 21/09/2016.
 */
public class ExpandableListOfertaIntegralAdapter extends BaseExpandableListAdapter {
    private Context _context;
    //Lista padre
    private List<CatalogoSubsegmentosDB> _listDataHeader;
    //Lista de los hijos
    private LinkedHashMap<CatalogoSubsegmentosDB, List<CatalogoProductoDB>> _listDataChild;

    public ExpandableListOfertaIntegralAdapter(Context context, List<CatalogoSubsegmentosDB> listDataHeader,
                                               LinkedHashMap<CatalogoSubsegmentosDB, List<CatalogoProductoDB>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    public ArrayList<SubsegmentosProducto> traerListaSubsegmento() {
        ArrayList<SubsegmentosProducto> listaSubsegmento = new ArrayList<>();
        SubsegmentosProducto subsegmentosProducto;

        for(int i = 0;i < this._listDataHeader.size();i++) {
            subsegmentosProducto = new SubsegmentosProducto();

            subsegmentosProducto.setIdSubsegmento(this._listDataHeader.get(i).getId() + "");
            subsegmentosProducto.setNombre(this._listDataHeader.get(i).getDescripcion());
            subsegmentosProducto.setTodosSeleccion(this._listDataHeader.get(i).isChecked());
            subsegmentosProducto.setProductos(traerGrupoCompletoHijo(i));
            listaSubsegmento.add(subsegmentosProducto);
        }

        return listaSubsegmento;
    }

    //Trae la lista de un grupo completo utilizando la posición del padre
    public ArrayList<Producto> traerGrupoCompletoHijo(int groupPosition) {
        ArrayList<Producto> listaProductos = new ArrayList<>();
        List<CatalogoProductoDB> alistHijo = _listDataChild.get(_listDataHeader.get(groupPosition));
        Producto producto;

        for (CatalogoProductoDB c : alistHijo) {
            producto = new Producto();

            producto.setNombre(c.getDescripcion());
            producto.setId(c.getId() + "");
            producto.setSeleccionado(c.isChecked());

            listaProductos.add(producto);
        }

        return listaProductos;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //Inicializa el hijo
        final CatalogoProductoDB childText = (CatalogoProductoDB) getChild(groupPosition, childPosition);

        if (convertView == null) {
            //Muestra la vista del hijo
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item_productos, null);
        }

        //Inicializa las variables de la vista
        TextView txtListChild = (TextView) convertView.findViewById(R.id.etNombre);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

        //Se inicaliza un listener para el checkbox del hijo
        CheckHijoChangedListener checkHijoChangedListener = new CheckHijoChangedListener(groupPosition,childPosition);
        checkBox.setOnCheckedChangeListener(checkHijoChangedListener);

        //Se marca el checkbox dependiendo de lo que tenga el objeto del hijo
        checkBox.setChecked(childText.isChecked());

        //Se pone en el TextView el nombre
        txtListChild.setText(childText.getDescripcion());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        //Inicializa el padre
        CatalogoSubsegmentosDB headerTitle = (CatalogoSubsegmentosDB) getGroup(groupPosition);

        if (convertView == null) {
            //Muestra la vista del padre
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_productos, null);
        }

        //Inicializa las variables de la vista
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.etNombre);
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

        //Se modifican las propiedades del texto
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getDescripcion());

        //Se inicaliza un listener para el checkbox del padre
        GroupCheckChangedListener groupCheckChangedListener = new GroupCheckChangedListener(groupPosition);
        checkBox.setOnCheckedChangeListener(groupCheckChangedListener);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupCheckChangedListener implements CheckBox.OnCheckedChangeListener{

        private final int groupPosition;

        private GroupCheckChangedListener(int groupPosition)
        {
            this.groupPosition = groupPosition;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            //Se obtiene la lista de los hijos dependiendo del padre
            List<CatalogoProductoDB> listaHijos = _listDataChild.get(_listDataHeader.get(groupPosition));
            CatalogoSubsegmentosDB catalogoSubsegmentosDB = new CatalogoSubsegmentosDB();

            //Se marcan todos los checkbox de los hijos que pertenecen a ese padre
            for(CatalogoProductoDB item: listaHijos) {
                item.setChecked(b);
            }

//            _listDataHeader.get(groupPosition).setChecked(b);

//            catalogoSubsegmentosDB = _listDataHeader.get(groupPosition);
            catalogoSubsegmentosDB.setId(_listDataHeader.get(groupPosition).getId());
            catalogoSubsegmentosDB.setDescripcion(_listDataHeader.get(groupPosition).getDescripcion());
            catalogoSubsegmentosDB.setIdCatalogo(_listDataHeader.get(groupPosition).getIdCatalogo());
            catalogoSubsegmentosDB.setIdPadre(_listDataHeader.get(groupPosition).getIdPadre());
            catalogoSubsegmentosDB.setChecked(b);

            CatalogoSubsegmentosRealm.cambiarEstatusSeleccionado(catalogoSubsegmentosDB);

            notifyDataSetChanged();

        }
    }

    private class CheckHijoChangedListener implements CheckBox.OnCheckedChangeListener{

        private final int groupPosition;
        private final int childPosition;

        private CheckHijoChangedListener(int groupPosition, int childPosition)
        {
            this.groupPosition = groupPosition;
            this.childPosition = childPosition;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            //Obtiene el hijo que se cambió su checkbox y nitifica el cambio
            _listDataChild.get(_listDataHeader.get(groupPosition)).get(childPosition).setChecked(b);
            notifyDataSetChanged();
        }
    }
}