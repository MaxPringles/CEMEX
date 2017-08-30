package com.telstock.tmanager.cemex.modulocitas;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.bumptech.glide.Glide;
import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.telstock.tmanager.cemex.modulocitas.funciones.TinyDB;
import com.telstock.tmanager.cemex.modulocitas.model.JSONfiltro;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiClient;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiInterface;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import io.realm.RealmList;
import mx.com.tarjetasdelnoreste.realmdb.ActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoActividadesPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonGlobalProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.actividades.MapsActivity;
import mx.com.tarjetasdelnoreste.realmdb.actividades.ObraDetalle;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlarmasActividades;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.ActividadesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonGlobalProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Actividade;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Actividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.ActividadAnterior;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OfertaIntegral;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.TipoActividad;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PrincipalCitasFragment extends Fragment implements View.OnClickListener{

    Context context;

    //Objetos del Foldable.
    private View mListTouchInterceptor; //Objeto que maneja la imagen.
    private View mDetailsLayout; //Layout con el contenido del folder al abrirse.
    private UnfoldableView mUnfoldableView; //Objeto que maneja las imágenes como folders.

    //Elementos dentro del folder.
    private ImageView imgFolderImagen;
    private ImageView imgFolderIcono;
    private TextView txtFolderNombreProspecto;
    private TextView txtFolderClasificacionProspecto;
    private TextView txtFolderReloj;
    private TextView txtFolderActividad;
    private TextView txtFolderDireccion;
    private TextView txtTotalCitas;
    private LinearLayout btn_folder_mapa;
    private LinearLayout btn_folder_contactos;
    private LinearLayout btn_folder_agendar_cita;
    private LinearLayout btn_folder_archivos;
    private LinearLayout btn_folder_cerrar;
    private LinearLayout btn_folder_iniciar_actividad;
    private LinearLayout btn_folder_informacion_obra;

    private ExpandingList expandingListMain;
    private ExpandingItem item;
    ProgressBar progressBar;
    //Muestra la capa de fondo cuando se abre el folder en módulo de Prospectos.
    private RelativeLayout relativeCapaFondo;

    //Variables para colocar el título y coordenadas del mapa en forma adecuada.
    private String titleMapa;
    private LatLng latLngMapa;

    private String idProspecto;
    private String nombreProspecto;
    private int idStatus;
    private long idEstatusProspecto;
    private int idTipoProspecto;
    private int subsegmento;
    private String nombreActividadActual;
    private int idActividad;
    private String idActividadActual;
    private boolean estaDescartado;
    private int estatusAgenda;

    OfertaIntegral ofertaIntegral;

    PlanSemanalDB planSemanalDBCopia = new PlanSemanalDB();

    ApiInterface apiInterface;

    //Se declara la SharedPreferences.
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    Toolbar toolbar;
    View view;

    Call<List<Json>> getProspectosFiltro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_citas_principal, container, false);

        context = getActivity();
        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        //Se inicializa la interface de Retrofit.
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = prefs.edit();

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);
        ((AppCompatActivity)context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)context).getSupportActionBar().setTitle("Citas/Visitas");

        imgFolderImagen = (ImageView) view.findViewById(R.id.img_folder_imagen);
        imgFolderIcono = (ImageView) view.findViewById(R.id.img_folder_icono);
        txtFolderNombreProspecto = (TextView) view.findViewById(R.id.txt_folder_nombre_prospecto);
        txtFolderClasificacionProspecto = (TextView) view.findViewById(R.id.txt_folder_clasificacion_prospecto);
        txtFolderReloj = (TextView) view.findViewById(R.id.txt_folder_reloj);
        txtFolderActividad = (TextView) view.findViewById(R.id.txt_folder_actividad);
        txtFolderDireccion = (TextView) view.findViewById(R.id.txt_folder_direccion);
        btn_folder_mapa = (LinearLayout) view.findViewById(R.id.btn_folder_mapa);
        btn_folder_contactos = (LinearLayout) view.findViewById(R.id.btn_folder_contactos);
        btn_folder_agendar_cita = (LinearLayout) view.findViewById(R.id.btn_folder_agendar_cita);
        btn_folder_archivos = (LinearLayout) view.findViewById(R.id.btn_folder_archivos);
        btn_folder_cerrar = (LinearLayout) view.findViewById(R.id.btn_folder_cerrar);
        btn_folder_iniciar_actividad = (LinearLayout) view.findViewById(R.id.btn_folder_iniciar_actividad);
        btn_folder_informacion_obra = (LinearLayout) view.findViewById(R.id.btn_folder_informacion_obra);
        txtTotalCitas = (TextView) view.findViewById(R.id.txt_citas_total);

        txtTotalCitas.setText(getResources().getString(R.string.prospectos_total_citas_visitas)
                + " 0");

        //Se colocan los listeners de los botones/LinearLayouts.
        btn_folder_mapa.setOnClickListener(this);
        btn_folder_contactos.setOnClickListener(this);
        btn_folder_agendar_cita.setOnClickListener(this);
        btn_folder_archivos.setOnClickListener(this);
        btn_folder_cerrar.setOnClickListener(this);
        btn_folder_iniciar_actividad.setOnClickListener(this);
        btn_folder_informacion_obra.setOnClickListener(this);

        //Muestra la capa de fondo cuando se abre el folder de Citas.
        relativeCapaFondo = (RelativeLayout) view.findViewById(R.id.relative_capa_fondo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        expandingListMain = (ExpandingList) view.findViewById(R.id.expanding_list_main);

        Bundle costal = getActivity().getIntent().getExtras();
        String nuevo="";
        prepararFolder();
        if (costal != null)
        {
            nuevo= costal.getString(Valores.FRAGMENT_GENERAL_MOSTRAR,"");
        }
        if(nuevo.equals(Valores.FRAGMENT_CITAS_VISITAS))
        {
            sincronizarCitas();
        }else
            {
                obtenerProspectosFiltro();
            }

        return view;
    }

    public void prepararFolder() {

        mListTouchInterceptor = view.findViewById(R.id.touch_interceptor_view);
        mListTouchInterceptor.setClickable(false);

        //Se oculta el layout con el contenido del folder, ya que en un principio
        //todos los folders estarán cerrados.
        mDetailsLayout = view.findViewById(R.id.details_layout);
        mDetailsLayout.setVisibility(View.INVISIBLE);

        //mUnfoldableView = Views.find(this, R.id.unfoldable_view);
        mUnfoldableView = (UnfoldableView) view.findViewById(R.id.unfoldable_view);

        /*try {
            //Código que muestra una línea blanca sobre el folder para aumentar el realismo al abrirse.
            Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
            mUnfoldableView.setFoldShading(new GlanceFoldShading(glance));
        } catch(Exception e) {
            Log.e("Error","Memoria");
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
                relativeCapaFondo.setVisibility(View.GONE);
            }
        });
    }

    //Método que abre el folder y muestra lo que hay dentro.
    public void openDetails(View coverView, PlanSemanalDB planSemanalDB, String horario) {

        idProspecto = planSemanalDB.getIdProspecto();
        nombreProspecto = planSemanalDB.getObra() + " - " + planSemanalDB.getCliente();
        idStatus = planSemanalDB.getIdStatus();
        idEstatusProspecto = planSemanalDB.getIdEstatusProspecto();
        idTipoProspecto = planSemanalDB.getIdTipoProspecto();
        subsegmento = planSemanalDB.getIdSubSegmentoProspecto();
        nombreActividadActual = planSemanalDB.getDescripcionObra();
        idActividad = planSemanalDB.getIdActividad();
        idActividadActual = planSemanalDB.getIdActividadAnterior();
        estaDescartado = planSemanalDB.isEstaDescartado();
        estatusAgenda = planSemanalDB.getEstatusAgenda();

        titleMapa = planSemanalDB.getObra() + " - " + planSemanalDB.getCliente();

        double latitudDouble = 0;
        double longitudDouble = 0;

        if (planSemanalDB.getLatitud() != null) {
            if (!planSemanalDB.getLatitud().equals("")) {
                latitudDouble = Double.parseDouble(planSemanalDB.getLatitud());
            }
        }
        if (planSemanalDB.getLongitud() != null) {
            if (!planSemanalDB.getLongitud().equals("")) {
                longitudDouble = Double.parseDouble(planSemanalDB.getLongitud());
            }
        }

        latLngMapa = new LatLng(latitudDouble, longitudDouble);

        txtFolderNombreProspecto.setText(nombreProspecto);
        txtFolderClasificacionProspecto.setText(planSemanalDB.getDescripcionTipoP());
        txtFolderReloj.setText(horario);
        txtFolderActividad.setText(planSemanalDB.getDescripcionObra());
        txtFolderDireccion.setText(planSemanalDB.getCalle() + " " + planSemanalDB.getNumero()
                + "\n" + planSemanalDB.getColonia() + " " + planSemanalDB.getCodigoPostal()
                + "\n" + planSemanalDB.getComentariosUbicacion());

        //Coloca las imágenes dentro del folder.
        colocarImagen(planSemanalDB.getImagen(), imgFolderImagen);
        colocarIcono(planSemanalDB.isEstaDescartado(), planSemanalDB.getEstatusAgenda(), imgFolderIcono);

        mUnfoldableView.unfold(coverView, mDetailsLayout);

        //Muestra la capa oscura de fondo.
        relativeCapaFondo.setVisibility(View.VISIBLE);
    }

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
                planSemanalDB.setEstatusAgenda(p.getEstatusAgenda());

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
                planSemanalDB.setIdTipoProspecto((int) p.getIdTipoProspecto());

                planSemanalDB.setIdEstatusProspecto(p.getIdEstatusProspecto());

                listaPlanSemanal.add(planSemanalDB);
            }
        }

        PlanSemanalRealm.guardarListaPlanSemanal(listaPlanSemanal);

        prepararExpandableList();
    }

    public void prepararExpandableList() {

        //Se configuran las alarmas de las actividades obtenidas.
        AlarmasActividades alarmasActividades = new AlarmasActividades(context);
        alarmasActividades.configurarAlarmasActividades();

        List<PlanSemanalDB> planSemanalDB;
        planSemanalDB = PlanSemanalRealm.mostrarListaPlanSemanalUltimasCitas();

        int totalCitas = planSemanalDB.size();

        if (planSemanalDB.size() > 0) {

            ArrayList<String> fechasDistintas = new ArrayList<>();
            //Ciclo que obtiene las fechas ya filtradas y depuradas. Esto para obtener
            //despues cuantas citas hay en cada dia.
            for (PlanSemanalDB fechas : planSemanalDB) {
                fechasDistintas.add(fechas.getHoraInicio().substring(0, 10));
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Calendar horaInicio;
            Calendar horaFin;
            String horario;
            String mesDistinto = ""; //Variable que compara el mes actual y el siguiente.
            //Bandera que indica si el mes actual y siguiente son distintos (de acuerdo a ello,
            //se colocará o no una nueva sección de mes).
            String diaDistinto; //Variable que ayuda a identificar cuantas citas hay en el dia especificado.
            boolean flagMesDistinto = true;
            int numeroCitas;
            View subItems;

            for (int i = 0; i < planSemanalDB.size(); i++) {

                mesDistinto = planSemanalDB.get(i).getHoraInicio().substring(5, 7);
                diaDistinto = planSemanalDB.get(i).getHoraInicio().substring(0, 10);

                //Sí es true, crea una nueva sección de mes.
                if (flagMesDistinto) {
                    flagMesDistinto = false;

                    colocarSeccion(planSemanalDB.get(i).getHoraInicio());
                }

                //Se indica que se creará una nueva lista (correspondiente a un día distinto).
                item = expandingListMain.createNewItem(R.layout.expanding_layout);

                //Se obtiene el número de citas que hay en el día especificado.
                numeroCitas = Collections.frequency(fechasDistintas, diaDistinto);
                //Se especifica el número de items que tendrá la cabecera (citas que habrá en un día específico).
                item.createSubItems(numeroCitas);

                //Se llena la cabecera con el día específico.
                ((TextView) item.findViewById(R.id.title)).setText(formatoFecha(planSemanalDB.get(i).getHoraInicio(), false));

                /** Ciclo que recorre todas las citas del día y las muestra en la lista
                 * NOTA: Se usara la posicion (j+i), ya que se esta accediendo a la lista general,
                 * por lo que se tiene que sumar la posicion actual mas la nueva posicion a acceder **/
                for (int j = 0; j < numeroCitas; j++) {
                    subItems = item.getSubItemView(j);

                    //Se obtiene el horario de la cita.
                    horaInicio = Calendar.getInstance();
                    horaFin = Calendar.getInstance();
                    try {
                        horaInicio.setTime(format.parse(planSemanalDB.get(j + i).getHoraInicio()));
                        horaFin.setTime(format.parse(planSemanalDB.get(j + i).getHoraFin()));

                        horario = colocarHora(horaInicio, horaFin);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        horario = "N/A";
                    }

                    //Se pinta las imágenes y los valores en los items de la lista.
                    colocarImagen(planSemanalDB.get(j + i).getImagen(),
                            (ImageView) subItems.findViewById(R.id.img_folder_imagen));
                    colocarIcono(planSemanalDB.get(j + i).isEstaDescartado(),
                            planSemanalDB.get(j + i).getEstatusAgenda(),
                            (ImageView) subItems.findViewById(R.id.img_folder_icono));

                    ((TextView) subItems.findViewById(R.id.txt_clasificacion_prospecto))
                            .setText(planSemanalDB.get(j + i).getDescripcionTipoP());
                    ((TextView) subItems.findViewById(R.id.txt_hora_prospecto))
                          .setText(horario);
                    ((TextView) subItems.findViewById(R.id.txt_nombre_prospecto))
                            .setText(planSemanalDB.get(j + i).getObra() + " - " + planSemanalDB.get(j + i).getCliente());
                    ((TextView) subItems.findViewById(R.id.txt_tarea_prospecto))
                            .setText(planSemanalDB.get(j + i).getDescripcionObra());

                    planSemanalDBCopia = new PlanSemanalDB();
                    planSemanalDBCopia.setObra(planSemanalDB.get(j + i).getObra());
                    planSemanalDBCopia.setCliente(planSemanalDB.get(j + i).getCliente());
                    planSemanalDBCopia.setLatitud(planSemanalDB.get(j + i).getLatitud());
                    planSemanalDBCopia.setLongitud(planSemanalDB.get(j + i).getLongitud());
                    planSemanalDBCopia.setIdEstatusProspecto(planSemanalDB.get(j + i).getIdEstatusProspecto());
                    planSemanalDBCopia.setDescripcionTipoP(planSemanalDB.get(j + i).getDescripcionTipoP());
                    planSemanalDBCopia.setCalle(planSemanalDB.get(j + i).getCalle());
                    planSemanalDBCopia.setNumero(planSemanalDB.get(j + i).getNumero());
                    planSemanalDBCopia.setColonia(planSemanalDB.get(j + i).getColonia());
                    planSemanalDBCopia.setCodigoPostal(planSemanalDB.get(j + i).getCodigoPostal());
                    planSemanalDBCopia.setComentariosUbicacion(planSemanalDB.get(j + i).getComentariosUbicacion());
                    planSemanalDBCopia.setImagen(planSemanalDB.get(j + i).getImagen());
                    planSemanalDBCopia.setIdActividad(planSemanalDB.get(j + i).getIdActividad());
                    planSemanalDBCopia.setEstatusAgenda(planSemanalDB.get(j + i).getEstatusAgenda());
                    planSemanalDBCopia.setDescripcionObra(planSemanalDB.get(j + i).getDescripcionObra());

                    planSemanalDBCopia.setIdProspecto(planSemanalDB.get(j + i).getIdProspecto());
                    planSemanalDBCopia.setIdStatus(planSemanalDB.get(j + i).getIdStatus());
                    planSemanalDBCopia.setIdTipoProspecto(planSemanalDB.get(j + i).getIdTipoProspecto());
                    planSemanalDBCopia.setIdSubSegmentoProspecto(planSemanalDB.get(j + i).getIdSubSegmentoProspecto());
                    planSemanalDBCopia.setIdActividadAnterior(planSemanalDB.get(j + i).getIdActividadAnterior());
                    planSemanalDBCopia.setEstaDescartado(planSemanalDB.get(j + i).isEstaDescartado());

                    //Se asignan los eventos onClick y se pasan parámetros que se mostrarán en el folder abierto.
                    configureSubItem(subItems, planSemanalDBCopia, horario);
                }

                //Se suman las citas del dia al conteo general. Recordando que se esta accediendo
                //a la lista general de fechas, NO a una lista independiente por dia.
                i = i + (numeroCitas - 1);

                //En caso de que el siguiente registro corresponda a un mes distinto, entonces
                //se activa la bandera, así se sabe que se debe colocar una nueva sección.
                try {
                    if (!mesDistinto.equals(planSemanalDB.get(i+1).getHoraInicio().substring(5, 7))) { //Verifica que sean distintos los meses.
//                        mesDistinto = fechasPlanSemanal.get(i+1).substring(5, 7); //Se guarda el nuevo valor a comparar.
                        flagMesDistinto = true;
                    }
                } catch (Exception e) { //Excepción controlada, para cuando el índice "i+1" no exista.

                }

                //Se indica el color e ícono de la cabecera.
                item.setIndicatorColorRes(R.color.colorAzulElectrico);
                item.setIndicatorIconRes(R.drawable.btn_arrow_down);
            }
            //Se quita el ProgressDialog.
            visibilidad(View.GONE);

            txtTotalCitas.setText(getResources().getString(R.string.prospectos_total_citas_visitas)
                    +" " + totalCitas);


        } else {
            Toast.makeText(context, "No hay citas disponibles", Toast.LENGTH_SHORT).show();

            txtTotalCitas.setText(getResources().getString(R.string.prospectos_total_citas_visitas)
                    +" 0");
            //Se quita el ProgressDialog.
            visibilidad(View.GONE);
        }
    }

    /** MÉTODO QUE ELIMINA LAS FECHAS (DÍAS) DUPLICADAS Y LOS DEVUELVE ORDENADOS DE FORMA ASCENDENTE **/
    public List<String> filtrarOrdenarFechas(List<PlanSemanalDB> planSemanalDB) {

        List<String> fechasPlanSemanal = new ArrayList<>();

        //Se obtiene únicamente "yyyy-MM-dd", esto para no considerar citas con el mismo día,
        //pero horario distinto.
        for (int i = 0; i < planSemanalDB.size(); i++) {
            fechasPlanSemanal.add(planSemanalDB.get(i).getHoraInicio().substring(0, 10));
        }

        //Elimina las fechas duplicadas.
        HashSet<String> hashSet = new HashSet<>();
        hashSet.addAll(fechasPlanSemanal);
        fechasPlanSemanal.clear();
        fechasPlanSemanal.addAll(hashSet);

        //Ordena de menor a mayor las fechas.
        Collections.sort(fechasPlanSemanal);

        return fechasPlanSemanal;
    }

    /** MÉTODO QUE CUSTOMIZA LAS FECHAS PARA MOSTRARLAS TANTO EN LAS CABECERAS COMO EN LAS SECCIONES **/
    public String formatoFecha(String fechaDB, boolean isSeccion) {

        //NOTA: Para customizar la fecha, primero se pasa el String recibido a Date, una vez
        //estando en Date, se da el formato deseado y se regresa nuevamente a String.

        SimpleDateFormat stringToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateToString;
        if (isSeccion) {
            dateToString = new SimpleDateFormat("MMMM, yyyy"); //Formato dado a las secciones.
        } else {
            dateToString = new SimpleDateFormat("dd EEEE"); //Formato dado a las cabeceras.
        }

        Calendar calendar = Calendar.getInstance();

        try {
            //Se pasa el string a tipo Date.
            Date date = stringToDate.parse(fechaDB);
            calendar.setTime(date);

            //Se da el formato deseado y se regresa nuevamente a String.
            return dateToString.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return "Fecha Desconocida";
        }
    }

    /** MÉTODO QUE CUSTOMIZA EL HORARIO DE VISITA **/
    public String colocarHora(Calendar calendarInicio, Calendar calendarFin) {

        String horaInicio;
        String minutoInicio;
        String horaFin;
        String minutoFin;

        if (calendarInicio.get(Calendar.HOUR_OF_DAY) >= 10) {
            horaInicio = calendarInicio.get(Calendar.HOUR_OF_DAY) + "";
        } else {
            horaInicio = "0" + calendarInicio.get(Calendar.HOUR_OF_DAY);
        }

        if (calendarInicio.get(Calendar.MINUTE) >= 10) {
            minutoInicio = calendarInicio.get(Calendar.MINUTE) + "";
        } else {
            minutoInicio = "0" + calendarInicio.get(Calendar.MINUTE);
        }

        if (calendarFin.get(Calendar.HOUR_OF_DAY) >= 10) {
            horaFin = calendarFin.get(Calendar.HOUR_OF_DAY) + "";
        } else {
            horaFin = "0" + calendarFin.get(Calendar.HOUR_OF_DAY);
        }

        if (calendarFin.get(Calendar.MINUTE) >= 10) {
            minutoFin = calendarFin.get(Calendar.MINUTE) + "";
        } else {
            minutoFin = "0" + calendarFin.get(Calendar.MINUTE);
        }

        return horaInicio + ":" + minutoInicio + " - " + horaFin + ":" + minutoFin;
    }

    /** MÉTODO QUE COLOCA UNA NUEVA SECCIÓN **/
    public void colocarSeccion(String fecha) {

        ExpandingItem item;
        TextView txtSeparatorLibrary;

        //Indica que se creará una nueva lista con 0 items.
        item = expandingListMain.createNewItem(R.layout.expanding_layout);
        item.createSubItems(0);

        item.findViewById(R.id.layout_header).setVisibility(View.GONE); //Se quita la cabecera para sólo dejar el separator (que funge como sección).
        item.findViewById(R.id.layout_separator).setVisibility(View.VISIBLE); //Se hace visible el separator.

        //Se coloca la fecha customizada dentro de la seccin y se indica que las letras sean mayúsculas.
        txtSeparatorLibrary = ((TextView) item.findViewById(R.id.txt_separator));
        txtSeparatorLibrary.setText(formatoFecha(fecha, true));
        txtSeparatorLibrary.setAllCaps(true);
    }

    public void colocarImagen(String stringImagen, ImageView imageView) {

        try {
            //Se coloca la imagen en bytes dentro del ImageView.
            Glide.with(context)
                    .load(Url.URL_WEBSERVICE + Url.getArchivoProspecto + stringImagen)
                    .placeholder(R.drawable.avatar_prospecto)
                    .error(R.drawable.avatar_prospecto)
                    .fitCenter()
                    .into(imageView);

        } catch (Exception e) {
            Log.e("CitasFragmentERROR", e.toString());
        }
    }

    public void colocarIcono(boolean isEstaDescartado, int estatusAgenda, ImageView imageView) {
        //Revisa si está descartado para poner el ícono correspondiente.
        if (isEstaDescartado) {
            Log.d("", "");
            Glide.with(context)
                    .load(R.drawable.status_descarted)
                    .into(imageView);
        } else { //Es necesario poner la excepción para que el ListView no coloque los íconos en posiciones indebidas.
            if (estatusAgenda == Valores.ID_ACTIVIDAD_REAGENDADA) { //En caso de no ser descartada, colocar ícono de reagendado si es necesario.
                Glide.with(context)
                        .load(R.drawable.status_reschedule)
                        .into(imageView);
            } else {
                Glide.with(context)
                        .load(R.drawable.btn_add_photo) //Ícono en blanco para indicar que no tiene ningún estatus particular.
                        .into(imageView);
            }
        }
    }

    /** MÉTODO QUE DETECTA EL EVENTO onClick de la lista y manda llamar al folder **/
    private void configureSubItem(final View view, final PlanSemanalDB planSemanalDB, final String horario) {

        view.findViewById(R.id.layout_row).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetails(view, planSemanalDB, horario);
            }
        });
    }

    /** MÉTODO QUE ESCONDE EL PROGRESSBAR Y MUESTRA EL LAYOUT CON CONTENIDO **/
    public void visibilidad(int visibility) {

        try {
            progressBar.setVisibility(visibility);
            expandingListMain.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);

        } catch (Exception e) {
            //En caso de que algún proceso ya haya escondido el progressBar antes.
            FirebaseCrash.report(e);
        }
    }

    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            cancelAllRequests();
            Funciones.onBackPressedFunction(context, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_folder_iniciar_actividad) {

            llenarObjetoParcialmente();

            TinyDB tinyDB = new TinyDB(context);

            editor.putString(Valores.SHAREDPREFERENCES_ID_PROSPECTO, idProspecto);
            editor.putInt(Valores.SHAREDPREFEREBCES_TIENE_ACTIVIDAD_ANTERIOR, estatusAgenda);
            //Se envía el tipo de prospecto para ocultar o mostrar el checkbox de oferta integral
            editor.putInt(Valores.SHAREDPREFERENCES_ID_TIPO_PROSPECTO, idTipoProspecto);
            editor.commit();

            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra("idProspecto", idProspecto);

            editor.putString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, idProspecto);
            editor.putString(Valores.SHAREDPREFERENCES_ID_ACTIVIDAD_ACTUAL, idActividadActual);
            editor.commit();

            if(!estaDescartado) {
                tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
                tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS);
                tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS);

                if (idActividad == Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO || idActividad == Valores.ID_ACTIVIDAD_CONTACTAR_CLIENTE) {
                    tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
                    tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS);
                    tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS);
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CONTACTAR_CLIENTE_PROSPECTO);
                    intent.putExtra(Valores.BUNDLE_ID_ACTIVIDAD, idActividad + "");
                    startActivity(intent);
                } else if (idActividad == Valores.ID_ACTIVIDAD_VISITAR_PROSPECTO) {
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_VISITAR_PROSPECTO);
                    startActivity(intent);
                } else if (idActividad == Valores.ID_ACTIVIDAD_RECABAR_INFORMACION) {
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_RECABAR_INFORMACION);
                    startActivity(intent);
                } else if (idActividad == Valores.ID_ACTIVIDAD_CALIFICAR_OPORTUNIDAD) {
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CALIFICAR_OPORTUNIDAD);
                    startActivity(intent);
                } else if (idActividad == Valores.ID_ACTIVIDAD_PREPARAR_PROPUESTA_DE_VALOR) {
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PREPARAR_PROPUESTA_DE_VALOR);
                    startActivity(intent);
                } else if (idActividad == Valores.ID_ACTIVIDAD_PRESENTAR_PROPUESTA) {
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PRESENTAR_PROPUESTA);
                    startActivity(intent);
                } else if (idActividad == Valores.ID_ACTIVIDAD_RECIBIR_RESPUESTA) {
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_RECIBIR_RESPUESTA);
                    startActivity(intent);
                } else if (idActividad == Valores.ID_ACTIVIDAD_NEGOCIAR_AJUSTAR_PROPUESTA) {
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CERRAR_VENTA);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, getString(R.string.plan_prospecto_finalizado), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, getString(R.string.plan_prospecto_descartado), Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.btn_folder_mapa) {

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
        }
    }

    public void llenarObjetoParcialmente() {

        //Guarda el id del prospecto seleccionado.
        PlanSemanalDB planSemanalDB = new PlanSemanalDB();
        /*planSemanalDB.setId(1+""); //Se guarda el atributo 1/12.
        planSemanalDB.setIdUsuario(1); //Se guarda el atributo 2/12.
        planSemanalDB.setIdActividad(2); //Se guarda el atributo 3/12. (se guardará en la pantalla de la actividad específica).
        planSemanalDB.setIdProspecto(idProspecto); //Se guarda el atributo 4/12.
        planSemanalDB.setDescripcion("Descripción 1"); //Se guarda el atributo 5/12.
        planSemanalDB.setIdStatus(idStatus); //Se guarda el atributo 6/12.
        planSemanalDB.setComentario("Comentario 1"); //Se guarda el atributo 7/12.
        planSemanalDB.setIdTipoProspecto(idTipoProspecto); //Se guarda el atributo 8/12.
        planSemanalDB.setIdSubSegmentoProspecto(subsegmento); //Se guarda el atributo 9/12.
        planSemanalDB.setStatus(0); //Se guarda el atributo 10/12.*/
        planSemanalDB.setId("");
        planSemanalDB.setHoraInicio("");
        planSemanalDB.setHoraFin("");
        planSemanalDB.setComentario("");
        

        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putPlanSemanalDB(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, planSemanalDB);
    }

    /** SE OBTIENEN LOS PRODUCTOS Y SERVICIOS DEL PROSPECTO SELECCIONADO **/
    public void obtenerProductosYServicios(final ProspectosDB prospectosDB) {

        //String idProspecto = prefs.getString(Valores.SHAREDPREFERENCES_ID_PROSPECTO, "");
        String idProspecto = prospectosDB.getId();
        long actividadActual;

        if (prospectosDB.getIdActividad() != 0) {
            actividadActual = prospectosDB.getIdActividad();
        } else {
            if (prospectosDB.getIdTipoProspecto() == Valores.ID_TIPO_PROSPECTO_NUEVO_PROSPECTO) {
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

    public void generarActividad(int idPaso, int idStatus, ProspectosDB prospectosDB) {

        String idProspecto = prospectosDB.getId();
        long actividadActual;

        if (prospectosDB.getIdActividad() != 0) {
            actividadActual = prospectosDB.getIdActividad();
        } else {
            if (prospectosDB.getIdTipoProspecto() == Valores.ID_TIPO_PROSPECTO_NUEVO_PROSPECTO) {
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
                            //Ejecuta el método onBackPressed() de la actividad madre (indica que no
                            // siga el flujo normal, esto hará que se cierre el folder, pero que
                            // no se cambie el fragment).
                            Funciones.onBackPressedFunction(context, false);
                        } else {
                            cancelAllRequests();
                            //Ejecuta el método onBackPressed() de la actividad madre.
                            Funciones.onBackPressedFunction(context, true);
                        }
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void obtenerProspectosFiltro() {

        //Se muestra el círculo de progreso.
        visibilidad(View.VISIBLE);

        JSONfiltro jsonfiltro = new JSONfiltro();

        jsonfiltro.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));

        if (ProspectosRealm.ultimafechaAltaProspecto() != 0) {
   //         jsonfiltro.setFechaAltaProspecto(ProspectosRealm.ultimafechaAltaProspecto());
            jsonfiltro.setFechaAltaProspecto(0);
        }

        getProspectosFiltro = apiInterface.getProspectoFiltro(jsonfiltro);

        getProspectosFiltro.enqueue(new Callback<List<Json>>() {
            @Override
            public void onResponse(Call<List<Json>> call, Response<List<Json>> response) {
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

                    obtenerActividadProspectos();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    Toast.makeText(context, getString(R.string.citas_prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                    obtenerActividadProspectos();
                }
            }

            @Override
            public void onFailure(Call<List<Json>> call, Throwable t) {
                Toast.makeText(context, getString(R.string.citas_prospectos_cargar_fail), Toast.LENGTH_LONG).show();
                obtenerActividadProspectos();
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

    /** MÉTODO QUE CANCELA TODAS LAS PETICIONES QUE ESTÉN EN CURSO AL MOMENTO DE SALIR DE LA PANTALLA **/
    public void cancelAllRequests() {
        if (getProspectosFiltro != null) {
            getProspectosFiltro.cancel();
        }
    }


    public void sincronizarCitas() {

        //Se muestra el progressDialog.
        visibilidad(View.VISIBLE);

        JSONfiltro jsonfiltro = new JSONfiltro();

        jsonfiltro.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));

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
                    visibilidad(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<List<Json>> call, Throwable t) {
                Log.e("GETPROSPECTOS", t.toString());
                FirebaseCrash.log("Error ProspectosDB");
                FirebaseCrash.report(t);

                Toast.makeText(context, getString(R.string.sincronizar_citas_fail), Toast.LENGTH_LONG).show();

                //Se quita el progressDialog.
                visibilidad(View.GONE);
            }
        });
    }


}
