package mx.com.tarjetasdelnoreste.realmdb.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.CoordenadasRealm;
import mx.com.tarjetasdelnoreste.realmdb.MenuRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.model.CoordenadasDB;
import mx.com.tarjetasdelnoreste.realmdb.model.Ruta;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiClient;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiInterface;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USRMICRO10 on 08/11/2016.
 */
public class ServiceEnvioDeCoordenadas extends IntentService {

    //MenuRealm realmDB;
    List<CoordenadasDB> listaCoordenadas;
    List<Ruta> listaRutas= new ArrayList<>();
    List<LatLng> listaCoordenadasLatLng = new ArrayList<>();
    LatLng coordenada;
    String coordenadasCodificadas;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private ApiInterface apiInterface;
    Realm realmDB;
    SharedPreferences sharedPreferences;

    public ServiceEnvioDeCoordenadas() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        realmDB = Realm.getDefaultInstance();

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, MODE_PRIVATE);

        listaCoordenadas = realmDB.where(CoordenadasDB.class)
                .equalTo("enviado", Valores.ESTATUS_NO_ENVIADO)
                .findAll();

        if(!listaCoordenadas.isEmpty()) {

            Log.d("FUNCION_COORDENADA", "SERVICIO");

            CoordenadasDB c;
            Ruta ruta = new Ruta();

            for(int i = 0; i < listaCoordenadas.size(); i++) {
                c = new CoordenadasDB();
                coordenada = new LatLng(listaCoordenadas.get(i).getLatitudCoordenada(),listaCoordenadas.get(i).getLongitudCoordenada());
                listaCoordenadasLatLng.add(coordenada);
                c.setFechaCoordenada(listaCoordenadas.get(i).getFechaCoordenada());
                c.setEnviado(Valores.ESTATUS_ENVIANDO);
                c.setLatitudCoordenada(listaCoordenadas.get(i).getLatitudCoordenada());
                c.setLongitudCoordenada(listaCoordenadas.get(i).getLongitudCoordenada());
                c.setIdAccion(listaCoordenadas.get(i).getIdAccion());

                ruta.setmFecha(listaCoordenadas.get(i).getFechaCoordenada());
                ruta.setmLatitud(listaCoordenadas.get(i).getLatitudCoordenada());
                ruta.setmLongitud(listaCoordenadas.get(i).getLongitudCoordenada());
                ruta.setmIdAccion(listaCoordenadas.get(i).getIdAccion());
                ruta.setmIdAltaOffline(UUID.randomUUID().toString());
                ruta.setmIdVendedor(sharedPreferences.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
                ruta.setmStatus(1);
                listaRutas.add(ruta);

                try {
                    realmDB.beginTransaction();
                    realmDB.copyToRealmOrUpdate(c);
                    realmDB.commitTransaction();
                } catch (Exception e) {
                    Log.e("ERROR_COORDENADAS_WS", e.toString());
                    Log.d("FUNCION_COORDENADA", "ERROR_DB");
                }
            }

            Log.d("FUNCION_COORDENADA", "SUCCESS_DB");

//            Ruta ruta = new Ruta();
//            ruta.setFecha(Calendar.getInstance().getTimeInMillis() / 1000);
//            ruta.setHoraInicio(obtenerHora(listaCoordenadas.get(0).getFechaCoordenada()));
//            ruta.setHoraFin(obtenerHora(listaCoordenadas.get(listaCoordenadas.size() - 1).getFechaCoordenada()));
//            ruta.setRuta(PolyUtil.encode(listaCoordenadasLatLng));
//            ruta.setIdVendedor(sharedPreferences.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));

       //     String json = new Gson().toJson(ruta);

            //Se inicializa la interfaz
            apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
//            Call<String> setRuta = apiInterface.setRuta(ruta);
            Call<String> setCoordenadas = apiInterface.setCoordenadas(listaRutas);

            setCoordenadas.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.code() == 200) {

                        Log.d("FUNCION_COORDENADA", "SERVICIO_SUCCESS");

                        Log.d("Ruta","Exitoso");
                        realmDB = Realm.getDefaultInstance();
                        CoordenadasDB c;
                        List<CoordenadasDB> listaCoordenadasPendientes = realmDB.where(CoordenadasDB.class)
                                .equalTo("enviado", Valores.ESTATUS_ENVIANDO)
                                .findAll();

                        for(int i = 0; i < listaCoordenadasPendientes.size(); i++) {
                            c = new CoordenadasDB();
                            c.setFechaCoordenada(listaCoordenadasPendientes.get(i).getFechaCoordenada());
                            c.setEnviado(Valores.ESTATUS_ENVIADO);
                            c.setLatitudCoordenada(listaCoordenadasPendientes.get(i).getLatitudCoordenada());
                            c.setLongitudCoordenada(listaCoordenadasPendientes.get(i).getLongitudCoordenada());
                            c.setIdAccion(listaCoordenadas.get(i).getIdAccion());

                            try {
                                realmDB.beginTransaction();
                                realmDB.copyToRealmOrUpdate(c);
                                realmDB.commitTransaction();
                            } catch (Exception e) {
                                Log.e("ERROR_COORDENADAS_WS", e.toString());

                                Log.d("FUNCION_COORDENADA", "SERVICIO_DB_FAIL");
                            }
                        }

                        realmDB.close();
                    } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                        cambiarStatusANoEnviado();
                        //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                        AlertTokenToLogin.showAlertService(getApplicationContext());
                    } else {
                        Log.e("Rutas_Code", "Error servicio de coordenadas");
                        cambiarStatusANoEnviado();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                    Log.d("FUNCION_COORDENADA", "SERVICIO_ERROR");
                    Log.e("Rutas",t.toString());

                    cambiarStatusANoEnviado();

                }
            });
        }

        realmDB.close();
    }

    public void cambiarStatusANoEnviado() {

        realmDB = Realm.getDefaultInstance();
        CoordenadasDB c;
        List<CoordenadasDB> listaCoordenadasPendientes = realmDB.where(CoordenadasDB.class)
                .equalTo("enviado", Valores.ESTATUS_ENVIANDO)
                .findAll();

        for(int i = 0; i < listaCoordenadasPendientes.size(); i++) {
            c = new CoordenadasDB();
            c.setFechaCoordenada(listaCoordenadasPendientes.get(i).getFechaCoordenada());
            c.setEnviado(Valores.ESTATUS_NO_ENVIADO);
            c.setLatitudCoordenada(listaCoordenadasPendientes.get(i).getLatitudCoordenada());
            c.setLongitudCoordenada(listaCoordenadasPendientes.get(i).getLongitudCoordenada());
            c.setIdAccion(listaCoordenadas.get(i).getIdAccion());


            try {
                realmDB.beginTransaction();
                realmDB.copyToRealmOrUpdate(c);
                realmDB.commitTransaction();
            } catch (Exception e) {
                Log.e("ERROR_COORDENADAS_WS", e.toString());
                Log.d("FUNCION_COORDENADA", "SERVICIO_ERROR_DB");
            }
        }

        realmDB.close();
    }

    private String obtenerHora(String fecha) {
        String hora = "";
        Calendar c = Calendar.getInstance(Locale.getDefault());

        try {
            c.setTime(simpleDateFormat.parse(fecha));
//            hora = String.format("%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE);
            hora = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        } catch (Exception e) {
            Log.d("Parser","Error parse hora");

            Log.d("FUNCION_COORDENADA", "ERROR_HORA");
        }

        return hora;
    }
}
