package com.telstock.tmanager.cemex.prospectos;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.telstock.tmanager.cemex.prospectos.funciones.TinyDB;

import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertDialogModal;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarAlertDialog;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by USRMICRO10 on 25/08/2016.
 */
public class AltaProspectoFragment extends Fragment implements ComunicarAlertDialog {

    //Modal que muestra mensaje de alerta.
    Dialog dialogModal;
    private static final String ID_SALIR = "idSalir";

    //Variable que obtiene el contexto de la interfaz.
    ComunicarAlertDialog comunicarAlertDialog;

    Toolbar toolbar;
    Context context;

    private boolean viewPagerContactos = false;
    TinyDB tinyDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_alta_prospecto, container, false);

        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.prospectos_title));
        toolbar.setTitle(getResources().getString(R.string.titulo_alta_prospecto));

        comunicarAlertDialog = this;

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
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
                    viewPagerContactos = true;
                    cerrarTeclado(view);
                } else {
                    viewPagerContactos = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Indica que antes de la rotación, se mostraba el fragmento de Contactos (posición 1).
        if (tinyDB.getBoolean(Valores.SHAREDPREFERENCES_INFORMACION_CONTACTO_VIEWPAGER)) {
            viewPager.setCurrentItem(1);
        }

        return view;
    }

    private void setUpViewPager(ViewPager viewPager) {
        //Se coloca getChildFragmentManager en vez de getSupportFragmentManager, ya que el último
        //no siempre funciona bien cuando se trata de albergar varias vistas.
        Bundle bundle = new Bundle();
        Adapter adapter = new Adapter(getChildFragmentManager(), bundle);
        adapter.addFragment(new AltaProspectoInformacionGeneral(), getResources().getString(R.string.tab_informacion_general));
        adapter.addFragment(new AltaListaContactos(), getResources().getString(R.string.tab_contactos));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            dialogModal = AlertDialogModal.showModalTwoButtonsNoTitle(context, comunicarAlertDialog,
                    "¿Estás seguro que deseas salir del Alta de Prospecto?",
                    getString(R.string.btn_ok), getString(R.string.btn_cancelar), ID_SALIR);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
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

        //Guarda el valor que detecta si se está en el fragmento de Contactos o no.
        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_INFORMACION_CONTACTO_VIEWPAGER, viewPagerContactos);

    }

    /** MÉTODO QUE SE EJECUTA AL CREAR/RECREAR EL FRAGMENTO **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se declara en este método, ya que es el primero en ser llamado.
        context = getActivity();

        //Inicializa TinyDB.
        tinyDB = new TinyDB(context);
    }

    //Se coloca la ejecución del botón "back" del dispositivo.
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        dialogModal = AlertDialogModal.showModalTwoButtonsNoTitle(context, comunicarAlertDialog,
                                "¿Estás seguro que deseas salir del Alta de Prospecto?",
                                getString(R.string.btn_ok), getString(R.string.btn_cancelar), ID_SALIR);

                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void alertDialogPositive(String idDialog) {

        switch (idDialog) {
            case ID_SALIR:
                //Ejecuta el método onBackPressed() de la actividad madre.
                Funciones.onBackPressedFunction(context, true);

                //Se borra la llave "viewPagerContactos" para que el viewPager de Prospectos se inicialice bien.
                tinyDB.remove(Valores.SHAREDPREFERENCES_INFORMACION_CONTACTO_VIEWPAGER);
                break;
        }
    }

    @Override
    public void alertDialogNegative(String idDialog) {

    }

    @Override
    public void alertDialogNeutral(String idDialog) {

    }
}
