package com.telstock.tmanager.cemex.modulocitas;

/**
 * Created by czamora on 10/13/16.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.telstock.tmanager.cemex.modulocitas.funciones.TinyDB;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.SubsegmentoProducto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

public class OfertaIntegralCitasFragment extends Fragment {

    private Context context;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button btnGuardar;
    private Button btnCancelar;
    private int idTipoProspecto;

    public static final int REQUEST_PRODUCTOS = 100;
    public static final int REQUEST_SERVICIOS = 200;
    public static final String STRING_PRODUCTOS = "productos";
    public static final String STRING_SERVICIOS = "servicios";

    SharedPreferences prefs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos_servicios_principal, container, false);

        context = getActivity();
        setHasOptionsMenu(true);

        //Se especifica que la actividad contendrá un ToolBar y que éste
        //tendrá el botón de regreso.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        btnGuardar = (Button) view.findViewById(R.id.btnGuardar);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelar);

        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle("Productos y Servicios");

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        //viewPager.setCurrentItem(2); Se elige en qué tabLayout iniciar el fragment.

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); //Se especifican los ViewPager que estarán ligados
        //a los TabLayouts.
        TinyDB tinyDB = new TinyDB(context);
        try {
           int y = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size();
           int x = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class).size();
            //Se inicializa la SharedPreferences.
            prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

            //Se obtiene el tipo de prospecto
            idTipoProspecto = prefs.getInt(Valores.SHAREDPREFERENCES_ID_TIPO_PROSPECTO, 0);
            Log.e("numero",""+x+" sdf" +y);
            if (idTipoProspecto == Valores.ID_TIPO_PROSPECTO_NUEVO_PROSPECTO) {
                if(y == 0 || x == 0)
                {
                    Funciones.onBackPressedFunction(context, true);
                    Toast.makeText(context, "Error al cargar productos y servicios", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.d("", "");
        }



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Producto p = new Producto();
                Servicio s = new Servicio();
                EventBus.getDefault().post(p);
                EventBus.getDefault().post(s);

//                OportunidadVenta oportunidadVenta = new OportunidadVenta();
                TinyDB tinyDB = new TinyDB(context);
                List<SubsegmentoProducto> listaSubsegmento = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
                List<Servicio> listaServicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

//                oportunidadVenta.setServicios(listaServicio);
//                oportunidadVenta.setSubsegmentosProductos(listaSubsegmento);
//
//                tinyDB.putOfertaIntegral(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, oportunidadVenta);

                Funciones.onBackPressedFunction(context, true);


            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Funciones.onBackPressedFunction(context, true);
            }
        });

        return view;
    }

    //Método que coloca los fragmentos (viewPager) y sus títulos.
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProductosCitasFragment(), "PRODUCTOS");
        adapter.addFragment(new ServiciosCitasFragment(), "SERVICIOS");

        viewPager.setAdapter(adapter); //Se ligan los fragmentos a cada uno de los ViewPager.
    }

    //Clase que permite ligar los ViewPager con los TabLayouts, así como devolver
    //sus títulos y el número de éstos.
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        //Devuelve el fragmento, dependiendo de la posición actual.
        @Override
        public Fragment getItem(int position){
            return mFragmentList.get(position);
        }

        //Devuelve el número de fragmentos ligados al viewPager.
        @Override
        public int getCount(){
            return mFragmentList.size();
        }

        //Método que añade el par fragmento-titulo a cada viewPager.
        public void addFragment(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //Ejecuta el método onBackPressed() de la actividad madre.
            Funciones.onBackPressedFunction(context, true);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
