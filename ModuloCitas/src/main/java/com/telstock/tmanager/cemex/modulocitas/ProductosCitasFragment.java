package com.telstock.tmanager.cemex.modulocitas;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.telstock.tmanager.cemex.modulocitas.adapters.AdapterProductosCitas;
import com.telstock.tmanager.cemex.modulocitas.funciones.TinyDB;
import com.telstock.tmanager.cemex.modulocitas.interfaces.OnCheckedCheckBox;
import com.telstock.tmanager.cemex.modulocitas.model.CheckboxOfertaIntegral;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiClient;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.SubsegmentoProducto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by czamora on 10/13/16.
 */
public class ProductosCitasFragment extends Fragment implements OnCheckedCheckBox {

    private Context context;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    AdapterProductosCitas adapter;
    ProgressDialog progressDialog;

    //Lista que recupera los datos de productos.
    List<SubsegmentoProducto> subsegmentosProductoList;
    List<Producto> productos = new ArrayList<>();

    //Interfaz para los métodos de WS
    private ApiInterface apiInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_servicios, container, false);

        context = getActivity();

        TinyDB tinyDB = new TinyDB(context);
        try {
            subsegmentosProductoList = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
            tinyDB.putSubsegmentosProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS_TEMPORAL, subsegmentosProductoList);
            Log.d("", "");
        } catch (Exception e) {
            Log.d("", "");
        }

        progressDialog = new ProgressDialog(context);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Se muestra el ProgressDialog.
        mostrarProgressDialog();
        //Se obtienen los productos y servicios correspondientes al prospecto.
        //descargarProductoServicio();
        //Muestra la lista de Productos.
        mostrarProductos();

        return view;
    }

    /**
     * MÉTODO QUE MUESTRA LA LISTA DE PRODUCTOS
     **/
    public void mostrarProductos() {

        //Se quita el ProgressDialog.
        progressDialog.dismiss();

        if (subsegmentosProductoList != null && subsegmentosProductoList.size() > 0) {
            productos = subsegmentosProductoList.get(0).getProductos();
        }

        adapter = new AdapterProductosCitas(context, productos, this);
        recyclerView.setAdapter(adapter);

    }

    /**
     * MÉTODO QUE MUESTRA UN PROGRESSDIALOG, ESTO PARA EVITAR QUE EL USUARIO CAMBIE DE PAGER
     * MIENTRAS SE CARGAN LOS DATOS
     **/
    public void mostrarProgressDialog() {
        progressDialog.setMessage("Obteniendo Productos y Servicios...");
        progressDialog.setCancelable(false); //Que no se permita que el usuario cierre el progressDialog.
        progressDialog.show();
    }

    @Override
    public void onCheckedCheckBoxProductos(int positionChecked, boolean isSelected) {

        TinyDB tinyDB = new TinyDB(context);

        //Se guarda la selección actual en las sharedPreferences.
        List<SubsegmentoProducto> subsegmentosProductoList = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
        tinyDB.putSubsegmentosProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS_TEMPORAL, subsegmentosProductoList);
        subsegmentosProductoList.get(0).getProductos().get(positionChecked).setSeleccionado(isSelected);
        tinyDB.putSubsegmentosProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, subsegmentosProductoList);

        if (tinyDB.getBoolean(Valores.SHAREDPREFERENCES_MOTIVO_EXCLUSION) || isSelected) {
            Intent intent = new Intent(context, ProductosDetailsActivity.class);
            tinyDB.putProductosList(OfertaIntegralCitasFragment.STRING_PRODUCTOS, subsegmentosProductoList.get(0).getProductos());
            intent.putExtra("posicionProducto", positionChecked);
            intent.putExtra("seleccionadoProducto", isSelected);
            startActivityForResult(intent, OfertaIntegralCitasFragment.REQUEST_PRODUCTOS);
        }

    }

    @Override
    public void onCheckedCheckBoxServicios(int positionChecked, boolean isSelected) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == OfertaIntegralCitasFragment.REQUEST_PRODUCTOS) {
                String productoContestado = data.getStringExtra(OfertaIntegralCitasFragment.STRING_PRODUCTOS);

                if (productoContestado != null) {
                    Toast.makeText(context, "Datos del producto " + productoContestado + " obtenidos correctamente", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Subscribe
    public void guardarProducto(Producto p) {

        TinyDB tinyDB = new TinyDB(context);
        List<Producto> listaProductosSeleccionados = adapter.obtenerProductosSeleccionados();
        List<SubsegmentoProducto> listaSubsegmento = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);

        for(int i = 0; i < listaSubsegmento.get(0).getProductos().size(); i++) {
            listaSubsegmento.get(0).getProductos().get(i).setSeleccionado(listaProductosSeleccionados.get(i).getSeleccionado());
        }

        tinyDB.putSubsegmentosProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, listaSubsegmento);

    }

    public void regresarCheckbox(CheckboxOfertaIntegral checkboxOfertaIntegral) {
        try {
            if(checkboxOfertaIntegral != null) {
                //Se quita el ProgressDialog.
                progressDialog.dismiss();

                productos.get(checkboxOfertaIntegral.getPosition()).setSeleccionado(checkboxOfertaIntegral.isEstado());
                adapter = new AdapterProductosCitas(context, productos, this);
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

        CheckboxOfertaIntegral checkboxOfertaIntegral = new CheckboxOfertaIntegral();
        boolean cancelo = false;

        TinyDB tinyDB = new TinyDB(context);
        try {
            subsegmentosProductoList = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
            checkboxOfertaIntegral = tinyDB.getCheckBoxOfertaIntegral(Valores.SHAREDPREFERENCES_CHECKBOX_OFERTA_INTEGRAL, CheckboxOfertaIntegral.class);
            cancelo = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CANCELO_PRODUCTO_SERVICIO);

            if (cancelo) {
                subsegmentosProductoList = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS_TEMPORAL, SubsegmentoProducto.class);
                tinyDB.putSubsegmentosProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, subsegmentosProductoList);
            }
            Log.d("", "");
        } catch (Exception e) {
            Log.d("", "");
        }

        progressDialog = new ProgressDialog(context);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Se muestra el ProgressDialog.
        mostrarProgressDialog();
        //Se obtienen los productos y servicios correspondientes al prospecto.
        //descargarProductoServicio();
        //Muestra la lista de Productos.

        mostrarProductos();

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
