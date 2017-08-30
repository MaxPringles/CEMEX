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

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.BuzonNotificacionesRealm;
import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.adapters.AdapterNotificacionesTodas;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
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

public class FragmentNotificacionesTodas extends Fragment {

    private Context context;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<BuzonNotificacionesDB> buzonNotificacionesDBList;

    private SharedPreferences prefs;
    private ApiInterface apiInterface;
    private Call<List<BuzonNotificacionesDB>> buzonNotificacionesCall;

    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notificaciones_todas, container, false);

        context = getActivity();
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Muestra el círculo de progreso.
        visibilidad(View.VISIBLE);
        obtenerNotificaciones();

        return view;
    }

    /** MÉTODO QUE OBTIENE TODAS LAS NOTIFICACIONES DEL WS **/
    public void obtenerNotificaciones() {

        JsonFiltroNotificaciones jsonFiltroNotificaciones = new JsonFiltroNotificaciones();
        jsonFiltroNotificaciones.setIdUsuario(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
        jsonFiltroNotificaciones.setStatus("1");

        buzonNotificacionesCall = apiInterface.getNotificaciones(jsonFiltroNotificaciones);

        buzonNotificacionesCall.enqueue(new Callback<List<BuzonNotificacionesDB>>() {
            @Override
            public void onResponse(Call<List<BuzonNotificacionesDB>> call, Response<List<BuzonNotificacionesDB>> response) {

                if (response.body() != null && response.code() == 200) {
                    BuzonNotificacionesRealm.guardarListaBuzonNotificaciones(response.body());
                    //Sea exitoso o fallido, se muestran las notificaciones guardadas en la BD.
                    mostrarListaNotificaciones();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    Toast.makeText(context, getString(R.string.notificaciones_cargar_fail), Toast.LENGTH_LONG).show();
                    //Sea exitoso o fallido, se muestran las notificaciones guardadas en la BD.
                    mostrarListaNotificaciones();
                }
            }

            @Override
            public void onFailure(Call<List<BuzonNotificacionesDB>> call, Throwable t) {
                if (!buzonNotificacionesCall.isCanceled()) {
                    Log.e("GETNOTIFICACIONES", t.toString());
                    FirebaseCrash.log("Error NotificacionesDB");
                    FirebaseCrash.report(t);

                    Toast.makeText(context, getString(R.string.notificaciones_cargar_fail), Toast.LENGTH_LONG).show();

                    //Se muestran las notificaciones guardadas en la BD.
                    mostrarListaNotificaciones();
                }
            }
        });
    }

    /** MÉTODO QUE MUESTRA TODAS LAS NOTIFICACIONES GUARDADAS EN LA BD **/
    public void mostrarListaNotificaciones() {

        EventBus.getDefault().post("notificacionesObtenidas");

        buzonNotificacionesDBList = BuzonNotificacionesRealm.mostrarListaBuzonNotificacionesTodas();

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

    public void cancelAllRequests() {

        if (buzonNotificacionesCall != null) {
            buzonNotificacionesCall.cancel();
        }
    }

    /** MÉTODO QUE MUESTRA U OCULTA EL PROGRESSBAR **/
    public void visibilidad(int visibilidad) {
        progressBar.setVisibility(visibilidad);
    }
}
