package com.telstock.tmanager.cemex.modulocitas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.telstock.tmanager.cemex.modulocitas.funciones.TinyDB;
import com.telstock.tmanager.cemex.modulocitas.model.JSONdescartar;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiClient;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoActividadesPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoStatusObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.GeneralOfflineRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonGlobalProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertDialogModal;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarAlertDialog;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GeneralOfflineDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Actividad;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.ActividadAnterior;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OfertaIntegral;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.SubsegmentoProducto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by czamora on 10/7/16.
 */
public class ActividadContactarProspectoCliente1 extends Fragment implements View.OnClickListener, ComunicarAlertDialog {

    Context context;

    private LinearLayout layoutSiContactaste;
    private LinearLayout layoutNoContactaste;
    private LinearLayout layoutOfertaIntegral;
    private RadioButton radioSiContactaste;
    private RadioButton radioNoContactaste;
    private CheckBox checkboxOfertaIntegral;
    private CheckBox checkboxDescartar;
    private Button btnAgendarNuevaCitaLlamada;
    private Button btnAgendarActividad;
    private Button btnCitasRealizarActividad;
    private Button btnDescartar;
    //    private Spinner spEstatusObra;
    private LinearLayout btnSeleccionarProductosServicios;
    private LinearLayout layoutDescartar;
    private LinearLayout layoutCheckBoxOfertaIntegral;
    private EditText etxComentarioSiContactaste;
    private EditText etxComentarioNoContactaste;

    private EditText etComentariosDescartar;
    private ApiInterface apiInterface;

    //Modal que muestra mensaje de alerta.
    Dialog dialogModal;
    private static final String ID_REAGENDAR = "idReagendar";
    private static final String ID_AGENDAR_ACTIVIDAD = "idAgendarActividad";
    private static final String ID_REALIZAR_ACTIVIDAD = "idRealizarActividad";
    private static final String ID_FORMULARIO_PRODUCTO = "idFormularioProducto";
    private static final String ID_FORMULARIO_SERVICIO = "idFormularioServicio";
    private static final String ID_DESCARTAR = "idDescartar";
    private static final String ID_MARCAR_OFERTA_INTEGRAL = "idMarcarOfertaIntegral";

    SharedPreferences prefs;

    ArrayList<String> estatusObraDesc = new ArrayList<>();
    List<CatalogoStatusObraDB> estatusObraListAll = new ArrayList<>();
    ArrayAdapter<String> adapter;

    int idActividadActual = Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO;

    boolean siPuedeAgendar = true;
    String idActividad;
    boolean esOfertaIntegral = true;
    int estatusActividad = 0;
    String titulo;

    OfertaIntegral ofertaIntegral;

    Toolbar toolbar;

    int idPaso;

    int idTipoProspecto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actividad_contactar_prospecto_1, container, false);

        context = getActivity();

        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        final Bundle args = getArguments();
        idActividad = args.getString(Valores.BUNDLE_ID_ACTIVIDAD);

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

        //Se obtiene el tipo de prospecto
        idTipoProspecto = prefs.getInt(Valores.SHAREDPREFERENCES_ID_TIPO_PROSPECTO, 0);

        //Se inicializa la interfaz que contiene los métodos de WS
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (idActividad.equals(Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO + "")) {
            ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.citas1_title_contactar));
            titulo = getString(R.string.citas1_title_contactar);

        } else {
            ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.citas1_title_contactar_cliente));
            titulo = getString(R.string.citas1_title_contactar_cliente);
        }

        layoutSiContactaste = (LinearLayout) view.findViewById(R.id.layout_si_contactaste);
        layoutNoContactaste = (LinearLayout) view.findViewById(R.id.layout_no_contactaste);
        layoutOfertaIntegral = (LinearLayout) view.findViewById(R.id.layout_oferta_integral);
        radioSiContactaste = (RadioButton) view.findViewById(R.id.radioSiContactasteProspecto);
        radioNoContactaste = (RadioButton) view.findViewById(R.id.radioNoContactasteProspecto);
        btnAgendarNuevaCitaLlamada = (Button) view.findViewById(R.id.btnAgendarNuevaCitaLlamada);
        btnAgendarActividad = (Button) view.findViewById(R.id.btnAgendarActividad);
        btnCitasRealizarActividad = (Button) view.findViewById(R.id.btnRealizarActividad);
        checkboxOfertaIntegral = (CheckBox) view.findViewById(R.id.chbx_oferta_integral);
        btnSeleccionarProductosServicios = (LinearLayout) view.findViewById(R.id.btn_seleccionar_productos_servicios);
        checkboxDescartar = (CheckBox) view.findViewById(R.id.chbx_descartar);
        btnDescartar = (Button) view.findViewById(R.id.btn_descartar);
        layoutDescartar = (LinearLayout) view.findViewById(R.id.layout_descartar);
        layoutCheckBoxOfertaIntegral = (LinearLayout) view.findViewById(R.id.layout_checkbox_oferta_integral);
        etxComentarioNoContactaste = (EditText) view.findViewById(R.id.etComentariosNoContactaste);
        etxComentarioSiContactaste = (EditText) view.findViewById(R.id.etComentariosSiContactaste);
        etComentariosDescartar = (EditText) view.findViewById(R.id.etComentariosDescartar);

        radioSiContactaste.setOnClickListener(this);
        radioNoContactaste.setOnClickListener(this);
        checkboxOfertaIntegral.setOnClickListener(this);
        btnAgendarNuevaCitaLlamada.setOnClickListener(this);
        btnAgendarActividad.setOnClickListener(this);
        btnCitasRealizarActividad.setOnClickListener(this);
        btnSeleccionarProductosServicios.setOnClickListener(this);
        checkboxDescartar.setOnClickListener(this);
        btnDescartar.setOnClickListener(this);
        layoutCheckBoxOfertaIntegral.setOnClickListener(this);

        cargarSpinner();

        mostrarOfertaIntegral();

        obtenerOportunidadDeVenta();

        return view;
    }

    //Método que muestra u oculta el checkbox de oferta integral dependiendo del tipo de prospecto
    public void mostrarOfertaIntegral() {
        if (idTipoProspecto == Valores.ID_TIPO_PROSPECTO_NUEVO_PROSPECTO) {
            layoutCheckBoxOfertaIntegral.setVisibility(View.VISIBLE);
            layoutOfertaIntegral.setVisibility(View.VISIBLE);
        } else {
            layoutCheckBoxOfertaIntegral.setVisibility(View.GONE);
            layoutOfertaIntegral.setVisibility(View.VISIBLE);
        }
    }

    public void cargarSpinner() {
        estatusObraListAll = CatalogoStatusObraRealm.mostrarListaStatusObra();
        estatusObraDesc.add(getString(R.string.formulario_spinners_default));
        for (CatalogoStatusObraDB m : estatusObraListAll) {
            estatusObraDesc.add(m.getDescripcion());
        }

        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, estatusObraDesc);
//        spEstatusObra.setAdapter(adapter);
    }

    public void obtenerOportunidadDeVenta() {

        TinyDB tinyDB = new TinyDB(context);

        if (tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class) == null
                || tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class).size() == 0
                || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class) == null
                || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size() == 0) {

            obtenerProductosYServicios();

        } else {
            esOfertaIntegral = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);

            marcarOfertaIntegral();
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.radioSiContactasteProspecto) {

            if (!checkboxDescartar.isChecked()) {
                layoutSiContactaste.setVisibility(View.VISIBLE);
                layoutNoContactaste.setVisibility(View.GONE);
            }


        } else if (v.getId() == R.id.radioNoContactasteProspecto) {

            if (!checkboxDescartar.isChecked()) {
                layoutSiContactaste.setVisibility(View.GONE);
                layoutNoContactaste.setVisibility(View.VISIBLE);
            }

        } else if (v.getId() == R.id.chbx_oferta_integral) {
            if (checkboxOfertaIntegral.isChecked()) {
                layoutOfertaIntegral.setVisibility(View.GONE);

                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "Oferta Integral", "¿Confirmas la selección de oferta integral?",
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_MARCAR_OFERTA_INTEGRAL);

            } else {
                layoutOfertaIntegral.setVisibility(View.VISIBLE);
                desmarcarTodaOfertaIntegral();

                marcarOfertaIntegral();
            }
        } else if (v.getId() == R.id.btnRealizarActividad) {

            if (validarCampos()) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "Realizar Actividad", "Visitar Prospecto",
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_REALIZAR_ACTIVIDAD);
            }
        } else if (v.getId() == R.id.btnAgendarNuevaCitaLlamada) {

            if (validarCampos()) {

                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "Agendar Actividad", "Reagendar " + titulo,
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_REAGENDAR);
            }

        } else if (v.getId() == R.id.btnAgendarActividad) {

            if (validarCampos()) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "Agendar Actividad", getString(R.string.citas2_title),
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_AGENDAR_ACTIVIDAD);
            }
        } else if (v.getId() == R.id.btn_seleccionar_productos_servicios || v.getId() == R.id.layout_checkbox_oferta_integral) {

            dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                    "", "Ir a Formulario de Productos y Servicios seleccionados",
                    getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_FORMULARIO_PRODUCTO);

        } else if (v.getId() == R.id.chbx_descartar) {

            if (checkboxDescartar.isChecked()) {
                layoutDescartar.setVisibility(View.VISIBLE);
                layoutSiContactaste.setVisibility(View.GONE);
                layoutNoContactaste.setVisibility(View.GONE);
            } else {
                layoutDescartar.setVisibility(View.GONE);
                if (radioSiContactaste.isChecked()) {
                    layoutSiContactaste.setVisibility(View.VISIBLE);
                    layoutNoContactaste.setVisibility(View.GONE);
                } else {
                    layoutSiContactaste.setVisibility(View.GONE);
                    layoutNoContactaste.setVisibility(View.VISIBLE);
                }
            }


        } else if (v.getId() == R.id.btn_descartar) {

            if (validarCamposDescartar()) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "", getString(R.string.mensaje_descartar_prospecto),
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_DESCARTAR);
            }

        }

    }

    public boolean validarCamposDescartar() {
//        if (etComentariosDescartar.getText().toString().isEmpty()) {
//            Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//            etComentariosDescartar.requestFocus();
//            return false;
//        }  else
//        if (spEstatusObra.getSelectedItemPosition() == 0) {
//            Toast.makeText(context, getString(R.string.motivos_spinner_vacio), Toast.LENGTH_SHORT).show();
//            etComentariosDescartar.requestFocus();
//            return false;
//        }
        return true;
    }

    public boolean validarCampos() {

//        if(radioSiContactaste.isChecked()) {
//            if(etxComentarioSiContactaste.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//                etxComentarioSiContactaste.requestFocus();
//                return false;
//            }
//        } else {
//            if(etxComentarioNoContactaste.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//                etxComentarioNoContactaste.requestFocus();
//                return false;
//            }
//        }

        return true;
    }


    @Override
    public void alertDialogPositive(String idDialog) {

        Intent intent;
        TinyDB tinyDB = new TinyDB(context);


        switch (idDialog) {
            case ID_REAGENDAR:

                estatusActividad = 2;
                idPaso = 1;
                llenarObjetoParcialmente(Integer.parseInt(idActividad), Valores.ID_ESTATUS_PROSPECTO_SIN_CONTACTO);

//                if(siPuedeAgendar) {
                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_CALENDAR);
                intent.putExtra("nombreActividadActual", "");
                startActivity(intent);
//                } else {
//                    Toast.makeText(context, "Prospecto sin productos y servicios.", Toast.LENGTH_LONG).show();
//                }

                break;
            case ID_AGENDAR_ACTIVIDAD:

                estatusActividad = 3;
                idPaso = 2;
                llenarObjetoParcialmente(Valores.ID_ACTIVIDAD_VISITAR_PROSPECTO, Valores.ID_ESTATUS_PROSPECTO_AGENDADO);

//                if(siPuedeAgendar) {
                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_CALENDAR);
                startActivity(intent);
//                } else {
//                    Toast.makeText(context, "Prospecto sin productos y servicios.", Toast.LENGTH_LONG).show();
//                }

                break;
            case ID_REALIZAR_ACTIVIDAD:

                if (checkboxOfertaIntegral.isChecked()) {
                    esOfertaIntegral = true;
                } else {
                    esOfertaIntegral = false;
                }

                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL, esOfertaIntegral);

                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_VISITAR_PROSPECTO);
                startActivity(intent);
                break;

            case ID_FORMULARIO_PRODUCTO:

//                obtenerProductosYServicios();

                //Se cambia la shared preference a falso para que no se muestre el formulario de motivos de exclusión
                //cuando se desmarcan productos y servicios
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_MOTIVO_EXCLUSION, false);
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_MOSTRAR_TODOS_LOS_DETALLES, false) ;

                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PRODUCTOS_SERVICIOS);
                startActivity(intent);

                break;
            case ID_FORMULARIO_SERVICIO:

//                obtenerProductosYServicios();

                //Se cambia la shared preference a falso para que no se muestre el formulario de motivos de exclusión
                //cuando se desmarcan productos y servicios
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_MOTIVO_EXCLUSION, false);

                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PRODUCTOS_SERVICIOS);
                startActivity(intent);
                break;

            case ID_MARCAR_OFERTA_INTEGRAL:
                marcarTodaOfertaIntegral();
                break;

            case ID_DESCARTAR:

                JSONdescartar jsonDescartar = new JSONdescartar();
                com.telstock.tmanager.cemex.modulocitas.model.MotivoExclusion motivoExclusion = new com.telstock.tmanager.cemex.modulocitas.model.MotivoExclusion();

                jsonDescartar.setIdProspecto(prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));
                motivoExclusion.setComentarios(etComentariosDescartar.getText().toString());
//                motivoExclusion.setId(estatusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getId());
                motivoExclusion.setId((long) 0);
                jsonDescartar.setMotivoExclusion(motivoExclusion);

                Gson gson = new Gson();
                final String json = gson.toJson(jsonDescartar);

                Call<ResponseBody> callDescartar = apiInterface.setDescartar(jsonDescartar);

                callDescartar.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.body() != null && response.code() == 200) {
                            Toast.makeText(context, getString(R.string.descatar_ok), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent();
                            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                            startActivity(intent);
                        } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                            //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                            AlertTokenToLogin.showAlertDialog(context);
                        } else {
                            Toast.makeText(context, getString(R.string.descarte_fail), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Descartar", t.toString());
                        FirebaseCrash.log("Error Descartar");
                        FirebaseCrash.report(t);

                       // Toast.makeText(context, getString(R.string.descarte_fail), Toast.LENGTH_LONG).show();
                        /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                        Toast.makeText(context, getString(R.string.envio_descartar_offline), Toast.LENGTH_LONG).show();

                        GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                                System.currentTimeMillis(),
                                Valores.ID_ENVIO_DESCARTAR,
                                "",
                                json,
                                Valores.ESTATUS_NO_ENVIADO
                        );
                        GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                        /**********************************************************************/

                        Intent intent = new Intent();
                        intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                        intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                        startActivity(intent);
                    }
                });

                break;
        }
    }

    @Override
    public void alertDialogNegative(String idDialog) {

        switch (idDialog) {
        case ID_MARCAR_OFERTA_INTEGRAL:
       // desmarcarTodaOfertaIntegral();
            esOfertaIntegral = false;
            marcarOfertaIntegral();
        break;
        }
    }

    @Override
    public void alertDialogNeutral(String idDialog) {
    }

    public void obtenerProductosYServicios() {

        String idProspecto = prefs.getString(Valores.SHAREDPREFERENCES_ID_PROSPECTO, "");

        Gson gson = new Gson();
        String jsonGlobal = JsonGlobalProspectosRealm.mostrarJsonGlobalProspecto(idProspecto).getJsonGlobalProspectos();
        try {
            JSONObject jsonObject = new JSONObject(jsonGlobal);
            TinyDB tinyDB = new TinyDB(context);
            List<SubsegmentoProducto> subsegmentoProducto = new ArrayList<>();
            List<Servicio> servicio = new ArrayList<>();

//            Pasos pasos = gson.fromJson(jsonObject.getString("pasos"), Pasos.class);

            switch (evaluarPasoActual()) {
                case 1:

                    if (tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class) == null
                            || tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class).size() == 0
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class) == null
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size() == 0) {
                        if (prefs.getInt(Valores.SHAREDPREFEREBCES_TIENE_ACTIVIDAD_ANTERIOR, 0) != Valores.ID_ACTIVIDAD_REAGENDADA) {

                            Call<OfertaIntegral> callOfertaIntegralInicial = apiInterface.getOportunidadVentaInicial(prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));

                            callOfertaIntegralInicial.enqueue(new Callback<OfertaIntegral>() {
                                @Override
                                public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
                                    if (response.body() != null && response.code() == 200) {
                                        ofertaIntegral = response.body();
                                        guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
                                    } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                                        //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                                        AlertTokenToLogin.showAlertDialog(context);
                                    } else {
                                        Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                                    Log.e("Oportunidad Venta", t.toString());
                                    FirebaseCrash.log("Error OV");
                                    FirebaseCrash.report(t);

                                    Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
                                }
                            });

                        } else {

                            Call<OfertaIntegral> callOfertaIntegral = apiInterface.getOportunidadVentaPaso(evaluarPasoActual() + "", prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));

                            callOfertaIntegral.enqueue(new Callback<OfertaIntegral>() {
                                @Override
                                public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
                                    if (response.body() != null && response.code() == 200) {
                                        ofertaIntegral = response.body();
                                        guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
                                    } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                                        //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                                        AlertTokenToLogin.showAlertDialog(context);
                                    } else {
                                        Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                                    Log.e("Oportunidad Venta", t.toString());
                                    FirebaseCrash.log("Error OV");
                                    FirebaseCrash.report(t);

                                    Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    } else {
                        esOfertaIntegral = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
                        subsegmentoProducto = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
                        servicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

                        marcarOfertaIntegral();
                    }

                    break;
                case 2:

                    if (tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class) == null
                            || tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class).size() == 0
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class) == null
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size() == 0) {

                        Call<OfertaIntegral> callOfertaIntegral = apiInterface.getOportunidadVentaPaso(evaluarPasoActual() + "", prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));

                        callOfertaIntegral.enqueue(new Callback<OfertaIntegral>() {
                            @Override
                            public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
                                if (response.body() != null && response.code() == 200) {
                                    ofertaIntegral = response.body();
                                    guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
                                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                                    AlertTokenToLogin.showAlertDialog(context);
                                } else {
                                    Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                                Log.e("Oportunidad Venta", t.toString());
                                FirebaseCrash.log("Error OV");
                                FirebaseCrash.report(t);

                                Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        esOfertaIntegral = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
                        subsegmentoProducto = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
                        servicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

                        marcarOfertaIntegral();
                    }
                    break;
                case 3:

                    if (tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class) == null
                            || tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class).size() == 0
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class) == null
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size() == 0) {

                        Call<OfertaIntegral> callOfertaIntegral = apiInterface.getOportunidadVentaPaso(evaluarPasoActual() + "", prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));

                        callOfertaIntegral.enqueue(new Callback<OfertaIntegral>() {
                            @Override
                            public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
                                if (response.body() != null && response.code() == 200) {
                                    ofertaIntegral = response.body();
                                    guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
                                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                                    AlertTokenToLogin.showAlertDialog(context);
                                } else {
                                    Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                                Log.e("Oportunidad Venta", t.toString());
                                FirebaseCrash.log("Error OV");
                                FirebaseCrash.report(t);

                                Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        esOfertaIntegral = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
                        subsegmentoProducto = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
                        servicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

                        marcarOfertaIntegral();
                    }

                    break;
                case 4:

                    if (tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class) == null
                            || tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class).size() == 0
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class) == null
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size() == 0) {

                        Call<OfertaIntegral> callOfertaIntegral = apiInterface.getOportunidadVentaPaso(evaluarPasoActual() + "", prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));

                        callOfertaIntegral.enqueue(new Callback<OfertaIntegral>() {
                            @Override
                            public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
                                if (response.body() != null && response.code() == 200) {
                                    ofertaIntegral = response.body();
                                    guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
                                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                                    AlertTokenToLogin.showAlertDialog(context);
                                } else {
                                    Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                                Log.e("Oportunidad Venta", t.toString());
                                FirebaseCrash.log("Error OV");
                                FirebaseCrash.report(t);

                                Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        esOfertaIntegral = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
                        subsegmentoProducto = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
                        servicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

                        marcarOfertaIntegral();
                    }
                    break;
                case 5:

                    if (tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class) == null
                            || tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class).size() == 0
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class) == null
                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size() == 0) {

                        Call<OfertaIntegral> callOfertaIntegral = apiInterface.getOportunidadVentaPaso(evaluarPasoActual() + "", prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));

                        callOfertaIntegral.enqueue(new Callback<OfertaIntegral>() {
                            @Override
                            public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
                                if (response.body() != null && response.code() == 200) {
                                    ofertaIntegral = response.body();
                                    guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
                                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                                    AlertTokenToLogin.showAlertDialog(context);
                                } else {
                                    Toast.makeText(context, response.code() + "", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<OfertaIntegral> call, Throwable t) {
                                Log.e("Oportunidad Venta", t.toString());
                                FirebaseCrash.log("Error OV");
                                FirebaseCrash.report(t);

                                Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {

                        esOfertaIntegral = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
                        subsegmentoProducto = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
                        servicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

                        marcarOfertaIntegral();
                    }
                    break;
            }


            Log.d("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int evaluarPasoActual() {

        int idActividadActual = Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO;

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

    public void llenarObjetoParcialmente(int idActividad, int idEstatusProspecto) {

        TinyDB tinyDB = new TinyDB(context);

        try {
//            //Se obtiene el objeto guardado parcialmente.
//            PlanSemanalDB planSemanalDB = tinyDB.getPlanSemanalDB(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO,
//                    PlanSemanalDB.class);
//            //Se llena el atributo que lleva el nombre de la actividad a mostrar en el calendario.
//            planSemanalDB.setIdActividad(idActividad);
//
//            //Se guarda el objeto modificado en TinyDB.
//            tinyDB.putPlanSemanalDB(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, planSemanalDB);

            List<SubsegmentoProducto> listaSubsegmento = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
            List<Servicio> listaServicios = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);
            Actividad actividad = new Actividad();
            mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.TipoActividad tipoActividad = new mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.TipoActividad();
            JsonAltaActividades jsonAltaActividades = new JsonAltaActividades();

            if (listaSubsegmento.size() == 0) {
                obtenerProductosYServicios();
                listaSubsegmento = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
            }

            if (listaServicios.size() == 0) {
                obtenerProductosYServicios();
                listaServicios = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);
            }

            if (listaSubsegmento.size() == 0 || listaServicios.size() == 0) {
                siPuedeAgendar = false;
            }

            if (checkboxOfertaIntegral.isChecked()) {
                jsonAltaActividades.setEsOfertaIntegral(true);
            } else {
                jsonAltaActividades.setEsOfertaIntegral(false);
            }

            jsonAltaActividades.setServicios(listaServicios);
            jsonAltaActividades.setSubsegmentoProductos(listaSubsegmento);
            jsonAltaActividades.setIdPaso(idPaso);
            jsonAltaActividades.setIdStatusProspecto(idEstatusProspecto);

            CatalogoActividadesPGVDB catalogoActividadesPGVDB = CatalogoActividadesPGVRealm.mostrarActividad(idActividad);
            tipoActividad.setId(catalogoActividadesPGVDB.getId());
            tipoActividad.setDescripcion(catalogoActividadesPGVDB.getDescripcion());
            tipoActividad.setIdCatalogo(catalogoActividadesPGVDB.getIdCatalogo());
            tipoActividad.setIdPadre(catalogoActividadesPGVDB.getIdPadre());
            actividad.setTipoActividad(tipoActividad);

            jsonAltaActividades.setActividad(actividad);

            ActividadAnterior actividadAnterior = new ActividadAnterior();
            actividadAnterior.setIdActividad(prefs.getString(Valores.SHAREDPREFERENCES_ID_ACTIVIDAD_ACTUAL, ""));
            actividadAnterior.setEstatusAgenda(estatusActividad);
            jsonAltaActividades.setmActividadAnterior(actividadAnterior);

//            if(siPuedeAgendar) {
            tinyDB.putJsonAltaActividades(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES, jsonAltaActividades);
//            }

            tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
            tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS);
            tinyDB.remove(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS);

            Log.d("", "");

        } catch (Exception e) {
            Log.e("CITAS_1", e.toString());
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

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void guardarOportunidadVenta(List<SubsegmentoProducto> subsegmentoProducto, List<Servicio> servicio) {

        TinyDB tinyDB = new TinyDB(context);
        esOfertaIntegral = ofertaIntegral.getEsOfertaIntegral();
        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL, esOfertaIntegral);

        if (subsegmentoProducto != null || subsegmentoProducto.size() > 0) {
            tinyDB.putSubsegmentosProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, subsegmentoProducto);
        }
        if (servicio != null || servicio.size() > 0) {
            tinyDB.putServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, servicio);
        }

        marcarOfertaIntegral();
    }

    public void marcarOfertaIntegral() {

        checkboxOfertaIntegral.setChecked(esOfertaIntegral);

        //Se muestra sólo si esta marcado el checkbox o es nuevo prospecto
        if (checkboxOfertaIntegral.isChecked() && idTipoProspecto == Valores.ID_TIPO_PROSPECTO_NUEVO_PROSPECTO) {
            layoutOfertaIntegral.setVisibility(View.GONE);
        } else {
            layoutOfertaIntegral.setVisibility(View.VISIBLE);
        }

    }

    public void marcarTodaOfertaIntegral() {

        TinyDB tinyDB = new TinyDB(context);

        ofertaIntegral.setSubsegmentosProductos(tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class));
        ofertaIntegral.setServicios(tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class));
        ofertaIntegral.setEsOfertaIntegral(true);

        for(int i = 0;i < ofertaIntegral.getSubsegmentosProductos().get(0).getProductos().size() ; i++) {
            ofertaIntegral.getSubsegmentosProductos().get(0).getProductos().get(i).setSeleccionado(true);
        }

        for(int i = 0;i < ofertaIntegral.getServicios().size(); i++) {
            ofertaIntegral.getServicios().get(i).setSeleccionado(true);
        }

        guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
    }
    public void desmarcarTodaOfertaIntegral() {

        TinyDB tinyDB = new TinyDB(context);

        ofertaIntegral.setSubsegmentosProductos(tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class));
        ofertaIntegral.setServicios(tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class));
        ofertaIntegral.setEsOfertaIntegral(false);

        for(int i = 0;i < ofertaIntegral.getSubsegmentosProductos().get(0).getProductos().size() ; i++) {
            ofertaIntegral.getSubsegmentosProductos().get(0).getProductos().get(i).setSeleccionado(false);
        }

        for(int i = 0;i < ofertaIntegral.getServicios().size(); i++) {
            ofertaIntegral.getServicios().get(i).setSeleccionado(false);
        }

        guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
    }
}
