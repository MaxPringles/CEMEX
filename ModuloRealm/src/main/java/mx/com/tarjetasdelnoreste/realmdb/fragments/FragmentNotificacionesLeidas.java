package mx.com.tarjetasdelnoreste.realmdb.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.BuzonNotificacionesRealm;
import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.adapters.AdapterNotificacionesTodas;
import mx.com.tarjetasdelnoreste.realmdb.model.BuzonNotificacionesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonFiltroNotificaciones;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiClient;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiInterface;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by usr_micro13 on 01/02/2017.
 */

public class FragmentNotificacionesLeidas extends Fragment {

    private Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<BuzonNotificacionesDB> buzonNotificacionesDBList = new ArrayList<>();
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones_todas, container, false);

        context = getActivity();

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdapterNotificacionesTodas(context, buzonNotificacionesDBList);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        //Muestra el círculo de progreso.
        visibilidad(View.VISIBLE);

        return view;
    }

    /** MÉTODO QUE MUESTRA TODAS LAS NOTIFICACIONES LEÍDAS GUARDADAS EN LA BD **/
    public void mostrarListaNotificaciones() {

        buzonNotificacionesDBList = BuzonNotificacionesRealm.mostrarListaBuzonNotificacionesLeidas();

        if (buzonNotificacionesDBList.size() > 0) {

            adapter = new AdapterNotificacionesTodas(context, buzonNotificacionesDBList);
            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        } else {
            //Toast.makeText(context, getString(R.string.notificaciones_empty), Toast.LENGTH_LONG).show();
        }

        //Quita el círculo de progreso.
        visibilidad(View.GONE);
    }

    /** MÉTODO QUE MUESTRA U OCULTA EL PROGRESSBAR **/
    public void visibilidad(int visibilidad) {
        progressBar.setVisibility(visibilidad);
    }

    /** MÉTODO QUE RECIBE EL MENSAJE CUANDO LA BD YA TIENE LAS NOTIFICACIONES GUARDADAS **/
    @Subscribe
    public void getNotificaciones(String message) {

        if (message.equals("notificacionesObtenidas")) {
            mostrarListaNotificaciones();
        }
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
