package com.telstock.tmanager.cemex.prospectos;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.telstock.tmanager.cemex.prospectos.adapters.AdapterProspectos;
import com.telstock.tmanager.cemex.prospectos.funciones.TinyDB;
import com.telstock.tmanager.cemex.prospectos.interfaces.OnClickProspectos;
import com.telstock.tmanager.cemex.prospectos.model.JSONfiltro;
import com.telstock.tmanager.cemex.prospectos.rest.ApiClient;
import com.telstock.tmanager.cemex.prospectos.rest.ApiInterface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import mx.com.tarjetasdelnoreste.realmdb.ActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoActividadesPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoClienteRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoProspectoRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonGlobalProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.SemaforoRealm;
import mx.com.tarjetasdelnoreste.realmdb.actividades.MapsActivity;
import mx.com.tarjetasdelnoreste.realmdb.actividades.ObraDetalle;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlarmasActividades;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.ActividadesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoProspectoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonGlobalProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.SemaforoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Actividade;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Actividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.ActividadAnterior;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OfertaIntegral;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.TipoActividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonSemaforo.JsonSemaforo;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonSemaforo.Reporte;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USRMICRO10 on 24/08/2016.
 */
public class ProspectosFragment extends Fragment implements View.OnClickListener, OnClickProspectos {

    Context context;

    private View mListTouchInterceptor; //Objeto que maneja la imagen.
    private View mDetailsLayout; //Layout con el contenido del folder al abrirse.
    private UnfoldableView mUnfoldableView; //Objeto que maneja las imágenes como folders.
    private ListView listView;

    private TextView txt_prospectos_total;

    //Objetos dentro del folder.
    private ImageView img_folder_imagen;
    private ImageView img_folder_icono;
    private TextView txt_folder_nombre_prospecto;
    private TextView txt_folder_clasificacion_prospecto;
    private TextView txt_folder_direccion;
    private LinearLayout btn_folder_mapa;
    private LinearLayout btn_folder_contactos;
    private LinearLayout btn_folder_agendar_cita;
    private LinearLayout btn_folder_archivos;
    private LinearLayout btn_folder_cerrar;
    private LinearLayout btn_folder_informacion_obra;

    private Dialog dialogModal; //Modal que muestra la clasificación de Prospectos.
    private Dialog dialogModalBusqueda;

    ProgressBar progressBar;
    private List<ProspectosDB> arrayListProspectos = new ArrayList<>();

    //Muestra la capa de fondo cuando se abre el folder en módulo de Prospectos.
    private RelativeLayout relative_capa_fondo;

    //Variables para envío de parámetros a la pantalla de Contactos.
    private String idProspecto;
    private String nombreProspecto;

    //Variables para colocar el título y coordenadas del mapa en forma adecuada.
    private String titleMapa;
    private LatLng latLngMapa;

    private OnClickProspectos onClickProspectos;
    List<CatalogoTipoProspectoDB> tipoProspectoListAll = new ArrayList<>();
    List<CatalogoActividadesPGVDB> listaActividades = new ArrayList<>();
    //Declaración de las variables de Retrofit.
    ApiInterface apiInterface;
    Call<List<Json>> getProspectosFiltro;
    Call<JsonSemaforo> getJsonSemaforo;

    OfertaIntegral ofertaIntegral;
    //Se declara la SharedPreferences.
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_propectos_principal, container, false);

        context = getActivity();
        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        //Se inicializa la interfaz de Retrofit.
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = prefs.edit();

        onClickProspectos = this;

        //Se coloca el Toolbar.
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.prospectos_title));

        //Muestra la capa de fondo cuando se abre el folder en módulo de Prospectos.
        relative_capa_fondo = (RelativeLayout) view.findViewById(R.id.relative_capa_fondo);

        txt_prospectos_total = (TextView) view.findViewById(R.id.txt_prospectos_total);
        //Instanciación de los objetos dentro del folder.
        img_folder_imagen = (ImageView) view.findViewById(R.id.img_folder_imagen);
        img_folder_icono = (ImageView) view.findViewById(R.id.img_folder_icono);
        txt_folder_nombre_prospecto = (TextView) view.findViewById(R.id.txt_folder_nombre_prospecto);
        txt_folder_clasificacion_prospecto = (TextView) view.findViewById(R.id.txt_folder_clasificacion_prospecto);
        txt_folder_direccion = (TextView) view.findViewById(R.id.txt_folder_direccion);
        btn_folder_mapa = (LinearLayout) view.findViewById(R.id.btn_folder_mapa);
        btn_folder_contactos = (LinearLayout) view.findViewById(R.id.btn_folder_contactos);
        btn_folder_agendar_cita = (LinearLayout) view.findViewById(R.id.btn_folder_agendar_cita);
        btn_folder_archivos = (LinearLayout) view.findViewById(R.id.btn_folder_archivos);
        btn_folder_informacion_obra = (LinearLayout) view.findViewById(R.id.btn_folder_informacion_obra);

        btn_folder_cerrar = (LinearLayout) view.findViewById(R.id.btn_folder_cerrar);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        //Muestra el total de prospectos (se inicializa en 0).
        txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                + " 0");

        //Se colocan los listeners de los botones/LinearLayouts.
        btn_folder_mapa.setOnClickListener(this);
        btn_folder_contactos.setOnClickListener(this);
        btn_folder_agendar_cita.setOnClickListener(this);
        btn_folder_archivos.setOnClickListener(this);
        btn_folder_cerrar.setOnClickListener(this);
        btn_folder_informacion_obra.setOnClickListener(this);

        //Hardcodeo de los prospectos.
        /*arrayListProspectos.add(new Prospectos("Nuevo Prospecto1", "Nextlalpan - Varon Metta",
                "Preparar Propuesta de Valor", R.drawable.avatar_prospecto));*/

        //Muestra el total de prospectos.
        txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                + " " + arrayListProspectos.size());

        //obtenerProspectos();
        obtenerSemaforo();

        //Lista que muestra las imágenes como folders.
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(new AdapterProspectos(context, this, arrayListProspectos));

        /*** CODIGO DEL FOLDABLE LAYOUT ***/
        mListTouchInterceptor = view.findViewById(R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        //Se oculta el layout con el contenido del folder, ya que en un principio
        //todos los folders estarán cerrados.
        mDetailsLayout = view.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);

        mUnfoldableView = (UnfoldableView) view.findViewById(R.id.unfoldable_view);

        /*try {
            //Código que muestra una línea blanca sobre el folder para aumentar el realismo al abrirse.
            Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
            mUnfoldableView.setFoldShading(new GlanceFoldShading(glance));
        } catch (Exception e) {
            Log.e("Error", "Memoria");
        }*/

        //Listener del objeto que maneja el folder.
        mUnfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {

            //Hacer texto e imágenes visibles mientras se abre el folder.
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
                mDetailsLayout.setVisibility(View.VISIBLE);
            }

            //Quitar el listener de la vista una vez que se ha abierto el folder.
            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
            }

            //Habilitar el listener de la imagen al estar cerrando el folder (esto permite
            //interactuar con el folder mientras se está cerrando).
            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(true);
            }

            //Quitar el listener de la imagen y ocultar el contenido dentro del folder.
            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                mListTouchInterceptor.setClickable(false);
                mDetailsLayout.setVisibility(View.INVISIBLE);

                //Quita la capa oscura de fondo.
                relative_capa_fondo.setVisibility(View.GONE);
            }
        });

        //Dialogs que muestran la clasificación de prospectos y filtro de prospectos.
//        showModalProspectos();
        showModalBusquedaProspectos();

        return view;
    }

    /**
     * MÉTODO QUE MUESTRA LA MODAL CON LA CLASIFICACIÓN DE PROSPECTOS
     **/
    public void showModalProspectos() {
        dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_modal_prospectos);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        ImageView btn_modal_cerrar = (ImageView) dialogModal.findViewById(R.id.btn_modal_cerrar);
        TextView marcador_prospeccion = (TextView) dialogModal.findViewById(R.id.marcador_prospeccion);
        TextView marcador_indagar = (TextView) dialogModal.findViewById(R.id.marcador_indagar);
        TextView marcador_calificar = (TextView) dialogModal.findViewById(R.id.marcador_calificar);
        TextView marcador_propuesta = (TextView) dialogModal.findViewById(R.id.marcador_propuesta);
        TextView marcador_cerrar = (TextView) dialogModal.findViewById(R.id.marcador_cerrar);

        LinearLayout layoutModalProspeccion = (LinearLayout) dialogModal.findViewById(R.id.layout_modal_prospeccion);
        LinearLayout layoutModalIndagar = (LinearLayout) dialogModal.findViewById(R.id.layout_modal_indagar);
        LinearLayout layoutModalCalificar = (LinearLayout) dialogModal.findViewById(R.id.layout_modal_calificar);
        LinearLayout layoutModalPresentarPropuesta = (LinearLayout) dialogModal.findViewById(R.id.layout_modal_presentar_propuesta);
        LinearLayout layoutModalCerrarVenta = (LinearLayout) dialogModal.findViewById(R.id.layout_modal_cerrar_venta);

        btn_modal_cerrar.setOnClickListener(this);
        layoutModalProspeccion.setOnClickListener(this);
        layoutModalIndagar.setOnClickListener(this);
        layoutModalCalificar.setOnClickListener(this);
        layoutModalPresentarPropuesta.setOnClickListener(this);
        layoutModalCerrarVenta.setOnClickListener(this);

        List<SemaforoDB> semaforoDBList = SemaforoRealm.mostrarListaSemaforo();

        for (SemaforoDB semaforoDB : semaforoDBList) {
            switch (semaforoDB.getIdPaso()) {
               case 1:
                    marcador_prospeccion.setText(semaforoDB.getTotalPaso() + "");
                    break;
                case 2:
                    marcador_indagar.setText(semaforoDB.getTotalPaso() + "");
                    break;
                case 3:
                    marcador_calificar.setText(semaforoDB.getTotalPaso() + "");
                    break;
                case 4:
                    marcador_propuesta.setText(semaforoDB.getTotalPaso() + "");
                    break;
                case 5:
                    marcador_cerrar.setText(semaforoDB.getTotalPaso() + "");
                    break;
            }
        }

        /*marcador_prospeccion.setText(
                Integer.toString(
                        ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_contactar_nuevo_prospecto)) +
                                ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_contactar_cliente))));

        marcador_indagar.setText(
                Integer.toString(
                        ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_visitar_prospecto)) +
                                ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_recabar_informacion))));

        marcador_calificar.setText(
                Integer.toString(
                        ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_calificar_oportunidad)) +
                                ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_propuesta_valor)) +
                                ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_descartar_oportunidad))));

        marcador_propuesta.setText(
                Integer.toString(
                        ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_presentar_propuesta))));

        marcador_cerrar.setText(
                Integer.toString(
                        ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_recibir_respuesta_propuesta)) +
                                ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_recibir_respuesta)) +
                                ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_negociar_propuesta)) +
                                ProspectosRealm.contarProspectosPasos(getString(R.string.actividades_cerrar_venta))));*/

        dialogModal.show();

    }

    public void showModalBusquedaProspectos() {
        dialogModalBusqueda = new Dialog(context);
        dialogModalBusqueda.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModalBusqueda.setContentView(R.layout.dialog_modal_busqueda_prospectos);
        dialogModalBusqueda.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModalBusqueda.setCancelable(false);
        //Se comenta la animación, ya que provoca un fade indeseado cuando fitsSystemWindows=true,
        //esta propiedad es necesaria, ya que permite que el AutoCompleteTextView no sea tapado por el teclado.
        //dialogModalBusqueda.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogModalBusqueda.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button btnModalCancelarBusqueda = (Button) dialogModalBusqueda.findViewById(R.id.btnCancelarBusquedaProspectos);
        Button btnModalConfirmarBusqueda = (Button) dialogModalBusqueda.findViewById(R.id.btn_confirmar_busqueda_prospecto);
        final Spinner spFiltro = (Spinner) dialogModalBusqueda.findViewById(R.id.sp_filtro);
        final Spinner spResultado = (Spinner) dialogModalBusqueda.findViewById(R.id.sp_resultados);
        final AutoCompleteTextView etBuscarProspecto = (AutoCompleteTextView) dialogModalBusqueda.findViewById(R.id.et_buscar_prospecto);
        final LinearLayout lyBuscarProspecto = (LinearLayout) dialogModalBusqueda.findViewById(R.id.ly_buscar_prospecto);

        etBuscarProspecto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                }
                return false;
            }
        });

        ArrayList<String> listaFiltros = new ArrayList<>();

        listaFiltros.add(getString(R.string.formulario_spinners_default));
        listaFiltros.add(getString(R.string.filtro_tipo_prospecto));
        listaFiltros.add(getString(R.string.filtro_actividad));
        listaFiltros.add(getString(R.string.filtro_nombre));
        listaFiltros.add(getString(R.string.filtro_numero_registro));
        listaFiltros.add(getString(R.string.filtro_ver_todos));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_style, listaFiltros);
        spFiltro.setAdapter(adapter);

        spFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) { //Selecciona opción "Selecciona".
                    spResultado.setVisibility(View.GONE);
                    lyBuscarProspecto.setVisibility(View.GONE);

                } else if (i == 1) { //Selecciona filtro por Tipo de Prospecto.
                    spResultado.setVisibility(View.VISIBLE);
                    lyBuscarProspecto.setVisibility(View.GONE);

                    tipoProspectoListAll = CatalogoTipoProspectoRealm.mostrarListaTipoProspecto();
                    ArrayList<String> tipoProspectoDesc = new ArrayList<String>();
                    tipoProspectoDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
                    for (CatalogoTipoProspectoDB tipoProspecto : tipoProspectoListAll) {
                        tipoProspectoDesc.add(tipoProspecto.getDescripcion());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_style, tipoProspectoDesc);
                    spResultado.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else if (i == 2) { //Selecciona filtro por Actividad.
                    spResultado.setVisibility(View.VISIBLE);
                    lyBuscarProspecto.setVisibility(View.GONE);

                    listaActividades = CatalogoActividadesPGVRealm.mostrarListaActividadesExistentes();
                    ArrayList<String> actividadesDesc = new ArrayList<String>();
                    actividadesDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
                    for (CatalogoActividadesPGVDB actividades : listaActividades) {
                        actividadesDesc.add(actividades.getDescripcion());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_style, actividadesDesc);
                    spResultado.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } else if (i == 3) { //Selecciona filtro por Nombre.

                    spResultado.setVisibility(View.GONE);
                    lyBuscarProspecto.setVisibility(View.VISIBLE);
                    etBuscarProspecto.setInputType(InputType.TYPE_CLASS_TEXT); //Se coloca el teclado con números.
                    etBuscarProspecto.setText("");

                    List<ProspectosDB> prospectosDB = ProspectosRealm.mostrarListaProspectos();
                    List<String> nombreProspectos = new ArrayList<>();
                    for (ProspectosDB prospectos : prospectosDB) {
                        if (prospectos.getObra() != null) {
                            nombreProspectos.add(prospectos.getObra());
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nombreProspectos);
                    etBuscarProspecto.setAdapter(adapter);

                } else if (i == 4) { //Selecciona filtro por Número de Registro.

                    spResultado.setVisibility(View.GONE);
                    lyBuscarProspecto.setVisibility(View.VISIBLE);
                    etBuscarProspecto.setInputType(InputType.TYPE_CLASS_NUMBER); //Se coloca el teclado con números.
                    etBuscarProspecto.setText("");

                    List<ProspectosDB> prospectosDB = ProspectosRealm.mostrarListaProspectos();
                    List<String> numeroRegistro = new ArrayList<>();

                    for (ProspectosDB prospectos : prospectosDB) {
                        numeroRegistro.add(Long.toString(prospectos.getNumeroRegistro()));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, numeroRegistro);
                    etBuscarProspecto.setAdapter(adapter);

                } else if (i == 5) { //Selecciona opción de Ver Todos.
                    spResultado.setVisibility(View.GONE);
                    lyBuscarProspecto.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnModalConfirmarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (spFiltro.getSelectedItemPosition() == 1) {

                    try {
                        Long tipoProspecto = tipoProspectoListAll.get(spResultado.getSelectedItemPosition() - 1).getId();
                        //Se obtienen los registros de la tabla.
                        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectosTipoDeProspecto(tipoProspecto);

                        if (listaProspectosDB.size() > 0) {
                            //Muestra el total de prospectos.
                            txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                                    + " " + listaProspectosDB.size());

                            AdapterProspectos adapterProspectos = new AdapterProspectos(context, onClickProspectos, listaProspectosDB);
                            listView.setAdapter(adapterProspectos);

                            adapterProspectos.notifyDataSetChanged();

                            dialogModalBusqueda.dismiss();

                        } else {
                            Toast.makeText(context, "No existen prospectos de ese tipo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ProspectosFragment", e.toString());
                        FirebaseCrash.log("Error ProspectosDB");
                        FirebaseCrash.report(e);
                    }

                } else if (spFiltro.getSelectedItemPosition() == 2) {

                    try {
                        String actividadProspecto = listaActividades.get(spResultado.getSelectedItemPosition() - 1).getDescripcion();
                        //Se obtienen los registros de la tabla.
                        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectosActividad(actividadProspecto);

                        if (listaProspectosDB.size() > 0) {
                            //Muestra el total de prospectos.
                            txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                                    + " " + listaProspectosDB.size());

                            AdapterProspectos adapterProspectos = new AdapterProspectos(context, onClickProspectos, listaProspectosDB);
                            listView.setAdapter(adapterProspectos);

                            adapterProspectos.notifyDataSetChanged();

                            dialogModalBusqueda.dismiss();

                        } else {
                            Toast.makeText(context, "No existen prospectos de ese tipo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ProspectosFragment", e.toString());
                        FirebaseCrash.log("Error ProspectosDB");
                        FirebaseCrash.report(e);
                    }
                } else if (spFiltro.getSelectedItemPosition() == 3) {

                    try {
                        String nombreProspecto = etBuscarProspecto.getText().toString();
                        //Se obtienen los registros de la tabla.
                        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectosPorNombre(nombreProspecto);

                        if (listaProspectosDB.size() > 0) {
                            //Muestra el total de prospectos.
                            txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                                    + " " + listaProspectosDB.size());

                            AdapterProspectos adapterProspectos = new AdapterProspectos(context, onClickProspectos, listaProspectosDB);
                            listView.setAdapter(adapterProspectos);

                            adapterProspectos.notifyDataSetChanged();

                            dialogModalBusqueda.dismiss();

                        } else {
                            Toast.makeText(context, "No existen prospectos de ese tipo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ProspectosFragment", e.toString());
                        FirebaseCrash.log("Error ProspectosDB");
                        FirebaseCrash.report(e);
                    }
                } else if (spFiltro.getSelectedItemPosition() == 4) {

                    try {
                        long numeroRegistro = Long.parseLong(etBuscarProspecto.getText().toString());
                        //Se obtienen los registros de la tabla.
                        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectosPorNumeroRegistro(numeroRegistro);

                        if (listaProspectosDB.size() > 0) {
                            //Muestra el total de prospectos.
                            txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                                    + " " + listaProspectosDB.size());

                            AdapterProspectos adapterProspectos = new AdapterProspectos(context, onClickProspectos, listaProspectosDB);
                            listView.setAdapter(adapterProspectos);

                            adapterProspectos.notifyDataSetChanged();

                            dialogModalBusqueda.dismiss();

                        } else {
                            Toast.makeText(context, "No se encontró el prospecto con ése Número de Registro", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ProspectosFragment", e.toString());
                        FirebaseCrash.log("Error ProspectosDB");
                        FirebaseCrash.report(e);
                    }

                } else if (spFiltro.getSelectedItemPosition() == 5) {
                    try {
                        //Se obtienen los registros de la tabla.
                        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectos();

                        if (listaProspectosDB.size() > 0) {
                            //Muestra el total de prospectos.
                            txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                                    + " " + listaProspectosDB.size());

                            AdapterProspectos adapterProspectos = new AdapterProspectos(context, onClickProspectos, listaProspectosDB);
                            listView.setAdapter(adapterProspectos);

                            adapterProspectos.notifyDataSetChanged();

                            dialogModalBusqueda.dismiss();

                        } else {
                            Toast.makeText(context, "No existen prospectos de ese tipo", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("ProspectosFragment", e.toString());
                        FirebaseCrash.log("Error ProspectosDB");
                        FirebaseCrash.report(e);
                    }
                }
            }
        });

        btnModalCancelarBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogModalBusqueda.dismiss();
            }
        });


    }

    //Método que abre el folder y muestra lo que hay dentro.
    public void openDetails(View coverView, ProspectosDB item) {

        idProspecto = item.getId();
        nombreProspecto = item.getObra() + " - " + item.getCliente();

        titleMapa = item.getObra() + " - " + item.getCliente();

        double latitudDouble = 0;
        double longitudDouble = 0;

        if (item.getLatitud() != null) {
            if (!item.getLatitud().equals("")) {
                latitudDouble = Double.parseDouble(item.getLatitud());
            }
        }
        if (item.getLongitud() != null) {
            if (!item.getLongitud().equals("")) {
                longitudDouble = Double.parseDouble(item.getLongitud());
            }
        }

        latLngMapa = new LatLng(latitudDouble, longitudDouble);

        txt_folder_nombre_prospecto.setText(nombreProspecto);
        txt_folder_clasificacion_prospecto.setText(item.getDescripcionTipoProspecto());
        txt_folder_direccion.setText(item.getCalle() + " " + item.getNumero()
                + "\n" + item.getColonia() + " " + item.getCodigoPostal()
                + "\n" + item.getNombrePais() + ", " + item.getNombreEstado()
                + "\n" + item.getComentariosUbicacion());

        /*try {
            //Se convierte la imagen en Base64 a array de bytes.
            byte[] imageByteArray = Base64.decode(item.getFotografia(), Base64.DEFAULT);
            //Se coloca la imagen en bytes dentro del ImageView.
            Glide.with(context)
                    .load(imageByteArray)
                    .asBitmap()
                    .placeholder(R.drawable.avatar_prospecto)
                    .error(R.drawable.avatar_prospecto)
                    .fitCenter()
                    .into(img_folder_imagen);
        } catch (Exception e) {
            Log.e("ProspectosFragmentERROR", e.toString());
        }*/
        Glide.with(context)
                .load(Url.URL_WEBSERVICE + Url.getArchivoProspecto + item.getFotografia())
                .placeholder(R.drawable.avatar_prospecto)
                .error(R.drawable.avatar_prospecto)
                .fitCenter()
                .into(img_folder_imagen);

        //Revisa si está descartado para poner el ícono correspondiente.
        if (item.isEstaDescartado()) {
            Log.d("", "");
            Glide.with(context)
                    .load(R.drawable.status_descarted)
                    .into(img_folder_icono);
        } else { //Es necesario poner la excepción para que el ListView no coloque los íconos en posiciones indebidas.
            if (item.getEstatusAgenda() == Valores.ID_ACTIVIDAD_REAGENDADA) { //En caso de no ser descartada, colocar ícono de reagendado si es necesario.
                Glide.with(context)
                        .load(R.drawable.status_reschedule)
                        .into(img_folder_icono);
            } else {
                Glide.with(context)
                        .load(R.drawable.btn_add_photo) //Ícono en blanco para indicar que no tiene ningún estatus particular.
                        .into(img_folder_icono);
            }
        }

        //Abre el folder y muestra el contenido.
        mUnfoldableView.unfold(coverView, mDetailsLayout);

        //Muestra la capa oscura de fondo.
        relative_capa_fondo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClickProspectos(View view, ProspectosDB item) {
        openDetails(view, item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_folder_mapa) {

            /*//Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra("prospectosFragment", "verMapa");
            startActivity(intent);*/

            Intent intent = new Intent(context, MapsActivity.class);
            intent.putExtra(Valores.MAPA_TITLE, titleMapa);
            intent.putExtra(Valores.MAPA_LATLNG, latLngMapa);
            startActivity(intent);

        } else if (v.getId() == R.id.btn_folder_informacion_obra) {

            Intent intent = new Intent(context, ObraDetalle.class);
            intent.putExtra(Valores.OBRA_ID_PROSPECTO, idProspecto);
            startActivity(intent);

        } else if (v.getId() == R.id.btn_folder_contactos) {

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PROSPECTOS_CONTACTOS);
            intent.putExtra(Valores.CONTACTOS_ID_PROSPECTO, idProspecto);
            intent.putExtra(Valores.CONTACTOS_NOMBRE_PROSPECTO, nombreProspecto);
            startActivity(intent);

        } else if (v.getId() == R.id.btn_folder_agendar_cita) {

            ProspectosDB prospectoDB = ProspectosRealm.mostrarProspectoId(idProspecto);

            if (prospectoDB != null) {
                if (prospectoDB.isEstaDescartado()) {
                    Toast.makeText(context, getString(R.string.plan_prospecto_descartado), Toast.LENGTH_LONG).show();
                } else {
                    if (prospectoDB.getIdActividad() != Valores.ID_ACTIVIDAD_CERRAR_VENTA) {
                        //Se obtienen los productos y servicios del prospecto seleccionado.
                        obtenerProductosYServicios(prospectoDB);

                    } else {
                        Toast.makeText(context, getString(R.string.plan_prospecto_finalizado), Toast.LENGTH_LONG).show();
                    }
                }
            }

        } else if (v.getId() == R.id.btn_folder_archivos) {

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PROSPECTOS_ARCHIVOS);
            editor.putString(Valores.CONTACTOS_ID_PROSPECTO, idProspecto);
            editor.commit();

            startActivity(intent);

        } else if (v.getId() == R.id.btn_folder_cerrar) {
            if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
                mUnfoldableView.foldBack();
            }
        } //Botones correspondientes a las modales.
        else if (v.getId() == R.id.btn_modal_cerrar) {
            dialogModal.dismiss();
        } else if (v.getId() == R.id.layout_modal_prospeccion) {
            filtroListaProspectos(1);
            dialogModal.dismiss();
        } else if (v.getId() == R.id.layout_modal_indagar) {
            filtroListaProspectos(2);
            dialogModal.dismiss();
        } else if (v.getId() == R.id.layout_modal_calificar) {
            filtroListaProspectos(3);
            dialogModal.dismiss();
        } else if (v.getId() == R.id.layout_modal_presentar_propuesta) {
            filtroListaProspectos(4);
            dialogModal.dismiss();
        } else if (v.getId() == R.id.layout_modal_cerrar_venta) {
            filtroListaProspectos(5);
            dialogModal.dismiss();
        } else if (v.getId() == R.id.btnCancelarBusquedaProspectos) {
            dialogModalBusqueda.dismiss();
        }
    }

    /** SE OBTIENEN LOS PRODUCTOS Y SERVICIOS DEL PROSPECTO SELECCIONADO **/
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
                    } else {
                        Toast.makeText(context, getString(R.string.citas_prospecto_fail), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                    Log.e("Oportunidad Venta", t.toString());
                    FirebaseCrash.log("Error OV");
                    FirebaseCrash.report(t);

                    Toast.makeText(context, getString(R.string.citas_prospecto_fail), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
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

    public void filtroListaProspectos(int idPaso) {


        SemaforoDB semaforoDB = SemaforoRealm.mostrarSemaforoIdPaso(idPaso);

        if (semaforoDB != null) {
            String []split = semaforoDB.getEstatusIncluidos().split("\\.");

            //Se obtienen los registros de la tabla.
            List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarFiltroProspectoIdEstatus(split);

            if (listaProspectosDB.size() > 0) {

                //Muestra el total de prospectos.
                txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                        + " " + listaProspectosDB.size());

                AdapterProspectos adapterProspectos = new AdapterProspectos(context, onClickProspectos, listaProspectosDB);
                listView.setAdapter(adapterProspectos);

                adapterProspectos.notifyDataSetChanged();
            } else {
                Toast.makeText(context, getString(R.string.plan_error_busqueda), Toast.LENGTH_LONG).show();
            }
        }

        /*
        //Se obtienen los registros de la tabla.
        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarFiltroProspecto(idPaso);

        if (listaProspectosDB.size() > 0) {

            //Muestra el total de prospectos.
            txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                    + " " + listaProspectosDB.size());

            AdapterProspectos adapterProspectos = new AdapterProspectos(context, onClickProspectos, listaProspectosDB);
            listView.setAdapter(adapterProspectos);

            adapterProspectos.notifyDataSetChanged();
        } else {
            Toast.makeText(context, getString(R.string.plan_error_busqueda), Toast.LENGTH_LONG).show();
        }*/
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

    private void obtenerSemaforo() {

        JSONfiltro jsonfiltro = new JSONfiltro();

        jsonfiltro.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));

        getJsonSemaforo = apiInterface.getSemaforo(jsonfiltro);

        getJsonSemaforo.enqueue(new Callback<JsonSemaforo>() {
            @Override
            public void onResponse(Call<JsonSemaforo> call, Response<JsonSemaforo> response) {
                if (response.body() != null && response.code() == 200) {

                    List<SemaforoDB> semaforoDBList = new ArrayList<>();
                    SemaforoDB semaforoDB;
                    String estatusIncluidos;

                    for (int i = 0; i < response.body().getReporte().size(); i++) {

                        semaforoDB = new SemaforoDB();

                        semaforoDB.setIdPaso(response.body().getReporte().get(i).getIdPaso());
                        semaforoDB.setTotalPaso(response.body().getReporte().get(i).getTotal());
                        estatusIncluidos = "";

                        for (Long estatus : response.body().getReporte().get(i).getEstatusIncluidos()) {
                            estatusIncluidos = estatusIncluidos + estatus + ".";
                        }

                        semaforoDB.setEstatusIncluidos(estatusIncluidos);

                        semaforoDBList.add(semaforoDB);
                    }

                    SemaforoRealm.guardarListaSemaforo(semaforoDBList);

                    obtenerProspectosFiltro();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    obtenerProspectosFiltro();
                }
            }

            @Override
            public void onFailure(Call<JsonSemaforo> call, Throwable t) {
                if (!getJsonSemaforo.isCanceled()) {
                    Log.e("GETSEMAFORO", t.toString());
                    FirebaseCrash.log("Error SemaforoDB");
                    FirebaseCrash.report(t);

                    obtenerProspectosFiltro();
                }
            }
        });

    }

    /************
     * OBTENCIÓN DE LOS DATOS DEL PROSPECTO, CONTACTOS Y ACTIVIDADES
     ***************/
    private void obtenerProspectosFiltro() {

        //Se muestra el círculo de progreso.
        visibilidad(View.VISIBLE);

        final JSONfiltro jsonfiltro = new JSONfiltro();

        jsonfiltro.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
        if (ProspectosRealm.ultimafechaAltaProspecto() != 0) {
//            jsonfiltro.setFechaAltaProspecto(ProspectosRealm.ultimafechaAltaProspecto());
            jsonfiltro.setFechaAltaProspecto(0);
        }

        getProspectosFiltro = apiInterface.getProspectoFiltro(jsonfiltro);

        getProspectosFiltro.enqueue(new Callback<List<Json>>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
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
                            prospectosDB.setFechaAltaProspecto(jsonList.get(i).getFechaAltaProspecto());
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

                    mostrarListaProspectos();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    //Se quita el círculo de progreso.
                    visibilidad(View.GONE);
                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();

                    mostrarListaProspectos(); //Se muestran los prospectos del dispositivo.
                }
            }

            @Override
            public void onFailure(Call<List<Json>> call, Throwable t) {
                if (!getProspectosFiltro.isCanceled()) {
                    Log.e("GETPROSPECTOS", t.toString());
                    FirebaseCrash.log("Error ProspectosDB");
                    FirebaseCrash.report(t);
                    //Se quita el círculo de progreso.
                    visibilidad(View.GONE);
                    Toast.makeText(context, getString(R.string.prospectos_cargar_fail), Toast.LENGTH_LONG).show();

                    mostrarListaProspectos(); //Se muestran los prospectos del dispositivo.
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

    /** MÉTODO QUE LLENA LA TABLA DE PLANSEMANALDB **/
    public void obtenerActividadProspectos() {

        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectosConActividad();
        ArrayList<PlanSemanalDB> listaPlanSemanal = new ArrayList<>();
        PlanSemanalDB planSemanalDB;
        for(ProspectosDB p : listaProspectosDB) {
            for(int i = 0;i < p.getActividadesDBRealmList().size(); i++) {
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

    /********************************************************************************************/

    public void descargaCliente() {

        final Call<List<ClienteDB>> getJsonClienteCall = apiInterface.getCatalogoCliente();

        getJsonClienteCall.enqueue(new Callback<List<ClienteDB>>() {
            @Override
            public void onResponse(Call<List<ClienteDB>> call, Response<List<ClienteDB>> response) {
                if (response.body() != null && response.code() == 200 && response.body().size() > 0) {

                    List<ClienteDB> catalogo = response.body();

                    //Se borra lo que hay en la tabla y se obtiene nuevamente.
                    CatalogoClienteRealm.eliminarTablaCliente();
                    CatalogoClienteRealm.guardarListaCliente(catalogo);


                    descargaObra();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    descargaObra();

                }
            }

            @Override
            public void onFailure(Call<List<ClienteDB>> call, Throwable t) {

                Log.d("Cliente", t.toString());
                FirebaseCrash.log("Descarga Cliente");
                FirebaseCrash.report(t);

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

                    //Se borra lo que hay en la tabla y se obtiene nuevamente.
                    CatalogoObraRealm.eliminarTablaObra();
                    CatalogoObraRealm.guardarListaObra(catalogo);

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                }
            }

            @Override
            public void onFailure(Call<List<ObraDB>> call, Throwable t) {
                Log.d("Obra", t.toString());
                FirebaseCrash.log("Descarga Obra");
                FirebaseCrash.report(t);
            }
        });

    }


    public void mostrarListaProspectos() {

        //Se actualizan los clientes y las obras.
        descargaCliente();

        //Muestra el semáforo
        showModalProspectos();

        //Se guarda la información de PlanSemanal para obtener las citas y poder configurar las alarmas.
        obtenerActividadProspectos();

        //Se configuran las alarmas de las actividades obtenidas.
        AlarmasActividades alarmasActividades = new AlarmasActividades(context);
        alarmasActividades.configurarAlarmasActividades();

        //Se quita el círculo de progreso.
        visibilidad(View.GONE);

        try {
            //Se obtienen los registros de la tabla.
            List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectos();

            if (listaProspectosDB.size() > 0) {
                //Muestra el total de prospectos.
                txt_prospectos_total.setText(getResources().getString(R.string.prospectos_total_prospectos)
                        + " " + listaProspectosDB.size());

                listView.setAdapter(new AdapterProspectos(context, this, listaProspectosDB));
            }
        } catch (Exception e) {
            Log.e("ProspectosFragment", e.toString());
            FirebaseCrash.log("Error ProspectosDB");
            FirebaseCrash.report(e);
        }
    }

    public void visibilidad(int visibility) {
        progressBar.setVisibility(visibility);
    }

    /**
     * MÉTODO QUE CANCELA TODAS LAS PETICIONES QUE ESTÉN EN CURSO AL MOMENTO DE SALIR DE LA PANTALLA
     **/
    public void cancelAllRequests() {

        if (getProspectosFiltro != null) {
            getProspectosFiltro.cancel();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_prospectos, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            //Ejecuta el método onBackPressed() de la actividad madre.
            Funciones.onBackPressedFunction(context, true);
            cancelAllRequests(); //Cancela todas las peticiones en curso.

            return true;
        } else if (item.getItemId() == R.id.menu_prospectos_buscar) {
            dialogModalBusqueda.show();
        } else if (item.getItemId() == R.id.menu_prospectos_status) {
            dialogModal.show();
        } else if (item.getItemId() == R.id.menu_prospectos_nuevo) {

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PROSPECTOS);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        //Se borra la llave "viewPagerContactos" para que el viewPager de Prospectos se inicialice bien.
        //Se borra la llave que tiene la información de alta del último prospecto creado.
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.remove(Valores.SHAREDPREFERENCES_INFORMACION_CONTACTO_VIEWPAGER);
        tinyDB.remove(Valores.SHAREDPREFERENCES_INFORMACION_GENERAL);
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

                        if (mUnfoldableView != null && (mUnfoldableView.isUnfolded() || mUnfoldableView.isUnfolding())) {
                            mUnfoldableView.foldBack();

                        } else {
                            //Ejecuta el método onBackPressed() de la actividad madre.
                            Funciones.onBackPressedFunction(context, true);
                            cancelAllRequests(); //Cancela todas las peticiones en curso.
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }
}


