package mx.com.tarjetasdelnoreste.realmdb.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;

/**
 * Created by usr_micro13 on 01/02/2017.
 */

public class FragmentNotificacionesGeneral extends Fragment {

    private Context context;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notificaciones, container, false);

        context = getActivity();
        setHasOptionsMenu(true);

        //Se especifica que la actividad contendrá un ToolBar y que éste
        //tendrá el botón de regreso.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.notificaciones_title));

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        setupViewPager(viewPager);
        //viewPager.setCurrentItem(2); Se elige en qué tabLayout iniciar el fragment.

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager); //Se especifican los ViewPager que estarán ligados

        return view;
    }

    //Método que coloca los fragmentos (viewPager) y sus títulos.
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentNotificacionesTodas(), "BUZÓN");
        adapter.addFragment(new FragmentNotificacionesLeidas(), "LEÍDAS");

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
