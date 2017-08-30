package com.telstock.tmanager.cemex.modulocitas;

import android.content.Context;
import android.content.Intent;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;

/**
 * Created by czamora on 11/18/16.
 */

public class ArchivosFragment extends Fragment {

    private Context context;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    public static final int REQUEST_PRODUCTOS = 100;
    public static final int REQUEST_SERVICIOS = 200;
    public static final String STRING_PRODUCTOS = "productos";
    public static final String STRING_SERVICIOS = "servicios";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_archivos_prospecto, container, false);

        context = getActivity();
        setHasOptionsMenu(true);

        //Se especifica que la actividad contendrá un ToolBar y que éste
        //tendrá el botón de regreso.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle("Archivos");

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        //viewPager.setCurrentItem(2); Se elige en qué tabLayout iniciar el fragment.

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); //Se especifican los ViewPager que estarán ligados
        //a los TabLayouts.

        return view;
    }

    //Método que coloca los fragmentos (viewPager) y sus títulos.
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ArchivosFragmentDescarga(), "VER ARCHIVOS");
        adapter.addFragment(new ArchivosFragmentAdd(), "AÑADIR");

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e){
            Log.d("ArchivosFragment", e.toString());
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

    /**
     * MENSAJE QUE SE RECIBE DE LA CLASE ArchivosFragmentAdd, RECIBE LA INDICACIÓN
     * DE MOSTRAR LA PANTALLA DE DESCARGA DE EXPEDIENTE
     **/
    @Subscribe
    public void getImagenesSubidas(String imagenesSubidas) {
        if (imagenesSubidas.equals(getString(R.string.uploader_citas_ok))) {
            viewPager.setCurrentItem(0, true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }
}
