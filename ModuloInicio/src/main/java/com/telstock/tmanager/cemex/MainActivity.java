package com.telstock.tmanager.cemex;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.telstock.tmanager.cemex.adapters.NavigationAdapter;
import com.telstock.tmanager.cemex.funciones.TinyDBInicio;
import com.telstock.tmanager.cemex.model.NavigationFilas;
import com.telstock.tmanager.cemex.modulocitas.ActividadCalificarOportunidad4;
import com.telstock.tmanager.cemex.modulocitas.ActividadCerrarVenta8;
import com.telstock.tmanager.cemex.modulocitas.ActividadContactarProspectoCliente1;
import com.telstock.tmanager.cemex.modulocitas.ActividadPrepararPropuestaDeValor5;
import com.telstock.tmanager.cemex.modulocitas.ActividadPresentarPropuesta6;
import com.telstock.tmanager.cemex.modulocitas.ActividadRecabarInformacion3;
import com.telstock.tmanager.cemex.modulocitas.ActividadRecibirRespuesta7;
import com.telstock.tmanager.cemex.modulocitas.ActividadVisitarProspecto2;
import com.telstock.tmanager.cemex.modulocitas.OfertaIntegralCitasFragment;
import com.telstock.tmanager.cemex.modulocitas.PrincipalCitasFragment;
import com.telstock.tmanager.cemex.modulocitas.interfaces.ClickListenerRecyclerView;
import com.telstock.tmanager.cemex.prospectos.AltaListaContactosSingle;
import com.telstock.tmanager.cemex.prospectos.AltaProspectoFragment;
import com.telstock.tmanager.cemex.prospectos.ArchivosFragment;
import com.telstock.tmanager.cemex.prospectos.OfertaIntegralFragment;
import com.telstock.tmanager.cemex.prospectos.ProspectosFragment;
import com.telstock.tmanager.cemex.prospectos.model.JSONfiltro;
import com.telstock.tmanager.cemex.rest.ApiClient;
import com.telstock.tmanager.cemex.rest.ApiInterface;
import com.telstock.tmanager.cemex.util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cemex.tmanager.telstock.com.moduloplansemanal.CalendarFragment;
import cemex.tmanager.telstock.com.moduloplansemanal.PlanSemanalAdministrativas;
import cemex.tmanager.telstock.com.moduloplansemanal.PlanSemanalAdministrativasVenta;
import cemex.tmanager.telstock.com.moduloplansemanal.PlanSemanalFragment;
import cemex.tmanager.telstock.com.moduloplansemanal.PlanSemanalSeleccionarTipoActividad;
import cemex.tmanager.telstock.com.moduloplansemanal.TimePickerFragment;
import cemex.tmanager.telstock.com.moduloplansemanal.TimePickerFragmentFin;
import io.realm.RealmList;
import mx.com.tarjetasdelnoreste.realmdb.ActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoAccionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoActividadesPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoCampaniaRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoCargoRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoClienteRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoCompetidorRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoEstadosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoEstatusPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoMotivoExclusionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoMunicipiosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoOportunidadVentaRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoProductosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSemaforoRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoServiciosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoStatusObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSubsegmentosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoNotificacionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoProspectoRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonGlobalProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonMostrarActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.MenuRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.broadcasts.BroadcastIniciarCoordenadas;
import mx.com.tarjetasdelnoreste.realmdb.fragments.FragmentNotificacionesGeneral;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertDialogModal;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarAlertDialog;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarFragments;
import mx.com.tarjetasdelnoreste.realmdb.model.ActividadesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoAccionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCampaniaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCargoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCompetidorDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstadosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstatusPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMotivoExclusionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMunicipiosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoOportunidadVentaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSemaforoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoNotificacionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoProspectoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoSubsegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.ItemsSupport;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonFiltroVendedorAsignado;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonGlobalProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.MenuDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Actividade;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades.JsonMostrarActividades;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements ComunicarFragments, ComunicarAlertDialog {

    private Activity activity;
    private Context context;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<NavigationFilas> navigationFilas = new ArrayList<>();

    boolean fragmentTransaction = false;
    Fragment fragment = null;

    //Muestra el progreso de la carga de catálogos.
    ProgressDialog mProgressDialog;
    private static final int NUM_CATALOG = 19;

    //Interfaz para los métodos de WS
    private ApiInterface apiInterface;

    //Hardcodeo de los menús del NavigationDrawer
    public static final String INICIO = "Inicio";
    public static final String PLAN_SEMANAL = "Plan Semanal";
    public static final String CITAS_VISITAS = "Citas / Visitas";
    public static final String SINCRONIZAR_CITAS_VISITAS = "Sincronizar Citas";
    public static final String PROSPECTOS = "Prospectos";
    public static final String NOTIFICACIONES = "Notificaciones";
    public static final String ACTUALIZAR_CATALOGOS = "Sincronizar Catálogos";
    public static final String SALIR = "Salir";

    //Elementos de la cabecera.
    public String NOMBRE_USUARIO = "";
    public String IMAGEN_USUARIO = "";

    private String verificarFragment;
    //    private String idSubsegmentoBundle;
    private boolean flujoNormalFragment = true;

    TinyDBInicio tinyDBInicio;

    //Servicio de alarma para obtener las coordenadas periódicamente.
    AlarmManager alarmInicioCoordenadas;
    PendingIntent pendingInicioCoordenadas;
    AlarmManager alarmFinCoordenadas;
    PendingIntent pendingFinCoordenadas;

    //Variable que indica si todos los catálogos se descargaron exitosamente o no.
    boolean catalogosDescargadosExitosamente = true;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    //Modal que muestra mensaje de alerta.
    Dialog dialogModal;
    private static final String ID_SALIR = "idSalir";

    private List<MenuDB> listaMenu;

    //Objetos que manejan Realml.
    public static MenuRealm realmDB;

    //Objeto que muestra el ProgressDialog mientras se sincronizan las citas del día.
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.toolbar_logo_cliente);
        getSupportActionBar().setTitle("");

        activity = this;
        context = this;

        //Se inicializan las SharedPreferences
        sharedPref = getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //Se instancia Realm.
        realmDB = new MenuRealm();
        realmDB.open();

        //FUENTES
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        //Se inicia la alarma para enviar coordenadas periódicamente.
        configurarAlarmaCoordenadas();

        Intent i = getIntent();
        verificarFragment = i.getStringExtra(Valores.FRAGMENT_GENERAL_MOSTRAR);
//        idSubsegmentoBundle = i.getStringExtra(Valores.BUNDLE_ID_SUBSEGMENTO);

        //Se inicializa la interfaz
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        //Se añade el listener y se sincroniza el ícono de la hamburguesa con el DrawerLayout, esto
        //para detectar los clicks sobre el ícono y generar la animación sobre el mismo al momento
        //de abrir el NavigationDrawer.
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        listaMenu = realmDB.mostrarListaMenu();

        try {
            if (listaMenu.size() > 0) {
                for (MenuDB m : listaMenu) {
                    navigationFilas.add(new NavigationFilas(m.getNombre(),
                            getResources().getIdentifier(m.getCssIconClass(), "drawable", context.getPackageName()),
                            0));
                }
            } else {
                navigationFilas.add(new NavigationFilas(INICIO, R.drawable.list_inicio, 0));
                navigationFilas.add(new NavigationFilas(PLAN_SEMANAL, R.drawable.list_plan_semanal, 0));
                navigationFilas.add(new NavigationFilas(CITAS_VISITAS, R.drawable.list_citas_visitas, 0));
                navigationFilas.add(new NavigationFilas(SINCRONIZAR_CITAS_VISITAS, R.drawable.list_sincronizar, 0));
                navigationFilas.add(new NavigationFilas(PROSPECTOS, R.drawable.list_prospectos, 0));
                navigationFilas.add(new NavigationFilas(NOTIFICACIONES, R.drawable.list_notificaciones, 0));
                navigationFilas.add(new NavigationFilas(ACTUALIZAR_CATALOGOS, R.drawable.list_sincronizar, 0));
                navigationFilas.add(new NavigationFilas(SALIR, R.drawable.list_salir, 0));
            }
        } catch (Exception e) {
            FirebaseCrash.log("Error al mostrar Menu");
            FirebaseCrash.report(e);
        }

//        try {
//            //Se recuperan los datos del objeto GetUsuarioPOJO (obtenidos en el Login).
//            tinyDBInicio = new TinyDBInicio(context);
//            GetUsuarioPOJO getUsuarioPOJO;
//            getUsuarioPOJO = tinyDBInicio.getUsuarioPOJO(Valores.SHARED_PREFERENCES_GET_USUARIO, GetUsuarioPOJO.class);
//            NOMBRE_USUARIO = getUsuarioPOJO.getNombre() + " " + getUsuarioPOJO.getAppPaterno()
//                    + " " + getUsuarioPOJO.getAppMaterno();
//            IMAGEN_USUARIO = getUsuarioPOJO.getImagen();
//        } catch (Exception e) {
//            //Significa que no existe el objeto.
//            FirebaseCrash.log("No existe objeto");
//            FirebaseCrash.report(e);
//        }


        recyclerView.addItemDecoration(new DividerItemDecoration(context));
        adapter = new NavigationAdapter(context, //this,
                navigationFilas,
                sharedPref.getString(Valores.SHARED_PREFERENCES_NOMBRE_VENDEDOR, ""),
                sharedPref.getString(Valores.SHARED_PREFERENCES_IMAGEN_VENDEDOR, ""));
        recyclerView.setAdapter(adapter);

        //Se muestra el fragmento inicial.
        inicializarFragment();

        if (!tinyDBInicio.getBoolean(Valores.SHAREDPREFERENCES_DESCARGA_CATALOGOS)) {
            descargaCatalogos();
            //Indica que ya se han descargado catálogos por primera vez y que no necesita volverlo a hacer.
            tinyDBInicio.putBoolean(Valores.SHAREDPREFERENCES_DESCARGA_CATALOGOS, true);
        }

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListenerRecyclerView() {
            @Override
            public void onClick(View view, int position) {

                if (position != 0) {
                    switch (navigationFilas.get(position - 1).getNombreFila()) {
                        case INICIO:
                            finish();
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                            break;

                        case PLAN_SEMANAL:

                            fragment = new PlanSemanalSeleccionarTipoActividad();
                            fragmentTransaction = true;

                            break;

                        case CITAS_VISITAS:

                            fragment = new PrincipalCitasFragment();
                            fragmentTransaction = true;

                            break;

                        case SINCRONIZAR_CITAS_VISITAS:

                            sincronizarCitas();

                            break;

                        case PROSPECTOS:

                            fragment = new ProspectosFragment();
                            fragmentTransaction = true;

                            break;

                        case NOTIFICACIONES:

                            fragment = new FragmentNotificacionesGeneral();
                            fragmentTransaction = true;

                            break;

                        case ACTUALIZAR_CATALOGOS:

                            descargaCatalogos();

                            break;

                        case SALIR:
                            //Ejecuta el método de cerrar sesión
                            cerrarSesion();
                            break;

                        default:
                            Toast.makeText(context, navigationFilas.get(position - 1).getNombreFila(), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

                if (fragmentTransaction) {

                    drawerLayout.closeDrawers();

                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        /*Calendar timestampInicio = Calendar.getInstance();
        timestampInicio.set(Calendar.MONTH, 0);
        timestampInicio.set(Calendar.DAY_OF_MONTH, 4);
        timestampInicio.set(Calendar.HOUR_OF_DAY, 0);
        timestampInicio.set(Calendar.MINUTE, 0);
        timestampInicio.set(Calendar.SECOND, 0);

        Calendar timestampFin = Calendar.getInstance();
        timestampInicio.set(Calendar.MONTH, 0);
        timestampInicio.set(Calendar.DAY_OF_MONTH, 4);
        timestampFin.set(Calendar.HOUR_OF_DAY, 23);
        timestampFin.set(Calendar.MINUTE, 59);
        timestampFin.set(Calendar.SECOND, 59);

        Log.d("HORAPRUEBA_INICIO", timestampInicio.getTimeInMillis() / 1000 + "");
        Log.d("HORAPRUEBA_FIN", timestampFin.getTimeInMillis() / 1000 + "");*/
    }

    /************
     * OBTENCIÓN DE LOS DATOS DEL PROSPECTO, CONTACTOS Y ACTIVIDADES
     ***************/
    public void sincronizarCitas() {

        //Se muestra el progressDialog.
        mostrarProgressDialog();

        JSONfiltro jsonfiltro = new JSONfiltro();

        jsonfiltro.setIdVendedorAsignado(sharedPref.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));

        Call<List<Json>> getProspectosFiltro = apiInterface.getProspectoFiltro(jsonfiltro);

        getProspectosFiltro.enqueue(new Callback<List<Json>>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<List<Json>> call, Response<List<Json>> response) {

                if (response.body() != null && response.code() == 200) {

                    //Se eliminan las tablas para que se llenen nuevamente en caso de que haya
                    //existido algún cambio.
                    ProspectosRealm.eliminarTabla();
                    ActividadesRealm.eliminarTabla();

                    List<Json> jsonList = response.body();

                    //Variables relacionadas con la información de Prospectos.
                    List<ProspectosDB> prospectosDBList = new ArrayList<>();
                    ProspectosDB prospectosDB;

                    //Variables relacionadas con la información de Actividades.
                    RealmList<ActividadesDB> actividadesDBList;

                    //Variables relacionadas con el guardado del json global de cada prospecto.
                    JsonGlobalProspectosDB jsonGlobalProspectosDB;
                    List<JsonGlobalProspectosDB> jsonGlobalProspectosDBList = new ArrayList<>();
                    Gson gson = new Gson(); //Objeto que convierte el objeto en un json.

                    String idProspecto;

                    for (int i = 0; i < jsonList.size(); i++) {

                        prospectosDB = new ProspectosDB();
                        actividadesDBList = new RealmList<>();
                        jsonGlobalProspectosDB = new JsonGlobalProspectosDB();

                        try {
                            idProspecto = jsonList.get(i).getId();

                            //Se guardan los datos correspondientes al json global del prospecto.
                            jsonGlobalProspectosDB.setIdProspecto(idProspecto);
                            jsonGlobalProspectosDB.setJsonGlobalProspectos(gson.toJson(jsonList.get(i)));
                            jsonGlobalProspectosDBList.add(jsonGlobalProspectosDB);

                            prospectosDB.setId(idProspecto);

                            prospectosDB.setNumeroRegistro(jsonList.get(i).getNumeroRegistro());
                            prospectosDB.setEstaDescartado(jsonList.get(i).isEstaDescartado());
                            prospectosDB.setFotografia(jsonList.get(i).getFotografia());
                            prospectosDB.setIdEstatusObraProspecto(jsonList.get(i).getEstatusObra().getId());
                            prospectosDB.setDescripcionEstatusObraProspecto(jsonList.get(i).getEstatusObra().getDescripcion());
                            prospectosDB.setIdSubsegmento(jsonList.get(i).getSubSegmento().getId());
                            prospectosDB.setDescripcionIdSubsegmento(jsonList.get(i).getSubSegmento().getDescripcion());
                            prospectosDB.setIdTipoProspecto(jsonList.get(i).getTipoProspecto().getId());
                            prospectosDB.setDescripcionTipoProspecto(jsonList.get(i).getTipoProspecto().getDescripcion());
                            prospectosDB.setIdEstatusProspecto(jsonList.get(i).getEstatusProspecto().getId());
                            prospectosDB.setDescripcionEstatusProspecto(jsonList.get(i).getEstatusProspecto().getDescripcion());

                            //Se llama el método que guarda la información de Contactos en una lista.
                            //obtenerContactosFiltro(idProspecto, jsonList.get(i).getContactos(), contactosDBList);

                            //Se devuelve la última actividad para guardarla en el objeto de prospectos.
                            obtenerActividadesFiltro(prospectosDB, idProspecto, jsonList.get(i).getActividades(), actividadesDBList);

                            /*if (jsonList.get(i).getArchivosAlta() != null) {
                                if (jsonList.get(i).getArchivosAlta().size() > 0) {
                                    //Se guardan todas las fotos/archivos del prospecto.
                                    obtenerArchivosProspecto(idProspecto, jsonList.get(i).getArchivosAlta(), archivosAltaDBList);
                                }
                            }*/

                            prospectosDB.setObra(jsonList.get(i).getObra().getNombre());
                            prospectosDB.setCliente(jsonList.get(i).getCliente().getNombre());
                            prospectosDB.setIdCampania(jsonList.get(i).getObra().getIdCampania());
                            //prospectosDB.setDescripcionCampania(jsonList.get(i).getCliente().getCampania().getDescripcion());
                            prospectosDB.setComentarios(jsonList.get(i).getCliente().getComentarios());
                            prospectosDB.setRazonSocial(jsonList.get(i).getCliente().getRazonSocial());
                            prospectosDB.setRFC(jsonList.get(i).getCliente().getRfc());
                            prospectosDB.setTelefono(jsonList.get(i).getCliente().getTelefono());

                            //Información relacionada con la ubicación del prospecto.
                            prospectosDB.setCalle(jsonList.get(i).getObra().getDireccion().getCalle());
                            prospectosDB.setNumero(jsonList.get(i).getObra().getDireccion().getNumero());
                            prospectosDB.setColonia(jsonList.get(i).getObra().getDireccion().getColonia());
                            prospectosDB.setCodigoPostal(jsonList.get(i).getObra().getDireccion().getCodigoPostal());
                            prospectosDB.setNombrePais(jsonList.get(i).getObra().getDireccion().getPais().getNombre());
                            prospectosDB.setNombreEstado(jsonList.get(i).getObra().getDireccion().getEstado().getNombre());
                            prospectosDB.setNombreMunicipio(jsonList.get(i).getObra().getDireccion().getMunicipio().getNombre());
                            prospectosDB.setComentariosUbicacion(jsonList.get(i).getObra().getDireccion().getComentarios());
                            prospectosDB.setLatitud(jsonList.get(i).getObra().getDireccion().getUbicacion().getLatitud());
                            prospectosDB.setLongitud(jsonList.get(i).getObra().getDireccion().getUbicacion().getLongitud());
                        } catch (Exception e) {
                            Log.e("ERROR_PROSPECTOS", e.toString());
                        }

                        //Se guarda la lista de actividades que tendrá el prospecto.
                        //NOTA: No es necesario guardar la lista de actividades en Realm directamente, ya que
                        //al incluir la lista como un atributo de ProspectosDB, Realm realiza el
                        //guardado de ambos objetos automáticamente.
                        prospectosDB.setActividadesDBRealmList(actividadesDBList);

                        prospectosDBList.add(prospectosDB);
                    }

                    //Se guarda la lista de Contactos en Realm.
                    ProspectosRealm.guardarListaProspectos(prospectosDBList);

                    //Se guarda la lista de json's globales de prospectos.
                    JsonGlobalProspectosRealm.guardarJsonGlobalProspectos(jsonGlobalProspectosDBList);

                    //Se obtienen las actividades de los prospectos y se guardan en PlanSemanal.
                    obtenerActividadProspectos();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    Toast.makeText(context, getString(R.string.sincronizar_citas_fail), Toast.LENGTH_LONG).show();

                    //Se quita el progressDialog.
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Json>> call, Throwable t) {
                Log.e("GETPROSPECTOS", t.toString());
                FirebaseCrash.log("Error ProspectosDB");
                FirebaseCrash.report(t);

                Toast.makeText(context, getString(R.string.sincronizar_citas_fail), Toast.LENGTH_LONG).show();

                //Se quita el progressDialog.
                progressDialog.dismiss();
            }
        });
    }

    private void obtenerActividadesFiltro(ProspectosDB prospectosDB, String idProspecto, List<Actividade> actividadeList,
                                          List<ActividadesDB> actividadesDBList) {

        ActividadesDB actividadesDB;
        String descripcionActividad = ""; //Variable que obtiene la última actividad del prospecto.
        long idActividad = 0; //Variable que obtiene el id de la última actividad del prospecto.
        String idActividadAnterior = "";
        int estatusAgenda = 0; //Variable que obtiene el estatus de la última actividad del prospecto.

        if (actividadeList.size() > 0) {
            for (int i = 0; i < actividadeList.size(); i++) {

                actividadesDB = new ActividadesDB(); //Se inicializa nuevamente para no duplicar los registros.
                descripcionActividad = actividadeList.get(i).getTipoActividad().getDescripcion();
                idActividad = actividadeList.get(i).getTipoActividad().getId();
                estatusAgenda = actividadeList.get(i).getEstatusAgenda();
                idActividadAnterior = actividadeList.get(i).getId();

                //Llave compuesta del idProspecto y el idActividad.
                actividadesDB.setCompoundId(idProspecto + actividadeList.get(i).getId());

                actividadesDB.setIdTipoActividad(actividadeList.get(i).getTipoActividad().getId());
                actividadesDB.setDescripcionActividad(descripcionActividad);
                actividadesDB.setFechaHoraCitaInicio(actividadeList.get(i).getFechaHoraCitaInicio());
                actividadesDB.setFechaHoraCitaFin(actividadeList.get(i).getFechaHoraCitaFin());
                actividadesDB.setComentarios(actividadeList.get(i).getComentario());
                actividadesDB.setFechaInicioObra(actividadeList.get(i).getFechaInicioObra());
                actividadesDB.setConfirmarVolumenObra(actividadeList.get(i).getConfirmarVolumenObra());
                actividadesDB.setIdArchivoAdjunto(actividadeList.get(i).getIdArchivoAdjunto());
                actividadesDB.setIdProspecto(idProspecto);

                actividadesDB.setIdActividad(actividadeList.get(i).getId());

                //Se agrega el nuevo contacto a la lista de contactos.
                actividadesDBList.add(actividadesDB);
            }

            //Revisa el status de la penúltima actividad, para saber si es reagendada o no.
            if (actividadeList.size() > 1) {
                if (actividadeList.get(actividadeList.size() - 2).getEstatusAgenda() == Valores.ID_ACTIVIDAD_REAGENDADA) {
                    estatusAgenda = Valores.ID_ACTIVIDAD_REAGENDADA;
                }
            }
        }

        //Se llenan los campos de actividad de ProspectosDB.
        prospectosDB.setDescripcionActividad(descripcionActividad);
        prospectosDB.setIdActividad(idActividad);
        prospectosDB.setEstatusAgenda(estatusAgenda);
        prospectosDB.setIdActividadAnterior(idActividadAnterior);

    }

    /**
     * MÉTODO QUE LLENA LA TABLA DE PLANSEMANALDB
     **/
    public void obtenerActividadProspectos() {

        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectosConActividad();
        ArrayList<PlanSemanalDB> listaPlanSemanal = new ArrayList<>();
        PlanSemanalDB planSemanalDB;
        for (ProspectosDB p : listaProspectosDB) {
            for (int i = 0; i < p.getActividadesDBRealmList().size(); i++) {
                planSemanalDB = new PlanSemanalDB();
                planSemanalDB.setIdProspecto(p.getId());
                planSemanalDB.setObra(p.getObra());
                planSemanalDB.setCliente(p.getCliente());
                planSemanalDB.setDescripcionTipoP(p.getDescripcionTipoProspecto());
                planSemanalDB.setIdActividad((int) p.getIdActividad());
                planSemanalDB.setDescripcionObra(p.getActividadesDBRealmList().get(i).getDescripcionActividad());
                planSemanalDB.setIdActividadAnterior(p.getActividadesDBRealmList().get(i).getIdActividad());

                planSemanalDB.setCalle(p.getCalle());
                planSemanalDB.setNumero(p.getNumero());
                planSemanalDB.setColonia(p.getColonia());
                planSemanalDB.setCodigoPostal(p.getCodigoPostal());
                planSemanalDB.setIdPais(p.getNombrePais());
                planSemanalDB.setIdEstado(p.getNombreEstado());
                planSemanalDB.setIdMunicipio(p.getNombreMunicipio());
                planSemanalDB.setComentariosUbicacion(p.getComentariosUbicacion());
                planSemanalDB.setImagen(p.getFotografia());
                planSemanalDB.setEstaDescartado(p.isEstaDescartado());
                planSemanalDB.setHoraInicio(p.getActividadesDBRealmList().get(i).getFechaHoraCitaInicio());
                planSemanalDB.setHoraFin(p.getActividadesDBRealmList().get(i).getFechaHoraCitaFin());
                planSemanalDB.setId(p.getActividadesDBRealmList().get(i).getCompoundId());
                planSemanalDB.setLatitud(p.getLatitud());
                planSemanalDB.setLongitud(p.getLongitud());

                planSemanalDB.setIdEstatusProspecto(p.getIdEstatusProspecto());

                listaPlanSemanal.add(planSemanalDB);
            }
        }

        PlanSemanalRealm.guardarListaPlanSemanal(listaPlanSemanal);

        obtenerTodasActividades();
    }

    public void obtenerTodasActividades() {

        JsonFiltroVendedorAsignado jsonfiltro = new JsonFiltroVendedorAsignado();

        jsonfiltro.setIdVendedorAsignado(sharedPref.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));

        Call<List<JsonMostrarActividades>> jsonMostrarActividadesTodoCall = apiInterface.getActividadesTodo(jsonfiltro);

        jsonMostrarActividadesTodoCall.enqueue(new Callback<List<JsonMostrarActividades>>() {
            @Override
            public void onResponse(Call<List<JsonMostrarActividades>> call, Response<List<JsonMostrarActividades>> response) {
                if (response.body() != null && response.code() == 200) {

                    JsonMostrarActividadesRealm.guardarListaActividadestodo(response.body());

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                }

                //Se quita el progressDialog.
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<JsonMostrarActividades>> call, Throwable t) {
                Log.d("", "");

                //Se quita el progressDialog.
                progressDialog.dismiss();
            }
        });

    }

    /********************************************************************************************/

    public void descargaCatalogos() {

        //Se muestra la barra de progreso de la descarga de catálogos.
        createProgressDialog();
        descargaEstados();
    }

    /**
     * MÉTODO QUE MUESTRA EL FRAGMENTO INICIAL
     **/
    public void inicializarFragment() {

        if (verificarFragment != null) {
            if (verificarFragment.equals(Valores.FRAGMENT_PROSPECTOS_PRINCIPAL)) {
                fragment = new ProspectosFragment();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PROSPECTOS)) {
                fragment = new AltaProspectoFragment();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PROSPECTOS_CONTACTOS)) {
                Intent i = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString(Valores.CONTACTOS_ID_PROSPECTO, i.getStringExtra(Valores.CONTACTOS_ID_PROSPECTO));
                bundle.putString(Valores.CONTACTOS_NOMBRE_PROSPECTO, i.getStringExtra(Valores.CONTACTOS_NOMBRE_PROSPECTO));

                fragment = new AltaListaContactosSingle();
                fragment.setArguments(bundle);
            } else if (verificarFragment.equals(Valores.FRAGMENT_PROSPECTOS_ARCHIVOS)) {

                fragment = new ArchivosFragment();
            } else if (verificarFragment.equals(Valores.FRAGMENT_MAPA)) {
                //fragment = new MapsFragment();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PLAN_VENTAS)) {
                fragment = new PlanSemanalFragment();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PLAN_ADMINISTRATIVAS_VENTAS)) {
                fragment = new PlanSemanalAdministrativasVenta();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PLAN_ADMINISTRATIVAS)) {
                fragment = new PlanSemanalAdministrativas();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PLAN_CALENDAR)) {
                Intent i = getIntent();
                Bundle bundle = new Bundle();

                //Revisa si se quiere ir directamente al calendario (sólo para visualización) o
                //si se desea dar de alta una actividad.
                if (i.getStringExtra(Valores.BUNDLE_IR_A_CALENDARIO) != null) {
                    bundle.putString(Valores.BUNDLE_IR_A_CALENDARIO,
                            i.getStringExtra(Valores.BUNDLE_IR_A_CALENDARIO));
                } else {
                    bundle.putString(Valores.BUNDLE_CALENDAR,
                            i.getStringExtra(Valores.BUNDLE_ACTIVIDADES_ADMINISTRATIVAS));
                }

                fragment = new CalendarFragment();
                fragment.setArguments(bundle);
            } else if (verificarFragment.equals(Valores.FRAGMENT_PLAN_TIMEPICKER_INICIO)) {
                Intent i = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString(Valores.BUNDLE_CALENDAR,
                        i.getStringExtra(Valores.BUNDLE_CALENDAR));
                fragment = new TimePickerFragment();
                fragment.setArguments(bundle);
            } else if (verificarFragment.equals(Valores.FRAGMENT_PLAN_TIMEPICKER_FIN)) {
                Intent i = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString(Valores.BUNDLE_CALENDAR,
                        i.getStringExtra(Valores.BUNDLE_CALENDAR));
                fragment = new TimePickerFragmentFin();
                fragment.setArguments(bundle);
            } else if (verificarFragment.equals(Valores.FRAGMENT_PROSPECTOS_OFERTA_INTEGRAL)) {
                Intent i = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString(Valores.BUNDLE_ID_SUBSEGMENTO, i.getStringExtra(Valores.BUNDLE_ID_SUBSEGMENTO));
                fragment = new OfertaIntegralFragment();
                fragment.setArguments(bundle);
            } else if (verificarFragment.equals(Valores.FRAGMENT_CONTACTAR_CLIENTE_PROSPECTO)) {
                Intent i = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString(Valores.BUNDLE_ID_ACTIVIDAD, i.getStringExtra(Valores.BUNDLE_ID_ACTIVIDAD));
                fragment = new ActividadContactarProspectoCliente1();
                fragment.setArguments(bundle);
            } else if (verificarFragment.equals(Valores.FRAGMENT_VISITAR_PROSPECTO)) {
                fragment = new ActividadVisitarProspecto2();
            } else if (verificarFragment.equals(Valores.FRAGMENT_RECABAR_INFORMACION)) {
                fragment = new ActividadRecabarInformacion3();
            } else if (verificarFragment.equals(Valores.FRAGMENT_CALIFICAR_OPORTUNIDAD)) {
                fragment = new ActividadCalificarOportunidad4();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PREPARAR_PROPUESTA_DE_VALOR)) {
                fragment = new ActividadPrepararPropuestaDeValor5();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PRESENTAR_PROPUESTA)) {
                fragment = new ActividadPresentarPropuesta6();
            } else if (verificarFragment.equals(Valores.FRAGMENT_RECIBIR_RESPUESTA)) {
                fragment = new ActividadRecibirRespuesta7();
            } else if (verificarFragment.equals(Valores.FRAGMENT_CERRAR_VENTA)) {
                fragment = new ActividadCerrarVenta8();
            } else if (verificarFragment.equals(Valores.FRAGMENT_PRODUCTOS_SERVICIOS)) {
                fragment = new OfertaIntegralCitasFragment();
            } else if (verificarFragment.equals(Valores.FRAGMENT_CITAS_VISITAS)) {
                fragment = new PrincipalCitasFragment();
            }
        } else {
            fragment = new HomeFragment();
        }

        fragmentTransaction = true;

        if (fragmentTransaction) {

            drawerLayout.closeDrawers();

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }

    /*@Override
    public void onClick(View view, NavigationFilas navigationFilas) {
        switch (navigationFilas.getNombreFila()) {
            case INICIO:
                finish();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                break;

            case PLAN_SEMANAL:

                fragment = new PlanSemanalFragment();
                fragmentTransaction = true;

                //((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                  //      .replace(R.id.content_frame, new PlanSemanalFragment())
                    //    .commit();
                break;

            case CITAS_VISITAS:

                fragment = new PrincipalCitasFragment();
                fragmentTransaction = true;

                break;

            case PROSPECTOS:

                fragment = new ProspectosFragment();
                fragmentTransaction = true;

                break;

            case SALIR:
                //Ejecuta el método de cerrar sesión
                cerrarSesion();
                break;

            default:
                Toast.makeText(context, navigationFilas.getNombreFila(), Toast.LENGTH_SHORT).show();
                break;
        }

        if (fragmentTransaction) {

            drawerLayout.closeDrawers();

            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }*/

    private void cerrarSesion() {

        /*if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);

            tinyDBInicio.putBoolean(Valores.SHAREDPREFERENCES_ALARMA_COORDENADAS, false);

            finish();
        }*/

        //Desuscribimos la app del tópico para que no lleguen notificaciones al cerrar
        //la sesión.
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Url.TOPICO_USUARIO
                + sharedPref.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Url.TOPICO_ACTUALIZACIONES
                + sharedPref.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
        FirebaseMessaging.getInstance().unsubscribeFromTopic(Url.TOPICO_VENDEDORES);

        tinyDBInicio.putBoolean(Valores.SHAREDPREFERENCES_DESCARGA_CATALOGOS, false);

        //Indica que se cerrará sesión, para que al abrir la pantalla de Login, no se haga el
        //redireccionamiento.
        editor.putBoolean(Valores.SHARED_PREFERENCES_SESION_INICIADA, false);
        editor.commit();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);


//        final Call<TokenPOJO> tokenPOJOCall = apiInterface.cerrarSesion(prefs.getString("token", null));
//
//        tokenPOJOCall.enqueue(new Callback<TokenPOJO>() {
//            @Override
//            public void onResponse(Call<TokenPOJO> call, Response<TokenPOJO> response) {
//                //Termina la actividad
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<TokenPOJO> call, Throwable t) {
//                //Muestra en el log y termina la actividad
//                FirebaseCrash.log("Error Cerrar Sesión");
//                FirebaseCrash.report(t);
//                finish();
//            }
//        });
    }

    public void descargaEstados() {

        final Call<GetCatalogoPOJO> getEstadosCall = apiInterface.getCatalogoEstado();

        getEstadosCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {
                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoEstadosDB catalogoEstadosDB;
                    ArrayList<CatalogoEstadosDB> listaEstadosDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoEstadosDB = new CatalogoEstadosDB();

                        catalogoEstadosDB.setId(items.getId());
                        catalogoEstadosDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoEstadosDB.setDescripcion(items.getDescripcion());
                        catalogoEstadosDB.setIdPadre(items.getIdPadre());
                        listaEstadosDB.add(catalogoEstadosDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoEstadosRealm.guardarListaEstados(listaEstadosDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaCatalogoMunicipio();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaCatalogoMunicipio();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Error Descarga Estados", t.toString());
                FirebaseCrash.log("Error Descarga Estados");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaCatalogoMunicipio();
            }
        });

    }

    public void descargaCatalogoMunicipio() {

        final Call<GetCatalogoPOJO> getMunicipioCall = apiInterface.getCatalogoMunicipio();

        getMunicipioCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoMunicipiosDB catalogoMunicipiosDB;
                    ArrayList<CatalogoMunicipiosDB> listaMunicipiosDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoMunicipiosDB = new CatalogoMunicipiosDB();

                        catalogoMunicipiosDB.setId(items.getId());
                        catalogoMunicipiosDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoMunicipiosDB.setDescripcion(items.getDescripcion());
                        catalogoMunicipiosDB.setIdPadre(items.getIdPadre());
                        listaMunicipiosDB.add(catalogoMunicipiosDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoMunicipiosRealm.guardarListaMunicipios(listaMunicipiosDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaEstatusObra();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaEstatusObra();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Municipios", t.toString());
                FirebaseCrash.log("Descarga Municipios");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaEstatusObra();
            }
        });
    }

    public void descargaEstatusObra() {

        final Call<GetCatalogoPOJO> getEstatusObraPOJOCall = apiInterface.getCatalogoEstatusObra();

        getEstatusObraPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoStatusObraDB catalogoStatusObraDB;
                    ArrayList<CatalogoStatusObraDB> listaStatusObraDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoStatusObraDB = new CatalogoStatusObraDB();

                        catalogoStatusObraDB.setId(items.getId());
                        catalogoStatusObraDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoStatusObraDB.setDescripcion(items.getDescripcion());
                        catalogoStatusObraDB.setIdPadre(items.getIdPadre());
                        listaStatusObraDB.add(catalogoStatusObraDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoStatusObraRealm.guardarListaStatusObra(listaStatusObraDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargarSubsegmentoProducto();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargarSubsegmentoProducto();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Estatus Obra", t.toString());
                FirebaseCrash.log("Descarga Estatus Obra");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargarSubsegmentoProducto();
            }
        });
    }

    public void descargarSubsegmentoProducto() {

        final Call<List<GetCatalogoSubsegmento>> getSubsegmentoPOJOCall = apiInterface.getCatalogoSubsegmentoProducto();

        getSubsegmentoPOJOCall.enqueue(new Callback<List<GetCatalogoSubsegmento>>() {
            @Override
            public void onResponse(Call<List<GetCatalogoSubsegmento>> call, Response<List<GetCatalogoSubsegmento>> response) {
                if (response.body() != null && response.code() == 200 && response.body().size() > 0) {

                    List<GetCatalogoSubsegmento> subsegmentos = response.body();
                    CatalogoSubsegmentosDB catalogoSubsegmentosDB;
                    ArrayList<CatalogoSubsegmentosDB> listaSubsegmentoDB = new ArrayList<>();
//
                    for (GetCatalogoSubsegmento items : subsegmentos) { //Se guardan los objetos individualmente.
                        catalogoSubsegmentosDB = new CatalogoSubsegmentosDB();

                        catalogoSubsegmentosDB.setId(items.getId());
                        catalogoSubsegmentosDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoSubsegmentosDB.setDescripcion(items.getDescripcion());
                        catalogoSubsegmentosDB.setIdPadre(items.getIdPadre());

                        CatalogoProductosRealm.guardarListaProductos(items.getProductos());
                        listaSubsegmentoDB.add(catalogoSubsegmentosDB);
                    }
//
//                    //Se guarda en la base de Realm.
                    CatalogoSubsegmentosRealm.guardarListaSubsegmentos(listaSubsegmentoDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargarTipoProspecto();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargarTipoProspecto();
                }
            }

            @Override
            public void onFailure(Call<List<GetCatalogoSubsegmento>> call, Throwable t) {
                Log.d("Descarga Subsegmento", t.toString());
                FirebaseCrash.log("Descarga Subsegmento");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargarTipoProspecto();
            }
        });
    }

    public void descargarTipoProspecto() {

        final Call<GetCatalogoPOJO> getTipoProspectoPOJOCall = apiInterface.getCatalogoTipoProspecto();

        getTipoProspectoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoTipoProspectoDB catalogoTipoProspectoDB;
                    ArrayList<CatalogoTipoProspectoDB> listaTipoProspectoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoTipoProspectoDB = new CatalogoTipoProspectoDB();

                        catalogoTipoProspectoDB.setId(items.getId());
                        catalogoTipoProspectoDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoTipoProspectoDB.setDescripcion(items.getDescripcion());
                        catalogoTipoProspectoDB.setIdPadre(items.getIdPadre());
                        listaTipoProspectoDB.add(catalogoTipoProspectoDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoTipoProspectoRealm.guardarListaTipoProspecto(listaTipoProspectoDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargarCampania();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargarCampania();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Tipo Prospecto", t.toString());
                FirebaseCrash.log("Descarga Tipo Prospecto");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargarCampania();
            }
        });
    }

    public void descargarCampania() {

        final Call<GetCatalogoPOJO> getTipoProspectoPOJOCall = apiInterface.getCampania();

        getTipoProspectoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoCampaniaDB catalogoCampaniaDB;
                    ArrayList<CatalogoCampaniaDB> listaTipoProspectoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoCampaniaDB = new CatalogoCampaniaDB();

                        catalogoCampaniaDB.setId(items.getId());
                        catalogoCampaniaDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoCampaniaDB.setDescripcion(items.getDescripcion());
                        catalogoCampaniaDB.setIdPadre(items.getIdPadre());
                        listaTipoProspectoDB.add(catalogoCampaniaDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoCampaniaRealm.guardarListaCampania(listaTipoProspectoDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargarServicios();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargarServicios();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Tipo Prospecto", t.toString());
                FirebaseCrash.log("Descarga Tipo Prospecto");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargarServicios();
            }
        });
    }


    public void descargarServicios() {
        final Call<GetCatalogoPOJO> getServicioCall = apiInterface.getServicio();

        getServicioCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoServiciosDB catalogoServicioDB;
                    ArrayList<CatalogoServiciosDB> listaServiciosDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoServicioDB = new CatalogoServiciosDB();

                        catalogoServicioDB.setId(items.getId());
                        catalogoServicioDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoServicioDB.setDescripcion(items.getDescripcion());
                        catalogoServicioDB.setIdPadre(items.getIdPadre());
                        listaServiciosDB.add(catalogoServicioDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoServiciosRealm.guardarListaServicios(listaServiciosDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargarActividadesPGV();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargarActividadesPGV();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Error Servicio", t.toString());
                FirebaseCrash.log("Descarga Servicio");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargarActividadesPGV();
            }
        });
    }

    public void descargarActividadesPGV() {
        final Call<GetCatalogoPOJO> getActividadesPGVCall = apiInterface.getCatalogoActividadesPGV();

        getActividadesPGVCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoActividadesPGVDB catalogoActividadPGVDB;
                    ArrayList<CatalogoActividadesPGVDB> listaActividadesPGVDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoActividadPGVDB = new CatalogoActividadesPGVDB();

                        catalogoActividadPGVDB.setId(items.getId());
                        catalogoActividadPGVDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoActividadPGVDB.setDescripcion(items.getDescripcion());
                        catalogoActividadPGVDB.setIdPadre(items.getIdPadre());
                        listaActividadesPGVDB.add(catalogoActividadPGVDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoActividadesPGVRealm.guardarListaActividadesPGV(listaActividadesPGVDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaSemaforo();


                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaSemaforo();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Error Servicio", t.toString());
                FirebaseCrash.log("Descarga Servicio");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaSemaforo();
            }
        });
    }

    public void descargaSemaforo() {

        final Call<GetCatalogoPOJO> getSemaforoPOJOCall = apiInterface.getCatalogoSemaforo();

        getSemaforoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoSemaforoDB catalogoSemaforoDB;
                    ArrayList<CatalogoSemaforoDB> listaSemaforoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoSemaforoDB = new CatalogoSemaforoDB();

                        catalogoSemaforoDB.setId(items.getId());
                        catalogoSemaforoDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoSemaforoDB.setDescripcion(items.getDescripcion());
                        catalogoSemaforoDB.setIdPadre(items.getIdPadre());
                        listaSemaforoDB.add(catalogoSemaforoDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoSemaforoRealm.guardarListaSemaforo(listaSemaforoDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaMotivoExclusion();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaMotivoExclusion();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Semaforo", t.toString());
                FirebaseCrash.log("Descarga Semaforo");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaMotivoExclusion();
            }
        });
    }

    public void descargaMotivoExclusion() {

        final Call<GetCatalogoPOJO> getMotivoExclusionPOJOCall = apiInterface.getCatalogoMotivosExclusion();

        getMotivoExclusionPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoMotivoExclusionDB catalogoMotivoExclusionDB;
                    ArrayList<CatalogoMotivoExclusionDB> listaMotivoExclusionDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoMotivoExclusionDB = new CatalogoMotivoExclusionDB();

                        catalogoMotivoExclusionDB.setId(items.getId());
                        catalogoMotivoExclusionDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoMotivoExclusionDB.setDescripcion(items.getDescripcion());
                        catalogoMotivoExclusionDB.setIdPadre(items.getIdPadre());
                        listaMotivoExclusionDB.add(catalogoMotivoExclusionDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoMotivoExclusionRealm.guardarListaMotivoExclusion(listaMotivoExclusionDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaCompetidor();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaCompetidor();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Motivo Exclusion", t.toString());
                FirebaseCrash.log("Descarga MotivoExclusion");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaCompetidor();
            }
        });
    }

    public void descargaCompetidor() {

        final Call<GetCatalogoPOJO> getCompetidorPOJOCall = apiInterface.getCatalogoCompetidor();

        getCompetidorPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoCompetidorDB catalogoCompetidorDB;
                    ArrayList<CatalogoCompetidorDB> listaCompetidorDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoCompetidorDB = new CatalogoCompetidorDB();

                        catalogoCompetidorDB.setId(items.getId());
                        catalogoCompetidorDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoCompetidorDB.setDescripcion(items.getDescripcion());
                        catalogoCompetidorDB.setIdPadre(items.getIdPadre());
                        listaCompetidorDB.add(catalogoCompetidorDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoCompetidorRealm.guardarListaCompetidor(listaCompetidorDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaOportunidadVenta();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaOportunidadVenta();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Competidor", t.toString());
                FirebaseCrash.log("Descarga Competidor");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaOportunidadVenta();
            }
        });
    }

    public void descargaOportunidadVenta() {

        final Call<GetCatalogoPOJO> getOportunidadVentaPOJOCall = apiInterface.getCatalogoOportunidadVenta();

        getOportunidadVentaPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoOportunidadVentaDB catalogoOportunidadVentaDB;
                    ArrayList<CatalogoOportunidadVentaDB> listaOportunidadVentaDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoOportunidadVentaDB = new CatalogoOportunidadVentaDB();

                        catalogoOportunidadVentaDB.setId(items.getId());
                        catalogoOportunidadVentaDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoOportunidadVentaDB.setDescripcion(items.getDescripcion());
                        catalogoOportunidadVentaDB.setIdPadre(items.getIdPadre());
                        listaOportunidadVentaDB.add(catalogoOportunidadVentaDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoOportunidadVentaRealm.guardarListaOportunidadVenta(listaOportunidadVentaDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaEstatusPGV();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaEstatusPGV();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Oportunidad Venta", t.toString());
                FirebaseCrash.log("Descarga Oportunidad Venta");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaEstatusPGV();
            }
        });
    }

    public void descargaEstatusPGV() {

        final Call<GetCatalogoPOJO> getEstatusPGVPOJOCall = apiInterface.getCatalogoEstatusPGV();

        getEstatusPGVPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoEstatusPGVDB catalogoEstatusPGVDB;
                    ArrayList<CatalogoEstatusPGVDB> listaEstatusPGVDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoEstatusPGVDB = new CatalogoEstatusPGVDB();

                        catalogoEstatusPGVDB.setId(items.getId());
                        catalogoEstatusPGVDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoEstatusPGVDB.setDescripcion(items.getDescripcion());
                        catalogoEstatusPGVDB.setIdPadre(items.getIdPadre());
                        listaEstatusPGVDB.add(catalogoEstatusPGVDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoEstatusPGVRealm.guardarListaEstatusPGV(listaEstatusPGVDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaTipoObra();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaTipoObra();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Estatus PGV", t.toString());
                FirebaseCrash.log("Descarga Estatus PGV");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaTipoObra();
            }
        });
    }

    public void descargaTipoObra() {

        final Call<GetCatalogoPOJO> getTipoObraPOJOCall = apiInterface.getCatalogoTipoObra();

        getTipoObraPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoTipoObraDB catalogoTipoObraDB;
                    ArrayList<CatalogoTipoObraDB> listaTipoObraDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoTipoObraDB = new CatalogoTipoObraDB();

                        catalogoTipoObraDB.setId(items.getId());
                        catalogoTipoObraDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoTipoObraDB.setDescripcion(items.getDescripcion());
                        catalogoTipoObraDB.setIdPadre(items.getIdPadre());
                        listaTipoObraDB.add(catalogoTipoObraDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoTipoObraRealm.guardarListaTipoObra(listaTipoObraDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaCargo();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaCargo();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Tipo Obra", t.toString());
                FirebaseCrash.log("Descarga Tipo Obra");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaCargo();
            }
        });
    }

    public void descargaCargo() {

        final Call<GetCatalogoPOJO> getCargoPOJOCall = apiInterface.getCatalogoCargo();

        getCargoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoCargoDB catalogoCargoDB;
                    ArrayList<CatalogoCargoDB> listaCargoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoCargoDB = new CatalogoCargoDB();

                        catalogoCargoDB.setId(items.getId());
                        catalogoCargoDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoCargoDB.setDescripcion(items.getDescripcion());
                        catalogoCargoDB.setIdPadre(items.getIdPadre());
                        listaCargoDB.add(catalogoCargoDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoCargoRealm.guardarListaCargo(listaCargoDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaTipoNotificacion();


                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaTipoNotificacion();

                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Tipo Obra", t.toString());
                FirebaseCrash.log("Descarga Tipo Obra");
                FirebaseCrash.report(t);

                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                descargaTipoNotificacion();
            }
        });
    }

    public void descargaTipoNotificacion() {
        final Call<GetCatalogoPOJO> getTipoNotificacionPOJOCall = apiInterface.getCatalogoTipoNotificacion();

        getTipoNotificacionPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoTipoNotificacionDB catalogoTipoNotificacionDB;
                    ArrayList<CatalogoTipoNotificacionDB> listaTipoNotificacionDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoTipoNotificacionDB = new CatalogoTipoNotificacionDB();

                        catalogoTipoNotificacionDB.setId(items.getId());
                        catalogoTipoNotificacionDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoTipoNotificacionDB.setDescripcion(items.getDescripcion());
                        catalogoTipoNotificacionDB.setIdPadre(items.getIdPadre());
                        listaTipoNotificacionDB.add(catalogoTipoNotificacionDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoTipoNotificacionRealm.guardarListaTipoNotificacion(listaTipoNotificacionDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaAccion();


                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaAccion();

                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Tipo Notificacion", t.toString());
                FirebaseCrash.log("Descarga Tipo Notificacion");
                FirebaseCrash.report(t);

                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaAccion();

            }
        });
    }

    public void descargaAccion() {

        final Call<GetCatalogoPOJO> getAccionCall = apiInterface.getCatalogoAccion();

        getAccionCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {
                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoAccionDB catalogoAccionDB;
                    ArrayList<CatalogoAccionDB> listaAccionDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoAccionDB = new CatalogoAccionDB();

                        catalogoAccionDB.setId(items.getId());
                        catalogoAccionDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoAccionDB.setDescripcion(items.getDescripcion());
                        catalogoAccionDB.setIdPadre(items.getIdPadre());
                        listaAccionDB.add(catalogoAccionDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoAccionRealm.guardarListaAccion(listaAccionDB);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaCliente();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    descargaCliente();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Error Descarga Accion", t.toString());
                FirebaseCrash.log("Error Descarga Accion");
                FirebaseCrash.report(t);
                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                descargaCliente();
            }
        });

    }

    public void descargaCliente() {

        final Call<List<ClienteDB>> getJsonClienteCall = apiInterface.getCatalogoCliente();

        getJsonClienteCall.enqueue(new Callback<List<ClienteDB>>() {
            @Override
            public void onResponse(Call<List<ClienteDB>> call, Response<List<ClienteDB>> response) {
                if (response.body() != null && response.code() == 200 && response.body().size() > 0) {

                    List<ClienteDB> catalogo = response.body();

                    CatalogoClienteRealm.guardarListaCliente(catalogo);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaObra();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                    descargaObra();

                }
            }

            @Override
            public void onFailure(Call<List<ClienteDB>> call, Throwable t) {

                Log.d("Cliente", t.toString());
                FirebaseCrash.log("Descarga Cliente");
                FirebaseCrash.report(t);

                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);


                descargaObra();
            }
        });
    }

    public void descargaObra() {
        final Call<List<ObraDB>> getJsonObraCall = apiInterface.getCatalogoObra();

        getJsonObraCall.enqueue(new Callback<List<ObraDB>>() {
            @Override
            public void onResponse(Call<List<ObraDB>> call, Response<List<ObraDB>> response) {
                if (response.body() != null && response.code() == 200 && response.body().size() > 0) {

                    List<ObraDB> catalogo = response.body();
                    CatalogoObraRealm.guardarListaObra(catalogo);

                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();

                            if (!catalogosDescargadosExitosamente) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                // Add the buttons
                                builder.setMessage("Ocurrió un problema al descargar los catálogos ¿Quieres volver a descargarlos?");
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        descargaCatalogos();
                                    }
                                });
                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                                builder.create();

//                                Toast.makeText(context, getString(R.string.catalogos_fail),
//                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }, 1000);

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {

                    //Indica que existe al menos un catálogo que no se descargó correctamente.
                    catalogosDescargadosExitosamente = false;
                    mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressDialog.dismiss();

                            if (!catalogosDescargadosExitosamente) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                // Add the buttons
                                builder.setMessage("Ocurrió un problema al descargar los catálogos ¿Quieres volver a descargarlos?");
                                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        descargaCatalogos();
                                    }
                                });
                                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // User cancelled the dialog
                                    }
                                });
                                builder.create();

//                                Toast.makeText(context, getString(R.string.catalogos_fail),
//                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    }, 1000);
                }
            }

            @Override
            public void onFailure(Call<List<ObraDB>> call, Throwable t) {
                Log.d("Obra", t.toString());
                FirebaseCrash.log("Descarga Obra");
                FirebaseCrash.report(t);

                //Indica que existe al menos un catálogo que no se descargó correctamente.
                catalogosDescargadosExitosamente = false;
                mProgressDialog.incrementProgressBy(mProgressDialog.getMax() / NUM_CATALOG);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();

                        if (!catalogosDescargadosExitosamente) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            // Add the buttons
                            builder.setMessage("Ocurrió un problema al descargar los catálogos ¿Quieres volver a descargarlos?");
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    descargaCatalogos();
                                }
                            });
                            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                }
                            });
                            builder.create().show();

//                            Toast.makeText(context, getString(R.string.catalogos_fail),
//                                    Toast.LENGTH_LONG).show();
                        }

                    }
                }, 1000);
            }
        });

    }


    /**
     * MÉTODO QUE INICIA LA ALARMA DE COORDENADAS
     **/
    public void configurarAlarmaCoordenadas() {

        //Se configura una alarma que se active a las 8:00
        // y otra que se active a las 18:00.
        Calendar calendarInicio = Calendar.getInstance();
        calendarInicio.setTimeInMillis(System.currentTimeMillis());
        calendarInicio.set(Calendar.HOUR_OF_DAY, Valores.VALOR_ALARMA_HORA_INICIO);
        calendarInicio.set(Calendar.MINUTE, 0);

        Calendar calendarFin = Calendar.getInstance();
        calendarFin.setTimeInMillis(System.currentTimeMillis());
        calendarFin.set(Calendar.HOUR_OF_DAY, Valores.VALOR_ALARMA_HORA_FIN);
        calendarFin.set(Calendar.MINUTE, 0);

        //Se configura la alarma que se ejecutará a las 8:00
        alarmInicioCoordenadas = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarmInicioCoordenadas = new Intent(context, BroadcastIniciarCoordenadas.class);
        intentAlarmInicioCoordenadas.putExtra(Valores.ALARMA_ENVIO_DE_COORDENADAS, Valores.VALOR_ALARMA_INICIO_COORDENADAS);
        pendingInicioCoordenadas = PendingIntent.getBroadcast(context, 0, intentAlarmInicioCoordenadas, 1);

        //Se configura la alarma que se ejecutará a las 18:00
        alarmFinCoordenadas = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarmFinCoordenadas = new Intent(context, BroadcastIniciarCoordenadas.class);
        intentAlarmFinCoordenadas.putExtra(Valores.ALARMA_ENVIO_DE_COORDENADAS, Valores.VALOR_ALARMA_FIN_COORDENADAS);
        pendingFinCoordenadas = PendingIntent.getBroadcast(context, 0, intentAlarmFinCoordenadas, 2);

        try {
            tinyDBInicio = new TinyDBInicio(context);
            //Activa la alarma, únicamente si no se ha hecho ya antes.
            if (!tinyDBInicio.getBoolean(Valores.SHAREDPREFERENCES_ALARMA_COORDENADAS)) {

                Log.d("FUNCION_COORDENADA", "CONFIGURACION_ALARMAS");

                //Indica que ya se ha iniciado la alarma de Coordenadas.
                tinyDBInicio.putBoolean(Valores.SHAREDPREFERENCES_ALARMA_COORDENADAS, true);

                //Se activan ambas alarmas.
                alarmInicioCoordenadas.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendarInicio.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,
                        pendingInicioCoordenadas);

                alarmFinCoordenadas.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendarFin.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,
                        pendingFinCoordenadas);

            }
        } catch (Exception e) {
            Log.e("ALARMA_COORDENADAS", "Ha ocurrido un error al iniciar la alarma de envío de coordenadas");
        }
    }

    /**
     * MÉTODO QUE CREA EL PROGRESSDIALOG QUE MUESTRA EL PROGRESO DE LA DESCARGA DE CATÁLOGOS
     **/
    public void createProgressDialog() {
        mProgressDialog = Funciones.customProgressDialogConMensaje(context, getString(R.string.catalogos_mensaje_descarga));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(NUM_CATALOG * 10);
        mProgressDialog.show();
    }

    //MÉTODO DE MANEJADOR CON GESTUREDETECTOR.
    //Clase que permite detectar los eventos del RecyclerView, ayudándose del
    //objeto GestureDetector.
    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        //Nos ayudamos de GestureDetector sólo en caso de que deseemos obtener
        //el evento onLongClick / onLongPress del RecyclerView.
        private GestureDetector gestureDetector;
        private ClickListenerRecyclerView clickListenerRecyclerView;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListenerRecyclerView clickListenerRecyclerView) {
            this.clickListenerRecyclerView = clickListenerRecyclerView;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                //Debe devolver true para que se pueda detectar el evento onClick.
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                //Método que detecta el evento onLongClick / onLongPress.
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListenerRecyclerView != null) {
                        clickListenerRecyclerView.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        //Detecta el onClick del RecyclerView.
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListenerRecyclerView != null && gestureDetector.onTouchEvent(e)) {
                clickListenerRecyclerView.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void mostrarProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(getString(R.string.sincronizar_citas_title));
        progressDialog.setMessage(getString(R.string.sincronizar_citas_message));
        progressDialog.show();
    }

    //Método que permite que la fuente de "Calligraphy" envuelva a la vista.
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //Manejo de la navegación entre fragments.
    @Override
    public void onBackPressed() {

        if (flujoNormalFragment) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.content_frame);
            if (f instanceof ProspectosFragment || f instanceof PlanSemanalSeleccionarTipoActividad || f instanceof PrincipalCitasFragment
                    || f instanceof FragmentNotificacionesGeneral) {
                //Se manda llamar esta misma clase, esto con el fin de dar un buen efecto
                //al momento de volver a la pantalla de Home.
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (f instanceof HomeFragment) {

                dialogModal = AlertDialogModal.showModalTwoButtonsNoTitle(context, this,
                        "¿Estás seguro que deseas cerrar sesión?",
                        getString(com.telstock.tmanager.cemex.prospectos.R.string.btn_ok), getString(com.telstock.tmanager.cemex.prospectos.R.string.btn_cancelar), ID_SALIR);

            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            Log.d("NavigationDrawer", e.toString());
        }
    }

    //Ejecuta el método onBackPressed() desde los fragments.
    @Override
    public void onBackPressedFragment(boolean flujoNormal) {
        flujoNormalFragment = flujoNormal;
        onBackPressed();
    }

    @Override
    public void alertDialogPositive(String idDialog) {

        switch (idDialog) {
            case ID_SALIR:

                cerrarSesion();

                break;
        }
    }

    @Override
    public void alertDialogNegative(String idDialog) {

    }

    @Override
    public void alertDialogNeutral(String idDialog) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Se cierran todas las instancias de Realm.
        try {
            if (!realmDB.isClosed())
                realmDB.close();
        } catch (Exception e) {
            FirebaseCrash.report(e);
        }
    }


}
