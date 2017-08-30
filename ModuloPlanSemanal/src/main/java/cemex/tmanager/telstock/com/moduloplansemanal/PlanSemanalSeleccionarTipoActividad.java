package cemex.tmanager.telstock.com.moduloplansemanal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by usr_micro13 on 28/02/2017.
 */

public class PlanSemanalSeleccionarTipoActividad extends Fragment implements View.OnClickListener {

    private Context context;

    private LinearLayout btnVenta;
    private LinearLayout btnAdministrativasVenta;
    private LinearLayout btnVentaAdministrativas;

    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seleccion_tipo_actividad, container, false);

        context = getActivity();
        setHasOptionsMenu(true);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.plan_titulo));

        btnVenta = (LinearLayout) view.findViewById(R.id.btn_venta);
        btnAdministrativasVenta = (LinearLayout) view.findViewById(R.id.btn_administrativas_venta);
        btnVentaAdministrativas = (LinearLayout) view.findViewById(R.id.btn_administrativas);

        btnVenta.setOnClickListener(this);
        btnAdministrativasVenta.setOnClickListener(this);
        btnVentaAdministrativas.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_venta) {

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_VENTAS);
            startActivity(intent);

        } else if (v.getId() == R.id.btn_administrativas_venta) {

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_ADMINISTRATIVAS_VENTAS);
            startActivity(intent);

        } else if (v.getId() == R.id.btn_administrativas) {

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_ADMINISTRATIVAS);
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_plan_semanal, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            Funciones.onBackPressedFunction(context, true);

            return true;
        } else if (item.getItemId() == R.id.menu_calendar) {
            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_CALENDAR);
            intent.putExtra(Valores.BUNDLE_IR_A_CALENDARIO, Valores.BUNDLE_IR_A_CALENDARIO);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
