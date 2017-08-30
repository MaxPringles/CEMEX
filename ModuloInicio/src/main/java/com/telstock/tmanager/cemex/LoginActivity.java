package com.telstock.tmanager.cemex;

import android.*;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;
import com.telstock.tmanager.cemex.funciones.InformacionTelefonica;
import com.telstock.tmanager.cemex.funciones.TinyDBInicio;
import com.telstock.tmanager.cemex.model.GetMenuPOJO;
import com.telstock.tmanager.cemex.model.GetUsuarioPOJO;
import com.telstock.tmanager.cemex.model.Token;
import com.telstock.tmanager.cemex.rest.ApiClient;
import com.telstock.tmanager.cemex.rest.ApiInterface;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.com.tarjetasdelnoreste.realmdb.MenuRealm;
import mx.com.tarjetasdelnoreste.realmdb.broadcasts.BroadcastCoordenadas;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Ventana de Login.
 */
public class LoginActivity extends AppCompatActivity {

    //Contexto de la aplicación.
    private Context context;

    // UI referencias.
    @Nullable
    @BindView(R.id.login_usuario)
    AutoCompleteTextView tvUsuario;

    @Nullable
    @BindView(R.id.login_contrasenia)
    AutoCompleteTextView tvContrasenia;

    @Nullable
    @BindView(R.id.accion_inicia_sesion)
    Button btnIniciarSesion;

    @Nullable
    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Nullable
    @BindView(R.id.layout_login_usuario)
    TextInputLayout layoutUsuario;

    @Nullable
    @BindView(R.id.layout_login_contrasena)
    TextInputLayout layoutContrasena;

    //Clase que nos permite obtener el imei del móvil.
    private InformacionTelefonica informacionTelefonica;

    private static final int CODIGO_DE_PERMISOS = 100;

    private ApiInterface apiInterface;

    List<String> listaPermisos = new ArrayList<>();
    //Se crean las variables para las SharedPreferences
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    //Objeto que usa la vista del botón para guardar el teclado y mostrar Snackbar al momento
    //de aceptar los permisos.
    View viewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Se inicializa el contexto.
        context = this;

        //Se inicializan las SharedPreferences
        sharedPref = getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //Se inicializan los elementos de la interfaz.
        ButterKnife.bind(this);

        //Revisa si la sesión ya había sido iniciada, de ser así, se redirecciona
        //a la pantalla de Home.
        if (sharedPref.getBoolean(Valores.SHARED_PREFERENCES_SESION_INICIADA, false)) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }

        //Se inicializa la interfaz que contiene los métodos de WS
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se establece la acción del botón de inicio de sesión.
        btnIniciarSesion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                viewButton = view;
                pedirPermisos(view);
                /*cerrarTeclado(view);
                intentarInicioSesion(view);*/

                //Intent intent = new Intent(context, MainActivity.class);
                //startActivity(intent);
                // Se ocultan los campos de texto para que se muestre el progress bar
//                visibilidad(View.VISIBLE);
//                obtenerMenu(view);
            }
        });

        //Se inicializa la clase para obtener IMEI.
        informacionTelefonica = new InformacionTelefonica(context);

        editor.putString(Valores.SHARED_PREFERENCES_URL_WS, Url.URL_WEBSERVICE);
        editor.commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
//        tvUsuario.setText("aaviezca");
//        tvContrasenia.setText("123456");
//        tvUsuario.setText("vendedorMax");
//        tvContrasenia.setText("123456");
//        tvUsuario.setText("jlmolinaq");
//        tvContrasenia.setText("garbanzo01");

        visibilidad(View.GONE);
    }

    /**
     * Función que hace la llamada a web service para que se valide el usuario y la contraseña.
     */
    private void intentarInicioSesion(final View view) {

        // Se resetan los errores en los campos.
        tvUsuario.setError(null);
        tvContrasenia.setError(null);


        // Se obtienen los valores del usuario y la contraseña.
        String usuario = tvUsuario.getText().toString();
        String contrasenia = tvContrasenia.getText().toString();

        //Bandera que indica si hay algún error en los campos.
        boolean cancel = false;
        //Elemento que debe de corregirse.
        View focusView = null;


        // Se valida si se capturo el usuario, si no se enfoca el campo para que se corrija.
        if (TextUtils.isEmpty(usuario)) {
            tvUsuario.setError(getString(R.string.error_campo_obligatorio));
            focusView = tvUsuario;
            cancel = true;
        } else if (TextUtils.isEmpty(contrasenia) && !isPasswordValid(contrasenia)) {   // Se valida si se capturó la contraseña,
            // si no se enfoca el campo para que se corrija.
            tvContrasenia.setError(getString(R.string.error_campo_obligatorio));
            focusView = tvContrasenia;
            cancel = true;
        }

        if (cancel) {
            // Hubo un error en la captura de los campos.
            // Se enfoca el campo que tiene el problema.
            focusView.requestFocus();
        } else {
            // Se ocultan los campos de texto para que se muestre el progress bar
            visibilidad(View.VISIBLE);
            // Realiza el llamado de la Web API
            // Se envía el usuario, contraseña, plataforma y empresa por el Web API
            final Call<Token> tokenCall = apiInterface.getToken(tvUsuario.getText().toString(),
                    tvContrasenia.getText().toString(),
                    Valores.plataforma,
                    Valores.idEmpresa,
//                    "358013050158618");
                    new InformacionTelefonica(context).obtenerImei());

            tokenCall.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    //Si es exitoso guardamos el token y se dirige al home
                    if (response.isSuccessful()) {
                        //Se guarda el token en las SharedPreferences
                        editor.putString(Valores.SHARED_PREFERENCES_TOKEN, response.body().getAccess_token());
                        editor.putString(Valores.SHARED_PREFERENCES_URL_WS, Url.URL_WEBSERVICE);
                        editor.putBoolean(Valores.SHARED_PREFERENCES_SESION_INICIADA, true);

                        //Se guarda el idVendedor en las SharedPreferences.
                        try {
                            JSONObject jsonObject = decoded(response.body().getAccess_token());
                            String idVendedor = jsonObject.getString("idUsuario");
                            String imagenVendedor = jsonObject.getString("imagen");
                            String nombreVendedor = jsonObject.getString("usuario");

                            if (idVendedor.equals("")) {
                                Toast.makeText(context, "No se ha logrado obtener el idVendedor", Toast.LENGTH_LONG).show();
                                editor.putString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""); //Se coloca vacío por default.
                            } else {
                                editor.putString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, idVendedor); //Se guarda el idVendedor.
                                //La app se suscribe al tópico específico del idVendedor.
                                FirebaseMessaging.getInstance().subscribeToTopic(Url.TOPICO_USUARIO + idVendedor);
                                FirebaseMessaging.getInstance().subscribeToTopic(Url.TOPICO_ACTUALIZACIONES + idVendedor);
                                FirebaseMessaging.getInstance().subscribeToTopic(Url.TOPICO_VENDEDORES);
                            }
                            editor.putString(Valores.SHARED_PREFERENCES_IMAGEN_VENDEDOR, imagenVendedor); //Se guarda el idVendedor.
                            editor.putString(Valores.SHARED_PREFERENCES_NOMBRE_VENDEDOR, nombreVendedor); //Se guarda el idVendedor.

                        } catch (Exception e) {
                            Log.e("JWT_ERROR", e.toString());
                        }

                        //Se finaliza el guardado de las SharedPreferences.
                        editor.commit();
                        //Se inicia la actividad del Home
                        obtenerMenu(view);

                    } else if (response.code() == 400){ // Si no contesta exitoso sólo muestra un mensaje de error
                        if(response.errorBody() != null) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                visibilidad(View.GONE);
                                Snackbar.make(view, jObjError.getString("message"), Snackbar.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("ErrorBody",response.code() + "");
                            }
                        } else {
                            visibilidad(View.GONE);
                            Snackbar.make(view, getResources().getText(R.string.usuario_contrasena_invalido), Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        visibilidad(View.GONE);
                        Snackbar.make(view, getResources().getText(R.string.usuario_contrasena_invalido), Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    // Si no contesta el Web API sólo muestra un mensaje de error
                    visibilidad(View.GONE);
                    Snackbar.make(view, getResources().getText(R.string.no_connection_error), Snackbar.LENGTH_LONG).show();
                    FirebaseCrash.log("Error LogIn");
                    FirebaseCrash.report(t);
                }
            });
        }
    }

    /**
     * MÉTODO QUE DECODIFICA EL HEADER Y EL BODY DEL JSON WEB TOKEN (JWT)
     **/
    public JSONObject decoded(String JWTEncoded) throws Exception {

        try {
            String[] split = JWTEncoded.split("\\.");
            //Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            //Log.d("JWT_DECODED", "Body: " + getJson(split[1]));

            JSONObject jsonObject = new JSONObject(getJson(split[1]));

            return jsonObject;

        } catch (Exception e) {
            Log.e("JWT_ERROR", e.toString());

            return new JSONObject();
        }
    }

    /**
     * MÉTODO QUE DECODIFICA EL BASE64 DEL JWT
     **/
    public String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    private void obtenerMenu(final View view) {
        final Call<GetMenuPOJO> getMenuPOJOCall = apiInterface.getMenu(Valores.plataforma);

        getMenuPOJOCall.enqueue(new Callback<GetMenuPOJO>() {
            @Override
            public void onResponse(Call<GetMenuPOJO> call, Response<GetMenuPOJO> response) {
                if (response.body() != null) {

                    try {
                        MenuRealm.guardarListaMenu(response.body().getMenuItem());
                    } catch (Exception e) {
                        Log.e("GETMENU", e.toString());
                        FirebaseCrash.log("Error MenuDB");
                        FirebaseCrash.report(e);
                    }
                }
                obtenerUsuario(view);
            }

            @Override
            public void onFailure(Call<GetMenuPOJO> call, Throwable t) {
                visibilidad(View.GONE);
                Snackbar.make(view, getResources().getText(R.string.no_connection_error), Snackbar.LENGTH_LONG).show();

                FirebaseCrash.log("Error al obtener Menu");
                FirebaseCrash.report(t);
            }
        });
    }

    private void obtenerUsuario(final View view) {
        final Call<GetUsuarioPOJO> getUsuarioPOJOCall = apiInterface.getUsuario("true");

        getUsuarioPOJOCall.enqueue(new Callback<GetUsuarioPOJO>() {
            @Override
            public void onResponse(Call<GetUsuarioPOJO> call, Response<GetUsuarioPOJO> response) {

                if (response.body() != null) {
                    GetUsuarioPOJO getUsuarioPOJO = new GetUsuarioPOJO();
                    getUsuarioPOJO.setImagen(response.body().getImagen());
                    getUsuarioPOJO.setIdUsuario(response.body().getIdUsuario());
                    getUsuarioPOJO.setDescripcionRol(response.body().getDescripcionRol());
                    getUsuarioPOJO.setNombre(response.body().getNombre());
                    getUsuarioPOJO.setAppPaterno(response.body().getAppPaterno());
                    getUsuarioPOJO.setAppMaterno(response.body().getAppMaterno());

                    //Se guardan los datos obtenidos dentro de una SharedPreferences.
                    TinyDBInicio tinyDBInicio = new TinyDBInicio(context);
                    tinyDBInicio.putUsuarioPOJO(Valores.SHARED_PREFERENCES_GET_USUARIO, getUsuarioPOJO);
                }

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<GetUsuarioPOJO> call, Throwable t) {
                Log.d("ERROR_USUARIO", t.toString());
                //Se quita el círculo de progreso.
                visibilidad(View.GONE);
                Snackbar.make(view, getResources().getText(R.string.no_connection_error), Snackbar.LENGTH_LONG).show();

                FirebaseCrash.log("Error al obtener Usuario");
                FirebaseCrash.report(t);
            }
        });
    }

    private boolean isPasswordValid(String password) {
        // Se valida en la contraseña una longitud miníma de 8 caracteres.
        return password.length() > 8;
    }

    /**
     * MÉTODO QUE CIERRA EL TECLADO
     **/
    public void cerrarTeclado(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void visibilidad(int visibility) {
        progressBar.setVisibility(visibility);
        tvUsuario.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        tvContrasenia.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        btnIniciarSesion.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        layoutUsuario.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        layoutContrasena.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    /** MÉTODO QUE REVISA SI EXISTEN PERMISOS POR ACEPTAR, EN CASO DE QUE ASÍ SEA, LOS PIDE **/
    public void pedirPermisos(View view) {

        //Lista de permisos que necesitan ser aceptados.
        String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR};

        //Se revisa si ya se han aceptado todos los permisos.
        if (!hasPermissions(PERMISSIONS)) {
            //En caso de que no se hayan obtenido todos los permisos, se crea una lista con los
            //permisos faltantes.
            PERMISSIONS = new String[listaPermisos.size()];

            for (int i = 0; i < listaPermisos.size(); i++) {
                PERMISSIONS[i] = listaPermisos.get(i);
            }

            //Se pide al usuario que acepte los permisos requeridos.
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS,
                    CODIGO_DE_PERMISOS);
        } else { //Si ya hay permisos, entonces se intenta iniciar sesión.
            cerrarTeclado(view);
            intentarInicioSesion(view);
        }
    }

    /** MÉTODO QUE REVISA QUÉ PERMISOS FALTAN POR ACEPTAR **/
    public boolean hasPermissions(String... permissions) {

        listaPermisos.clear(); //Lista que guarda los permisos que no se han aceptado.
        boolean permisosObtenidos = true; //Variable que indica si ya se han aceptado todos los permisos.

        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                    listaPermisos.add(permission); //Guarda el permiso que no ha sido aceptado aún.
                    permisosObtenidos = false; //Indica que existe al menos un permiso no aceptado.
                }
            }
        }

        return permisosObtenidos;
    }

    /**
     * MÉTODO QUE PIDE LOS PERMISOS PARA USAR EL GPS
     **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODIGO_DE_PERMISOS) {
            if (permisosAceptados(grantResults)) { //Revisa si se aceptaron todos los permisos.
                pedirPermisos(viewButton); //Si se aceptaron todos, se va al método de permisos (sabiendo de antemano
                //que entrará al caso en que ya están aceptados todos los permisos).
            } else {
                Snackbar.make(viewButton, getString(R.string.mensaje_permisos_necesarios), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /** MÉTODO QUE VERIFICA QUE SE HAYAN ACEPTADOS TODOS LOS PERMISOS **/
    public boolean permisosAceptados(int[] grantResults) {

        for (int resultados : grantResults) {
            if (resultados != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}

