package com.telstock.tmanager.cemex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.telstock.tmanager.cemex.modulocitas.ActividadCalificarOportunidad4;
import com.telstock.tmanager.cemex.modulocitas.ActividadCerrarVenta8;
import com.telstock.tmanager.cemex.modulocitas.ActividadContactarProspectoCliente1;
import com.telstock.tmanager.cemex.modulocitas.ActividadPresentarPropuesta6;
import com.telstock.tmanager.cemex.modulocitas.ActividadVisitarProspecto2;
import com.telstock.tmanager.cemex.modulocitas.OfertaIntegralCitasFragment;
import com.telstock.tmanager.cemex.modulocitas.PrincipalCitasFragment;
import com.telstock.tmanager.cemex.prospectos.ProspectosFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cemex.tmanager.telstock.com.moduloplansemanal.PlanSemanalFragment;
import cemex.tmanager.telstock.com.moduloplansemanal.PlanSemanalSeleccionarTipoActividad;

/**
 * Created by czamora on 8/10/16.
 */
public class HomeFragment extends Fragment {

    //Contexto de la aplicación.
    private Context context;

    // UI referencias.
    @Nullable
    @BindView(R.id.btn_menu_plan_semanal)
    LinearLayout btn_menu_plan_semanal;
    @Nullable
    @BindView(R.id.btn_menu_prospectos)
    LinearLayout btn_menu_prospectos;
    @Nullable
    @BindView(R.id.btn_menu_cita_visita)
    LinearLayout btn_menu_cita_visita;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Se inicializa el contexto.
        context = getActivity();
        //Se inicializan los elementos de la interfaz.
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.btn_menu_plan_semanal, R.id.btn_menu_prospectos, R.id.btn_menu_cita_visita})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu_plan_semanal:
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new PlanSemanalSeleccionarTipoActividad())
                        .commit();
                break;

            case R.id.btn_menu_cita_visita:
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new PrincipalCitasFragment())
                        .commit();

//                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, new ActividadCerrarVenta8())
//                        .commit();

                /*((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new OfertaIntegralCitasFragment())
                        .commit();*/
                //Toast.makeText(context, "CITAS / VISITAS", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_menu_prospectos:
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, new ProspectosFragment())
                        .commit();

                break;
        }
    }
}
