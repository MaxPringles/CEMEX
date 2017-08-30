package cemex.tmanager.telstock.com.moduloplansemanal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cemex.tmanager.telstock.com.moduloplansemanal.adapters.AdapterPlanSemanalFiltros;
import cemex.tmanager.telstock.com.moduloplansemanal.funciones.TinyDB;
import cemex.tmanager.telstock.com.moduloplansemanal.interfaces.OnClickProspectos;
import cemex.tmanager.telstock.com.moduloplansemanal.model.JSONfiltro;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiClient;
import cemex.tmanager.telstock.com.moduloplansemanal.rest.ApiInterface;
import cemex.tmanager.telstock.com.moduloplansemanal.util.DividerItemDecoration;
import io.realm.RealmList;
import mx.com.tarjetasdelnoreste.realmdb.ActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoActividadesPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoProductosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSubsegmentosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoProspectoRealm;
import mx.com.tarjetasdelnoreste.realmdb.ContactosRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonGlobalProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlarmasActividades;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.ActividadesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoProspectoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ContactosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoSubsegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.ItemsSupport;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonGlobalProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Actividade;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Actividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.ActividadAnterior;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OfertaIntegral;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.SubsegmentoProducto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.TipoActividad;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanSemanalFragment extends Fragment implements OnClickProspectos {

    Context context;

    ArrayAdapter<String> adapter;
    ArrayList<CatalogoTipoProspectoDB> arrayResponseTipoProspecto;
    ArrayList<CatalogoSubsegmentosDB> arrayResponseSubsegmento;
    ArrayList<CatalogoActividadesPGVDB> arrayResponseTipoActividad;

    ArrayList<String> arrayCatalogoTipoProspecto = new ArrayList<>();
    ArrayList<String> arrayCatalogoSubsegmento = new ArrayList<>();
    ArrayList<String> arrayCatalogoTipoActividad = new ArrayList<>();

    List<CatalogoTipoProspectoDB> catalogoTipoProspectoDBList;
    List<CatalogoSubsegmentosDB> catalogoSubsegmentosDBList;
    List<CatalogoActividadesPGVDB> catalogoActividadesPGVDBList;

    Spinner spinner_plan_tipo_prospecto;
    Spinner spinner_plan_subsegmento;
    Spinner spinner_tipo_actividad;

    Button btn_plan_buscar;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapterBusqueda;
    RecyclerView.LayoutManager layoutManager;

    List<ProspectosDB> listaProspectosDB;

    //Declaración de las variables de Retrofit.
    ApiInterface apiInterface;
    Call<GetCatalogoPOJO> getActividadesPGVCall;
    Call<List<GetCatalogoSubsegmento>> getSubsegmentoPOJOCall;
    Call<GetCatalogoPOJO> getTipoProspectoPOJOCall;
    Call<List<Json>> getProspectosFiltro;

    NestedScrollView nested_scrolll_view;
    ProgressBar progressBar;
    boolean scrollToPosition = false;

    //Se declara la SharedPreferences.
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    OnClickProspectos onClickProspectos;

    OfertaIntegral ofertaIntegral;

    List<SubsegmentoProducto> subsegmentoProducto = new ArrayList<>();
    List<Servicio> servicio = new ArrayList<>();



    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_semanal_principal, container, false);

        context = getActivity();
        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        //Se instancia la interface.
        onClickProspectos = this;

        //Se inicializa la interface de Retrofit.
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = prefs.edit();

        //Se coloca el Toolbar.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.plan_titulo));

        nested_scrolll_view = (NestedScrollView) view.findViewById(R.id.nested_scrolll_view);
        spinner_plan_tipo_prospecto = (Spinner) view.findViewById(R.id.spinner_plan_tipo_prospecto);
        spinner_plan_subsegmento = (Spinner) view.findViewById(R.id.spinner_plan_subsegmento);
        spinner_tipo_actividad = (Spinner) view.findViewById(R.id.spinner_tipo_actividad);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);



        btn_plan_buscar = (Button) view.findViewById(R.id.btn_plan_buscar);
        recyclerView = (RecyclerView) view.findViewById(R.id.plan_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(context));
        //Sirve para que el scroll se vea suave al estar dentro de un NestedScrollView.
        recyclerView.setNestedScrollingEnabled(false);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Se muestra el círculo de progreso.
        visibilidad(View.VISIBLE);
        //Consume los catálogos y los resultados se colocan en los Spinners. Además consulta
        //todos los prospectos para manejar los filtros de forma local.
        descargarActividadesPGV();

        //Listeners de Spinners.
        spinner_plan_tipo_prospecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkSpinnersContestados();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_plan_subsegmento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkSpinnersContestados();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_tipo_actividad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkSpinnersContestados();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Listeners de Botones.
        btn_plan_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarListaProspectos();
            }
        });

        return view;
    }

    /*public void descargaEstatusObra() {

        getEstatusObraPOJOCall = apiInterface.getCatalogoEstatusObra();

        getEstatusObraPOJOCall.enqueue(new Callback<List<GetCatalogoPOJO>>() {
            @Override
            public void onResponse(Call<List<GetCatalogoPOJO>> call, Response<List<GetCatalogoPOJO>> response) {
                if (response.body() != null && response.code() == 200) {

                    List<GetCatalogoPOJO> catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.get(0).getItems();
                    CatalogoStatusObraDB catalogoStatusObraDB;
                    arrayResponseStatus = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoStatusObraDB = new CatalogoStatusObraDB();

                        catalogoStatusObraDB.setId(items.getId());
                        catalogoStatusObraDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoStatusObraDB.setDescripcion(items.getDescripcion());
                        catalogoStatusObraDB.setIdPadre(items.getIdPadre());
                        arrayResponseStatus.add(catalogoStatusObraDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoStatusObraRealm.guardarListaStatusObra(arrayResponseStatus);

                    descargarSubsegmentoProducto();

                } else {
                    visibilidad(View.GONE);
                    Snackbar.make(getView(), context.getResources().getString(R.string.plan_error_catalogos), Snackbar.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners de la BD.
                }
            }

            @Override
            public void onFailure(Call<List<GetCatalogoPOJO>> call, Throwable t) {

                if (!getEstatusObraPOJOCall.isCanceled()) {
                    Log.d("Descarga Estatus Obra", t.toString());
                    progressBar.setVisibility(View.GONE); //Se quita el progressBar.
                    Snackbar.make(getView(), context.getResources().getString(R.string.no_connection_error_plan), Snackbar.LENGTH_LONG).show();
                    FirebaseCrash.log("Descarga Es  tatus Obra");
                    FirebaseCrash.report(t);

                    mostrarSpinnersOffline(); //Se muestran los Spinners de la BD.
                }
            }
        });
    }*/

    public void descargarActividadesPGV() {

        getActividadesPGVCall = apiInterface.getCatalogoActividadesPGV();

        getActividadesPGVCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoActividadesPGVDB catalogoActividadPGVDB;
                    arrayResponseTipoActividad = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoActividadPGVDB = new CatalogoActividadesPGVDB();

                        catalogoActividadPGVDB.setId(items.getId());
                        catalogoActividadPGVDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoActividadPGVDB.setDescripcion(items.getDescripcion());
                        catalogoActividadPGVDB.setIdPadre(items.getIdPadre());
                        arrayResponseTipoActividad.add(catalogoActividadPGVDB);

                    }

                    //Se guarda en la base de Realm.
                    CatalogoActividadesPGVRealm.guardarListaActividadesPGV(arrayResponseTipoActividad);

                    descargarSubsegmentoProducto();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    visibilidad(View.GONE);
                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                if (!getActividadesPGVCall.isCanceled()) {
                    Log.d("Descarga Tipo Actividad", t.toString());
                    progressBar.setVisibility(View.GONE); //Se quita el progressBar.
                    FirebaseCrash.log("Descarga Tipo Actividad");
                    FirebaseCrash.report(t);

                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
            }
        });
    }

    public void descargarSubsegmentoProducto() {

        getSubsegmentoPOJOCall = apiInterface.getCatalogoSubsegmentoProducto();

        getSubsegmentoPOJOCall.enqueue(new Callback<List<GetCatalogoSubsegmento>>() {
            @Override
            public void onResponse(Call<List<GetCatalogoSubsegmento>> call, Response<List<GetCatalogoSubsegmento>> response) {
                if (response.body() != null && response.code() == 200) {

                    List<GetCatalogoSubsegmento> subsegmentos = response.body();
                    CatalogoSubsegmentosDB catalogoSubsegmentosDB;
                    arrayResponseSubsegmento = new ArrayList<>();

                    for (GetCatalogoSubsegmento items : subsegmentos) { //Se guardan los objetos individualmente.
                        catalogoSubsegmentosDB = new CatalogoSubsegmentosDB();

                        catalogoSubsegmentosDB.setId(items.getId());
                        catalogoSubsegmentosDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoSubsegmentosDB.setDescripcion(items.getDescripcion());
                        catalogoSubsegmentosDB.setIdPadre(items.getIdPadre());
                        CatalogoProductosRealm.guardarListaProductos(items.getProductos());
                        arrayResponseSubsegmento.add(catalogoSubsegmentosDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoSubsegmentosRealm.guardarListaSubsegmentos(arrayResponseSubsegmento);

                    descargarTipoProspecto();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    visibilidad(View.GONE);
                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
            }

            @Override
            public void onFailure(Call<List<GetCatalogoSubsegmento>> call, Throwable t) {
                if (!getSubsegmentoPOJOCall.isCanceled()) {
                    Log.d("Descarga Subsegmento", t.toString());
                    progressBar.setVisibility(View.GONE); //Se quita el progressBar.
                    FirebaseCrash.log("Descarga Subsegmento");
                    FirebaseCrash.report(t);

                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
            }
        });
    }

    public void descargarTipoProspecto() {

        getTipoProspectoPOJOCall = apiInterface.getCatalogoTipoProspecto();

        getTipoProspectoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoTipoProspectoDB catalogoTipoProspectoDB;
                    arrayResponseTipoProspecto = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoTipoProspectoDB = new CatalogoTipoProspectoDB();

                        catalogoTipoProspectoDB.setId(items.getId());
                        catalogoTipoProspectoDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoTipoProspectoDB.setDescripcion(items.getDescripcion());
                        catalogoTipoProspectoDB.setIdPadre(items.getIdPadre());
                        arrayResponseTipoProspecto.add(catalogoTipoProspectoDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoTipoProspectoRealm.guardarListaTipoProspecto(arrayResponseTipoProspecto);

                    obtenerProspectosFiltro();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    visibilidad(View.GONE);
                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                if (!getTipoProspectoPOJOCall.isCanceled()) {
                    Log.d("Descarga Tipo Prospecto", t.toString());
                    progressBar.setVisibility(View.GONE); //Se quita el progressBar.
                    FirebaseCrash.log("Descarga Tipo Prospecto");
                    FirebaseCrash.report(t);

                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
            }
        });
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
    }

    public void mostrarSpinnersOffline() {

        //Se guarda la información de PlanSemanal para obtener las citas y poder configurar las alarmas.
        obtenerActividadProspectos();

        //Se configuran las alarmas de las actividades obtenidas.
        AlarmasActividades alarmasActividades = new AlarmasActividades(context);
        alarmasActividades.configurarAlarmasActividades();

        //Spinner de TipoActividad.
        arrayCatalogoTipoActividad.clear();
        arrayCatalogoTipoActividad.add("-- Selecciona --");

        catalogoActividadesPGVDBList = CatalogoActividadesPGVRealm.mostrarListaActividadesExistentes();
        for (CatalogoActividadesPGVDB catalogoActividadesPGVDB : catalogoActividadesPGVDBList) {
            arrayCatalogoTipoActividad.add(catalogoActividadesPGVDB.getDescripcion());
        }
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style,
                arrayCatalogoTipoActividad);
        spinner_tipo_actividad.setAdapter(adapter);

        //Spinner de Subsegmento.
        arrayCatalogoSubsegmento.clear();
        arrayCatalogoSubsegmento.add("-- Selecciona --");

        catalogoSubsegmentosDBList = CatalogoSubsegmentosRealm.mostrarListaSubsegmentos();
        for (CatalogoSubsegmentosDB catalogoSubsegmentosDB : catalogoSubsegmentosDBList) {
            arrayCatalogoSubsegmento.add(catalogoSubsegmentosDB.getDescripcion());
        }
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style,
                arrayCatalogoSubsegmento);
        spinner_plan_subsegmento.setAdapter(adapter);

        //Spinner de Tipo de Prospecto.
        arrayCatalogoTipoProspecto.clear();
        arrayCatalogoTipoProspecto.add("-- Selecciona --");

        catalogoTipoProspectoDBList = CatalogoTipoProspectoRealm.mostrarListaTipoProspecto();
        for (CatalogoTipoProspectoDB catalogoTipoProspectoDB : catalogoTipoProspectoDBList) {
            arrayCatalogoTipoProspecto.add(catalogoTipoProspectoDB.getDescripcion());
        }
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style,
                arrayCatalogoTipoProspecto);
        spinner_plan_tipo_prospecto.setAdapter(adapter);


        //Una vez descargados los catálogos, se muestran los spinners llenos.
        visibilidad(View.GONE);
    }

    /************
     * OBTENCIÓN DE LOS DATOS DEL PROSPECTO, CONTACTOS Y ACTIVIDADES
     ***************/
    private void obtenerProspectosFiltro() {

        final JSONfiltro jsonfiltro = new JSONfiltro();

        jsonfiltro.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
        if (ProspectosRealm.ultimafechaAltaProspecto() != 0) {
          //  jsonfiltro.setFechaAltaProspecto(ProspectosRealm.ultimafechaAltaProspecto());
            jsonfiltro.setFechaAltaProspecto(0);
        }

        getProspectosFiltro = apiInterface.getProspectoFiltro(jsonfiltro);

        getProspectosFiltro.enqueue(new Callback<List<Json>>() {
            @Override
            public void onResponse(Call<List<Json>> call, Response<List<Json>> response) {
                Log.d("", "");
                if (response.body() != null && response.code() == 200) {

                    //Se eliminan las tablas para que se llenen nuevamente en caso de que haya
                    //existido algún cambio.
//                    ProspectosRealm.eliminarTabla();
//                    ActividadesRealm.eliminarTabla();

                    List<Json> jsonList = response.body();

                    //Variables relacionadas con la información de Prospectos.
                    List<ProspectosDB> prospectosDBList = new ArrayList<>();
                    ProspectosDB prospectosDB;

                    //Variables relacionadas con la información de Contactos.
                    List<ContactosDB> contactosDBList = new ArrayList<>();

                    //Variables relacionadas con la información de Actividades.
                    RealmList<ActividadesDB> actividadesDBList;

                    //Variable relacionada con las imágenes/archivos del prospecto.
                    RealmList<ArchivosAltaDB> archivosAltaDBList;

                    //Variables relacionadas con el guardado del json global de cada prospecto.
                    JsonGlobalProspectosDB jsonGlobalProspectosDB;
                    List<JsonGlobalProspectosDB> jsonGlobalProspectosDBList = new ArrayList<>();
                    Gson gson = new Gson(); //Objeto que convierte el objeto en un json.

                    String idProspecto;

                    for (int i = 0; i < jsonList.size(); i++) {

                        prospectosDB = new ProspectosDB();
                        actividadesDBList = new RealmList<>();
                        archivosAltaDBList = new RealmList<>();
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

                    mostrarSpinnersOffline(); //Se muestran los Spinners.

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
            }

            @Override
            public void onFailure(Call<List<Json>> call, Throwable t) {
                if (!getProspectosFiltro.isCanceled()) {
                    Log.e("GETPROSPECTOS", t.toString());
                    FirebaseCrash.log("Error ProspectosDB");
                    FirebaseCrash.report(t);

                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarSpinnersOffline(); //Se muestran los Spinners.
                }
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
                if (actividadeList.get(actividadeList.size() - 2).getEstatusAgenda() == Valores.ID_ACTIVIDAD_REAGENDADA)  {
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

    /********************************************************************************************/

    /**
     * MÉTODO QUE FILTRA LOS PROSPECTOS Y MUESTRA LOS RESULTADOS
     **/
    public void mostrarListaProspectos() {

        scrollToPosition = true;
        long idTipoProspecto = 0;
        long idSubsegmento = 0;
        long idTipoActividad = 0;

        try {

            //Se considera el -- Selecciona --
            if (spinner_plan_tipo_prospecto.getSelectedItemPosition() != 0) {
                idTipoProspecto = catalogoTipoProspectoDBList.get(spinner_plan_tipo_prospecto.getSelectedItemPosition() - 1).getId();
            }
            if (spinner_plan_subsegmento.getSelectedItemPosition() != 0) {
                idSubsegmento = catalogoSubsegmentosDBList.get(spinner_plan_subsegmento.getSelectedItemPosition() - 1).getId();
            }
            if (spinner_tipo_actividad.getSelectedItemPosition() != 0) {
                idTipoActividad = catalogoActividadesPGVDBList.get(spinner_tipo_actividad.getSelectedItemPosition() - 1).getId();
            }

            //Se obtienen los registros de la tabla.
            listaProspectosDB = ProspectosRealm.mostrarListaProspectosFiltro(
                    idTipoProspecto, //Se considera el -- Selecciona --
                    idSubsegmento,
                    idTipoActividad);

            if (listaProspectosDB.size() > 0) {
                adapterBusqueda = new AdapterPlanSemanalFiltros(context, onClickProspectos, listaProspectosDB);
                recyclerView.setAdapter(adapterBusqueda);
                adapterBusqueda.notifyDataSetChanged();

                recyclerView.setVisibility(View.VISIBLE);

                //Listener que detecta cuando el recyclerView ya se ha llenado.
                recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //En este punto el RecyclerView ya se ha llenado y sus dimensiones son conocidas.
                        //Se scrollea automáticamente hasta el botón, a fin de ver los resultados de la búsqueda.
                        if (scrollToPosition) {
                            nested_scrolll_view.smoothScrollTo(0, (int) btn_plan_buscar.getY());
                            scrollToPosition = false;
                        }
                    }
                });
            } else {
                adapterBusqueda = new AdapterPlanSemanalFiltros(context, onClickProspectos, listaProspectosDB);
                recyclerView.setAdapter(adapterBusqueda);
                adapterBusqueda.notifyDataSetChanged();

                recyclerView.setVisibility(View.GONE);
                Toast.makeText(context, getString(R.string.plan_error_busqueda), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("ProspectosFragment", e.toString());
            FirebaseCrash.log("Error ProspectosDB");
            FirebaseCrash.report(e);

            Toast.makeText(context, getString(R.string.plan_error_busqueda), Toast.LENGTH_LONG).show();
        }
    }

    public void checkSpinnersContestados() {

        /*if (spinner_plan_tipo_prospecto.getSelectedItemPosition() == 0
                && spinner_plan_subsegmento.getSelectedItemPosition() == 0
                && spinner_tipo_actividad.getSelectedItemPosition() == 0) {

            btn_plan_buscar.setBackgroundResource(R.color.colorGrisOscuro);
            btn_plan_buscar.setEnabled(false);
        } else {
            btn_plan_buscar.setBackgroundResource(R.color.colorAzulElectrico);
            btn_plan_buscar.setEnabled(true);
        }*/
    }


    /**
     * MÉTODO QUE ESCONDE EL PROGRESSBAR Y MUESTRA EL LAYOUT CON CONTENIDO
     **/
    public void visibilidad(int visibility) {

        try {
            progressBar.setVisibility(visibility);
            nested_scrolll_view.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            //En caso de que algún proceso ya haya escondido el progressBar antes.
            Log.d("Error progressbar", e.toString());
            FirebaseCrash.log("Error progressbar plan semanal");
            FirebaseCrash.report(e);
        }
    }


    @Override
    public void onClickProspectos(View view, int item) {

        if (listaProspectosDB.get(item).isEstaDescartado()) {
            Toast.makeText(context, getString(R.string.plan_prospecto_descartado), Toast.LENGTH_LONG).show();
        } else {
            if (listaProspectosDB.get(item).getIdActividad() != Valores.ID_ACTIVIDAD_CERRAR_VENTA) {

               // visibilidad(View.VISIBLE);

                    obtenerProductosYServicios(listaProspectosDB.get(item));
                //visibilidad(View.GONE);

            } else {
                Toast.makeText(context, getString(R.string.plan_prospecto_finalizado), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void obtenerProductosYServicios(final ProspectosDB prospectosDB) {

        //String idProspecto = prefs.getString(Valores.SHAREDPREFERENCES_ID_PROSPECTO, "");
        String idProspecto = prospectosDB.getId();
        long actividadActual;

        if (prospectosDB.getIdActividad() != 0) {
            actividadActual = prospectosDB.getIdActividad();
        } else {
            if (prospectosDB.getIdTipoProspecto() == 1) {
                actividadActual = Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO;
            } else {
                actividadActual = Valores.ID_ACTIVIDAD_CONTACTAR_CLIENTE;
            }
        }
        visibilidad(View.VISIBLE);
        final int pasoActual = evaluarPasoActual(actividadActual);
        final int idStatus = evaluarIdStatus(actividadActual);
        Gson gson = new Gson();
        String jsonGlobal = JsonGlobalProspectosRealm.mostrarJsonGlobalProspecto(idProspecto).getJsonGlobalProspectos();

        Call<OfertaIntegral> callOfertaIntegral;

        try {
            JSONObject jsonObject = new JSONObject(jsonGlobal);
//
            callOfertaIntegral = apiInterface.getOportunidadVentaPaso(pasoActual + "", idProspecto);

            callOfertaIntegral.enqueue(new Callback<OfertaIntegral>() {
                @Override
                public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
                    if (response.body() != null && response.code() == 200) {
                        ofertaIntegral = response.body();
                        generarActividad(pasoActual, idStatus, prospectosDB);




                    } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                        //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                        AlertTokenToLogin.showAlertDialog(context);
                        visibilidad(View.GONE);
                    } else {

                        Toast.makeText(context, getString(R.string.citas_prospecto_fail), Toast.LENGTH_LONG).show();
                        visibilidad(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                    Log.e("Oportunidad Venta", t.toString());
                    FirebaseCrash.log("Error OV");
                    FirebaseCrash.report(t);
                    visibilidad(View.GONE);
                    Toast.makeText(context, getString(R.string.citas_prospecto_fail), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            visibilidad(View.GONE);
            Log.e("ERROR_PLAN_ACTIVIDAD", e.toString());
        }
    }

    public int evaluarPasoActual(long idActividadActual) {

        //String nombreActividadActual = getString(R.string.actividades_contactar_nuevo_prospecto);

        //SE USA IF-ELSE, YA QUE NO SE PUEDE USAR "CASE" CON VALORES DE RECURSOS DE STRINGS.
        if (idActividadActual == Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO
                || idActividadActual == Valores.ID_ACTIVIDAD_CONTACTAR_CLIENTE) {
            return 1;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_VISITAR_PROSPECTO
                || idActividadActual == Valores.ID_ACTIVIDAD_RECABAR_INFORMACION) {
            return 2;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_CALIFICAR_OPORTUNIDAD
                || idActividadActual == Valores.ID_ACTIVIDAD_PREPARAR_PROPUESTA_DE_VALOR
                || idActividadActual == Valores.ID_ACTIVIDAD_DESCARTAR_OPORTUNIDAD) {
            return 3;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_PRESENTAR_PROPUESTA) {
            return 4;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_RECIBIR_RESPUESTA
                || idActividadActual == Valores.ID_ACTIVIDAD_NEGOCIAR_AJUSTAR_PROPUESTA) {
            return 5;
        } else {

            return 1;
        }
    }

    public int evaluarIdStatus(long idActividadActual) {

        if (idActividadActual == Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO
                || idActividadActual == Valores.ID_ACTIVIDAD_CONTACTAR_CLIENTE) {
            return Valores.ID_ESTATUS_PROSPECTO_SIN_CONTACTO;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_VISITAR_PROSPECTO) {
            return Valores.ID_ESTATUS_PROSPECTO_AGENDADO;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_RECABAR_INFORMACION) {
            return Valores.ID_ESTATUS_PROSPECTO_VISITA_1_10;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_CALIFICAR_OPORTUNIDAD) {
            return Valores.ID_ESTATUS_PROSPECTO_CALIFICA;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_DESCARTAR_OPORTUNIDAD) {
            return Valores.ID_ESTATUS_PROSPECTO_NO_CALIFICA;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_PREPARAR_PROPUESTA_DE_VALOR
                || idActividadActual == Valores.ID_ACTIVIDAD_PRESENTAR_PROPUESTA) {
            return Valores.ID_ESTATUS_PROSPECTO_PREPARAR_PROPUESTA;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_RECIBIR_RESPUESTA) {
            return Valores.ID_ESTATUS_PROSPECTO_PROPUESTA_ENTREGADA;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_NEGOCIAR_AJUSTAR_PROPUESTA) {
            return Valores.ID_ESTATUS_PROSPECTO_EN_NEGOCIACION;
        } else if (idActividadActual == Valores.ID_ACTIVIDAD_CERRAR_VENTA) {
            return Valores.ID_ESTATUS_PROSPECTO_GANADA;
        } else {
            return 0;
        }
    }

    /**
     * MÉTODO QUE CANCELA TODAS LAS PETICIONES QUE ESTÉN EN CURSO AL MOMENTO DE SALIR DE LA PANTALLA
     **/
    public void cancelAllRequests() {

        if (getTipoProspectoPOJOCall != null) {
            getTipoProspectoPOJOCall.cancel();
        }
        if (getSubsegmentoPOJOCall != null) {
            getSubsegmentoPOJOCall.cancel();
        }
        if (getTipoProspectoPOJOCall != null) {
            getTipoProspectoPOJOCall.cancel();
        }
        if (getProspectosFiltro != null) {
            getProspectosFiltro.cancel();
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
            cancelAllRequests(); //Cancela todas las peticiones en curso.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Se coloca la ejecución del botón "back" del dispositivo.
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
if(arrayCatalogoTipoProspecto.size()!=0)
{
    visibilidad(View.GONE);
}

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //Ejecuta el método onBackPressed() de la actividad madre.
                        Funciones.onBackPressedFunction(context, true);
                        cancelAllRequests(); //Cancela todas las peticiones en curso.
                        return true;
                    }
                }
                return false;
            }
        });
    }


    public void generarActividad(int idPaso, int idStatus, ProspectosDB prospectosDB) {

        String idProspecto = prospectosDB.getId();
        long actividadActual;

        if (prospectosDB.getIdActividad() != 0) {
            actividadActual = prospectosDB.getIdActividad();
        } else {
            if (prospectosDB.getIdTipoProspecto() == 1) {
                actividadActual = Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO;
            } else {
                actividadActual = Valores.ID_ACTIVIDAD_CONTACTAR_CLIENTE;
            }
        }

        Gson gson = new Gson();
        String jsonGlobal = JsonGlobalProspectosRealm.mostrarJsonGlobalProspecto(idProspecto).getJsonGlobalProspectos();

        /** SE GUARDA EL OBJETO JsonAltaActividades en TinyDB **/
        TinyDB tinyDB = new TinyDB(context);

        JsonAltaActividades jsonAltaActividades = new JsonAltaActividades();
        Actividad actividad = new Actividad();
        TipoActividad tipoActividad = new TipoActividad();

        //Se obtiene la información de CatalogoActividadesPGVDB.
        CatalogoActividadesPGVDB catalogoActividadesPGVDB =
                CatalogoActividadesPGVRealm.mostrarInformacionActividad(actividadActual);
        tipoActividad.setDescripcion(catalogoActividadesPGVDB.getDescripcion());
        tipoActividad.setId(catalogoActividadesPGVDB.getId());
        tipoActividad.setIdCatalogo(catalogoActividadesPGVDB.getIdCatalogo());
        tipoActividad.setIdPadre(catalogoActividadesPGVDB.getIdPadre());

        //Se llena el objeto de actividad (más tarde se cambiará la horaInicio y horaFin).
        actividad.setComentarios("");
        actividad.setConfirmarVolumenObra("");
        actividad.setFechaHoraCitaFin("");
        actividad.setFechaHoraCitaInicio("");
        actividad.setFechaInicioObra("");
        actividad.setIdArchivoAdjunto("");
        actividad.setTipoActividad(tipoActividad);

        jsonAltaActividades.setActividad(actividad);
        jsonAltaActividades.setIdPaso(idPaso);
        jsonAltaActividades.setIdStatusProspecto(idStatus);
        jsonAltaActividades.setServicios(ofertaIntegral.getServicios());
        jsonAltaActividades.setSubsegmentoProductos(ofertaIntegral.getSubsegmentosProductos());
        jsonAltaActividades.setEsOfertaIntegral(ofertaIntegral.getEsOfertaIntegral());

        ActividadAnterior actividadAnterior = new ActividadAnterior();
        if (prospectosDB.getIdActividadAnterior().equals("")) {
            actividadAnterior.setEstatusAgenda(0);
        } else {
            actividadAnterior.setEstatusAgenda(2);
        }
        actividadAnterior.setIdActividad(prospectosDB.getIdActividadAnterior());

        jsonAltaActividades.setmActividadAnterior(actividadAnterior);

        tinyDB.putJsonAltaActividades(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES, jsonAltaActividades);

        editor.putString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, idProspecto);
        editor.commit();

        Log.d("", "");

        //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
        Intent intent = new Intent();
        intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
        intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_CALENDAR);
        startActivity(intent);

    }
}
