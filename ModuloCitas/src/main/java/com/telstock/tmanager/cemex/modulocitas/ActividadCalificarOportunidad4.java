package com.telstock.tmanager.cemex.modulocitas;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.telstock.tmanager.cemex.modulocitas.funciones.TinyDB;
import com.telstock.tmanager.cemex.modulocitas.model.JSONdescartar;
import com.telstock.tmanager.cemex.modulocitas.model.MotivoExclusion;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiClient;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.SubsegmentoProducto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by czamora on 10/11/16.
 */
public class ActividadCalificarOportunidad4 extends Fragment
        implements View.OnClickListener, ComunicarAlertDialog {

    private Context context;

    private LinearLayout layoutSi;
    private LinearLayout layoutNo;
    private RadioButton radioSi;
    private RadioButton radioNo;
    private LinearLayout layoutSiCalifica;
    private LinearLayout layoutNoCalifica;
    private EditText etxComentarios;
    private Button btnReagendarActividad;
    private RadioButton radioSiOportunidad;
    private RadioButton radioNoOportunidad;
    private EditText etxComentariosNoCalifica;
    private EditText etxComentariosSiCalifica;
    private Button btnDescartarOportunidad;
    private Button btnAgendarActividad;
    private Button btnRealizarActividad;

    private CheckBox checkboxDescartar;
    private CheckBox checkboxOfertaIntegral;
    private Button btnDescartar;
    private LinearLayout btnSeleccionarProductosServicios;
    private LinearLayout layoutOfertaIntegral;
    //    private Spinner spEstatusObra;
    private LinearLayout layoutDescartar;
    private LinearLayout layoutCheckBoxOfertaIntegral;

    private EditText etComentariosDescartar;
    private ApiInterface apiInterface;
    boolean esOfertaIntegral = true;

    //Modal que muestra mensaje de alerta.
    Dialog dialogModal;
    private static final String ID_FORMULARIO_PRODUCTO = "idFormularioProducto";
    private static final String ID_FORMULARIO_SERVICIO = "idFormularioServicio";
    private static final String ID_REAGENDAR_ACTIVIDAD = "idReagendarActividad";
    private static final String ID_DESCARTAR_OPORTUNIDAD = "idDescartarOportunidad";
    private static final String ID_DESCARTAR_OPORTUNIDAD_SUCCESS = "idDescartarOportunidadSuccess";
    private static final String ID_AGENDAR_ACTIVIDAD = "idAgendarActividad";
    private static final String ID_REALIZAR_ACTIVIDAD = "idRealizarActividad";
    private static final String ID_DESCARTAR = "idDescartar";
    private static final String ID_MARCAR_OFERTA_INTEGRAL = "idMarcarOfertaIntegral";

    ArrayList<String> estatusObraDesc = new ArrayList<>();
    List<CatalogoStatusObraDB> estatusObraListAll = new ArrayList<>();
    ArrayAdapter<String> adapter;
    SharedPreferences prefs;
    int estatusActividad = 0;

    OfertaIntegral ofertaIntegral = new OfertaIntegral();

    ProgressDialog progressDialog;
    ComunicarAlertDialog comunicarAlertDialog;

    View view;

    Toolbar toolbar;

    int idPaso;

    int idTipoProspecto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.actividad_calificar_oportunidad_4, container, false);

        context = getActivity();
        comunicarAlertDialog = this;

        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.citas4_title));

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);

        //Se obtiene el tipo de prospecto
        idTipoProspecto = prefs.getInt(Valores.SHAREDPREFERENCES_ID_TIPO_PROSPECTO, 0);

        //Se inicializa la interfaz que contiene los métodos de WS
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        layoutSi = (LinearLayout) view.findViewById(R.id.layout_si);
        layoutNo = (LinearLayout) view.findViewById(R.id.layout_no);
        radioSi = (RadioButton) view.findViewById(R.id.radioSi);
        radioNo = (RadioButton) view.findViewById(R.id.radioNo);
        etxComentarios = (EditText) view.findViewById(R.id.etx_comentarios);
        btnReagendarActividad = (Button) view.findViewById(R.id.btn_citas4_reagendar_actividad);
        radioSiOportunidad = (RadioButton) view.findViewById(R.id.radioSiOportunidad);
        radioNoOportunidad = (RadioButton) view.findViewById(R.id.radioNoOportunidad);
        etxComentariosNoCalifica = (EditText) view.findViewById(R.id.etx_comentarios_no_califica);
        etxComentariosSiCalifica = (EditText) view.findViewById(R.id.etx_comentarios_si_califica);
        btnDescartarOportunidad = (Button) view.findViewById(R.id.btn_citas4_descartar_oportunidad);
        layoutSiCalifica = (LinearLayout) view.findViewById(R.id.layout_si_califica);
        layoutNoCalifica = (LinearLayout) view.findViewById(R.id.layout_no_califica);
        btnAgendarActividad = (Button) view.findViewById(R.id.btnAgendarActividad);
        btnRealizarActividad = (Button) view.findViewById(R.id.btnRealizarActividad);
        etComentariosDescartar = (EditText) view.findViewById(R.id.etComentariosDescartar);

        checkboxOfertaIntegral = (CheckBox) view.findViewById(R.id.chbx_oferta_integral);
        btnSeleccionarProductosServicios = (LinearLayout) view.findViewById(R.id.btn_seleccionar_productos_servicios);
        checkboxDescartar = (CheckBox) view.findViewById(R.id.chbx_descartar);
//        spEstatusObra = (Spinner) view.findViewById(R.id.sp_estatus_obra);
        btnDescartar = (Button) view.findViewById(R.id.btn_descartar);
        layoutDescartar = (LinearLayout) view.findViewById(R.id.layout_descartar);
        layoutOfertaIntegral = (LinearLayout) view.findViewById(R.id.layout_oferta_integral);
        layoutCheckBoxOfertaIntegral = (LinearLayout) view.findViewById(R.id.layout_checkbox_oferta_integral);

        radioSi.setOnClickListener(this);
        radioNo.setOnClickListener(this);
        btnReagendarActividad.setOnClickListener(this);
        radioSiOportunidad.setOnClickListener(this);
        radioNoOportunidad.setOnClickListener(this);
        btnDescartarOportunidad.setOnClickListener(this);
        btnRealizarActividad.setOnClickListener(this);
        btnAgendarActividad.setOnClickListener(this);

        btnSeleccionarProductosServicios.setOnClickListener(this);
        checkboxDescartar.setOnClickListener(this);
        btnDescartar.setOnClickListener(this);
        checkboxOfertaIntegral.setOnClickListener(this);
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

    public void cargarSpinner() {
        estatusObraListAll = CatalogoStatusObraRealm.mostrarListaStatusObra();
        estatusObraDesc.add(getString(R.string.formulario_spinners_default));
        for (CatalogoStatusObraDB m : estatusObraListAll) {
            estatusObraDesc.add(m.getDescripcion());
        }

        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, estatusObraDesc);
//        spEstatusObra.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.radioSi) {
            layoutSi.setVisibility(View.VISIBLE);
            layoutNo.setVisibility(View.GONE);
            layoutNoCalifica.setVisibility(View.GONE);
            layoutSiCalifica.setVisibility(View.GONE);

        } else if (v.getId() == R.id.radioNo) {
            layoutSi.setVisibility(View.GONE);
            layoutNo.setVisibility(View.VISIBLE);
            radioSiOportunidad.setChecked(true);
            layoutSiCalifica.setVisibility(View.VISIBLE);

        } else if (v.getId() == R.id.btn_citas4_reagendar_actividad) {

            if (validarCampos(v)) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "Agendar Actividad", getString(R.string.citas4_title),
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_REAGENDAR_ACTIVIDAD);
            }

        } else if (v.getId() == R.id.radioSiOportunidad) {

            layoutSiCalifica.setVisibility(View.VISIBLE);
            layoutNoCalifica.setVisibility(View.GONE);

        } else if (v.getId() == R.id.radioNoOportunidad) {

            layoutSiCalifica.setVisibility(View.GONE);
            layoutNoCalifica.setVisibility(View.VISIBLE);

        } else if (v.getId() == R.id.btn_citas4_descartar_oportunidad) {

            if (validarCampos(v)) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "CONFIRMACIÓN", "¿Estás seguro de Descartar la Oportunidad de Venta?",
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_DESCARTAR_OPORTUNIDAD);
            }
        } else if (v.getId() == R.id.btnRealizarActividad) {

            if (validarCampos(v)) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "Realizar Actividad", getString(R.string.citas5_title),
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_REALIZAR_ACTIVIDAD);
            }

                /*Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PREPARAR_PROPUESTA_DE_VALOR);
                startActivity(intent);*/

        } else if (v.getId() == R.id.btnAgendarActividad) {
            if (validarCampos(v)) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "Agendar Actividad", getString(R.string.citas5_title),
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_AGENDAR_ACTIVIDAD);
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
        } else if (v.getId() == R.id.chbx_descartar) {

            if (checkboxDescartar.isChecked()) {
                layoutDescartar.setVisibility(View.VISIBLE);
                layoutSi.setVisibility(View.GONE);
                layoutNo.setVisibility(View.GONE);
                layoutNoCalifica.setVisibility(View.GONE);
                layoutSiCalifica.setVisibility(View.GONE);

            } else {
                layoutDescartar.setVisibility(View.GONE);
                if(radioSi.isChecked()) {
                    layoutSi.setVisibility(View.VISIBLE);
                    layoutNo.setVisibility(View.GONE);
                    layoutNoCalifica.setVisibility(View.GONE);
                    layoutSiCalifica.setVisibility(View.GONE);
                } else {
                    layoutSi.setVisibility(View.GONE);
                    layoutNo.setVisibility(View.VISIBLE);
                    if(radioSiOportunidad.isChecked()) {
                        layoutSiCalifica.setVisibility(View.VISIBLE);
                        layoutNoCalifica.setVisibility(View.GONE);
                    } else {
                        layoutSiCalifica.setVisibility(View.GONE);
                        layoutNoCalifica.setVisibility(View.VISIBLE);
                    }
                }
            }


        } else if (v.getId() == R.id.btn_descartar) {

            if (validarCamposDescartar()) {
                dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                        "", getString(R.string.mensaje_descartar_prospecto),
                        getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_DESCARTAR);
            }

        } else if (v.getId() == R.id.btn_seleccionar_productos_servicios  || v.getId() == R.id.layout_checkbox_oferta_integral) {

            dialogModal = AlertDialogModal.showModalTwoButtons(context, this,
                    "", "Ir a Formulario de Productos y Servicios seleccionados",
                    getString(R.string.citas_dialog_confirmar), getString(R.string.citas_dialog_cancelar), ID_FORMULARIO_PRODUCTO);

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

    public boolean validarCampos(View view) {

//        if (view.getId() == R.id.btn_citas4_reagendar_actividad) {
//            if (etxComentarios.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//                etxComentarios.requestFocus();
//                return false;
//            }
//        } else if (view.getId() == R.id.btn_citas4_descartar_oportunidad) {
//            if (etxMotivosNoCalifica.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_motivos_no_califica_empty), Toast.LENGTH_SHORT).show();
//                etxComentarios.requestFocus();
//                return false;
//            }
//        } else if (view.getId() == R.id.btnRealizarActividad || view.getId() == R.id.btnAgendarActividad) {
//            if (etxComentariosDolor.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//                etxComentariosDolor.requestFocus();
//                return false;
//            }
//        }

//        if (radioSi.isChecked() && layoutNo.getVisibility() == View.GONE) {
//            if (etxComentarios.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//                etxComentarios.requestFocus();
//                return false;
//            }
//        } else if (radioNoOportunidad.isChecked() && layoutNoCalifica.getVisibility() == View.VISIBLE) {
//            if (etxComentariosNoCalifica.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//                etxComentariosNoCalifica.requestFocus();
//                return false;
//            }
//        } else if (radioSiOportunidad.isChecked() && layoutSiCalifica.getVisibility() == View.VISIBLE) {
//            if (etxComentariosSiCalifica.getText().toString().isEmpty()) {
//                Toast.makeText(context, getString(R.string.citas_comentarios_empty), Toast.LENGTH_SHORT).show();
//                etxComentariosSiCalifica.requestFocus();
//                return false;
//            }
//        }


        return true;
    }

    @Override
    public void alertDialogPositive(String idDialog) {

        TinyDB tinyDB = new TinyDB(context);
        Intent intent;
        List<SubsegmentoProducto> listaSubsegmento = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
        List<Servicio> listaServicios = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);

        switch (idDialog) {

            case ID_REAGENDAR_ACTIVIDAD:

                if (revisarAlMenosUnProductoSeleccionado(listaSubsegmento, listaServicios)) {
                    estatusActividad = 2;
                    idPaso = 3;
                    llenarObjetoParcialmente(Valores.ID_ACTIVIDAD_CALIFICAR_OPORTUNIDAD, Valores.ID_ESTATUS_PROSPECTO_CALIFICA, false);

                    intent = new Intent();
                    intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_CALENDAR);
                    startActivity(intent);
                }

                break;
            case ID_DESCARTAR_OPORTUNIDAD:

                showProgress(); //Se muestra el ProgressDialog.

                llenarObjetoParcialmente(Valores.ID_ACTIVIDAD_DESCARTAR_OPORTUNIDAD, Valores.ID_ESTATUS_PROSPECTO_NO_CALIFICA, true);

                break;
            case ID_DESCARTAR_OPORTUNIDAD_SUCCESS:

                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_CITAS_VISITAS);
                startActivity(intent);

                break;

            case ID_MARCAR_OFERTA_INTEGRAL:
                marcarTodaOfertaIntegral();
                break;

            case ID_FORMULARIO_PRODUCTO:
//                obtenerProductosYServicios();

                //Se cambia la shared preference a verdadero para que no se muestre el formulario de motivos de exclusión
                //cuando se desmarcan productos y servicios
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_MOTIVO_EXCLUSION, true);
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_MOSTRAR_TODOS_LOS_DETALLES, false) ;
                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PRODUCTOS_SERVICIOS);
                startActivity(intent);

                break;
            case ID_FORMULARIO_SERVICIO:
//                obtenerProductosYServicios();

                //Se cambia la shared preference a verdadero para que no se muestre el formulario de motivos de exclusión
                //cuando se desmarcan productos y servicios
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_MOTIVO_EXCLUSION, true);
                tinyDB.putBoolean(Valores.SHAREDPREFERENCES_MOSTRAR_TODOS_LOS_DETALLES, false) ;
                intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PRODUCTOS_SERVICIOS);
                startActivity(intent);
                break;
            case ID_REALIZAR_ACTIVIDAD:

                if (revisarAlMenosUnProductoSeleccionado(listaSubsegmento, listaServicios)) {

                    if (checkboxOfertaIntegral.isChecked()) {
                        esOfertaIntegral = true;
                    } else {
                        esOfertaIntegral = false;
                    }


                    tinyDB.putBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL, esOfertaIntegral);

                    intent = new Intent();
                    intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PREPARAR_PROPUESTA_DE_VALOR);
                    startActivity(intent);
                }
                break;
            case ID_AGENDAR_ACTIVIDAD:



                if (revisarAlMenosUnProductoSeleccionado(listaSubsegmento, listaServicios)) {
                    estatusActividad = 3;
                    idPaso = 3;
                    llenarObjetoParcialmente(Valores.ID_ACTIVIDAD_PREPARAR_PROPUESTA_DE_VALOR, Valores.ID_ESTATUS_PROSPECTO_PREPARAR_PROPUESTA, false);

                    intent = new Intent();
                    intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PLAN_CALENDAR);
                    startActivity(intent);
                }
                break;

            case ID_DESCARTAR:

                JSONdescartar jsonDescartar = new JSONdescartar();
                MotivoExclusion motivoExclusion = new MotivoExclusion();

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
            case ID_DESCARTAR_OPORTUNIDAD:
                break;
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

    public void llenarObjetoParcialmente(int idActividad, int idEstatusProspecto, boolean descartada) {

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

//            if(listaSubsegmento.size() == 0 || listaServicios.size() == 0) {
//                siPuedeAgendar = false;
//            }

            if (checkboxOfertaIntegral.isChecked()) {
                jsonAltaActividades.setEsOfertaIntegral(true);
            } else {
                jsonAltaActividades.setEsOfertaIntegral(false);
            }

            jsonAltaActividades.setServicios(listaServicios);
            jsonAltaActividades.setSubsegmentoProductos(listaSubsegmento);
            jsonAltaActividades.setIdPaso(idPaso);
            jsonAltaActividades.setIdStatusProspecto(idEstatusProspecto);

            if (radioSi.isChecked() && layoutNo.getVisibility() == View.GONE) {
                actividad.setComentarios(etxComentarios.getText().toString());
            } else if (radioNoOportunidad.isChecked() && layoutNoCalifica.getVisibility() == View.VISIBLE) {
                actividad.setComentarios(etxComentariosNoCalifica.getText().toString());
            } else if (radioSiOportunidad.isChecked() && layoutSiCalifica.getVisibility() == View.VISIBLE) {
                actividad.setComentarios(etxComentariosSiCalifica.getText().toString());
            }


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

            if (descartada) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.SECOND, 0);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String dateTime = dateFormat.format(calendar.getTime());

                actividad.setFechaHoraCitaInicio(dateTime + "Z");
                actividad.setFechaHoraCitaFin(dateTime + "Z");

                jsonAltaActividades.setActividad(actividad);

                mandarInformacion(jsonAltaActividades);
            }

        } catch (Exception e) {
            Log.e("CITAS_1", e.toString());
        }
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

//                    if(tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, SubsegmentoProducto.class) == null
//                            || tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, SubsegmentoProducto.class).size() == 0
//                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class) == null
//                            || tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class).size() == 0) {
//                        if (idActividadActual == Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO
//                                || idActividadActual == Valores.ID_ACTIVIDAD_CONTACTAR_CLIENTE) {
//
//                            Call<OfertaIntegral> callOfertaIntegralInicial = apiInterface.getOportunidadVentaInicial(prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));
//
//                            callOfertaIntegralInicial.enqueue(new Callback<OfertaIntegral>() {
//                                @Override
//                                public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
//                                    if (response.body() != null && response.code() == 200) {
//                                        ofertaIntegral = response.body();
//                                        guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
//                                    } else {
//                                        Toast.makeText(context, response.code()+ "", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<OfertaIntegral> call, Throwable t) {
//                                    Log.e("Oportunidad Venta", t.toString());
//                                    FirebaseCrash.log("Error OV");
//                                    FirebaseCrash.report(t);
//
//                                    Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                        } else {
//
//                            Call<OfertaIntegral> callOfertaIntegral = apiInterface.getOportunidadVentaPaso(evaluarPasoActual() + "",prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""));
//
//                            callOfertaIntegral.enqueue(new Callback<OfertaIntegral>() {
//                                @Override
//                                public void onResponse(Call<OfertaIntegral> call, Response<OfertaIntegral> response) {
//                                    if (response.body() != null && response.code() == 200) {
//                                        ofertaIntegral = response.body();
//                                        guardarOportunidadVenta(ofertaIntegral.getSubsegmentosProductos(), ofertaIntegral.getServicios());
//                                    } else {
//                                        Toast.makeText(context, response.code()+ "", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<OfertaIntegral> call, Throwable t) {
//                                    Log.e("Oportunidad Venta", t.toString());
//                                    FirebaseCrash.log("Error OV");
//                                    FirebaseCrash.report(t);
//
//                                    Toast.makeText(context, getString(R.string.oportunidad_venta_fail), Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                        }
//                    } else {
//                        esOfertaIntegral = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL);
//                        subsegmentoProducto = tinyDB.getSubsegmentoProductosList(Valores.SHAREDPREFERENCES_CITAS_PRODUCTOS, SubsegmentoProducto.class);
//                        servicio = tinyDB.getServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, Servicio.class);
//                    }

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

        int idActividadActual = Valores.ID_ACTIVIDAD_CALIFICAR_OPORTUNIDAD;

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

    public void mandarInformacion(JsonAltaActividades jsonAltaActividades) {

        Call<ResponseBody> sendActividadAlta = apiInterface.setActividadAlta(
                prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""), jsonAltaActividades);

        final String json = new Gson().toJson(jsonAltaActividades);

        sendActividadAlta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    progressDialog.dismiss(); //Se cierra el ProgressDialog.

                    dialogModal = AlertDialogModal.showModalOneButton(context, comunicarAlertDialog,
                            "OPORTUNIDAD DESCARTADA", "Se DESCARTÓ la Oportunidad de Venta",
                            getString(R.string.citas_dialog_confirmar), ID_DESCARTAR_OPORTUNIDAD_SUCCESS);

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    Snackbar.make(view, getString(R.string.plan_time_picker_fail_descartar), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR_TIMEPICKERDIALOG", t.toString());
                progressDialog.dismiss(); //Se cierra el ProgressDialog.
                //Snackbar.make(view, getString(R.string.plan_time_picker_fail_descartar), Snackbar.LENGTH_LONG).show();
                FirebaseCrash.log("Error al mandar Cerrar Venta");
                FirebaseCrash.report(t);

                /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                Toast.makeText(context, getString(R.string.envio_descartar_offline), Toast.LENGTH_LONG).show();

                GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                        System.currentTimeMillis(),
                        Valores.ID_ENVIO_ALTA_ACTIVIDAD,
                        prefs.getString(Valores.SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO, ""),
                        json,
                        Valores.ESTATUS_NO_ENVIADO
                );
                GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                /**********************************************************************/
            }
        });
    }

    public void showProgress() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Espere un momento por favor...");
        progressDialog.show();
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

    public void guardarOportunidadVenta(List<SubsegmentoProducto> subsegmentoProducto, List<mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio> servicio) {

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

    public boolean revisarAlMenosUnProductoSeleccionado(List<SubsegmentoProducto> subsegmentoProductoList, List<Servicio> servicioList) {

        boolean estaSeleccionadoUnProductoServicio = false;

        if(idTipoProspecto == Valores.ID_TIPO_PROSPECTO_NUEVO_PROSPECTO) {
            if (subsegmentoProductoList.size() != 0 || servicioList.size() != 0) {
                for (Producto producto : subsegmentoProductoList.get(0).getProductos()) {
                    if (producto.getSeleccionado()) {
                        estaSeleccionadoUnProductoServicio = true;
                    }
                }

                for (Servicio servicio : servicioList) {
                    if (servicio.getSeleccionado()) {
                        estaSeleccionadoUnProductoServicio = true;
                    }
                }
            }
        }  else {
            estaSeleccionadoUnProductoServicio = true;
        }

        if(!estaSeleccionadoUnProductoServicio) {
            Toast.makeText(context, "Debe seleccionar al menos un Producto/Servicio", Toast.LENGTH_LONG).show();
        }

        return estaSeleccionadoUnProductoServicio;
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
