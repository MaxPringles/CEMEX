package com.telstock.tmanager.cemex.prospectos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.telstock.tmanager.cemex.prospectos.adapters.AdapterContactos;
import com.telstock.tmanager.cemex.prospectos.interfaces.OnClickContacto;
import com.telstock.tmanager.cemex.prospectos.model.InformacionContacto;
import com.telstock.tmanager.cemex.prospectos.rest.ApiClient;
import com.telstock.tmanager.cemex.prospectos.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.CatalogoCargoRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCargoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Cargo;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by czamora on 9/9/16.
 */
public class AltaListaContactos extends Fragment implements OnClickContacto {

    private Context context;
    private TextView txt_contactos_total;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Contacto> listaContactos;
    private final static int LISTA_VACIA = 0;
    private final static int LISTA = 1;

    private static final int CODIGO_DE_PERMISOS_LLAMAR = 100;
    private String telefonoLlamar;

//    ArrayList<InformacionContacto> listaContactos = new ArrayList<>();
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
    Button btnFinalizar;

    InformacionContacto informacionContacto;
    ApiInterface apiInterface;

    List<CatalogoCargoDB> cargoListAll;
    ArrayList<String> cargoDesc = new ArrayList<>();
    ArrayAdapter<String> adapterCargo;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_alta_lista_contactos, container, false);

        context = getActivity();
        setHasOptionsMenu(true); //Indica que el fragmento implementará opciones de menú en el Toolbar.

        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        txt_contactos_total = (TextView) view.findViewById(R.id.txt_contactos_total);
        btnFinalizar = (Button) view.findViewById(R.id.btnFinalizar);
        btnFinalizar.setVisibility(View.GONE);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contactos);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        txt_contactos_total.setText(getString(R.string.alta_lista_total) + " 0");

        Contacto contactosProspectoDB = new Contacto();
        contactosProspectoDB.setNombres(R.string.alta_lista_vacia + "");

        //Se limpia la lista, y se le agrega un registro "fantasma" con sólo un atributo para
        //activar el recyclerView.
        listaContactos = new ArrayList<>();
        listaContactos.add(contactosProspectoDB);
        adapter = new AdapterContactos(context, listaContactos, LISTA_VACIA, "", this);
        recyclerView.setAdapter(adapter);

        return view;
    }

    /** MÉTODO QUE ACTUALIZA LA LISTA CON EL NUEVO CONTACTO AGREGADO **/
    public void actualizarLista(Contacto contactosDB) {
        if(listaContactos.get(0).getNombres().equals(R.string.alta_lista_vacia + "")) {
            listaContactos.remove(0);
        }
        listaContactos.add(contactosDB);
        txt_contactos_total.setText(getString(R.string.alta_lista_total) + " " + listaContactos.size());
        adapter = new AdapterContactos(context, listaContactos, LISTA, "", this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        } /*else if (etExtension.getText().toString().isEmpty()) {
            Snackbar.make(view, getString(R.string.formulario_fail_contactos_extension), Snackbar.LENGTH_LONG).show();
            etExtension.requestFocus();
            return true;
        } else if (etEmail.getText().toString().isEmpty()) {
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
        contactos.setTelefono(etTelefono.getText().toString());

        if (radio_si.isChecked()) {
            //verifica si exiten contactos
            if (listaContactos.isEmpty() || listaContactos.get(0).getPrincipal() == null) {
                contactos.setPrincipal(true);
                actualizarLista(contactos);
                EventBus.getDefault().post(contactos);
            } else {
                //verifica que no exita contacto principál
                if (exiteContactoPrincipal()) {
                    //si exite la ventana pregunta si se desea remplazar
                    VentanaActulizarPrincipla(context, contactos);
                } else {
                    contactos.setPrincipal(true);
                    actualizarLista(contactos);
                    EventBus.getDefault().post(contactos);
                }

            }

        } else {

            contactos.setPrincipal(false);
            actualizarLista(contactos);
            EventBus.getDefault().post(contactos);
        }


        dialogModalAltaContacto.dismiss();
    }

    public void VentanaActulizarPrincipla (Context context, final Contacto contactos) {

        AlertDialog.Builder contactoPrincipal = new AlertDialog.Builder(context);
        contactoPrincipal.setTitle("Ya exite contacto principal");
        contactoPrincipal.setMessage("¿Quieres guardar este contacto como principal?");

        contactoPrincipal.setCancelable(true);
        contactoPrincipal.setPositiveButton("SI", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int i) {

                for (int x =0 ; x< listaContactos.size();x++)
                {
                    if(listaContactos.get(x).getPrincipal())
                    {
                        listaContactos.get(x).setPrincipal(false);
                    }
                }
                contactos.setPrincipal(true);
                actualizarLista(contactos);
                EventBus.getDefault().post(contactos);
            }
        });
        contactoPrincipal.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int i) {
                contactos.setPrincipal(false);
                actualizarLista(contactos);
                EventBus.getDefault().post(contactos);
            }
        });
        contactoPrincipal.create();
        contactoPrincipal.show();


    }
    public boolean exiteContactoPrincipal()
    {
        boolean validacion;
        for (int i =0 ; i< listaContactos.size();i++)
        {
            if(listaContactos.get(i).getPrincipal())
            {
                return  validacion= true;
            }
        }
        return validacion = false;

    }


    public void realizarLlamada(String telefono) {

        Intent intentLlamar = new Intent(Intent.ACTION_CALL);
        intentLlamar.setData(Uri.parse("tel: " + telefono));

        startActivity(intentLlamar);
    }

    /**
     * MÉTODO QUE CIERRA EL TECLADO
     **/
    public void cerrarTeclado(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onClickProspectos(View view) {
        showAltaProspectoDatosContacto();
    }

    @Override
    public void onClickLlamar(String telefono) {
        Toast.makeText(context, "Teléfono: " + telefono, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_alta_contactos, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_contacto_nuevo) {

            if (listaContactos.size() < 5) {
                showAltaProspectoDatosContacto();
            } else {
                Snackbar.make(view, getString(R.string.contactos_alta_maximo), Snackbar.LENGTH_LONG).show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
