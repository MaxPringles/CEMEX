package com.telstock.tmanager.cemex.modulocitas;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.telstock.tmanager.cemex.modulocitas.funciones.TinyDB;
import com.telstock.tmanager.cemex.modulocitas.model.CheckboxOfertaIntegral;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoCompetidorRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoMotivoExclusionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoOportunidadVentaRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSemaforoRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoStatusObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCompetidorDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMotivoExclusionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoOportunidadVentaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSemaforoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Competidor;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.MotivoExclusion;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OportunidadVenta;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Semaforo;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by czamora on 10/16/16.
 */
public class ServiciosDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;

    EditText etxFechaInicioObra;
    EditText etxDuracionMeses;
    EditText etxMesesRestantes;
    EditText etxVolumenTotal;
    EditText etxVolumenRestante;
    EditText etxVolumenProximosMeses;
    EditText etxNumeroObra;
    EditText etxRazonPerdida;
    Spinner spSemaforo;
    Spinner spObra;
    Spinner spMotivoExclusion;
    Spinner spEstatusObra;
    Spinner spCompetidor;
    Spinner spOportunidadVenta;
    LinearLayout layoutVolumenTotal;
    LinearLayout layoutVolumenRestante;
    LinearLayout layoutVolumenProximo;
    LinearLayout layoutNumeroObra;
    LinearLayout llRecibirRespuesta;
    LinearLayout llPorqueFuePerdida;
//    View viewVolumenProximosMeses;

    private Button btnProductosCancelar;
    private Button btnProductosGuardar;

    private Servicio servicio = new Servicio();
    Toolbar toolbar;

    ArrayAdapter<String> adapter;
    List<CatalogoSemaforoDB> semaforoListAll;
    List<CatalogoStatusObraDB> statusObraListAll;
    List<CatalogoMotivoExclusionDB> motivosListAll;
    List<CatalogoCompetidorDB> competidorListAll;
    List<CatalogoOportunidadVentaDB> oportunidadVentaListAll;
    List<CatalogoTipoObraDB> tipoObraListAll;
    List<Servicio> listaServicio = new ArrayList<>();

    ArrayList<String> semaforoDesc = new ArrayList<>();
    ArrayList<String> statusObraDesc = new ArrayList<>();
    ArrayList<String> motivosDesc = new ArrayList<>();
    ArrayList<String> competidorDesc = new ArrayList<>();
    ArrayList<String> oportunidadVentaDesc = new ArrayList<>();
    ArrayList<String> tipoObraDesc = new ArrayList<>();

    private Calendar calendarInicio;

    LinearLayout layoutMotivoExclusion;
    LinearLayout layoutRazonDescartada;
    //Variables que manejan la información obtenida.
    int posicionServicio;
    boolean seleccionadoServicio;
    boolean mostrarTodosLosDetalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_productos_details);

        context = this;

        posicionServicio = getIntent().getIntExtra("posicionServicio", 0);
        seleccionadoServicio = getIntent().getBooleanExtra("seleccionadoServicio", false);

        TinyDB tinyDB = new TinyDB(context);
        try {
            mostrarTodosLosDetalles = tinyDB.getBoolean(Valores.SHAREDPREFERENCES_MOSTRAR_TODOS_LOS_DETALLES);
            listaServicio = tinyDB.getServiciosList(OfertaIntegralCitasFragment.STRING_SERVICIOS, Servicio.class);
            servicio = tinyDB.getServiciosList(OfertaIntegralCitasFragment.STRING_SERVICIOS, Servicio.class).get(posicionServicio);
            Log.d("", "");
        } catch (Exception e) {
            Log.d("", "");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(servicio.getNombre());

        calendarInicio = Calendar.getInstance();
        calendarInicio.set(Calendar.YEAR, 2000);
        calendarInicio.set(Calendar.MONTH, Calendar.JANUARY);
        calendarInicio.set(Calendar.DAY_OF_MONTH, 1);

        etxFechaInicioObra = (EditText) findViewById(R.id.etx_fecha_inicio_obra);
        etxDuracionMeses = (EditText) findViewById(R.id.etx_duracion_meses);
        etxMesesRestantes = (EditText) findViewById(R.id.etx_meses_restantes);
        etxVolumenTotal = (EditText) findViewById(R.id.etx_volumen_total);
        etxVolumenRestante = (EditText) findViewById(R.id.etx_volumen_restante);
        etxVolumenProximosMeses = (EditText) findViewById(R.id.etx_volumen_proximos_meses);
        etxNumeroObra = (EditText) findViewById(R.id.etx_numero_obra);
        etxRazonPerdida = (EditText) findViewById(R.id.etx_razon_perdida);
        btnProductosCancelar = (Button) findViewById(R.id.btn_productos_cancelar);
        btnProductosGuardar = (Button) findViewById(R.id.btn_productos_guardar);
//        viewVolumenProximosMeses = findViewById(R.id.view_volumen_proximos_meses);

        spSemaforo = (Spinner) findViewById(R.id.sp_semaforo);
        spObra = (Spinner) findViewById(R.id.sp_obra);
        spMotivoExclusion = (Spinner) findViewById(R.id.sp_motivos);
        spEstatusObra = (Spinner) findViewById(R.id.sp_estatus_obra);
        spCompetidor = (Spinner) findViewById(R.id.sp_competidor);
        spOportunidadVenta = (Spinner) findViewById(R.id.sp_oportunidad_venta);
        layoutVolumenTotal = (LinearLayout) findViewById(R.id.layout_volumen_total);
        layoutVolumenRestante = (LinearLayout) findViewById(R.id.layout_volumen_restante);
        layoutVolumenProximo = (LinearLayout) findViewById(R.id.layout_volumen_proximo);
        layoutNumeroObra = (LinearLayout) findViewById(R.id.layout_numero_obra);
        layoutMotivoExclusion = (LinearLayout) findViewById(R.id.layout_productos_motivo_exclusion);
        layoutRazonDescartada = (LinearLayout) findViewById(R.id.layout_razon_descartada);
        llRecibirRespuesta = (LinearLayout) findViewById(R.id.layout_recibir_respuesta);
        llPorqueFuePerdida = (LinearLayout) findViewById(R.id.layout_porque_fue_perdida);

        btnProductosCancelar.setOnClickListener(this);
        btnProductosGuardar.setOnClickListener(this);

        cargarSpinners();

        if (servicio != null) {
            llenarCampos();
        }

        ocultarCampos();

        etxFechaInicioObra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar cal;
                        calendarInicio.set(year, month, dayOfMonth);
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                        etxFechaInicioObra.setText(df.format(calendarInicio.getTime()));
                    }
                }, calendarInicio.get(Calendar.YEAR), calendarInicio.get(Calendar.MONTH), calendarInicio.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Se quitan los campos que indican exclusión.
        if (seleccionadoServicio) {
            layoutMotivoExclusion.setVisibility(View.GONE);
            layoutRazonDescartada.setVisibility(View.GONE);
            etxRazonPerdida.setVisibility(View.GONE);
            llPorqueFuePerdida.setVisibility(View.GONE);
        }

        if(!mostrarTodosLosDetalles) {
            llRecibirRespuesta.setVisibility(View.GONE);
        }
    }

    public void ocultarCampos() {
//        layoutVolumenTotal.setVisibility(View.GONE);
//        layoutVolumenRestante.setVisibility(View.GONE);
//        layoutVolumenProximo.setVisibility(View.GONE);
//        viewVolumenProximosMeses.setVisibility(View.GONE);
        layoutNumeroObra.setVisibility(View.GONE);
//        etxVolumenTotal.setVisibility(View.GONE);
//        etxVolumenRestante.setVisibility(View.GONE);
//        etxVolumenProximosMeses.setVisibility(View.GONE);
        etxNumeroObra.setVisibility(View.GONE);
    }

    public void cargarSpinners() {

        semaforoListAll = CatalogoSemaforoRealm.mostrarListaSemaforo();
        semaforoDesc.add(getString(R.string.formulario_spinners_default));
        for (CatalogoSemaforoDB catalogoSemaforoDB : semaforoListAll) {
            semaforoDesc.add(catalogoSemaforoDB.getDescripcion());
        }

        statusObraListAll = CatalogoStatusObraRealm.mostrarListaStatusObra();
        statusObraDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoStatusObraDB statusObra : statusObraListAll) {
            statusObraDesc.add(statusObra.getDescripcion());
        }

        motivosListAll = CatalogoMotivoExclusionRealm.mostrarListaMotivoExclusion();
        motivosDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoMotivoExclusionDB catalogoMotivoExclusionDB : motivosListAll) {
            motivosDesc.add(catalogoMotivoExclusionDB.getDescripcion());
        }

        competidorListAll = CatalogoCompetidorRealm.mostrarListaCompetidor();
        competidorDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoCompetidorDB catalogoCompetidorDB : competidorListAll) {
            competidorDesc.add(catalogoCompetidorDB.getDescripcion());
        }

        oportunidadVentaListAll = CatalogoOportunidadVentaRealm.mostrarListaOportunidadVenta();
        oportunidadVentaDesc.add(getString(R.string.formulario_spinners_default)); //Coloca opción por default.
        for (CatalogoOportunidadVentaDB catalogoOportunidadVentaDB : oportunidadVentaListAll) {
            oportunidadVentaDesc.add(catalogoOportunidadVentaDB.getDescripcion());
        }

        tipoObraListAll = CatalogoTipoObraRealm.mostrarListaTipoObra();
        tipoObraDesc.add(getString(R.string.formulario_spinners_default));
        for (CatalogoTipoObraDB catalogoTipoObraDB : tipoObraListAll) {
            tipoObraDesc.add(catalogoTipoObraDB.getDescripcion());
        }

        //Se llenan los Spinners.
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, semaforoDesc);
        spSemaforo.setAdapter(adapter);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, statusObraDesc);
        spEstatusObra.setAdapter(adapter);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, tipoObraDesc);
        spObra.setAdapter(adapter);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, motivosDesc);
        spMotivoExclusion.setAdapter(adapter);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, competidorDesc);
        spCompetidor.setAdapter(adapter);
        adapter = new ArrayAdapter<>(context, R.layout.spinner_style, oportunidadVentaDesc);
        spOportunidadVenta.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_productos_cancelar) {

            CheckboxOfertaIntegral checkboxOfertaIntegral = new CheckboxOfertaIntegral();
            checkboxOfertaIntegral.setPosition(posicionServicio);
            if (seleccionadoServicio) {
                checkboxOfertaIntegral.setEstado(false);
            } else {
                checkboxOfertaIntegral.setEstado(true);
            }

            TinyDB tinyDB = new TinyDB(context);
            tinyDB.putCheckboxOfertaIntegral(Valores.SHAREDPREFERENCES_CHECKBOX_OFERTA_INTEGRAL, checkboxOfertaIntegral);

            tinyDB.putBoolean(Valores.SHAREDPREFERENCES_CANCELO_PRODUCTO_SERVICIO, true);

            super.onBackPressed(); //Ejecuta el botón "back" del dispositivo.

        } else if (v.getId() == R.id.btn_productos_guardar) {

            if (validarCampos()) {
//                listaServicio.get(posicionServicio).setFechaInicioObra(etxFechaInicioObra.getText().toString());
//                listaServicio.get(posicionServicio).setDuracionMeses(Long.parseLong(etxDuracionMeses.getText().toString()));
//                listaServicio.get(posicionServicio).setMesesRestantes(Long.parseLong(etxMesesRestantes.getText().toString()));

                listaServicio.get(posicionServicio).setPeriodoTotal(Double.parseDouble(etxVolumenTotal.getText().toString()));
                listaServicio.get(posicionServicio).setPeriodoRestante(Double.parseDouble(etxVolumenRestante.getText().toString()));
                if(mostrarTodosLosDetalles) {
                    listaServicio.get(posicionServicio).setPeriodoProximosMeses(Double.parseDouble(etxVolumenProximosMeses.getText().toString()));
                }

                if (!seleccionadoServicio) { //Guarda la razón de pérdida en caso de que el producto sea deseleccionado.
                    listaServicio.get(posicionServicio).setComentariosMotivoPerdida(etxRazonPerdida.getText().toString());
                } else {
                    listaServicio.get(posicionServicio).setComentariosMotivoPerdida("");
                }

                Semaforo semaforo = new Semaforo();
                semaforo.setDescripcion(semaforoListAll.get(spSemaforo.getSelectedItemPosition() - 1 ).getDescripcion());
                semaforo.setId(semaforoListAll.get(spSemaforo.getSelectedItemPosition() - 1).getId());
                semaforo.setIdCatalogo((long) semaforoListAll.get(spSemaforo.getSelectedItemPosition() - 1).getIdCatalogo());
                semaforo.setIdPadre((long) semaforoListAll.get(spSemaforo.getSelectedItemPosition() - 1).getIdPadre());
                listaServicio.get(posicionServicio).setSemaforo(semaforo);

//                Obra obra = new Obra();
//                obra.setDescripcion(tipoObraListAll.get(spObra.getSelectedItemPosition() - 1).getDescripcion());
//                obra.setId(tipoObraListAll.get(spObra.getSelectedItemPosition() - 1).getId());
//                obra.setIdCatalogo((long) tipoObraListAll.get(spObra.getSelectedItemPosition() - 1).getIdCatalogo());
//                obra.setIdPadre((long) tipoObraListAll.get(spObra.getSelectedItemPosition() - 1).getIdPadre());
//                listaServicio.get(posicionServicio).setObra(obra);

                MotivoExclusion motivoExclusion = new MotivoExclusion();
                if (!seleccionadoServicio) {
                    motivoExclusion.setDescripcion(motivosListAll.get(spMotivoExclusion.getSelectedItemPosition() - 1).getDescripcion());
                    motivoExclusion.setId(motivosListAll.get(spMotivoExclusion.getSelectedItemPosition() - 1).getId());
                    motivoExclusion.setIdPadre((long) motivosListAll.get(spMotivoExclusion.getSelectedItemPosition() - 1).getIdPadre());
                    motivoExclusion.setIdCatalogo((long) motivosListAll.get(spMotivoExclusion.getSelectedItemPosition() - 1).getIdCatalogo());
                }
                listaServicio.get(posicionServicio).setMotivoExclusion(motivoExclusion);

//                EstatusObra estatusObra = new EstatusObra();
//                estatusObra.setDescripcion(statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getDescripcion());
//                estatusObra.setId(statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getId());
//                estatusObra.setIdCatalogo((long) statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getIdCatalogo());
//                estatusObra.setIdPadre((long) statusObraListAll.get(spEstatusObra.getSelectedItemPosition() - 1).getIdPadre());
//                listaServicio.get(posicionServicio).setEstatusObra(estatusObra);

                if(mostrarTodosLosDetalles) {
                    Competidor competidor = new Competidor();
                    competidor.setDescripcion(competidorListAll.get(spCompetidor.getSelectedItemPosition() - 1).getDescripcion());
                    competidor.setId(competidorListAll.get(spCompetidor.getSelectedItemPosition() - 1).getId());
                    competidor.setIdCatalogo((long) competidorListAll.get(spCompetidor.getSelectedItemPosition() - 1).getIdCatalogo());
                    competidor.setIdPadre((long) competidorListAll.get(spCompetidor.getSelectedItemPosition() - 1).getIdPadre());
                    listaServicio.get(posicionServicio).setCompetidor(competidor);

                    OportunidadVenta oportunidadVenta = new OportunidadVenta();
                    oportunidadVenta.setDescripcion(oportunidadVentaListAll.get(spOportunidadVenta.getSelectedItemPosition() - 1).getDescripcion());
                    oportunidadVenta.setId(oportunidadVentaListAll.get(spOportunidadVenta.getSelectedItemPosition() - 1).getId());
                    oportunidadVenta.setIdCatalogo((long) oportunidadVentaListAll.get(spOportunidadVenta.getSelectedItemPosition() - 1).getIdCatalogo());
                    oportunidadVenta.setIdPadre((long) oportunidadVentaListAll.get(spOportunidadVenta.getSelectedItemPosition() - 1).getIdPadre());
                    listaServicio.get(posicionServicio).setOportunidadVenta(oportunidadVenta);
                }

                TinyDB tinyDB = new TinyDB(context);
                tinyDB.putServiciosList(Valores.SHAREDPREFERENCES_CITAS_SERVICIOS, listaServicio);

//                Intent intent = new Intent();
//                intent.putExtra(OfertaIntegralCitasFragment.STRING_SERVICIOS, servicio.getNombre());
//                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    public void llenarCampos() {

//        etxFechaInicioObra.setText(servicio.getFechaInicioObra());
//        etxDuracionMeses.setText(servicio.getDuracionMeses() + "");
//        etxMesesRestantes.setText(servicio.getMesesRestantes() + "");
        if(servicio.getPeriodoTotal() != 0) {
            etxVolumenTotal.setText(servicio.getPeriodoTotal() + "");
        }

        if(servicio.getPeriodoRestante() != 0) {
            etxVolumenRestante.setText(servicio.getPeriodoRestante() + "");
        }

        if(servicio.getPeriodoProximosMeses() != 0) {
            etxVolumenProximosMeses.setText(servicio.getPeriodoProximosMeses() + "");
        }

//        etxNumeroObra.setText(servicio.getNumeroObra());
        etxRazonPerdida.setText(servicio.getComentariosMotivoPerdida());

        int posicion;

        posicion = semaforoDesc.indexOf(servicio.getSemaforo().getDescripcion());
        if(posicion != -1) {
            spSemaforo.setSelection(posicion);
        }

//        posicion = statusObraDesc.indexOf(servicio.getObra().getDescripcion());
//        if(posicion != -1) {
//            spObra.setSelection(posicion);
//        }

        posicion = motivosDesc.indexOf(servicio.getMotivoExclusion().getDescripcion());
        if(posicion != -1) {
            spMotivoExclusion.setSelection(posicion);
        }

//        posicion = statusObraDesc.indexOf(servicio.getObra().getDescripcion());
//        if(posicion != -1) {
//            spEstatusObra.setSelection(posicion);
//        }

        posicion = competidorDesc.indexOf(servicio.getCompetidor().getDescripcion());
        if(posicion != -1) {
            spCompetidor.setSelection(posicion);
        }

        posicion = oportunidadVentaDesc.indexOf(servicio.getOportunidadVenta().getDescripcion());
        if(posicion != -1) {
            spOportunidadVenta.setSelection(posicion);
        }
    }

    public boolean validarCampos() {

//        if (etxFechaInicioObra.getText().toString().isEmpty()) {
//            Toast.makeText(context, getString(R.string.productos_fecha_inicio_obra_empty), Toast.LENGTH_LONG).show();
//            etxFechaInicioObra.requestFocus();
//            return false;
//        } else if (etxDuracionMeses.getText().toString().isEmpty()) {
//            Toast.makeText(context, getString(R.string.productos_duracion_meses_empty), Toast.LENGTH_LONG).show();
//            etxDuracionMeses.requestFocus();
//            return false;
//        } else if (etxMesesRestantes.getText().toString().isEmpty()) {
//            Toast.makeText(context, getString(R.string.productos_meses_restantes_empty), Toast.LENGTH_LONG).show();
//            etxMesesRestantes.requestFocus();
//            return false;
//        } else
        if (etxVolumenTotal.getText().toString().isEmpty()) {
            Toast.makeText(context, getString(R.string.productos_volumen_total_empty), Toast.LENGTH_LONG).show();
            etxVolumenTotal.requestFocus();
            return false;
        } else if (etxVolumenRestante.getText().toString().isEmpty()) {
            Toast.makeText(context, getString(R.string.productos_volumen_restante_empty), Toast.LENGTH_LONG).show();
            etxVolumenRestante.requestFocus();
            return false;
        } else if (etxVolumenProximosMeses.getText().toString().isEmpty() && mostrarTodosLosDetalles) {
            Toast.makeText(context, getString(R.string.productos_volumen_proximos_meses_empty), Toast.LENGTH_LONG).show();
            etxVolumenProximosMeses.requestFocus();
            return false;
        }
        else if (spSemaforo.getSelectedItemPosition() == 0) {
            Toast.makeText(context, getString(R.string.productos_semaforo_empty), Toast.LENGTH_LONG).show();
            spSemaforo.requestFocus();
            return false;
        }
//        else if (spObra.getSelectedItemPosition() == 0) {
//            Toast.makeText(context, getString(R.string.productos_obra_empty), Toast.LENGTH_LONG).show();
//            spObra.requestFocus();
//            return false;
//        }
        else if (!seleccionadoServicio) { //Valida el spinner en caso de que el producto haya sido deseleccionado.
            if (spMotivoExclusion.getSelectedItemPosition() == 0) {
                Toast.makeText(context, getString(R.string.productos_motivos_exclusion_empty), Toast.LENGTH_LONG).show();
                spMotivoExclusion.requestFocus();
                return false;
            } else if (etxRazonPerdida.getText().toString().isEmpty()) {
                Toast.makeText(context, getString(R.string.productos_razon_perdida_descartada_empty), Toast.LENGTH_LONG).show();
                etxRazonPerdida.requestFocus();
                return false;
            }
        }
//        else if (spEstatusObra.getSelectedItemPosition() == 0) {
//            Toast.makeText(context, getString(R.string.productos_estatus_obra_empty), Toast.LENGTH_LONG).show();
//            spEstatusObra.requestFocus();
//            return false;
//        }
        else if (spCompetidor.getSelectedItemPosition() == 0&& mostrarTodosLosDetalles) {
            Toast.makeText(context, getString(R.string.productos_competidor_empty), Toast.LENGTH_LONG).show();
            spCompetidor.requestFocus();
            return false;
        } else if (spOportunidadVenta.getSelectedItemPosition() == 0 && mostrarTodosLosDetalles) {
            Toast.makeText(context, getString(R.string.productos_oportunidad_venta_empty), Toast.LENGTH_LONG).show();
            spOportunidadVenta.requestFocus();
            return false;
        } else if (Double.parseDouble(etxVolumenRestante.getText().toString()) > Double.parseDouble(etxVolumenTotal.getText().toString())) {
            Toast.makeText(context, getString(R.string.productos_volumen_incorrecto), Toast.LENGTH_LONG).show();
            etxVolumenRestante.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed(); //Ejecuta el botón "back" del dispositivo.
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
