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
import android.widget.TextView;
import android.widget.Toast;

import com.telstock.tmanager.cemex.prospectos.adapters.ListServiciosAdapter;
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
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetProductosUpCross;
import mx.com.tarjetasdelnoreste.realmdb.model.ItemsSupport;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.OportunidadVentaInicial;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubsegmentosProducto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USRMICRO10 on 22/09/2016.
 */
public class ServiciosFragment extends Fragment {
    private Context context;

    //Objetos de la vista
    private RecyclerView rv;

    ArrayList<Servicio> listaServicios = new ArrayList<>();
    ArrayList<Servicio> listaServiciosSeleccionados = new ArrayList<>();
    ListServiciosAdapter listServiciosAdapter;

    String idSubsegmento;

    SharedPreferences prefs;

    boolean esNuevoProspecto;
    String idObra;
    String idTipoProspecto;

    private ApiInterface apiInterface;

    //Inicializa las listas
    List<CatalogoServiciosDB> listaTemp = new ArrayList<>();
    Servicio servicio;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_productos_fragment, container, false);

        context = getContext();

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

    private void prepararLista() {

        //Revisa si ya se han seleccionado servicios anteriormente.
        final TinyDB tinyDB = new TinyDB(context);
        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
        try {
            oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
        } catch (Exception e) {
            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
        }

        //En caso de que no se hayan seleccionado servicios previamente, se obtienen
        //aquellos que están en el catálogo.
        if (oportunidadVentaInicial.getServicios() == null || !esNuevoProspecto) {

            if(esNuevoProspecto) {
                //Obtiene la lista de los servicios de la base de datos
                listaTemp = CatalogoServiciosRealm.mostrarListaServicios();

                for (CatalogoServiciosDB c : listaTemp) {
                    servicio = new Servicio();

                    servicio.setId(c.getId() + "");
                    servicio.setNombre(c.getDescripcion());
                    servicio.setSeleccionado(c.isChecked());

                    listaServicios.add(servicio);
                }
            } else {
                ProductosServiciosUpCross productosServiciosUpCross = new ProductosServiciosUpCross();

                productosServiciosUpCross.setIdObra(idObra);
                productosServiciosUpCross.setIdTipoProspecto(Long.parseLong(idTipoProspecto));

                Call<GetCatalogoPOJO> getServiciosUpCross = apiInterface.getServiciosUpCross(productosServiciosUpCross);

                getServiciosUpCross.enqueue(new Callback<GetCatalogoPOJO>() {
                    @Override
                    public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                        if(response.body() != null && response.code() == 200) {

                            if (response.body().getItems().size() > 0) {

                                ArrayList<ItemsSupport> listaTempServicios = response.body().getItems();

                                for (ItemsSupport c : listaTempServicios) {
                                    servicio = new Servicio();

                                    servicio.setId(c.getId() + "");
                                    servicio.setNombre(c.getDescripcion());
                                    servicio.setSeleccionado(false);

                                    listaServicios.add(servicio);
                                }

                                //La lista que se obtuvo del método anterior se guarda en el adaptador
                                listServiciosAdapter = new ListServiciosAdapter(listaServicios);
                                rv.setAdapter(listServiciosAdapter);

                            } else {
                                Toast.makeText(context, getString(R.string.sin_servicios_cross), Toast.LENGTH_LONG).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                        Log.e("Error Serivicios", t.toString());
                    }
                });



            }
        } else { //Si ya se habían seleccionado servicios, se llena la lista con éstos.

            if(esNuevoProspecto) {
                listaServicios.clear();

                listaServicios.addAll(oportunidadVentaInicial.getServicios());
            }

        }

        //La lista que se obtuvo del método anterior se guarda en el adaptador
        listServiciosAdapter = new ListServiciosAdapter(listaServicios);
        rv.setAdapter(listServiciosAdapter);
    }

    private boolean todosSeleccionados() {
        boolean seleccion = true;

        for(Servicio s : listaServiciosSeleccionados) {
            if (!s.getSeleccionado()) {
                seleccion = false;
            }
        }

        return seleccion;
    }


    @Subscribe
    public void guardarServicio(Servicio servicio) {

        //Se obtienen los servicios seleccionados
        listaServiciosSeleccionados = listServiciosAdapter.traerServiciosSeleccionados();

        //Se guardan los servicios seleccionados en las shared preferences
        final TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListServiciosSeleccionados(Valores.SHAREDPREFERENCES_SERVICIOS_SELECCIONADOS, listaServiciosSeleccionados);

        if(todosSeleccionados() ) {
            tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_SERVICIOS, true);
        }

        /*if (tinyDB.getBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_PRODUCTOS) && todosSeleccionados() ) {
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

        //oportunidadVentaInicial.setEsOfertaIntegral(true);
        oportunidadVentaInicial.setServicios(listaServiciosSeleccionados);

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
