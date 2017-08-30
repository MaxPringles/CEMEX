package mx.com.tarjetasdelnoreste.realmdb.actividades;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoStatusObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.GeneralOfflineRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonGlobalProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.ObraDetalleRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GeneralOfflineDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ObraDetalleDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonObra.JsonObra;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiClient;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiInterface;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by USRMICRO10 on 20/12/2016.
 */

public class ObraDetalle extends AppCompatActivity implements View.OnClickListener {

    private Context context;

    private ProgressDialog progressDialog;
    private LinearLayout layoutObraDetalles;
    private ProgressBar progressBar;
    private TextView txtTituloObra;
    private EditText etFechaInicioObra;
    private EditText etDuracionMeses;
    private EditText etMesesRestantes;
    private Spinner spEstatusObra;
    private Spinner spObra;
    private LinearLayout btnCancelar;
    private LinearLayout btnGuardar;
    private LinearLayout footDescarga;

    private Calendar calendarInicio;

    ArrayAdapter<String> adapterEstatusObra;
    ArrayAdapter<String> adapterObra;
    List<CatalogoTipoObraDB> tipoObraListAll;
    List<CatalogoStatusObraDB> statusObraListAll;

    ArrayList<String> tipoObraDesc = new ArrayList<>();
    ArrayList<String> statusObraDesc = new ArrayList<>();

    long fechaInicioObra;
    boolean fechaVacia = false;
    JsonObra jsonObra = new JsonObra();

    String idProspecto;
    String nombreProspecto = "Obra: ";

    private ApiInterface apiInterface;

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obra_details_principal);

        context = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.obra_titulo));

        Intent i = getIntent();
        idProspecto = i.getStringExtra(Valores.OBRA_ID_PROSPECTO);

        layoutObraDetalles = (LinearLayout) findViewById(R.id.layout_obra_detalles);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        txtTituloObra = (TextView) findViewById(R.id.txt_titulo_obra);
        etFechaInicioObra = (EditText) findViewById(R.id.etx_fecha_inicio_obra);
        etDuracionMeses = (EditText) findViewById(R.id.etx_duracion_meses);
        etMesesRestantes = (EditText) findViewById(R.id.etx_meses_restantes);
        spEstatusObra = (Spinner) findViewById(R.id.sp_estatus_obra);
        spObra = (Spinner) findViewById(R.id.sp_obra);
        btnCancelar = (LinearLayout) findViewById(R.id.btn_cancelar_obra);
        btnGuardar = (LinearLayout) findViewById(R.id.btn_guardar_obra);
        footDescarga = (LinearLayout) findViewById(R.id.foot_descarga);

        calendarInicio = Calendar.getInstance();
        calendarInicio.setTimeInMillis(System.currentTimeMillis());
//        calendarInicio.set(Calendar.YEAR, 2000);
//        calendarInicio.set(Calendar.MONTH, Calendar.JANUARY);
//        calendarInicio.set(Calendar.DAY_OF_MONTH, 1);

        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        etFechaInicioObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendarInicio.set(year, month, dayOfMonth);
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        etFechaInicioObra.setText(df.format(calendarInicio.getTime()));
                        fechaInicioObra = calendarInicio.getTimeInMillis();
                    }
                }, calendarInicio.get(Calendar.YEAR), calendarInicio.get(Calendar.MONTH), calendarInicio.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnGuardar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        cargarSpinner();

        //Se coloca el nombre del prospecto en la cabecera de la pantalla.
        ProspectosDB prospectosDB = ProspectosRealm.mostrarProspectoId(idProspecto);

        if (prospectosDB != null) {
            nombreProspecto = "Obra: " + prospectosDB.getObra() + " - " + prospectosDB.getCliente();
        }

        txtTituloObra.setText(nombreProspecto);

        Gson gson = new Gson();
        String jsonGlobal = JsonGlobalProspectosRealm.mostrarJsonGlobalProspecto(idProspecto).getJsonGlobalProspectos();
        try {
            JSONObject jsonObject = new JSONObject(jsonGlobal);
            jsonObra = gson.fromJson(jsonObject.getString("obra"), JsonObra.class);

            /*DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            etFechaInicioObra.setText(df.format(jsonObra.getFechaInicioObra() * 1000));

            etDuracionMeses.setText(jsonObra.getDuracionMeses() + "");
            etMesesRestantes.setText(jsonObra.getMesesRestantes() + "");

            spEstatusObra.setSelection(adapterEstatusObra.getPosition(CatalogoStatusObraRealm.mostrarNombreEstatusObra(jsonObra.getIdEstatusObra().intValue())));
            spObra.setSelection(adapterObra.getPosition(CatalogoTipoObraRealm.mostrarNombreTipoObra(jsonObra.getIdTipoObra().intValue())));
            */

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Método que oculta los botones inferiores cuando sale el teclado y los hace aparecer
        //cuando el teclado se esconde. Esto sirve para evitar que los botones se encimen
        //al momento de salir el teclado.
        final View rootView = getWindow().getDecorView().getRootView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);

                int screenHeight = rootView.getHeight();
                int keyboardHeight = screenHeight - (rect.bottom - rect.top);
                if (keyboardHeight > screenHeight / 3) {

                    footDescarga.setVisibility(View.GONE);
                    //Especifica que el teclado siempre está visible, esto para evitar problemas con las
                    //animaciones del Toolbar y el scroll.
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                } else {

                    footDescarga.setVisibility(View.VISIBLE);
                }
            }
        });

        //Se recuperan los detalles de la obra del WS.
        obtenerDetallesObra();
    }

    public void obtenerDetallesObra() {

        //Se muestra el Spinner de carga.
        visibilidad(View.VISIBLE);

        Call<ObraDetalleDB> getObraDetalle = apiInterface.getObraDetalle(jsonObra.getId());

        getObraDetalle.enqueue(new Callback<ObraDetalleDB>() {
            @Override
            public void onResponse(Call<ObraDetalleDB> call, Response<ObraDetalleDB> response) {
                if (response.body() != null && response.code() == 200) {

                    ObraDetalleRealm.guardarObraDetalle(response.body());
                    
                    cargarCampos();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(getApplicationContext());
                } else {
                    Toast.makeText(context, "No se ha podido obtener la información. Se muestra aquella guardada en el dispositivo",
                            Toast.LENGTH_LONG).show();
                    cargarCampos();
                }
            }

            @Override
            public void onFailure(Call<ObraDetalleDB> call, Throwable t) {
                Toast.makeText(context, "No se ha podido obtener la información. Se muestra aquella guardada en el dispositivo",
                        Toast.LENGTH_LONG).show();
                cargarCampos();
            }
        });
    }

    public void cargarCampos() {

        ObraDetalleDB obraDetalleDB = ObraDetalleRealm.mostrarObraDetalle(jsonObra.getId());

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        if (obraDetalleDB.getFechaInicioObra() != 0) {
            etFechaInicioObra.setText(df.format(obraDetalleDB.getFechaInicioObra() * 1000));
        } else {
            fechaVacia = true;
        }

        etDuracionMeses.setText(obraDetalleDB.getDuracionMeses() + "");
        etMesesRestantes.setText(obraDetalleDB.getMesesRestantes() + "");

        try {
            spEstatusObra.setSelection(adapterEstatusObra.getPosition(CatalogoStatusObraRealm.mostrarNombreEstatusObra(obraDetalleDB.getIdEstatusObra().intValue())));
        } catch (Exception e) {
        }

        try {
            spObra.setSelection(adapterObra.getPosition(CatalogoTipoObraRealm.mostrarNombreTipoObra(obraDetalleDB.getIdTipoObra().intValue())));
        } catch (Exception e) {
        }

        //Se quita el Spinner de carga.
        visibilidad(View.GONE);
    }

    private void cargarSpinner() {
        statusObraListAll = CatalogoStatusObraRealm.mostrarListaStatusObra();
        statusObraDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoStatusObraDB statusObra : statusObraListAll) {
            statusObraDesc.add(statusObra.getDescripcion());
        }

        tipoObraListAll = CatalogoTipoObraRealm.mostrarListaTipoObra();
        tipoObraDesc.add(getString(R.string.formulario_spinners_default));
        for (CatalogoTipoObraDB catalogoTipoObraDB : tipoObraListAll) {
            tipoObraDesc.add(catalogoTipoObraDB.getDescripcion());
        }

        adapterEstatusObra = new ArrayAdapter<>(context, R.layout.spinner_style, statusObraDesc);
        spEstatusObra.setAdapter(adapterEstatusObra);
        adapterObra = new ArrayAdapter<>(context, R.layout.spinner_style, tipoObraDesc);
        spObra.setAdapter(adapterObra);
    }

    private boolean validarCampos() {
        if (etFechaInicioObra.getText().toString().isEmpty()) {
            Toast.makeText(context, getString(R.string.productos_fecha_inicio_obra_empty), Toast.LENGTH_LONG).show();
            etFechaInicioObra.requestFocus();
            return false;
        } else if (etDuracionMeses.getText().toString().isEmpty()) {
            Toast.makeText(context, getString(R.string.productos_duracion_meses_empty), Toast.LENGTH_LONG).show();
            etDuracionMeses.requestFocus();
            return false;
        } else if (etMesesRestantes.getText().toString().isEmpty()) {
            Toast.makeText(context, getString(R.string.productos_meses_restantes_empty), Toast.LENGTH_LONG).show();
            etMesesRestantes.requestFocus();
            return false;
        } else if (spEstatusObra.getSelectedItemPosition() == 0) {
            Toast.makeText(context, getString(R.string.productos_estatus_obra_empty), Toast.LENGTH_LONG).show();
            spEstatusObra.requestFocus();
            return false;
        }  else if (spObra.getSelectedItemPosition() == 0) {
            Toast.makeText(context, getString(R.string.productos_tipo_obra_empty), Toast.LENGTH_LONG).show();
            spObra.requestFocus();
            return false;
        } else if (Long.parseLong(etDuracionMeses.getText().toString()) < Long.parseLong(etMesesRestantes.getText().toString())) {
            Toast.makeText(context, getString(R.string.productos_error_duracion), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void enviar() {

        //Se muestra el ProgressDialog.
        mostrarProgressDialog();

        final String json = new Gson().toJson(jsonObra);
        Call<ResponseBody> putObra = apiInterface.putObra(jsonObra.getId(), jsonObra);

        putObra.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Toast.makeText(context, getString(R.string.obra_alta_success), Toast.LENGTH_LONG).show();
                    finish();
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
                        Toast.makeText(context, getString(R.string.obra_alta_fail), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, getString(R.string.obra_alta_fail), Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                /** EN CASO DE ERROR, GUARDAR LA INFORMACIÓN EN REALM PARA MANDARLA CUANDO SE DETECTE INTERNET **/
                Toast.makeText(context, getString(R.string.obra_alta_offline), Toast.LENGTH_LONG).show();

                GeneralOfflineDB generalOfflineDB = new GeneralOfflineDB(
                        System.currentTimeMillis(),
                        Valores.ID_ENVIO_ALTA_OBRA,
                        jsonObra.getId(),
                        json,
                        Valores.ESTATUS_NO_ENVIADO
                );
                GeneralOfflineRealm.guardarGeneralOffline(generalOfflineDB);
                /**********************************************************************/

                progressDialog.dismiss();

                finish();
            }
        });
    }

    public void visibilidad(int visibility) {

        progressBar.setVisibility(visibility);
        layoutObraDetalles.setVisibility(visibility == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    public void mostrarProgressDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Espere un momento por favor...");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_cancelar_obra) {
            onBackPressed(); //Ejecuta la misma acción que el botón físico de regresar del dispositivo.
        } else if (v.getId() == R.id.btn_guardar_obra) {
            if (validarCampos()) {

//                jsonObra.setFechaInicioObra(fechaInicioObra);
                jsonObra.setFechaInicioObra(fechaInicioObra / 1000);
                jsonObra.setDuracionMeses(Long.parseLong(etDuracionMeses.getText().toString()));
                jsonObra.setMesesRestantes(Long.parseLong(etMesesRestantes.getText().toString()));
                jsonObra.setIdEstatusObra(statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getId());
                jsonObra.setIdTipoObra(tipoObraListAll.get(spObra.getSelectedItemPosition() - 1).getId());
                jsonObra.setIdAltaOffline(UUID.randomUUID().toString());

                enviar();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); //Ejecuta la misma acción que el botón físico de regresar del dispositivo.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
