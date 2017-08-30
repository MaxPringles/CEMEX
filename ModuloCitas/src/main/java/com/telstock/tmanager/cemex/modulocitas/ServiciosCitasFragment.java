package com.telstock.tmanager.cemex.modulocitas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.telstock.tmanager.cemex.modulocitas.adapters.AdapterProductosCitas;
import com.telstock.tmanager.cemex.modulocitas.adapters.AdapterServiciosCitas;
import com.telstock.tmanager.cemex.modulocitas.funciones.TinyDB;
import com.telstock.tmanager.cemex.modulocitas.interfaces.OnCheckedCheckBox;
import com.telstock.tmanager.cemex.modulocitas.model.CheckboxOfertaIntegral;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by czamora on 10/13/16.
 */
public class ServiciosCitasFragment extends Fragment implements OnCheckedCheckBox {

    private Context context;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AdapterServiciosCitas adapter;

    //Lista que recupera los datos de productos.
    List<Servicio> servicioList;
    List<Servicio> servicios = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_servicios, container, false);

        context = getActivity();

        TinyDB tinyDB = new TinyDB(context);
        try {
            servicioList = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);
            tinyDB.putServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS_TEMPORAL, servicioList);
            Log.d("", "");
        } catch (Exception e) {
            Log.d("", "");
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Muestra la lista de Productos.
        mostrarServicios();

        return view;
    }

    /**
     * MÉTODO QUE MUESTRA LA LISTA DE SERVICIOS
     **/
    public void mostrarServicios() {

        if (servicioList != null && servicioList.size() > 0) {
            servicios = servicioList;
        }

        adapter = new AdapterServiciosCitas(context, servicios, this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onCheckedCheckBoxProductos(int positionChecked, boolean isSelected) {
    }

    @Override
    public void onCheckedCheckBoxServicios(int positionChecked, boolean isSelected) {

        TinyDB tinyDB = new TinyDB(context);

        //Se guarda la selección actual en las sharedPreferences.
        List<Servicio> listaServicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);
        tinyDB.putServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS_TEMPORAL, listaServicio);
        listaServicio.get(positionChecked).setSeleccionado(isSelected);
        tinyDB.putServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, listaServicio);

        if (tinyDB.getBoolean(Valores.SHAREDPREFERENCES_MOTIVO_EXCLUSION) || isSelected) {
            Intent intent = new Intent(context, ServiciosDetailsActivity.class);
            tinyDB.putServiciosList(OfertaIntegralCitasFragment.STRING_SERVICIOS, servicioList);
            intent.putExtra("posicionServicio", positionChecked);
            intent.putExtra("seleccionadoServicio", isSelected);
            startActivityForResult(intent, OfertaIntegralCitasFragment.REQUEST_SERVICIOS);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OfertaIntegralCitasFragment.REQUEST_SERVICIOS) {
                String servicioContestado = data.getStringExtra(OfertaIntegralCitasFragment.STRING_SERVICIOS);

                if (servicioContestado != null) {
                    Toast.makeText(context, "Datos del servicio " + servicioContestado + " obtenidos correctamente", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @org.greenrobot.eventbus.Subscribe
    public void guardarServicio(Servicio s) {

        TinyDB tinyDB = new TinyDB(context);
        List<Servicio> listaServiciosSeleccionados = adapter.obtenerServiciosSeleccionados();
        List<Servicio> listaServicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

        for(int i =0; i < listaServicio.size(); i++) {
            listaServicio.get(i).setSeleccionado(listaServiciosSeleccionados.get(i).getSeleccionado());
        }

        tinyDB.putServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, listaServicio);

    }

    public void regresarCheckbox(CheckboxOfertaIntegral checkboxOfertaIntegral) {
        try {
            if(checkboxOfertaIntegral != null) {
                servicios.get(checkboxOfertaIntegral.getPosition()).setSeleccionado(checkboxOfertaIntegral.isEstado());
                adapter = new AdapterServiciosCitas(context, servicios, this);
                recyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e("No hay cambios checkbox", e.toString());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        TinyDB tinyDB = new TinyDB(context);
        CheckboxOfertaIntegral checkboxOfertaIntegral = new CheckboxOfertaIntegral();
        boolean cancelo = false;
        try {
            servicioList = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);
            checkboxOfertaIntegral = tinyDB.getCheckBoxOfertaIntegral(Valores.SHAREDPREFERENCES_CHECKBOX_OFERTA_INTEGRAL, CheckboxOfertaIntegral.class);
            cancelo = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CANCELO_PRODUCTO_SERVICIO);

            if (cancelo) {
                servicioList = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS_TEMPORAL, Servicio.class);
                tinyDB.putServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, servicioList);
            }

            Log.d("", "");
        } catch (Exception e) {
            Log.d("", "");
        }

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Muestra la lista de Productos.


        mostrarServicios();

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
