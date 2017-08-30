package com.telstock.tmanager.cemex.prospectos;

/**
 * Created by czamora on 9/12/16.
 */

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.telstock.tmanager.cemex.prospectos.adapters.AdapterContactos;
import com.telstock.tmanager.cemex.prospectos.interfaces.OnClickContacto;
import com.telstock.tmanager.cemex.prospectos.model.InformacionContacto;
import com.telstock.tmanager.cemex.prospectos.rest.ApiClient;
import com.telstock.tmanager.cemex.prospectos.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoCargoRealm;
import mx.com.tarjetasdelnoreste.realmdb.ContactosRealm;
import mx.com.tarjetasdelnoreste.realmdb.GeneralOfflineRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertDialogModal;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.interfaces.ComunicarAlertDialog;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCargoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ContactosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GeneralOfflineDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Cargo;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class AltaListaContactosSingle extends Fragment
        implements OnClickContacto, ComunicarAlertDialog {

    private Context context;
    private TextView txt_contactos_total;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Contacto> listaContactos;
    List<Contacto> listaContactosEnvio = new ArrayList<>();
    private final static int LISTA_VACIA = 0;
    private final static int LISTA = 1;

    private static final int CODIGO_DE_PERMISOS_LLAMAR = 100;
    private String telefonoLlamar;

    private ProgressDialog progressDialog;
    /****
     * VARIABLES PARA DAR DE ALTA UN CONTACTO
     ********/
    private Dialog dialogModalAltaContacto;

    EditText etNombre;
    EditText etApellidoPaterno;
    EditText etApellidoMaterno;
    EditText etTelefono;
    EditText etExtension;
    EditText etEmail;
    Spinner spCargo;
    EditText etComentarios;
    Button btnGuardar;
    Button btnCancelar;
    RadioButton radio_si;
    RadioButton radio_no;
    Button btnGuardarTodo;

    InformacionContacto informacionContacto;
    ApiInterface apiInterface;
    Call<List<Contacto>> contactoCall;

    //Variables que obtienen los datos recibidos de la pantalla de ProspectosFragment.
    private String idProspecto;
    private String nombreProspecto;

    List<CatalogoCargoDB> cargoListAll;
    ArrayList<String> cargoDesc = new ArrayList<>();
    ArrayAdapter<String> adapterCargo;

    //Modal que muestra mensaje de alerta.
    Dialog dialogModal;
    private static final String ID_SALIR = "idSalir";

    //Variable que obtiene el contexto de la interfaz.
    ComunicarAlertDialog comunicarAlertDialog;

    ProgressBar progressBar;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alta_lista_contactos_single, container, false);

        context = getActivity();

        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) context).setSupportActionBar(toolbar);
        ((AppCompatActivity) context).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) context).getSupportActionBar().setTitle(getString(R.string.contactos_titulo));

        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        comunicarAlertDialog = this;

        //Se recupera el idProspecto para obtener sus contactos.
        idProspecto = getArguments().getString(Valores.CONTACTOS_ID_PROSPECTO, "");
        nombreProspecto = getArguments().getString(Valores.CONTACTOS_NOMBRE_PROSPECTO, "");

        txt_contactos_total = (TextView) view.findViewById(R.id.txt_contactos_total);
        btnGuardarTodo = (Button) view.findViewById(R.id.btn_guardar_todo);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contactos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        btnGuardarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darAltaContactos();
            }
        });

        obtenerContactos();

        return view;
    }



    /********
     * MÉTODOS PARA DAR DE ALTA UN CONTACTO
     *****************/

    public void showAltaProspectoDatosContacto() {
        dialogModalAltaContacto = new Dialog(context);
        dialogModalAltaContacto.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModalAltaContacto.setContentView(R.layout.activity_alta_prospecto_datos_contacto);
        dialogModalAltaContacto.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialogModalAltaContacto.setCancelable(false);
        dialogModalAltaContacto.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialogModalAltaContacto.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        etNombre = (EditText) dialogModalAltaContacto.findViewById(R.id.etNombre);
        etApellidoPaterno = (EditText) dialogModalAltaContacto.findViewById(R.id.etApellidoPaterno);
        etApellidoMaterno = (EditText) dialogModalAltaContacto.findViewById(R.id.etApellidoMaterno);
        etTelefono = (EditText) dialogModalAltaContacto.findViewById(R.id.etTelefono);
        etExtension = (EditText) dialogModalAltaContacto.findViewById(R.id.etExtension);
        etEmail = (EditText) dialogModalAltaContacto.findViewById(R.id.etEmail);
        spCargo = (Spinner) dialogModalAltaContacto.findViewById(R.id.spCargo);
        etComentarios = (EditText) dialogModalAltaContacto.findViewById(R.id.etComentariosContacto);
        btnGuardar = (Button) dialogModalAltaContacto.findViewById(R.id.btnGuardarContacto);
        btnCancelar = (Button) dialogModalAltaContacto.findViewById(R.id.btnCancelarContacto);
        radio_si = (RadioButton) dialogModalAltaContacto.findViewById(R.id.radio_si);
        radio_no = (RadioButton) dialogModalAltaContacto.findViewById(R.id.radio_no);

        cargoDesc.clear(); //Se limipia la lista para evitar filas duplicadas.
        cargoListAll = CatalogoCargoRealm.mostrarListaCargo();
        cargoDesc.add(getString(R.string.formulario_spinners_default));
        for (CatalogoCargoDB catalogoCargoDB : cargoListAll) {
            cargoDesc.add(catalogoCargoDB.getDescripcion());
        }
        adapterCargo = new ArrayAdapter<>(context, R.layout.spinner_style, cargoDesc);
        spCargo.setAdapter(adapterCargo);

        //Se inicializa el Objeto.
        informacionContacto = new InformacionContacto();

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogModalAltaContacto.dismiss();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Si no hay campos vacíos
                if (!hayCamposVacios(view)) {
                    cerrarTeclado(view); //Se cierra el teclado.
                    guardarObjeto();
                }
            }
        });

        dialogModalAltaContacto.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        if (dialogModalAltaContacto.isShowing()) {
                            dialogModalAltaContacto.dismiss();

                        }
                        return true;
                    }
                }
                return false;
            }
        });

        dialogModalAltaContacto.show();
    }

    /** MÉTODO QUE VALIDA QUE LOS CAMPOS REQUERIDOS HAYAN SIDO LLENADOS **/
    public boolean hayCamposVacios(View view) {

        if (etNombre.getText().toString().isEmpty()) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_nombre), Snackbar.LENGTH_LONG).show();
            etNombre.requestFocus();
            return true;
        } else if (etApellidoPaterno.getText().toString().isEmpty()) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_apellido_paterno), Snackbar.LENGTH_LONG).show();
            etApellidoPaterno.requestFocus();
            return true;
        }
//        else if (etApellidoMaterno.getText().toString().isEmpty()) {
//            Snackbar.make(view, getString(R.string.formulario_fail_contactos_apellido_materno), Snackbar.LENGTH_LONG).show();
//            etApellidoMaterno.requestFocus();
//            return true;
//        }
        else if (spCargo.getSelectedItemPosition() == 0) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_cargo), Snackbar.LENGTH_LONG).show();
            spCargo.requestFocus();
            return true;
        } else if (etTelefono.getText().toString().isEmpty()) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_telefono), Snackbar.LENGTH_LONG).show();
            etTelefono.requestFocus();
            return true;
        }else if (etTelefono.getText().toString().length() != 10) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_telefono_logitud), Snackbar.LENGTH_LONG).show();
            etExtension.requestFocus();
            return true;
        } /* else if (etEmail.getText().toString().isEmpty()) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_email), Snackbar.LENGTH_LONG).show();
            etEmail.requestFocus();
            return true;
        } else if (etComentarios.getText().toString().isEmpty()) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_comentarios), Snackbar.LENGTH_LONG).show();
            etComentarios.requestFocus();
            return true;
        } else if (radio_si.isChecked() == false && radio_no.isChecked() == false) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_principal), Snackbar.LENGTH_LONG).show();
            return true;
        }*/
        else if (!etEmail.getText().toString().trim().equals("")) {
            if (!etEmail.getText().toString().trim().matches("^[_a-zA-Z0-9-.]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,3})$")) {
                Snackbar.make(view, getString(R.string.formulario_fail_contactos_email), Snackbar.LENGTH_LONG).show();
                etEmail.requestFocus();
                return true;
            }
        }

        return false;
    }

    /** MÉTODO QUE LLENA EL OBJETO DE CONTACTO **/
    public void guardarObjeto() {

        Contacto contactos = new Contacto();
        Cargo cargo = new Cargo();
        contactos.setApellidoMaterno(etApellidoMaterno.getText().toString());
        contactos.setApellidoPaterno(etApellidoPaterno.getText().toString());
        cargo.setId(cargoListAll.get(spCargo.getSelectedItemPosition() - 1).getId().intValue());
        cargo.setCargo(cargoListAll.get(spCargo.getSelectedItemPosition() - 1).getDescripcion());
        contactos.setCargo(cargo);
        contactos.setComentarios(etComentarios.getText().toString());
        contactos.setEmail(etEmail.getText().toString());
        contactos.setExtension(etExtension.getText().toString());
        contactos.setFotografia(""); ////// No se tiene contemplada la imagen en formulario de Contacto.
        contactos.setId(0);
        contactos.setNombres(etNombre.getText().toString());
        contactos.setIdAltaOffline(UUID.randomUUID().toString());

        contactos.setTelefono(etTelefono.getText().toString());
        if (radio_si.isChecked()) {
            //verifica si exiten contactos
            if (listaContactos.isEmpty() || listaContactos.get(0).getPrincipal() == null) {
                contactos.setPrincipal(true);
                actualizarLista(contactos);

            } else {
                //verifica que no exita contacto principál
                if (exiteContactoPrincipal()) {
                    //si exite la ventana pregunta si se desea remplazar
                    VentanaActulizarPrincipla(context, contactos);
                } else {
                    contactos.setPrincipal(true);
                    actualizarLista(contactos);

                }

            }

        } else {

            contactos.setPrincipal(false);
            actualizarLista(contactos);
        }


        dialogModalAltaContacto.dismiss();
    }

    /** MÉTODO QUE ACTUALIZA LA LISTA CON EL NUEVO CONTACTO AGREGADO **/
    public void actualizarLista(Contacto contactosDB) {
        if(listaContactos.get(0).getNombres().equals(R.string.alta_lista_vacia + "")) {
            listaContactos.remove(0);
        }
        listaContactos.add(contactosDB);
        listaContactosEnvio.add(contactosDB);
        txt_contactos_total.setText(getString(R.string.alta_lista_total) + " " + listaContactos.size());
        adapter = new AdapterContactos(context, listaContactos, LISTA, nombreProspecto, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Al crear al menos un contacto nuevo, se muestra el botón de guardado.
        btnGuardarTodo.setVisibility(View.VISIBLE);
    }


    public void VentanaActulizarPrincipla (Context context, final Contacto contactos) {

        AlertDialog.Builder contactoPrincipal = new AlertDialog.Builder(context);
        contactoPrincipal.setTitle("Ya exite contacto principal");
        contactoPrincipal.setMessage("¿Quieres guardar este contacto como principal?");

        contactoPrincipal.setCancelable(true);
        contactoPrincipal.setPositiveButton("SI", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int i) {

                for (int x = 0; x < listaContactos.size(); x++) {
                         if (listaContactos.get(x).getPrincipal()) {
                            listaContactos.get(x).setPrincipal(false);
                        }

                }
                contactos.setPrincipal(true);
                actualizarLista(contactos);
            }
        });
        contactoPrincipal.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int i) {
                contactos.setPrincipal(false);
                actualizarLista(contactos);
            }
        });
        contactoPrincipal.create();
        contactoPrincipal.show();


    }


    public boolean exiteContactoPrincipal() {
        boolean validacion;
        for (int i = 0; i < listaContactos.size(); i++) {
                 if (listaContactos.get(i).getPrincipal()) {
                    return validacion = true;
                }

        }
        return validacion = false;

    }

    /** MÉTODO QUE OBTIENE LOS CONTACTOS DEL PROSPECTO **/
    public void obtenerContactos() {

        //Se muestra el círculo de progreso.
        visibilidad(View.VISIBLE);

        contactoCall = apiInterface.getContactos(idProspecto);

        contactoCall.enqueue(new Callback<List<Contacto>>() {
            @Override
            public void onResponse(Call<List<Contacto>> call, Response<List<Contacto>> response) {
                if (response.body() != null && response.code() == 200) {
                    //Se eliminan las tablas para que se llenen nuevamente en caso de que haya
                    //existido algún cambio.
                    ContactosRealm.eliminarTablaContactos();
                    guardarContactos(response.body());
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {

                    Toast.makeText(context, getString(R.string.contactos_cargar_fail), Toast.LENGTH_LONG).show();
                    mostrarContactos(); //Se muestran los contactos del dispositivo.
                }
            }

            @Override
            public void onFailure(Call<List<Contacto>> call, Throwable t) {
                if (!contactoCall.isCanceled()) {
                    Log.e("GETCONTACTOS", t.toString());
                    FirebaseCrash.log("Error ContactosDB");
                    FirebaseCrash.report(t);
                    //Se quita el círculo de progreso.
                    visibilidad(View.GONE);
                    Toast.makeText(context, getString(R.string.contactos_cargar_fail), Toast.LENGTH_LONG).show();

                    mostrarContactos(); //Se muestran los contactos del dispositivo.
                }
            }
        });
    }

    /** MÉTODO QUE GUARDA LOS CONTACTOS OBTENIDOS EN LA BD **/
    private void guardarContactos(List<Contacto> contactoList) {

        List<ContactosDB> contactosDBList = new ArrayList<>();
        ContactosDB contactosDB;

        for (int i = 0; i < contactoList.size(); i++) {

            contactosDB = new ContactosDB(); //Se inicializa nuevamente para no duplicar los registros.

            //Llave compuesta del idProspecto y el idContacto.
            contactosDB.setCompoundId(idProspecto + contactoList.get(i).getId());

            contactosDB.setIdProspecto(idProspecto);
            contactosDB.setFotografia(contactoList.get(i).getFotografia());
            contactosDB.setNombres(contactoList.get(i).getNombres());
            contactosDB.setApellidoPaterno(contactoList.get(i).getApellidoPaterno());
            contactosDB.setApellidoMaterno(contactoList.get(i).getApellidoMaterno());
            contactosDB.setCargo(contactoList.get(i).getCargo().getCargo());
            contactosDB.setIdCargo(contactoList.get(i).getCargo().getId() + "");
            contactosDB.setTelefono(contactoList.get(i).getTelefono());
            contactosDB.setExtension(contactoList.get(i).getExtension());
            contactosDB.setEmail(contactoList.get(i).getEmail());
            contactosDB.setComentarios(contactoList.get(i).getComentarios());
            contactosDB.setPrincipal(contactoList.get(i).getPrincipal());

            //Se agrega el nuevo contacto a la lista de contactos.
            contactosDBList.add(contactosDB);
        }

        ContactosRealm.guardarListaContactos(contactosDBList);

        //Se muestran los contactos obtenidos.
        mostrarContactos();
    }

    /** MÉTODO QUE MUESTRA LOS CONTACTOS OBTENIDOS **/
    public void mostrarContactos() {

        //Se muestra el círculo de progreso.
        visibilidad(View.GONE);

        listaContactos = ContactosRealm.mostrarListaContactos(idProspecto);
        if (listaContactos.size() > 0) {

            txt_contactos_total.setText(getString(R.string.alta_lista_total) + " " + listaContactos.size());

            adapter = new AdapterContactos(context, listaContactos, LISTA, nombreProspecto, this);
            recyclerView.setAdapter(adapter);
        } else {

            txt_contactos_total.setText(getString(R.string.alta_lista_total) + " 0");

            Contacto contactosProspectoDB = new Contacto();
            contactosProspectoDB.setNombres(R.string.alta_lista_vacia + "");

            //Se limpia la lista, y se le agrega un registro "fantasma" con sólo un atributo para
            //activar el recyclerView.
            listaContactos = new ArrayList<>();
            listaContactos.add(contactosProspectoDB);
            adapter = new AdapterContactos(context, listaContactos, LISTA_VACIA, "", this);
            recyclerView.setAdapter(adapter);
        }
    }

    /** MÉTODO QUE ENVÍA LA LISTA DE CONTACTOS AL SERVIDOR **/
    public void darAltaContactos() {

        //Se muestra el ProgressDialog.
        mostrarProgressDialog();

        Call<Void> altaContacto = apiInterface.setContactos(idProspecto, listaContactosEnvio);

        final String json = new Gson().toJson(listaContactosEnvio);
        Log.d("JSON", json);

        altaContacto.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    Toast.makeText(context, getString(R.string.contactos_alta_success), Toast.LENGTH_SHORT).show();

                    //Activa el botón "back" del dispositivo.
                    Funciones.onBackPressedFunction(context, true);
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else if (response.code() == 400){ // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    } else {
                        Toast.makeText(context, getString(R.string.contactos_alta_fail), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.contactos_alta_fail), Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("ERRORALTA", t.toString());

                /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                Toast.makeText(context, getString(R.string.contacto_alta_offline), Toast.LENGTH_LONG).show();

                GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                        System.currentTimeMillis(),
                        Valores.ID_ENVIO_ALTA_CONTACTOS,
                        idProspecto,
                        json,
                        Valores.ESTATUS_NO_ENVIADO
                );
                GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                /**********************************************************************/

                progressDialog.dismiss();

                //Activa el botón "back" del dispositivo.
                Funciones.onBackPressedFunction(context, true);
            }
        });

        dialogModalAltaContacto.dismiss();
    }

    public void visibilidad(int visibility) {
        progressBar.setVisibility(visibility);
    }

    /** MÉTODO QUE LANZA EL INTENT DE LLAMADA **/
    public void realizarLlamada(String telefono) {

        Intent intentLlamar = new Intent(Intent.ACTION_CALL);
        intentLlamar.setData(Uri.parse("tel: " + telefono));

        startActivity(intentLlamar);
    }

    @Override
    public void onClickProspectos(View view) {

        showAltaProspectoDatosContacto();
    }

    @Override
    public void onClickLlamar(String telefono) {
        Toast.makeText(context, "Teléfono: " + telefono, Toast.LENGTH_LONG).show();
        telefonoLlamar = telefono;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{
                    android.Manifest.permission.CALL_PHONE
            }, CODIGO_DE_PERMISOS_LLAMAR);
        } else {
            realizarLlamada(telefono);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CODIGO_DE_PERMISOS_LLAMAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                realizarLlamada(telefonoLlamar);
            }
        }
    }

    /**
     * MÉTODO QUE CIERRA EL TECLADO
     **/
    public void cerrarTeclado(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_alta_contactos, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            if (btnGuardarTodo.getVisibility() == View.VISIBLE) {
                dialogModal = AlertDialogModal.showModalTwoButtonsNoTitle(context, comunicarAlertDialog,
                        "¿Estás seguro que deseas salir del Alta de Contactos?",
                        getString(R.string.btn_ok), getString(R.string.btn_cancelar), ID_SALIR);
            } else {
                Funciones.onBackPressedFunction(context, true);
            }

            return true;
        } else if (item.getItemId() == R.id.menu_contacto_nuevo) {

            if (listaContactos != null) {
                if (listaContactos.size() < 5) {
                    showAltaProspectoDatosContacto();
                } else {
                    Snackbar.make(getView(), getString(R.string.contactos_alta_maximo), Snackbar.LENGTH_LONG).show();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mostrarProgressDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Espere un momento por favor...");
        progressDialog.show();
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

                        if (btnGuardarTodo.getVisibility() == View.VISIBLE) {
                            dialogModal = AlertDialogModal.showModalTwoButtonsNoTitle(context, comunicarAlertDialog,
                                    "¿Estás seguro que deseas salir del Alta de Contactos?",
                                    getString(R.string.btn_ok), getString(R.string.btn_cancelar), ID_SALIR);
                        } else {
                            Funciones.onBackPressedFunction(context, true);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
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
}
