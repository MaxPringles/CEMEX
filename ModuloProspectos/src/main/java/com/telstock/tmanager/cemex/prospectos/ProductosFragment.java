package com.telstock.tmanager.cemex.prospectos;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.telstock.tmanager.cemex.prospectos.adapters.ListProductosAdapter;
import com.telstock.tmanager.cemex.prospectos.funciones.TinyDB;
import com.telstock.tmanager.cemex.prospectos.model.ProductosServiciosUpCross;
import com.telstock.tmanager.cemex.prospectos.rest.ApiClient;
import com.telstock.tmanager.cemex.prospectos.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoProductosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoServiciosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSubsegmentosRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetProductosUpCross;
import mx.com.tarjetasdelnoreste.realmdb.model.ItemsSupport;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.SubsegmentoProducto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.OportunidadVentaInicial;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubsegmentosProducto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USRMICRO10 on 22/09/2016.
 */
public class ProductosFragment extends Fragment {

    private Context context;

    //Objetos de la vista
    private RecyclerView rv;

    ListProductosAdapter listProductosAdapter;

    ArrayList<Producto> listaProductosSeleccionados = new ArrayList<>();
    ArrayList<SubsegmentosProducto> listaSubSegmento = new ArrayList<>();
    ArrayList<Producto> listaProductos = new ArrayList<>();
    SubsegmentosProducto subSegmento = new SubsegmentosProducto();

    Dialog dialogModal; //Modal que muestra mensaje de aviso.

    String idSubsegmento;

    SharedPreferences prefs;

    boolean esNuevoProspecto;
    String idObra;
    String idTipoProspecto;

    private ApiInterface apiInterface;

    //Inicializa las listas
    List<CatalogoProductoDB> listaTemp = new ArrayList<>();

    Producto producto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_productos_fragment, container, false);

        context = getActivity();

        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

        esNuevoProspecto = prefs.getBoolean(Valores.SHARED_PREFERENCES_ES_NUEVO_PROSPECTO, false);

        if (!esNuevoProspecto) {
            idObra = prefs.getString(Valores.SHAREDPREFERENCES_ID_OBRA, "");
            idTipoProspecto = prefs.getString(Valores.SHAREDPREFERENCES_ID_TIPO_PROSPECTO, "");
        }

        //Se inicializan los de los objetos de la vista
        rv = (RecyclerView) view.findViewById(R.id.lvProductos);

        //Se inicializa la interfaz que contiene los métodos de WS
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        final Bundle args = getArguments();
        idSubsegmento = args.getString(Valores.BUNDLE_ID_SUBSEGMENTO);

        rv.setLayoutManager(new LinearLayoutManager((rv.getContext())));

        //Método que prepara la la lista de productos que se agregarán en el adaptador
        prepararLista();

        return view;
    }

    private boolean todosSeleccionados() {
        boolean seleccion = true;

        for(Producto p : listaProductosSeleccionados) {
            if (!p.getSeleccionado()) {
                seleccion = false;
            }
        }

        return seleccion;
    }

    private void prepararLista() {

        //Revisa si ya se han seleccionado productos anteriormente.
        final TinyDB tinyDB = new TinyDB(context);
        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
        try {
            oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
        } catch (Exception e) {
            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
        }

        //En caso de que no se hayan seleccionado productos previamente, se obtienen
        //aquellos que están en el catálogo.
        if (oportunidadVentaInicial.getServicios() == null || !esNuevoProspecto) {

            if(esNuevoProspecto) {
                //Obtiene la lista de los servicios de la base de datos
                listaTemp = CatalogoProductosRealm.mostrarListaProductoPorId(Long.parseLong(idSubsegmento));

                for (CatalogoProductoDB c : listaTemp) {
                    producto = new Producto();

                    producto.setId(c.getId() + "");
                    producto.setNombre(c.getDescripcion());
                    producto.setSeleccionado(c.isChecked());

                    listaProductos.add(producto);
                }
            } else {
                ProductosServiciosUpCross productosServiciosUpCross = new ProductosServiciosUpCross();

                productosServiciosUpCross.setIdObra(idObra);
                productosServiciosUpCross.setIdTipoProspecto(Long.parseLong(idTipoProspecto));

                Call<List<GetProductosUpCross>> getProductosUpCross = apiInterface.getProductosUpCross(productosServiciosUpCross);

                getProductosUpCross.enqueue(new Callback<List<GetProductosUpCross>>() {
                    @Override
                    public void onResponse(Call<List<GetProductosUpCross>> call, Response<List<GetProductosUpCross>> response) {
                        if (response.body() != null && response.code() == 200) {

                            if(response.body().get(0).getItems().size() > 0) {
                                ArrayList<ItemsSupport> listaTempProductos = response.body().get(0).getItems();

                                for (ItemsSupport c : listaTempProductos) {
                                    producto = new Producto();

                                    producto.setId(c.getId() + "");
                                    producto.setNombre(c.getDescripcion());
                                    producto.setSeleccionado(false);

                                    listaProductos.add(producto);

                                    //Los productos obtenidos se muestran en el recyclerView.
                                    listProductosAdapter = new ListProductosAdapter(listaProductos);
                                    rv.setAdapter(listProductosAdapter);
                                }
                            } else {
                                Toast.makeText(context, getString(R.string.sin_productos_cross), Toast.LENGTH_LONG).show();
                            }

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<List<GetProductosUpCross>> call, Throwable t) {

                    }
                });
            }


        } else { //Si ya se habían seleccionado productos, se llena la lista con éstos.

            if (esNuevoProspecto) {
                listaProductos.clear();

                listaProductos.addAll(oportunidadVentaInicial.getSubsegmentosProductos().get(0).getProductos());
            }

        }

        //Los productos obtenidos se muestran en el recyclerView.
        listProductosAdapter = new ListProductosAdapter(listaProductos);
        rv.setAdapter(listProductosAdapter);
    }

    @Subscribe
    public void guardarProducto(SubsegmentosProducto subsegmentosProducto) {

        listaSubSegmento.clear();

        //Se crea una lista para guardar los productos seleccionados
        listaProductosSeleccionados = listProductosAdapter.traerProductosSeleccionados();

        subSegmento.setProductos(listaProductosSeleccionados);
        subSegmento.setIdSubsegmento(idSubsegmento);
        subSegmento.setNombre(CatalogoSubsegmentosRealm.obtenerNombreSubsegmento(idSubsegmento));
        subSegmento.setTodosSeleccion(todosSeleccionados());
        listaSubSegmento.add(subSegmento);

        //Se guarda la lista de productos seleccionados en las shared preferences
        final TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListSubsegmentosSeleccionados(Valores.SHAREDPREFERENCES_SUBSEGMENTOS_SELECCIONADOS, listaSubSegmento);

        if(todosSeleccionados() ) {
            tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_PRODUCTOS, true);
        }

        /*if (tinyDB.getBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_SERVICIOS) && todosSeleccionados() ) {
            tinyDB.putBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL, true);
        } else {
            tinyDB.putBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL, false);
        }*/

        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
        try {
            oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL_TEMPORAL, OportunidadVentaInicial.class);
        } catch (Exception e) {
            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
        }
        //oportunidadVentaInicial.setEsOfertaIntegral(false);

        oportunidadVentaInicial.setSubsegmentosProductos(listaSubSegmento);

        tinyDB.putOfertaIntegral(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL_TEMPORAL, oportunidadVentaInicial);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
