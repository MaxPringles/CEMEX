package com.telstock.tmanager.cemex.prospectos;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.telstock.tmanager.cemex.prospectos.funciones.TinyDB;
import com.telstock.tmanager.cemex.prospectos.model.ProductosSeleccionados;
import com.telstock.tmanager.cemex.prospectos.model.ProductosServiciosUpCross;
import com.telstock.tmanager.cemex.prospectos.rest.ApiClient;
import com.telstock.tmanager.cemex.prospectos.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.zelory.compressor.Compressor;
import mx.com.tarjetasdelnoreste.realmdb.ActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoAccionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoCampaniaRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoClienteRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoEstadosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoMunicipiosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoProductosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoServiciosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoStatusObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSubsegmentosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoProspectoRealm;
import mx.com.tarjetasdelnoreste.realmdb.CoordenadasRealm;
import mx.com.tarjetasdelnoreste.realmdb.GeneralOfflineRealm;
import mx.com.tarjetasdelnoreste.realmdb.MenuRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.actividades.MapsActivity;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertDialogModal;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.ConnectionDetector;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.funciones.LocationTracker;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarAlertDialog;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoAccionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCampaniaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstadosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMunicipiosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoProspectoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CoordenadasDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GeneralOfflineDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetProductosUpCross;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Campania;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Cliente;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Direccion;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Estado;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.EstatusObra;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.JsonAltaProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Municipio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.OportunidadVentaInicial;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Pais;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubSegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubsegmentosProducto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.TipoProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Ubicacion;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USRMICRO10 on 25/08/2016.
 */
public class AltaProspectoInformacionGeneral extends Fragment
        implements ComunicarAlertDialog {

    Context context;

    EditText etCliente;
    EditText etObra;
    EditText etComentariosProspecto;
    EditText etRazonSocial;
    EditText etRFC;
    EditText etPaginaWeb;
    EditText etCalle;
    EditText etNumero;
    EditText etNumeroInterior;
    EditText etColonia;
    EditText etCodigoPostal;
    EditText etComentariosDireccion;
    Button btnGuardar;
    Button btnCancelar;
    ImageButton btnFoto;
    Spinner spCampania;
    Spinner spSubSegmento;
    Spinner spTipoProspecto;
    Spinner spEstatusObra;
    Spinner spMunicipio;
    Spinner spEstado;
    Spinner spCliente;
    Spinner spObra;
    RadioGroup radioGroup;
    RadioButton radioSi;
    RadioButton radioNo;
    boolean tomoFoto = false;
    String uriFoto;
    String fotoBase64;
    LinearLayout llOfertaIntegralNuevoProspecto;
    LinearLayout llOfertaIntegralCrossUpSelling;
    LinearLayout llSubsegmento;

    String idFoto;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterTipoProspecto;
    ArrayAdapter<String> adapterSubsegmento;
    ArrayAdapter<String> adapterEstatusObra;
    ArrayAdapter<String> adapterCampania;
    ArrayAdapter<String> adapterEstado;
    ArrayAdapter<String> adapterMunicipio;
    List<CatalogoCampaniaDB> campaniaListAll;
    List<CatalogoStatusObraDB> statusObraListAll;
    List<CatalogoSubsegmentosDB> subsegmentosListAll;
    List<CatalogoTipoProspectoDB> tipoProspectoListAll;
    List<CatalogoEstadosDB> estadosListAll;
    List<CatalogoMunicipiosDB> municipiosListAll;
    List<ObraDB> obraListAllRealm;
    List<ObraDB> obraListAll = new ArrayList<>();
    List<ClienteDB> clienteListAllRealm;
    List<ClienteDB> clienteListAll = new ArrayList<>();

    ArrayList<String> campaniaDesc = new ArrayList<>();
    ArrayList<String> statusObraDesc = new ArrayList<>();
    ArrayList<String> subsegmentosDesc = new ArrayList<>();
    ArrayList<String> tipoProspectoDesc = new ArrayList<>();
    ArrayList<String> estadosDesc = new ArrayList<>();
    ArrayList<String> municipiosDesc = new ArrayList<>();
    ArrayList<String> clienteDesc = new ArrayList<>();
    ArrayList<String> obraDesc = new ArrayList<>();

    ClienteDB clienteSeleccionadoDB;
    ObraDB obraSeleccionadaDB;

    List<Contacto> contactos = new ArrayList<>();

    //Modal que muestra mensaje de alerta.
    Dialog dialogModalSalir;
    private static final String ID_SALIR = "idSalir";

    boolean esNuevoProspecto = false;

    //Variable que obtiene el contexto de la interfaz.
    ComunicarAlertDialog comunicarAlertDialog;

    private static final int CODIGO_DE_PERMISOS = 123;
    private static final int CODIGO_RESPUESTA_CAMARA = 100;
    private static final int GUARDAR_ROTACION_PANTALLA = 1;
    private static final int GUARDAR_Y_ENVIAR = 2;
    private static final int GUARDAR_Y_ENVIAR_OFFLINE = 3;

    //Bandera que indica que se están recuperando los datos de Municipio (debido a la rotación
    // del dispositivo). Esto evita que el Spinner de Estados lo resetee aún después de la
    //recuperación de los datos.
    private boolean recuperarMunicipioDeJson = false;

    JsonAltaProspecto jsonAltaProspecto;

    private ApiInterface apiInterface;

    //Variables para la oferta integral.
    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<Servicio> listaServicios = new ArrayList<>();

    Dialog dialogModal; //Modal que muestra mensaje de aviso.

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean salirFragment = false;

    //Variables que revisan la conexión a internet.
    ConnectionDetector connectionDetector;
    Boolean isInternetPresent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_alta_prospecto_informacion_general, container, false);

        context = getActivity();
        setRetainInstance(true);
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = prefs.edit();

        comunicarAlertDialog = this;

        etCliente = (EditText) view.findViewById(R.id.etCliente);
        etObra = (EditText) view.findViewById(R.id.etObra);
        etComentariosProspecto = (EditText) view.findViewById(R.id.etComentariosProspecto);
        etRazonSocial = (EditText) view.findViewById(R.id.etRazonSocial);
        etRFC = (EditText) view.findViewById(R.id.etRFC);
//        etTelefono = (EditText) view.findViewById(R.id.etTelefono);
        etPaginaWeb = (EditText) view.findViewById(R.id.etPaginaWeb);
        etCalle = (EditText) view.findViewById(R.id.etCalle);
        etNumero = (EditText) view.findViewById(R.id.etNumero);
        etNumeroInterior = (EditText) view.findViewById(R.id.etNumeroInterior);
        etColonia = (EditText) view.findViewById(R.id.etColonia);
        etCodigoPostal = (EditText) view.findViewById(R.id.etCodigoPostal);
        etComentariosDireccion = (EditText) view.findViewById(R.id.etComentariosDireccion);
        spEstatusObra = (Spinner) view.findViewById(R.id.spinnerEstatusObra);
        spSubSegmento = (Spinner) view.findViewById(R.id.spinnerSubsegmento);
        spTipoProspecto = (Spinner) view.findViewById(R.id.spinnerTipoProspecto);
        spCampania = (Spinner) view.findViewById(R.id.spinnerCampania);
        spMunicipio = (Spinner) view.findViewById(R.id.spinnerMunicipio);
        spEstado = (Spinner) view.findViewById(R.id.spinnerEstado);
        btnGuardar = (Button) view.findViewById(R.id.btnGuardarInformacionGeneral);
        btnCancelar = (Button) view.findViewById(R.id.btnCancelarInformacionGeneral);
        btnFoto = (ImageButton) view.findViewById(R.id.btnFoto);
        radioSi = (RadioButton) view.findViewById(R.id.radioSi);
        radioNo = (RadioButton) view.findViewById(R.id.radioNo);
        radioGroup =(RadioGroup)view.findViewById(R.id.radioOfertaIntegral);
        spCliente = (Spinner) view.findViewById(R.id.sp_cliente);
        spObra = (Spinner) view.findViewById(R.id.sp_obra);
        llOfertaIntegralNuevoProspecto = (LinearLayout) view.findViewById(R.id.layout_oferta_integral_nuevo_prospecto);
        llOfertaIntegralCrossUpSelling = (LinearLayout) view.findViewById(R.id.btn_seleccionar_productos_servicios);
        llSubsegmento = (LinearLayout) view.findViewById(R.id.layout_subsegmento);

        //Se ocultan los botones de oferta integral, para después mostrarlo dependiendo del tipo de prospecto.
        llOfertaIntegralCrossUpSelling.setVisibility(View.GONE);
        llOfertaIntegralNuevoProspecto.setVisibility(View.GONE);

        //Se elimina el botón de emojis
        etNumeroInterior.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

        //Se limpian los campos al iniciar el fragmento.
        limpiarCamposAlIniciar();
//        spMunicipio.setEnabled(false);

        //Se inicializa la interfaz que contiene los métodos de WS
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL, false);
        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_PRODUCTOS, false);
        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_SERVICIOS, false);
        tinyDB.remove(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL);

        //Pregunta permisos de cámara y para escribir en la memoria externa
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Deshabilita el botón para tomas fotografía
            btnFoto.setEnabled(false);
            this.requestPermissions(new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CODIGO_DE_PERMISOS);
        }

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarFoto();
            }
        });




        radioNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spSubSegmento.getSelectedItemPosition() == 0) {
                    Snackbar.make(view, getString(R.string.formulario_fail_subsegmento), Snackbar.LENGTH_LONG).show();
                    spSubSegmento.requestFocus();
//                    radioNo.setChecked(false);
                } else {
                    radioNo.setChecked(true);
                    showModalOfertaIntegral(getResources().getString(R.string.alert_seleccionar_productos_servicios));
                }

            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                // Si no hay campos vacíos
                if (!hayCamposVacios(view)) {
                    if (contactos.size() != 0) {
                        if (tomoFoto && uriFoto != null) {
                            mandarImagen(view);
                        } else {

                            showModalCarga();

                            idFoto = "";
                            //Se guarda toda la información del prospecto en el objeto.
                            guardarObjeto(view, GUARDAR_Y_ENVIAR);
                        }
                    } else {
                        showModalNoHayContactos(getResources().getString(R.string.alert_no_hay_contactos), view);
                    }
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModal = AlertDialogModal.showModalTwoButtonsNoTitle(context, comunicarAlertDialog,
                        "¿Estás seguro que deseas salir del Alta de Prospecto?",
                        getString(R.string.btn_ok), getString(R.string.btn_cancelar), ID_SALIR);
            }
        });

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!recuperarMunicipioDeJson) {
                    //Se limpia el array de Municipios y su adapter.
                    municipiosDesc.clear();
                    spMunicipio.setAdapter(null);

                    //Se hace la consulta para colocar los Municipios de acuerdo al Estado elegido.

                    if (position != 0) {
                        municipiosListAll =
                                CatalogoMunicipiosRealm.mostrarListaMunicipiosidPadre(estadosListAll.get(position - 1).getId());
                    }

                    //Se llena el array de Municipios.
                    municipiosDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
                    if (municipiosListAll != null) {
                        for (CatalogoMunicipiosDB catalogoMunicipiosDB : municipiosListAll) {
                            municipiosDesc.add(catalogoMunicipiosDB.getDescripcion());
                        }
                    }

                    //Se llena el Spinner de Municipios.
                    adapterMunicipio = new ArrayAdapter<>(context, R.layout.spinner_style, municipiosDesc);
                    spMunicipio.setAdapter(adapterMunicipio);
//                    spMunicipio.setEnabled(true);

                    if (obraSeleccionadaDB != null && spObra.getSelectedItemPosition() > 1) {
                        spMunicipio.setSelection(adapterMunicipio.getPosition(obraSeleccionadaDB.getDireccion().getMunicipio().getNombre()));
//                        spMunicipio.setEnabled(false);
                    }
                }

                recuperarMunicipioDeJson = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                obraDesc.clear();
                if (position == 1) {
                    etCliente.setVisibility(View.VISIBLE);

//                    etRazonSocial.setEnabled(true);
//                    etRFC.setEnabled(true);
//                    etPaginaWeb.setEnabled(true);

                    etRazonSocial.setText("");
                    etRFC.setText("");
                    etPaginaWeb.setText("");

                    obraDesc.add(getString(R.string.formulario_spinners_default));
                    obraDesc.add(getString(R.string.formulario_spinners_nueva_obra));
                    adapter = new ArrayAdapter<>(context, R.layout.spinner_style, obraDesc);
                    spObra.setAdapter(adapter);

                } else {
                    etCliente.setVisibility(View.GONE);

                    if (position != 0) {
                        clienteSeleccionadoDB = clienteListAll.get(position - 2);

                        try {
                            etRazonSocial.setText(clienteSeleccionadoDB.getRazonSocial());
                            etRFC.setText(clienteSeleccionadoDB.getRfc());
                            etPaginaWeb.setText(clienteSeleccionadoDB.getSitioweb());

                            //                        etRazonSocial.setEnabled(false);
//                        etRFC.setEnabled(false);
//                        etPaginaWeb.setEnabled(false);

                            obraListAllRealm = CatalogoObraRealm.mostrarListaObraIdCliente(clienteSeleccionadoDB.getId());
                            obraListAll.clear();
                            obraListAll.addAll(obraListAllRealm);

                            //Se acomoda el arreglo de acuerdo al nombre.
                            Collections.sort(obraListAll, new Comparator<ObraDB>() {
                                @Override
                                public int compare(ObraDB o1, ObraDB o2) {
                                    return o1.getNombre().toLowerCase().compareTo(o2.getNombre().toLowerCase());
                                }
                            });

                            //Se coloca la primera letra en mayúscula.
                            String sString;
                            for (ObraDB obra : obraListAll) {
                                sString = obra.getNombre().toLowerCase();
                                sString = sString.substring(0, 1).toUpperCase() + sString.substring(1);
                                obraDesc.add(sString);
                            }

                            obraDesc.add(0, getString(R.string.formulario_spinners_default));
                            obraDesc.add(1, getString(R.string.formulario_spinners_nueva_obra));

                            adapter = new ArrayAdapter<>(context, R.layout.spinner_style, obraDesc);
                            spObra.setAdapter(adapter);
                        }catch (Exception e) {
                            Toast.makeText(getContext(),"error al cargar los datos del cliente vuelva a intentarlo",Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack();
                            Funciones.onBackPressedFunction(context, true);
                            Log.e("error reaml", "" + e);
                        }


                    } else {

                        obraDesc.add(getString(R.string.formulario_spinners_default));
                        obraDesc.add(getString(R.string.formulario_spinners_nueva_obra));
                        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, obraDesc);
                        spObra.setAdapter(adapter);

                        etRazonSocial.setText("");
                        etRFC.setText("");
                        etPaginaWeb.setText("");

//                        etRazonSocial.setEnabled(true);
//                        etRFC.setEnabled(true);
//                        etPaginaWeb.setEnabled(true);

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spObra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    etObra.setVisibility(View.VISIBLE);

//                    spEstatusObra.setEnabled(true);
//                    spCampania.setEnabled(true);
//                    etCalle.setEnabled(true);
//                    etNumero.setEnabled(true);
//                    etColonia.setEnabled(true);
//                    etCodigoPostal.setEnabled(true);
//                    spEstado.setEnabled(true);
//                    spMunicipio.setEnabled(true);
                } else {
                    etObra.setVisibility(View.GONE);

                    if (position != 0) {
                        obraSeleccionadaDB = obraListAll.get(spObra.getSelectedItemPosition() - 2);

                        try {
                            etCalle.setText(obraSeleccionadaDB.getDireccion().getCalle());
                            etNumero.setText(obraSeleccionadaDB.getDireccion().getNumero());
                            etNumeroInterior.setText(obraSeleccionadaDB.getDireccion().getNumeroInterior());
                            etColonia.setText(obraSeleccionadaDB.getDireccion().getColonia());
                            etCodigoPostal.setText(obraSeleccionadaDB.getDireccion().getCodigoPostal());
                            String estatusObra = CatalogoStatusObraRealm.mostrarNombreEstatusObra(obraSeleccionadaDB.getIdEstatusObra());
                            String campania = CatalogoCampaniaRealm.mostrarNombreCampania(obraSeleccionadaDB.getIdCampania());
                            spEstatusObra.setSelection(adapterEstatusObra.getPosition(estatusObra));
                            spCampania.setSelection(adapterCampania.getPosition(campania));

                            spEstado.setSelection(adapterEstado.getPosition(obraSeleccionadaDB.getDireccion().getEstado().getNombre()));

                        }catch (Exception e){

                            Toast.makeText(getContext(),"error al cargar los datos de la obra vuelva a intentarlo",Toast.LENGTH_LONG).show();
                            getFragmentManager().popBackStack();
                            Funciones.onBackPressedFunction(context, true);
                            Log.e("error reaml",""+e);
                        }

//                        spEstatusObra.setEnabled(false);
//                        spCampania.setEnabled(false);
//                        etCalle.setEnabled(false);
//                        etNumero.setEnabled(false);
//                        etColonia.setEnabled(false);
//                        etCodigoPostal.setEnabled(false);
//                        spEstado.setEnabled(false);

                    } else {
                        etCalle.setText("");
                        etNumero.setText("");
                        etNumeroInterior.setText("");
                        etColonia.setText("");
                        etCodigoPostal.setText("");
                        spEstado.setSelection(0);
                        spMunicipio.setSelection(0);
                        spEstatusObra.setSelection(0);
                        spCampania.setSelection(0);

//                        spEstatusObra.setEnabled(true);
//                        spCampania.setEnabled(true);
//                        etCalle.setEnabled(true);
//                        etNumero.setEnabled(true);
//                        etColonia.setEnabled(true);
//                        etCodigoPostal.setEnabled(true);
//                        spEstado.setEnabled(true);
//                        spMunicipio.setEnabled(true);
                    }
                }

                //En caso de que no se seleccione la opción "Selecciona" ni "Nueva Obra".
                if (position > Valores.ID_TIPO_PROSPECTO_NUEVO_PROSPECTO) {
                    //Up-selling y Cross-selling.
                    tipoProspectoDesc.clear();

                    esNuevoProspecto = false;
                    editor.putBoolean(Valores.SHARED_PREFERENCES_ES_NUEVO_PROSPECTO, false);
                    editor.commit();

                    //Se muestra el apartado de Oferta Integral
                    llOfertaIntegralNuevoProspecto.setVisibility(View.GONE);
                    llOfertaIntegralCrossUpSelling.setVisibility(View.VISIBLE);
                    llSubsegmento.setVisibility(View.GONE);

                    tipoProspectoListAll = CatalogoTipoProspectoRealm.mostrarListaTipoProspectoSinNuevo();
                    tipoProspectoDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
                    for (CatalogoTipoProspectoDB tipoProspecto : tipoProspectoListAll) {
                        tipoProspectoDesc.add(tipoProspecto.getDescripcion());
                    }

                    adapter = new ArrayAdapter<>(context, R.layout.spinner_style, tipoProspectoDesc);
                    spTipoProspecto.setAdapter(adapter);
                } else {
                    //Nuevo prospecto.
                    tipoProspectoDesc.clear();

                    esNuevoProspecto = true;
                    editor.putBoolean(Valores.SHARED_PREFERENCES_ES_NUEVO_PROSPECTO, true);
                    editor.commit();

                    //Se muestra el apartado de Oferta Integral
                    llOfertaIntegralNuevoProspecto.setVisibility(View.VISIBLE);
                    llOfertaIntegralCrossUpSelling.setVisibility(View.GONE);
                    llSubsegmento.setVisibility(View.VISIBLE);
                    spSubSegmento.setEnabled(true);

                    tipoProspectoListAll = CatalogoTipoProspectoRealm.mostrarListaProspectoNuevo();
                    tipoProspectoDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
                    for (CatalogoTipoProspectoDB tipoProspecto : tipoProspectoListAll) {
                        tipoProspectoDesc.add(tipoProspecto.getDescripcion());
                    }

                    adapter = new ArrayAdapter<>(context, R.layout.spinner_style, tipoProspectoDesc);
                    spTipoProspecto.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTipoProspecto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!esNuevoProspecto) {

                    if(spObra.getSelectedItemPosition() != 0 && spTipoProspecto.getSelectedItemPosition() != 0) {

                        ProductosServiciosUpCross productosServiciosUpCross = new ProductosServiciosUpCross();

                        productosServiciosUpCross.setIdObra(obraSeleccionadaDB.getId());
                        productosServiciosUpCross.setIdTipoProspecto(tipoProspectoListAll.get(spTipoProspecto.getSelectedItemPosition() - 1).getId());

                        Call<List<GetProductosUpCross>> getProductosUpCross = apiInterface.getProductosUpCross(productosServiciosUpCross);

                        getProductosUpCross.enqueue(new Callback<List<GetProductosUpCross>>() {
                            @Override
                            public void onResponse(Call<List<GetProductosUpCross>> call, Response<List<GetProductosUpCross>> response) {
                                if (response.body() != null && response.code() == 200) {
                                    if(response.body().get(0).getItems().size() > 0) {
                                        llSubsegmento.setVisibility(View.VISIBLE);
                                        spSubSegmento.setEnabled(false);
                                        String descripcion = response.body().get(0).getDescripcion();
                                        spSubSegmento.setSelection(adapterSubsegmento.getPosition(descripcion));
                                    } else {
                                        llOfertaIntegralCrossUpSelling.setVisibility(View.GONE);
                                        Toast.makeText(context, getString(R.string.sin_productos_servicios_up_cross), Toast.LENGTH_LONG).show();

                                    }

                                } else {
                                    llOfertaIntegralCrossUpSelling.setVisibility(View.GONE);
                                    Toast.makeText(context, getString(R.string.sin_productos_servicios_up_cross), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<GetProductosUpCross>> call, Throwable t) {
                                Log.e("Error Producto",t.toString());
                            }
                        });
                    }



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        llOfertaIntegralCrossUpSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spTipoProspecto.getSelectedItemPosition() == 0) {
                    Snackbar.make(view, getString(R.string.formulario_fail_tipo_prospecto), Snackbar.LENGTH_LONG).show();
                    spTipoProspecto.requestFocus();
                } else {
                    showModalOfertaIntegral(getResources().getString(R.string.alert_seleccionar_productos_servicios));
                }

            }
        });

        //Se llama el método que llena los Spinners.
        cargarSpinners();

        //Revisa si "informacionGeneral" ya se ha llenado en el método onCreate(), de ser así,
        //entonces llena los campos de texto.
        if (jsonAltaProspecto != null) {
            llenarCampos();
        }
        //Después de llenar(o no) los campos de texto, el objeto se inicializa para poder ser
        //usado desde cero.
        //jsonAltaProspecto = new JsonAltaProspecto();

        return view;
    }

    /**
     * MÉTODO QUE LIMPIA LOS CAMPOS AL INICIAR EL FRAGMENTO
     **/
    public void limpiarCamposAlIniciar() {
        etCliente.setText("");
        etObra.setText("");
        etComentariosProspecto.setText("");
        etRazonSocial.setText("");
        etRFC.setText("");
        etPaginaWeb.setText("");
        etCalle.setText("");
        etNumero.setText("");
        etColonia.setText("");
        etCodigoPostal.setText("");
        etComentariosDireccion.setText("");
    }

    public void mandarImagen(final View view) {

        showModalCarga();

        File file = new File(uriFoto);

        /*Bitmap bmp = BitmapFactory.decodeFile(uriFoto);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);*/
        //File compressedImageBitmap = Compressor.getDefault(context).compressToFile(file);
        File compressedImageBitmap = new Compressor.Builder(context)
                .setQuality(50)
                .build()
                .compressToFile(file);


        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageBitmap);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("archivo", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "archivo";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);


        Call<ResponseBody> cargarArchivo = apiInterface.setCargaArchivo(body, description, UUID.randomUUID().toString());

        cargarArchivo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.body() != null && response.code() == 200) {
                    try {
                        idFoto = response.body().string();

                        //Se guarda toda la información del prospecto en el objeto.
                        guardarObjeto(view, GUARDAR_Y_ENVIAR);

                    } catch (IOException e) {
                        Log.e("CARGAR_ARCHIVO", e.toString());
                        FirebaseCrash.log("Error al dar de alta imagen " + response.code());
                        FirebaseCrash.report(e);
                        Snackbar.make(view, getString(R.string.formulario_envio_fail), Snackbar.LENGTH_LONG).show();
                        dialogModal.dismiss();
                    }
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else if (response.code() == 400){ // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Snackbar.make(view, jObjError.getString("message"), Snackbar.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    } else {
                        Snackbar.make(view, getString(R.string.formulario_envio_fail), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, getString(R.string.formulario_envio_fail), Snackbar.LENGTH_LONG).show();
                    FirebaseCrash.log("Error al dar de alta imagen " + response.code());
                    dialogModal.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("CARGAR_ARCHIVO", t.toString());
                dialogModal.dismiss();

                //Se guarda toda la información del prospecto en el objeto.
                guardarObjeto(view, GUARDAR_Y_ENVIAR_OFFLINE);
            }
        });

    }

    public void showModalNoHayContactos(String mensaje, final View view) {

        dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_aviso);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_txt_message = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        Button dialog_btn_confirmar = (Button) dialogModal.findViewById(R.id.btnConfirmarDialog);
        Button dialog_btn_cancelar = (Button) dialogModal.findViewById(R.id.btnCancelarDialog);

        dialog_txt_message.setText(mensaje);

        dialog_btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                enviar(view);
                dialogModal.dismiss();

                if (tomoFoto && uriFoto != null) {
                    mandarImagen(view);
                } else {
                    showModalCarga();

                    //Se guarda toda la información del prospecto en el objeto.
                    idFoto = "";
                    guardarObjeto(view, GUARDAR_Y_ENVIAR);
                }
            }
        });

        dialog_btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogModal.dismiss();
            }
        });

        dialogModal.show();
    }

    public void showModalOfertaIntegral(String mensaje) {

        dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_aviso);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView dialog_txt_message = (TextView) dialogModal.findViewById(R.id.dialog_txt_message);
        Button dialog_btn_confirmar = (Button) dialogModal.findViewById(R.id.btnConfirmarDialog);
        Button dialog_btn_cancelar = (Button) dialogModal.findViewById(R.id.btnCancelarDialog);

        dialog_txt_message.setText(mensaje);

        dialog_btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModal.dismiss();
                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.

                Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PROSPECTOS_OFERTA_INTEGRAL);
                if(esNuevoProspecto) {
                    intent.putExtra(Valores.BUNDLE_ID_SUBSEGMENTO, subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId() + "");
                } else {
                    intent.putExtra(Valores.BUNDLE_ID_SUBSEGMENTO, subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId() + "");
                    editor.putString(Valores.SHAREDPREFERENCES_ID_OBRA, obraSeleccionadaDB.getId());
                    editor.putString(Valores.SHAREDPREFERENCES_ID_TIPO_PROSPECTO, tipoProspectoListAll.get(spTipoProspecto.getSelectedItemPosition() - 1).getId() + "");
                    editor.commit();
                }
                startActivity(intent);
            }
        });

        dialog_btn_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogModal.dismiss();
                radioSi.setChecked(true);
                final TinyDB tinyDB = new TinyDB(context);
                OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
                try {
                    oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
                } catch (Exception e) {
                    Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
                }

                //Revisa si ya se han llenado los productos y servicios previamente (para esto,
                //basta con revisar si los servicios son null), en caso de que no se hayan llenado,
                //entonces se llena la oportunidad de venta con los productos y servicios vacíos.
                if (oportunidadVentaInicial.getServicios() == null || !esNuevoProspecto) {
                    llenarProductosVacios();
                    llenarServiciosVacios();
                }
            }
        });

        dialogModal.show();
    }

    private void llenarProductosVacios() {

        ArrayList<SubsegmentosProducto> listaSubSegmentosProductos = new ArrayList<>();

        //Se obtiene la lista de productos vacíos.
        prepararListaProductos();

        SubsegmentosProducto subSegmento = new SubsegmentosProducto();
        subSegmento.setProductos(listaProductos);
        subSegmento.setIdSubsegmento(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId() + "");
        subSegmento.setNombre(CatalogoSubsegmentosRealm.obtenerNombreSubsegmento(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId() + ""));
        subSegmento.setTodosSeleccion(false);
        listaSubSegmentosProductos.add(subSegmento);

        //Se guarda la lista de productos seleccionados en las shared preferences
        final TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListSubsegmentosSeleccionados(Valores.SHAREDPREFERENCES_SUBSEGMENTOS_SELECCIONADOS, listaSubSegmentosProductos);

        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_PRODUCTOS, false);

        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
        try {
            oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
        } catch (Exception e) {
            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
        }

        /*** DESDE AQUÍ SE INDICA QUE LA OFERTA NO SERÁ INTEGRAL ***/
        oportunidadVentaInicial.setEsOfertaIntegral(false);

        oportunidadVentaInicial.setSubsegmentosProductos(listaSubSegmentosProductos);

        tinyDB.putOfertaIntegral(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, oportunidadVentaInicial);
    }

    public void llenarServiciosVacios() {

        //Se obtiene la lista de servicios vacíos.
        prepararListaServicios();

        //Se guardan los servicios seleccionados en las shared preferences
        final TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListServiciosSeleccionados(Valores.SHAREDPREFERENCES_SERVICIOS_SELECCIONADOS, listaServicios);

        tinyDB.putBoolean(Valores.SHAREDPREFERENCES_TODOS_LOS_SERVICIOS, false);

        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();
        try {
            oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);
        } catch (Exception e) {
            Log.d("ERROR_OFERTA_INTEGRAL", e.toString());
        }

        oportunidadVentaInicial.setServicios(listaServicios);

        tinyDB.putOfertaIntegral(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, oportunidadVentaInicial);
    }

    private void prepararListaProductos() {
        //Inicializa las listas
        List<CatalogoProductoDB> listaTemp;

        Producto producto;

        //Obtiene la lista de los servicios de la base de datos
        listaTemp = CatalogoProductosRealm.mostrarListaProductoPorId(Long.parseLong(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId() + ""));
        for (CatalogoProductoDB c : listaTemp) {
            producto = new Producto();

            producto.setId(c.getId() + "");
            producto.setNombre(c.getDescripcion());
            producto.setSeleccionado(c.isChecked());

            listaProductos.add(producto);
        }
    }

    private void prepararListaServicios() {
        //Inicializa las listas
        List<CatalogoServiciosDB> listaTemp;
        Servicio servicio;

        //Obtiene la lista de los servicios de la base de datos
        listaTemp = CatalogoServiciosRealm.mostrarListaServicios();

        for (CatalogoServiciosDB c : listaTemp) {
            servicio = new Servicio();

            servicio.setId(c.getId() + "");
            servicio.setNombre(c.getDescripcion());
            servicio.setSeleccionado(c.isChecked());

            listaServicios.add(servicio);
        }
    }

    public void showModalCarga() {
        dialogModal = new Dialog(context);
        dialogModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setContentView(R.layout.dialog_carga);
        dialogModal.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModal.setCancelable(false);
        dialogModal.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogModal.show();
    }

    public void cargarSpinners() {

        connectionDetector = new ConnectionDetector(getActivity());
        isInternetPresent = connectionDetector.isConnectingToInternet(); //Verdadero o falso.

        campaniaListAll = CatalogoCampaniaRealm.mostrarListaCampania();
        campaniaDesc.add(getString(R.string.formulario_spinners_default));
        for (CatalogoCampaniaDB campaniaDB : campaniaListAll) {
            campaniaDesc.add(campaniaDB.getDescripcion());
        }

        statusObraListAll = CatalogoStatusObraRealm.mostrarListaStatusObra();
        statusObraDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoStatusObraDB statusObra : statusObraListAll) {
            statusObraDesc.add(statusObra.getDescripcion());
        }

        subsegmentosListAll = CatalogoSubsegmentosRealm.mostrarListaSubsegmentos();
        subsegmentosDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoSubsegmentosDB subsegmentos : subsegmentosListAll) {
            subsegmentosDesc.add(subsegmentos.getDescripcion());
        }

        tipoProspectoDesc.clear();
        tipoProspectoListAll = CatalogoTipoProspectoRealm.mostrarListaTipoProspecto();
        tipoProspectoDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoTipoProspectoDB tipoProspecto : tipoProspectoListAll) {
            tipoProspectoDesc.add(tipoProspecto.getDescripcion());
        }

        estadosListAll = CatalogoEstadosRealm.mostrarListaEstados();
        estadosDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoEstadosDB estados : estadosListAll) {
            estadosDesc.add(estados.getDescripcion());
        }

        //Verifica si tiene red para mostrar los clientes
        if(isInternetPresent) {
            clienteListAllRealm = CatalogoClienteRealm.mostrarListaCliente();
            clienteListAll.addAll(clienteListAllRealm);

            //Se acomoda el arreglo de acuerdo al nombre.
            Collections.sort(clienteListAll, new Comparator<ClienteDB>() {
                @Override
                public int compare(ClienteDB o1, ClienteDB o2) {
                    return o1.getNombre().toLowerCase().compareTo(o2.getNombre().toLowerCase());
                }
            });

            //Se coloca la primera letra en mayúscula.
            String sString;
            for (ClienteDB cliente : clienteListAll) {

                sString = cliente.getNombre();
                sString = sString.substring(0, 1).toUpperCase() + sString.substring(1);

                clienteDesc.add(sString);
            }

            //Collections.sort(clienteDesc);

        }

        clienteDesc.add(0, getString(R.string.formulario_spinners_default));
        clienteDesc.add(1, getString(R.string.formulario_spinners_nuevo_cliente));
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, clienteDesc);
        spCliente.setAdapter(adapter);

        //Se llenan los Spinners.
        adapterEstatusObra = new ArrayAdapter<>(context, R.layout.spinner_style, statusObraDesc);
        spEstatusObra.setAdapter(adapterEstatusObra);
        adapterSubsegmento = new ArrayAdapter<>(context, R.layout.spinner_style, subsegmentosDesc);
        spSubSegmento.setAdapter(adapterSubsegmento);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, tipoProspectoDesc);
        spTipoProspecto.setAdapter(adapter);
        adapterCampania = new ArrayAdapter<>(context, R.layout.spinner_style, campaniaDesc);
        spCampania.setAdapter(adapterCampania);
        adapterEstado = new ArrayAdapter<>(context, R.layout.spinner_style, estadosDesc);
        spEstado.setAdapter(adapterEstado);

    }

    public boolean hayCamposVacios(View view) {
        if (etCliente.getText().toString().isEmpty() && spCliente.getSelectedItemPosition() == 1) {
            Snackbar.make(view, getString(R.string.formulario_fail_cliente), Snackbar.LENGTH_LONG).show();
            etCliente.requestFocus();
            return true;
        } else if (etObra.getText().toString().isEmpty() && spObra.getSelectedItemPosition() == 1) {
            Snackbar.make(view, getString(R.string.formulario_fail_obra), Snackbar.LENGTH_LONG).show();
            etObra.requestFocus();
            return true;
        } else if (spEstatusObra.getSelectedItemPosition() == 0) {
            Snackbar.make(view, getString(R.string.formulario_fail_estatus_obra), Snackbar.LENGTH_LONG).show();
            spEstatusObra.requestFocus();
            return true;
        } else if (spSubSegmento.getSelectedItemPosition() == 0 && esNuevoProspecto) {
            Snackbar.make(view, getString(R.string.formulario_fail_subsegmento), Snackbar.LENGTH_LONG).show();
            spSubSegmento.requestFocus();
            return true;
        } else if (spTipoProspecto.getSelectedItemPosition() == 0) {
            Snackbar.make(view, getString(R.string.formulario_fail_tipo_prospecto), Snackbar.LENGTH_LONG).show();
            spTipoProspecto.requestFocus();
            return true;
        } else if (spCampania.getSelectedItemPosition() == 0) {
            Snackbar.make(view, getString(R.string.formulario_fail_campana), Snackbar.LENGTH_LONG).show();
            spCampania.requestFocus();
            return true;
        }
//        else if (etComentariosProspecto.getText().toString().isEmpty()) {
//            Snackbar.make(view, getString(R.string.formulario_fail_comentarios), Snackbar.LENGTH_LONG).show();
//            etComentariosProspecto.requestFocus();
//            return true;
//        }
        else if (spEstado.getSelectedItemPosition() == 0) {
            Snackbar.make(view, getString(R.string.formulario_fail_estados), Snackbar.LENGTH_LONG).show();
            spCampania.requestFocus();
            return true;
        } else if (spMunicipio.getSelectedItemPosition() == 0) {
            Snackbar.make(view, getString(R.string.formulario_fail_municipio), Snackbar.LENGTH_LONG).show();
            spCampania.requestFocus();
            return true;
        } else if (!etRFC.getText().toString().equals("")) {
            //Verifica que el RFC tenga una expresión válida (persona física).
            if (!etRFC.getText().toString().matches("^([A-Z][AEIOU][A-Z]{2})" +
                    "\\d{2}((01|03|05|07|08|10|12)(0[1-9]|[12]\\d|3[01])|02(0[1-9]|[12]\\d)|(04|06|09|11)(0[1-9]|[12]\\d|30))" +
                    "([A-Z0-9]{2}[0-9A])?$") && //Verifica que el RFC tenga una expresión válida (persona moral).
                    !etRFC.getText().toString().matches("^([A-Z&Ññ]{3})" +
                            "\\d{2}((01|03|05|07|08|10|12)(0[1-9]|[12]\\d|3[01])|02(0[1-9]|[12]\\d)|(04|06|09|11)(0[1-9]|[12]\\d|30))" +
                            "([A-Z0-9]{2}[0-9A])$")) {
                Snackbar.make(view, getString(R.string.formulario_fail_rfc), Snackbar.LENGTH_LONG).show();
                etRFC.requestFocus();
                return true;
            }

        } else if (!etPaginaWeb.getText().toString().equals("")) {
            //Verifica que la URL introducida sea válida.
            if (!isValidUrl(etPaginaWeb.getText().toString())) {
                Snackbar.make(view, getString(R.string.formulario_fail_sitio_web), Snackbar.LENGTH_LONG).show();
                etPaginaWeb.requestFocus();
                return true;
            }
        } else if (!etCodigoPostal.getText().toString().equals("")) {
            //Verificaa que el código postal sea válido.
            if (etCodigoPostal.getText().toString().length() < 5) {
                Snackbar.make(view, getString(R.string.formulario_fail_codigo_postal), Snackbar.LENGTH_LONG).show();
                etCodigoPostal.requestFocus();
                return true;
            }
        }

        return false;
    }

    /**
     * MÉTODO QUE VERIFICA QUE LA EXPRESIÓN INGRESADA SEA VÁLIDA COMO URL
     **/
    private boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

    public void guardarObjeto(View view, int razonGuardado) {

        /*informacionGeneral.setCliente(etCliente.getText().toString());
        informacionGeneral.setObra(etObra.getText().toString());
        informacionGeneral.setComentarios(etComentariosProspecto.getText().toString());
        //informacionGeneral.setCampania(etCampana.getText().toString());
        informacionGeneral.setRazonSocial(etRazonSocial.getText().toString());
        informacionGeneral.setRFC(etRFC.getText().toString());
        informacionGeneral.setTelefono(etTelefono.getText().toString());
        informacionGeneral.setCalle(etCalle.getText().toString());
        informacionGeneral.setNumero(etNumero.getText().toString());
        informacionGeneral.setColonia(etColonia.getText().toString());
        informacionGeneral.setCodigoPostal(etCodigoPostal.getText().toString());
        informacionGeneral.setComentariosUbicacion(etComentariosDireccion.getText().toString());*/

        jsonAltaProspecto = new JsonAltaProspecto();

        Cliente cliente = new Cliente();
        Direccion direccion = new Direccion();
        Estado estado = new Estado();
        Municipio municipio = new Municipio();
        Pais pais = new Pais();
        Ubicacion ubicacion = new Ubicacion();

        if (spEstado.getSelectedItemPosition() != 0) {
            estado.setId(estadosListAll.get(spEstado.getSelectedItemPosition() - 1).getId().toString());
            estado.setNombre(estadosListAll.get(spEstado.getSelectedItemPosition() - 1).getDescripcion());
        }
        if (spMunicipio.getSelectedItemPosition() != 0) {
            municipio.setId(municipiosListAll.get(spMunicipio.getSelectedItemPosition() - 1).getId().toString());
            municipio.setNombre(municipiosListAll.get(spMunicipio.getSelectedItemPosition() - 1).getDescripcion());
        }
        pais.setId("1");
        pais.setNombre("Mexico");

        LocationTracker gps = new LocationTracker(context);

        ubicacion.setLatitud(gps.getLatitude() + "");
        ubicacion.setLongitud(gps.getLongitude() + "");

        direccion.setCalle(etCalle.getText().toString());
        direccion.setCodigoPostal(etCodigoPostal.getText().toString());
        direccion.setColonia(etColonia.getText().toString());
        direccion.setComentarios(etComentariosDireccion.getText().toString());
        direccion.setEstado(estado);
        direccion.setMunicipio(municipio);
        direccion.setNumero(etNumero.getText().toString());
        direccion.setNumeroInterior(etNumeroInterior.getText().toString());
        direccion.setPais(pais);
        direccion.setUbicacion(ubicacion);

        Campania campania = new Campania();
        if (spCampania.getSelectedItemPosition() != 0) {
            campania.setDescripcion(campaniaListAll.get(spCampania.getSelectedItemPosition() - 1).getDescripcion());
            campania.setId(campaniaListAll.get(spCampania.getSelectedItemPosition() - 1).getId());
            campania.setIdCatalogo(campaniaListAll.get(spCampania.getSelectedItemPosition() - 1).getIdCatalogo());
            campania.setIdPadre(campaniaListAll.get(spCampania.getSelectedItemPosition() - 1).getIdPadre());
        }


        cliente.setCampania(campania);
        cliente.setComentarios(etComentariosProspecto.getText().toString());
        cliente.setDireccion(direccion);

        if (spCliente.getSelectedItemPosition() > 1) {
            cliente.setNombre(clienteListAll.get(spCliente.getSelectedItemPosition() - 2).getNombre());
        } else {
            cliente.setNombre(etCliente.getText().toString());
        }

        if (spObra.getSelectedItemPosition() > 1) {
            cliente.setObra(obraListAll.get(spObra.getSelectedItemPosition() - 2).getNombre());
        } else {
            cliente.setObra(etObra.getText().toString());
        }

        cliente.setRazonSocial(etRazonSocial.getText().toString());
        cliente.setRfc(etRFC.getText().toString());
        cliente.setTelefono("");
        cliente.setmSitioWeb(etPaginaWeb.getText().toString());

        EstatusObra estatusObra = new EstatusObra();
        if (spEstatusObra.getSelectedItemPosition() != 0) {
            estatusObra.setDescripcion(statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getDescripcion());
            estatusObra.setId(statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getId());
            estatusObra.setIdCatalogo(statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getIdCatalogo());
            estatusObra.setIdPadre(statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getIdPadre());
            estatusObra.setStatus(1);
        }

        SubSegmento subSegmento = new SubSegmento();
        if (spSubSegmento.getSelectedItemPosition() != 0) {
            subSegmento.setDescripcion(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getDescripcion());
            subSegmento.setId(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId());
            subSegmento.setIdCatalogo(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getIdCatalogo());
            subSegmento.setIdPadre(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getIdPadre());
        }

        TipoProspecto tipoProspecto = new TipoProspecto();
        if (spTipoProspecto.getSelectedItemPosition() != 0) {
            tipoProspecto.setDescripcion(tipoProspectoListAll.get(spTipoProspecto.getSelectedItemPosition() - 1).getDescripcion());
            tipoProspecto.setId(tipoProspectoListAll.get(spTipoProspecto.getSelectedItemPosition() - 1).getId());
            tipoProspecto.setIdCatalogo(tipoProspectoListAll.get(spTipoProspecto.getSelectedItemPosition() - 1).getIdCatalogo());
            tipoProspecto.setIdPadre(tipoProspectoListAll.get(spTipoProspecto.getSelectedItemPosition() - 1).getIdPadre());
        }

        if (radioSi.isChecked() && esNuevoProspecto) {
            //Guarda toda la oferta integral con true.
            jsonAltaProspecto.setOportunidadVentaInicial(traerOfertaIntegral());

        } else {
            //Lee SharedPreferences de oferta integral y elimina para que no se cree un loop
            final TinyDB tinyDB = new TinyDB(context);
            try {
                OportunidadVentaInicial oportunidadVentaInicial = tinyDB.getOportunidadDeVentaInicial(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL, OportunidadVentaInicial.class);

                jsonAltaProspecto.setOportunidadVentaInicial(oportunidadVentaInicial);
            } catch (Exception e) {
                Log.e("ERROR_OPORTUNIDAD_VENTA", e.toString());
            }
        }

        jsonAltaProspecto.setCliente(cliente);
        jsonAltaProspecto.setContactos(contactos);
        jsonAltaProspecto.setEstatusObra(estatusObra);
        jsonAltaProspecto.setFotografia(idFoto);
        jsonAltaProspecto.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
        jsonAltaProspecto.setSubSegmento(subSegmento);
        jsonAltaProspecto.setTipoProspecto(tipoProspecto);
        jsonAltaProspecto.setIdAltaOffline(UUID.randomUUID().toString());

        if (razonGuardado == GUARDAR_Y_ENVIAR) {
            enviar(view);
        } else if (razonGuardado == GUARDAR_Y_ENVIAR_OFFLINE) {

            String json = new Gson().toJson(jsonAltaProspecto);

            /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
            Toast.makeText(context, getString(R.string.prospecto_alta_offline), Toast.LENGTH_LONG).show();

            GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                    System.currentTimeMillis(),
                    Valores.ID_ENVIO_ALTA_PROSPECTOS,
                    uriFoto,
                    json,
                    Valores.ESTATUS_NO_ENVIADO
            );
            GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);

            TinyDB tinyDB = new TinyDB(context);
            tinyDB.remove(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL);
            tinyDB.remove(Valores.SHAREDPREFERENCES_SUBSEGMENTOS_SELECCIONADOS);
            tinyDB.remove(Valores.SHAREDPREFERENCES_SERVICIOS_SELECCIONADOS);
            tinyDB.remove(Valores.SHAREDPREFERENCES_INFORMACION_GENERAL);
            jsonAltaProspecto = null;

            //Indica que después de esta operación, se saldrá del fragmento.
            salirFragment = true;
            /**********************************************************************/

            //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
            Intent intent = new Intent();
            intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
            intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PROSPECTOS_PRINCIPAL);
            startActivity(intent);
        }
    }

    public void enviar(final View view) {

        final String json = new Gson().toJson(jsonAltaProspecto);

        Log.d("JSON", json);

        Call<ResponseBody> setProspecto = apiInterface.setProspecto(jsonAltaProspecto);

        //Indica que después de esta operación, se saldrá del fragmento.
        salirFragment = true;

        setProspecto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null && response.code() == 200) {
                    Toast.makeText(context, getString(R.string.formulario_envio_exitoso), Toast.LENGTH_LONG).show();

                    TinyDB tinyDB = new TinyDB(context);
                    tinyDB.remove(Valores.SHAREDPREFERENCES_OFERTA_INTEGRAL);
                    tinyDB.remove(Valores.SHAREDPREFERENCES_SUBSEGMENTOS_SELECCIONADOS);
                    tinyDB.remove(Valores.SHAREDPREFERENCES_SERVICIOS_SELECCIONADOS);
                    tinyDB.remove(Valores.SHAREDPREFERENCES_INFORMACION_GENERAL);
                    jsonAltaProspecto = null;

                    ProspectosRealm.eliminarTabla();
                    ActividadesRealm.eliminarTabla();

                    enviarCheckIn();

                    //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                    Intent intent = new Intent();
                    intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                    intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PROSPECTOS_PRINCIPAL);
                    startActivity(intent);
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Snackbar.make(view, jObjError.getString("message"), Snackbar.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    } else {
                        Snackbar.make(view, getString(R.string.formulario_envio_fail), Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(view, getString(R.string.formulario_envio_fail), Snackbar.LENGTH_LONG).show();
                    FirebaseCrash.log("Error Alta de Prospecto " + response.code());
                    Log.e("ERROR_ALTA_PROSPECTO", "Retrofit Response: " + response.errorBody().toString());
                }

                dialogModal.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("ERROR_ALTA_PROSPECTO", t.toString());
                FirebaseCrash.log("Error al dar de alta al prospecto");
                FirebaseCrash.report(t);

                /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                Toast.makeText(context, getString(R.string.prospecto_alta_offline), Toast.LENGTH_LONG).show();

                GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                        System.currentTimeMillis(),
                        Valores.ID_ENVIO_ALTA_PROSPECTOS,
                        "",
                        json,
                        Valores.ESTATUS_NO_ENVIADO
                );
                GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                /**********************************************************************/

                dialogModal.dismiss();

                //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
                Intent intent = new Intent();
                intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.MainActivity");
                intent.putExtra(Valores.FRAGMENT_GENERAL_MOSTRAR, Valores.FRAGMENT_PROSPECTOS_PRINCIPAL);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_DE_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Si aceptan los permisos activa el botón
                btnFoto.setEnabled(true);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODIGO_RESPUESTA_CAMARA) {
            if (resultCode == getActivity().RESULT_OK) {
                // Muestra la foto en el formulario
                Glide.with(context).load(uriFoto).thumbnail(0.8f).into(btnFoto);
                tomoFoto = true;

                //Se convierte la imagen en base64.
                /*new AsyncTask<Void,Void,Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        try {
                            Bitmap bm = Glide.
                                    with(context).
                                    load(new File(uriFoto)).
                                    asBitmap().
                                    into(500, 500).
                                    get();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] arrayImage = baos.toByteArray();
                            fotoBase64 = Base64.encodeToString(arrayImage, Base64.DEFAULT);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            FirebaseCrash.report(e);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                            FirebaseCrash.report(e);
                        }
                        return null;
                    }
                }.execute();*/
            }
        }
    }

    public void tomarFoto() {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, CODIGO_RESPUESTA_CAMARA);
    }

    public Uri setImageUri() {
        //Guarda la imagen en DCIM.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");

        Uri imgUri;

        if (Build.VERSION.SDK_INT< 24) {
            imgUri = Uri.fromFile(mediaStorageDir); //Guarda la Uri del archivo.
        } else {
            imgUri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID + ".provider",
                    mediaStorageDir);
        }


        uriFoto = mediaStorageDir.getAbsolutePath(); //Guarda la ruta del archivo.

        return imgUri;
    }

    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CEMEX");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    /**
     * MÉTODO QUE SE EJECUTA AL DETECTAR UN CAMBIO DE ROTACIÓN DEL DISPOSITIVO
     **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Revisa si ya se saldrá del fragment, en caso contrario, se guardan los datos
        //(suponiendo que se trata de una rotación de pantalla).
        if (!salirFragment) {
            //Al rotar la pantalla, se guardan los datos en el objeto.
            guardarObjeto(getView(), GUARDAR_ROTACION_PANTALLA);
            //Se guardan los datos del Objeto.
            TinyDB tinyDB = new TinyDB(context);
            tinyDB.putJsonAltaProspecto(Valores.SHAREDPREFERENCES_INFORMACION_GENERAL, jsonAltaProspecto);
        }
    }

    /**
     * MÉTODO QUE SE EJECUTA AL CREAR/RECREAR EL FRAGMENTO
     **/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se declara en este método, ya que es el primero en ser llamado.
        context = getContext();

        try {
            //Se recuperan los datos del Objeto.
            TinyDB tinyDB = new TinyDB(context);
            jsonAltaProspecto = tinyDB.getJsonAltaProspecto(
                    Valores.SHAREDPREFERENCES_INFORMACION_GENERAL, JsonAltaProspecto.class);
        } catch (Exception e) {
            //Significa que no hay ningún objeto guardado.
            FirebaseCrash.report(e);
        }
    }

    /**
     * MÉTODO QUE LLENA LOS CAMPOS AL RECREAR EL FRAGMENTO (DESPUÉS DE LA ROTACIÓN DEL DISPOSITIVO)
     **/
    public void llenarCampos() {

        if (jsonAltaProspecto.getCliente().getNombre() != null) {
            etCliente.setText(jsonAltaProspecto.getCliente().getNombre());
        }
        if (jsonAltaProspecto.getCliente().getObra() != null) {
            etObra.setText(jsonAltaProspecto.getCliente().getObra());
        }
        if (jsonAltaProspecto.getCliente().getComentarios() != null) {
            etComentariosProspecto.setText(jsonAltaProspecto.getCliente().getComentarios());
        }
        if (jsonAltaProspecto.getCliente().getRazonSocial() != null) {
            etRazonSocial.setText(jsonAltaProspecto.getCliente().getRazonSocial());
        }
        if (jsonAltaProspecto.getCliente().getRfc() != null) {
            etRFC.setText(jsonAltaProspecto.getCliente().getRfc());
        }
//        if (jsonAltaProspecto.getCliente().getTelefono() != null) {
//            etTelefono.setText(jsonAltaProspecto.getCliente().getTelefono());
//        }
        if (jsonAltaProspecto.getCliente().getDireccion().getCalle() != null) {
            etCalle.setText(jsonAltaProspecto.getCliente().getDireccion().getCalle());
        }
        if (jsonAltaProspecto.getCliente().getDireccion().getNumero() != null) {
            etNumero.setText(jsonAltaProspecto.getCliente().getDireccion().getNumero());
        }
        if (jsonAltaProspecto.getCliente().getDireccion().getColonia() != null) {
            etColonia.setText(jsonAltaProspecto.getCliente().getDireccion().getColonia());
        }
        if (jsonAltaProspecto.getCliente().getDireccion().getCodigoPostal() != null) {
            etCodigoPostal.setText(jsonAltaProspecto.getCliente().getDireccion().getCodigoPostal());
        }
        if (jsonAltaProspecto.getCliente().getDireccion().getComentarios() != null) {
            etComentariosDireccion.setText(jsonAltaProspecto.getCliente().getDireccion().getComentarios());
        }
        if (jsonAltaProspecto.getCliente().getmSitioWeb() != null) {
            etPaginaWeb.setText(jsonAltaProspecto.getCliente().getmSitioWeb());
        }

        /**** Recuperación de Spinners ****/
        if (jsonAltaProspecto.getEstatusObra().getDescripcion() != null) {
            for (int i = 0; i < statusObraListAll.size(); i++) {
                if (jsonAltaProspecto.getEstatusObra().getDescripcion().equals(statusObraListAll.get(i).getDescripcion())) {
                    spEstatusObra.setSelection(i + 1);
                }
            }
        }
        if (jsonAltaProspecto.getSubSegmento().getDescripcion() != null) {
            for (int i = 0; i < subsegmentosListAll.size(); i++) {
                if (jsonAltaProspecto.getSubSegmento().getDescripcion().equals(subsegmentosListAll.get(i).getDescripcion())) {
                    spSubSegmento.setSelection(i + 1);
                }
            }
        }
        if (jsonAltaProspecto.getTipoProspecto().getDescripcion() != null) {
            for (int i = 0; i < tipoProspectoListAll.size(); i++) {
                if (jsonAltaProspecto.getTipoProspecto().getDescripcion().equals(tipoProspectoListAll.get(i).getDescripcion())) {
                    spTipoProspecto.setSelection(i + 1);
                }
            }
        }
        if (jsonAltaProspecto.getCliente().getCampania().getDescripcion() != null) {
            for (int i = 0; i < campaniaListAll.size(); i++) {
                if (jsonAltaProspecto.getCliente().getCampania().getDescripcion().equals(campaniaListAll.get(i).getDescripcion())) {
                    spCampania.setSelection(i + 1);
                }
            }
        }
        if (jsonAltaProspecto.getCliente().getDireccion().getEstado().getNombre() != null) {
            for (int i = 0; i < estadosListAll.size(); i++) {
                if (jsonAltaProspecto.getCliente().getDireccion().getEstado().getNombre()
                        .equals(estadosListAll.get(i).getDescripcion())) {
                    spEstado.setSelection(i + 1);

                    municipiosListAll =
                            CatalogoMunicipiosRealm.mostrarListaMunicipiosidPadre(estadosListAll.get(i).getId());

                    recuperarMunicipioDeJson = true;
                }
            }
        }

        if (jsonAltaProspecto.getCliente().getDireccion().getMunicipio() != null) {
            if (jsonAltaProspecto.getCliente().getDireccion().getMunicipio().getNombre() != null) {
                for (int i = 0; i < municipiosListAll.size(); i++) {
                    if (jsonAltaProspecto.getCliente().getDireccion().getMunicipio().getNombre()
                            .equals(municipiosListAll.get(i).getDescripcion())) {

                        //Se llena el array de Municipios.
                        municipiosDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
                        if (municipiosListAll != null) {
                            for (CatalogoMunicipiosDB catalogoMunicipiosDB : municipiosListAll) {
                                municipiosDesc.add(catalogoMunicipiosDB.getDescripcion());
                            }
                        }

                        //Se llena el Spinner de Municipios.
                        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, municipiosDesc);
                        spMunicipio.setAdapter(adapter);
                        spMunicipio.setEnabled(true);

                        //Se selecciona el municipio guardado antes de la rotación del dispositivo.
                        spMunicipio.setSelection(i + 1);
                    }
                }
            }
        }
    }

    private OportunidadVentaInicial traerOfertaIntegral() {
        OportunidadVentaInicial oportunidadVentaInicial = new OportunidadVentaInicial();

        oportunidadVentaInicial.setEsOfertaIntegral(true);
        oportunidadVentaInicial.setSubsegmentosProductos(traerListaSubsegmento());
        oportunidadVentaInicial.setServicios(traerListaServicios());

        return oportunidadVentaInicial;
    }

    private ArrayList<Servicio> traerListaServicios() {

        ArrayList<Servicio> listaServicio = new ArrayList<>();
        Servicio serviciosSeleccionados;
        List<CatalogoServiciosDB> listaServiciosDB = CatalogoServiciosRealm.mostrarListaServicios();

        for (CatalogoServiciosDB c : listaServiciosDB) {
            serviciosSeleccionados = new Servicio();

            serviciosSeleccionados.setId(c.getId() + "");
            serviciosSeleccionados.setNombre(c.getDescripcion());
            serviciosSeleccionados.setSeleccionado(true);

            listaServicio.add(serviciosSeleccionados);
        }

        return listaServicio;
    }

    public ArrayList<SubsegmentosProducto> traerListaSubsegmento() {

        List<CatalogoSubsegmentosDB> listaSubsegmentosDB = CatalogoSubsegmentosRealm.mostrarListaSubsegmentos();

        ArrayList<SubsegmentosProducto> listaSubsegmento = new ArrayList<>();
        SubsegmentosProducto subsegmentosProducto;

        subsegmentosProducto = new SubsegmentosProducto();

        if (spSubSegmento.getSelectedItemPosition() > 0) {
            subsegmentosProducto.setIdSubsegmento(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId() + "");
            subsegmentosProducto.setNombre(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getDescripcion());
            subsegmentosProducto.setTodosSeleccion(true);
            subsegmentosProducto.setProductos(traerGrupoCompletoProducto(subsegmentosListAll.get(spSubSegmento.getSelectedItemPosition() - 1).getId()));
            listaSubsegmento.add(subsegmentosProducto);
        }


        return listaSubsegmento;
    }

    public ArrayList<Producto> traerGrupoCompletoProducto(long groupPosition) {
        ArrayList<Producto> listaProductos = new ArrayList<>();
        List<CatalogoProductoDB> alistHijo = CatalogoProductosRealm.mostrarListaProductoPorId(groupPosition);
        Producto producto;

        for (CatalogoProductoDB c : alistHijo) {
            producto = new Producto();

            producto.setNombre(c.getDescripcion());
            producto.setId(c.getId() + "");
            producto.setSeleccionado(true);

            listaProductos.add(producto);
        }

        return listaProductos;
    }

    /**
     * MENSAJE QUE SE RECIBE DE LA CLASE AltaListaContactos, RECIBE LOS CONTACTOS QUE
     * PERTENECEN AL PROSPECTO QUE SE ESTÁ DANDO DE ALTA
     **/
    @Subscribe
    public void getContactos(Contacto contacto) {
        contactos.add(contacto);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
        TinyDB tinyDB = new TinyDB(context);
        if (tinyDB.getBoolean(Valores.SHAREDPREFERENCES_ES_OFERTA_INTEGRAL)) {
            radioSi.setChecked(true);
        }
    }

    @Override
    public void alertDialogPositive(String idDialog) {

        switch (idDialog) {
            case ID_SALIR:
                //Ejecuta el método onBackPressed() de la actividad madre.
                Funciones.onBackPressedFunction(context, true);

                break;
        }
    }

    @Override
    public void alertDialogNegative(String idDialog) {

    }

    @Override
    public void alertDialogNeutral(String idDialog) {

    }
    public void enviarCheckIn(){

        LocationTracker gps;
        Calendar calendarCoordenada;
        MenuRealm realmDB;
        //Objeto que maneja Realml.
        realmDB = new MenuRealm();

        calendarCoordenada = Calendar.getInstance();
        int accion=2;
        List<CatalogoAccionDB> accionListAll = CatalogoAccionRealm.mostrarListaAccion();
        if(accionListAll!=null) {
            for (CatalogoAccionDB acciones : accionListAll) {

                if (acciones.getDescripcion().equals("Alta Prospecto")) {
                    accion = acciones.getId().intValue();
                }
            }
        }
        gps = new LocationTracker(context);
        if (gps.canGetLocation()) {

            Log.d("FUNCION_COORDENADA", gps.getLatitude() + "/" + gps.getLongitude());

            try {
                //Se abre una instancia propia de Realm, para que no haya problema en caso de que la
                //actividad principal de la aplicación sea destruida.
                realmDB.open();
                //Se guarda el registro con la hora, la latitud y longitud, y el status que indica si
                //ya se ha enviado o no.
                CoordenadasRealm.guardarListaCoordenadas(new CoordenadasDB(
                        calendarCoordenada.getTimeInMillis() / 1000,
                        gps.getLatitude(),
                        gps.getLongitude(),
                        Valores.ESTATUS_NO_ENVIADO,
                        accion));
                realmDB.close(); //Se cierra la instancia de Realm.
            } catch (Exception e) {
                Log.e("ERROR_COORDENADAS", e.toString());
            }
        } else {
            gps.showSettingsAlert();
        }

    }
}
