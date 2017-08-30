package cemex.tmanager.telstock.com.moduloplansemanal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cemex.tmanager.telstock.com.moduloplansemanal.adapters.AdapterTipoActividad;
import cemex.tmanager.telstock.com.moduloplansemanal.funciones.TinyDB;
import cemex.tmanager.telstock.com.moduloplansemanal.interfaces.OnClickProspectos;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoActividadesPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas.JsonAltaActividadesAdministrativas;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by usr_micro13 on 28/02/2017.
 */

public class PlanSemanalAdministrativasVenta extends Fragment
    implements OnClickProspectos, View.OnClickListener {

    private Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager linearLayoutManager;

    private TextView txtSeleccionarActividad;
    private LinearLayout btnAdministrativasVentaCancelar;
    private LinearLayout btnAdministrativasVentaAgendar;

    private List<CatalogoActividadesPGVDB> catalogoActividadesPGVDBList = new ArrayList<>();
    private CatalogoActividadesPGVDB catalogoActividadesPGVDBSeleccionado;

    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tipo_administrativas_venta, container, false);

        context = getActivity();
        setHasOptionsMenu(true);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.plan_titulo));

        btnAdministrativasVentaCancelar = (LinearLayout) view.findViewById(R.id.btn_administrativas_venta_cancelar);
        btnAdministrativasVentaAgendar = (LinearLayout) view.findViewById(R.id.btn_administrativas_venta_agendar);
        txtSeleccionarActividad = (TextView) view.findViewById(R.id.txt_seleccionar_actividad);

        txtSeleccionarActividad.setText(getString(R.string.plan_tipo_administrativas_venta_title));

        btnAdministrativasVentaCancelar.setOnClickListener(this);
        btnAdministrativasVentaAgendar.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);

        catalogoActividadesPGVDBList = CatalogoActividadesPGVRealm.mostrarActividadesIdPadre(Valores.ID_PADRE_ACTIVIDADES_ADMINISTRATIVAS_VENTA);

        adapter = new AdapterTipoActividad(context, catalogoActividadesPGVDBList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onClickProspectos(View view, int item) {

        //Se guarda el estado (posición) del recyclerView.
        //Esto para que al actualizar la lista, el scroll no vuelva hasta la parte superior.
        Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        //Se colocan todos los radioButtons deseleccionados.
        for (int i = 0; i < catalogoActividadesPGVDBList.size(); i++) {
            catalogoActividadesPGVDBList.get(i).setChecked(false);
        }

        //Únicamente se muestra seleccionado el último radioButton presionado.
        //Se obtiene la actividad seleccionada específicamente.
        catalogoActividadesPGVDBList.get(item).setChecked(true);
        catalogoActividadesPGVDBSeleccionado = catalogoActividadesPGVDBList.get(item);

        adapter = new AdapterTipoActividad(context, catalogoActividadesPGVDBList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //Se recupera el estado (posición) del recyclerView.
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_administrativas_venta_cancelar) {
            Funciones.onBackPressedFunction(context, true);
        } else if (v.getId() == R.id.btn_administrativas_venta_agendar) {

            //Se guardan los datos del objeto que contiene la información de la actividad seleccionada.
            llenarActividadAdministrativaVenta();

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_CALENDAR);
            intent.putExtra(Valores.BUNDLE_ACTIVIDADES_ADMINISTRATIVAS, Valores.BUNDLE_ACTIVIDADES_ADMINISTRATIVAS);
            startActivity(intent);
        }
    }

    /** MÉTODO QUE GUARDA EL JSON DE LAS ACTIVIDADES ADMINISTRATIVAS EN TINYDB **/
    public void llenarActividadAdministrativaVenta() {

        JsonAltaActividadesAdministrativas jsonAltaActividadesAdministrativas = new JsonAltaActividadesAdministrativas();

        jsonAltaActividadesAdministrativas.setTipoActividad(catalogoActividadesPGVDBSeleccionado);

        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putJsonAltaActividadesAdministrativas(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS,
                jsonAltaActividadesAdministrativas);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Funciones.onBackPressedFunction(context, true);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
