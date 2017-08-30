package com.telstock.tmanager.cemex.prospectos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.telstock.tmanager.cemex.prospectos.adapters.ListProductosAdapter;
import com.telstock.tmanager.cemex.prospectos.funciones.TinyDB;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoProductosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoServiciosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSubsegmentosRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.OportunidadVentaInicial;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubsegmentosProducto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by USRMICRO10 on 25/08/2016.
 */
public class OfertaIntegralFragment extends Fragment {

    Context context;
    String idSubsegmento;
    private Button btnGuardar;
    private Button btnCancelar;

    OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
    List<Servicio> listaServiciosSeleccionados = new ArrayList<>();
    List<Producto> listaSubSegmento = new ArrayList<>();

    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<SubsegmentosProducto> listaSubSegmentosProductos = new ArrayList<>();
    ArrayList<Servicio> listaServicios = new ArrayList<>();

    SharedPreferences prefs;
    boolean esNuevoProspecto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_productos_servicios, container, false);

        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        esNuevoProspecto = prefs.getBoolean(Valores.SHARED_PREFERENCES_ES_NUEVO_PROSPECTO, false);

        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        btnGuardar = (Button) view.findViewById(R.id.btnGuardar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);

        final Bundle args = getArguments();
        idSubsegmento = args.getString(Valores.BUNDLE_ID_SUBSEGMENTO);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        if(viewPager != null) {
            setUpViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //Detecta tanto el scroll como el click del ViewPager.
            @Override
            public void onPageSelected(int position) {
                //Cierra el teclado al pasar al fragmento AltaListaContactos.
                if (viewPager.getCurrentItem() == 1) {
                    cerrarTeclado(view);
                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubsegmentosProducto subsegmentosProducto = new SubsegmentosProducto();
                Servicio servicio = new Servicio();

                EventBus.getDefault().post(subsegmentosProducto);
                EventBus.getDefault().post(servicio);
                TinyDB tinyDB = new TinyDB(context);
                oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL_TEMPORAL, OportunidadVentaInicial.class);

                if (oportunidadVentaInicial.getSubsegmentosProductos() != null &&
                        oportunidadVentaInicial.getServicios() != null) {
                    listaSubSegmento = oportunidadVentaInicial.getSubsegmentosProductos().get(0).getProductos();
                    listaServiciosSeleccionados = oportunidadVentaInicial.getServicios();

                    if (tieneProductoServicio()) {
                        try {


                            if (todosSeleccionadosProductos() && todosSeleccionadosServicios() && esNuevoProspecto ) {
                                oportunidadVentaInicial.setEsOfertaIntegral(true);

                                //Coloca el radioButton de "sí" en seleccionado.
                                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL, true);
                            } else {
                                oportunidadVentaInicial.setEsOfertaIntegral(false);

                                //Coloca el radioButton de "no" en seleccionado.
                                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL, false);
                            }

                            tinyDB.putOfertaIntegral(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, oportunidadVentaInicial);

                        } catch (Exception e) {
                            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
                        }
                        Funciones.onBackPressedFunction(context, true);
                    } else {
                        Toast.makeText(context, "Debe selecionar almenos un producto/servicio", Toast.LENGTH_LONG).show();
                    }
                }

                //Se ejecuta el botón "back" del dispositivo.

            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final TinyDB tinyDB = new TinyDB(context);
                OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();

                try {
                    oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
                } catch (Exception e) {
                    Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
                }


                //Revisa si ya se han llenado los productos y servicios previamente (para esto,
                //basta con revisar si los servicios son null), en caso de que no se hayan llenado,
                //entonces se llena la oportunidad de venta con los productos y servicios vacíos.
                if (oportunidadVentaInicial.getServicios() == null || !esNuevoProspecto) {

                    if (esNuevoProspecto) {
                        llenarProductosVacios();
                        llenarServiciosVacios();
                    }

                    //Coloca el radioButton de "si" en seleccionado.
                    tinyDB.putBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL, true);
                } else {
                    if (oportunidadVentaInicial.getEsOfertaIntegral()) {
                        //Coloca el radioButton de "sí" en seleccionado.
                        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL, true);
                    }
                }


                //Se borran las sharedPreferences correspondientes a productos y servicios.
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_PRODUCTOS, false);
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_SERVICIOS, false);

                //Se ejecuta el botón "back" del dispositivo.
                Funciones.onBackPressedFunction(context, true);
            }
        });


        return view;
    }

    private void llenarProductosVacios() {

        listaSubSegmentosProductos.clear();

        prepararListaProductos();

        SubsegmentosProducto subSegmento = new SubsegmentosProducto();
        subSegmento.setProductos(listaProductos);
        subSegmento.setIdSubsegmento(idSubsegmento);
        subSegmento.setNombre(CatalogoSubsegmentosRealm.obtenerNombreSubsegmento(idSubsegmento));
        subSegmento.setTodosSeleccion(false);
        listaSubSegmentosProductos.add(subSegmento);

        //Se guarda la lista de productos seleccionados en las shared preferences
        final TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListSubsegmentosSeleccionados(Valores.SHAREDPREFERENCES_SUBSEGMENTOS_SELECCIONADOS, listaSubSegmentosProductos);

        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_PRODUCTOS, false);

        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
        try {
            oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
        } catch (Exception e) {
            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
        }

        /*** DESDE AQUÍ SE INDICA QUE LA OFERTA NO SERÁ INTEGRAL ***/
        oportunidadVentaInicial.setEsOfertaIntegral(false);

        oportunidadVentaInicial.setSubsegmentosProductos(listaSubSegmentosProductos);

        tinyDB.putOfertaIntegral(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, oportunidadVentaInicial);
    }

    public void llenarServiciosVacios() {

        //Se obtiene la lista de servicios vacíos.
        prepararListaServicios();

        //Se guardan los servicios seleccionados en las shared preferences
        final TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListServiciosSeleccionados(Valores.SHAREDPREFERENCES_SERVICIOS_SELECCIONADOS, listaServicios);

        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_SERVICIOS, false);

        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
        try {
            oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
        } catch (Exception e) {
            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
        }

        oportunidadVentaInicial.setServicios(listaServicios);

        tinyDB.putOfertaIntegral(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, oportunidadVentaInicial);
    }

    private void prepararListaProductos() {
        //Inicializa las listas
        List<CatalogoProductoDB> listaTemp;

        Producto producto;

        //Obtiene la lista de los servicios de la base de datos
        listaTemp = CatalogoProductosRealm.mostrarListaProductoPorId(Long.parseLong(idSubsegmento));
        for(CatalogoProductoDB c : listaTemp) {
            producto = new Producto();

            producto.setId(c.getId() + "");
            producto.setNombre(c.getDescripcion());
            producto.setSeleccionado(c.isChecked());

            listaProductos.add(producto);
        }
    }

    private void prepararListaServicios() {
        //Inicializa las listas
        List<CatalogoServiciosDB> listaTemp;
        Servicio servicio;

        //Obtiene la lista de los servicios de la base de datos
        listaTemp = CatalogoServiciosRealm.mostrarListaServicios();

        for (CatalogoServiciosDB c : listaTemp) {
            servicio = new Servicio();

            servicio.setId(c.getId() + "");
            servicio.setNombre(c.getDescripcion());
            servicio.setSeleccionado(c.isChecked());

            listaServicios.add(servicio);
        }
    }


    private void setUpViewPager(ViewPager viewPager) {
        //Se coloca getChildFragmentManager en vez de getSupportFragmentManager, ya que el último
        //no siempre funciona bien cuando se trata de albergar varias vistas.
        Bundle bundle = new Bundle();
        bundle.putString("idSubsegmento",idSubsegmento);
        Adapter adapter = new Adapter(getChildFragmentManager(), bundle);
        adapter.addFragment(new ProductosFragment(), getResources().getString(R.string.tab_productos));
        adapter.addFragment(new ServiciosFragment(), getResources().getString(R.string.tab_servicios));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    /** MÉTODO QUE CIERRA EL TECLADO **/
    public void cerrarTeclado(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** MÉTODO QUE SE EJECUTA AL DETECTAR UN CAMBIO DE ROTACIÓN DEL DISPOSITIVO **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    /** MÉTODO QUE SE EJECUTA AL CREAR/RECREAR EL FRAGMENTO **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se declara en este método, ya que es el primero en ser llamado.
        context = getActivity();
    }

    private boolean todosSeleccionadosProductos() {
        boolean seleccion = true;

        for(Producto p : listaSubSegmento) {
            if (!p.getSeleccionado()) {
                seleccion = false;
            }
        }

        return seleccion;
    }

    private boolean todosSeleccionadosServicios() {
        boolean seleccion = true;

        for(Servicio s : listaServiciosSeleccionados) {
            if (!s.getSeleccionado()) {
                seleccion = false;
            }
        }

        return seleccion;
    }
    private boolean tieneProductoServicio() {
        boolean seleccion = false;

        for(Servicio s : listaServiciosSeleccionados) {
            if (s.getSeleccionado()) {
                seleccion = true;
                return seleccion;
            }
        }
        for(Producto p : listaSubSegmento) {
            if (p.getSeleccionado()) {
                seleccion = true;
                return seleccion;
            }
        }
        return seleccion;
    }

}
