package com.telstock.tmanager.cemex.modulocitas;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.crash.FirebaseCrash;
import com.telstock.tmanager.cemex.modulocitas.adapters.AdapterVerExpediente;
import com.telstock.tmanager.cemex.modulocitas.interfaces.OnClickDescarga;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiClient;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmList;
import mx.com.tarjetasdelnoreste.realmdb.ArchivosAltaRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.ConnectionDetector;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by czamora on 11/18/16.
 */

public class ArchivosFragmentDescarga extends Fragment implements View.OnClickListener, OnClickDescarga {

    private Context context;

    private FloatingActionMenu menu_descarga;
    private FloatingActionButton fab_descarga_seleccionar;
    private FloatingActionButton fab_descarga_todo;
    private LinearLayout btn_cancelar_descarga;
    private LinearLayout btn_aceptar_descarga;

    private List<FloatingActionMenu> menus = new ArrayList<>(); //Lista que añade los FAB a la vista.
    private Handler mUiHandler = new Handler();

    private RecyclerView recyclerViewDescarga;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    List<ArchivosAltaDB> archivosAltaDBList = new ArrayList<>();
    //List<ArchivosAltaDB> archivosSeleccionados = new ArrayList<>();
    Map<Integer, ArchivosAltaDB> archivosSeleccionados = new HashMap<>();

    //Variables que revisan la conexión a internet.
    ConnectionDetector connectionDetector;
    Boolean isInternetPresent;

    //Declaración de las variables de Retrofit.
    ApiInterface apiInterface;

    ProgressBar progressBar;

    //Variables para recuperar el idProspecto.
    SharedPreferences prefs;
    String idProspecto;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_archivos_descarga, container, false);

        context = getActivity();

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        idProspecto = prefs.getString(Valores.CONTACTOS_ID_PROSPECTO, "");

        //Se inicializa la interfaz de Retrofit.
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        menu_descarga = (FloatingActionMenu) view.findViewById(R.id.fab_descarga);
        fab_descarga_seleccionar = (FloatingActionButton) view.findViewById(R.id.fab_descarga_seleccionar);
        fab_descarga_todo = (FloatingActionButton) view.findViewById(R.id.fab_descarga_todo);
        btn_cancelar_descarga = (LinearLayout) view.findViewById(R.id.btn_cancelar_descarga);
        btn_aceptar_descarga = (LinearLayout) view.findViewById(R.id.btn_aceptar_descarga);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        fab_descarga_seleccionar.setOnClickListener(this);
        fab_descarga_todo.setOnClickListener(this);
        btn_cancelar_descarga.setOnClickListener(this);
        btn_aceptar_descarga.setOnClickListener(this);

        recyclerViewDescarga = (RecyclerView) view.findViewById(R.id.recyclerView_descarga);
        layoutManager = new LinearLayoutManager(context);

        recyclerViewDescarga.setHasFixedSize(true);
        recyclerViewDescarga.setLayoutManager(layoutManager);

        obtenerArchivosProspecto();

        menus.add(menu_descarga);
        menu_descarga.hideMenuButton(false);

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        createCustomAnimation();

        return view;
    }

    private void obtenerArchivosProspecto() {

        //Se muestra el círculo de progreso.
        visibilidad(View.VISIBLE);

        Call<List<ArchivosAltaDB>> archivosAltaCall = apiInterface.getArchivosProspecto(idProspecto);

        archivosAltaCall.enqueue(new Callback<List<ArchivosAltaDB>>() {
            @Override
            public void onResponse(Call<List<ArchivosAltaDB>> call, Response<List<ArchivosAltaDB>> response) {
                if (response.body() != null && response.code() == 200) {
                    guardarArchivosProspecto(response.body());

                    //Se muestra la lista de archivos guardada en la BD.
                    mostrarListaArchivos();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                } else {
                    Toast.makeText(context, getString(R.string.archivos_cargar_fail), Toast.LENGTH_LONG).show();
                    //Se muestra la lista de archivos guardada en la BD.
                    mostrarListaArchivos();
                }
            }

            @Override
            public void onFailure(Call<List<ArchivosAltaDB>> call, Throwable t) {
                Log.e("GETARCHIVOS", t.toString());
                FirebaseCrash.log("Error GetArchivos");
                FirebaseCrash.report(t);

                Toast.makeText(context, getString(R.string.archivos_cargar_fail), Toast.LENGTH_LONG).show();

                //Se muestra la lista de archivos guardada en la BD.
                mostrarListaArchivos();
            }
        });
    }

    private void guardarArchivosProspecto(List<ArchivosAltaDB> archivosAltaList) {

        ArchivosAltaDB archivosAltaDB;
        RealmList<ArchivosAltaDB> archivosAltaDBRealmList = new RealmList<>();

        if (archivosAltaList.size() > 0) {
            for (int i = 0; i < archivosAltaList.size(); i++) {

                archivosAltaDB = new ArchivosAltaDB(); //Se inicializa nuevamente para no duplicar los registros.

                //Llave compuesta del idProspecto y el idActividad.
                archivosAltaDB.setCompoundId(idProspecto + archivosAltaList.get(i).getId());
                archivosAltaDB.setIdProspecto(idProspecto);
                archivosAltaDB.setId(archivosAltaList.get(i).getId());
                archivosAltaDB.setNombre(archivosAltaList.get(i).getNombre());
                archivosAltaDB.setMimeType(archivosAltaList.get(i).getMimeType());
                archivosAltaDB.setType(archivosAltaList.get(i).getType());

                //Se agrega el nuevo contacto a la lista de contactos.
                archivosAltaDBRealmList.add(archivosAltaDB);
            }
        }

        ProspectosDB prospectosDB = new ProspectosDB();
        prospectosDB.setArchivosAltaDBRealmList(archivosAltaDBRealmList);

        ProspectosRealm.guardarProspecto(prospectosDB);
    }

    public void mostrarListaArchivos() {

        archivosAltaDBList = ArchivosAltaRealm.mostrarListaArchivos(idProspecto);

        adapter = new AdapterVerExpediente(this, archivosAltaDBList, false);
        recyclerViewDescarga.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //Se quita el círculo de progreso.
        visibilidad(View.GONE);
    }

    //Método que genera la animación del FAB cuando es presionado.
    private void createCustomAnimation() {

        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menu_descarga.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menu_descarga.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menu_descarga.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menu_descarga.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        //Método que maneja el ícono a mostrar, despendiendo si el FAB está
        //abierto o cerrado.
        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menu_descarga.getMenuIconView().setImageResource(menu_descarga.isOpened()
                        ? R.drawable.btn_flotante_cerrar_descargas : R.drawable.btn_flotante_abrir_descargas);
                if (menu_descarga.isOpened()) {
                } else {
                }
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menu_descarga.setIconToggleAnimatorSet(set);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_descarga_seleccionar) {

            adapter = new AdapterVerExpediente(this, archivosAltaDBList, true);
            recyclerViewDescarga.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            menu_descarga.close(true); //Cierra los FAB.

        } else if (v.getId() == R.id.fab_descarga_todo) {
            descargarExpediente(archivosAltaDBList);

            menu_descarga.close(true); //Cierra los FAB.
        } else if (v.getId() == R.id.btn_cancelar_descarga) {
            Funciones.onBackPressedFunction(context, true);

        } else if (v.getId() == R.id.btn_aceptar_descarga) {
            if (archivosSeleccionados.size() > 0) {
                descargarExpedienteSeleccionados(archivosSeleccionados);
            } else {
                Toast.makeText(context, getString(R.string.downloader_empty), Toast.LENGTH_LONG).show();
            }
        }
    }

    /** MÉTODO QUE DESCARGA TODOS LOS ARCHIVOS **/
    public void descargarExpediente(List<ArchivosAltaDB> archivosAltaDBList) {

        connectionDetector = new ConnectionDetector(getActivity());
        isInternetPresent = connectionDetector.isConnectingToInternet(); //Verdadero o falso.

        for (int i = 0; i < archivosAltaDBList.size(); i++) {

            if (!isInternetPresent) {
                Toast.makeText(getActivity(), "La operación requiere de Internet", Toast.LENGTH_SHORT).show();
            } else {

                //Se instancia un objeto de tipo DownloadManager que contiene todos las opciones deseadas.
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
                        Url.URL_WEBSERVICE + Url.getArchivoProspecto + archivosAltaDBList.get(i).getId()));
                //Título y descripción de la descarga.
                request.setTitle(archivosAltaDBList.get(i).getNombre());
                request.setDescription("Descargando...");

                request.allowScanningByMediaScanner(); //Permite acomodar el archivo donde debe ir (e.g. un mp3 guardarlo en Música).
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Permite observar cómo va la descarga.
                /*String nameOfFile = URLUtil.guessFileName(
                        Url.URL_WEBSERVICE + Url.getArchivoProspecto + archivosAltaDBList.get(i).getId(),
                        null, MimeTypeMap.getFileExtensionFromUrl(
                                Url.URL_WEBSERVICE + Url.getArchivoProspecto + archivosAltaDBList.get(i).getId())); //Nombre del archivo descargado.
                */
                String nameOfFile = archivosAltaDBList.get(i).getNombre();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile); //Lugar donde se guardará y el nombre que llevará el archivo.
                DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE); //Instanciamos el propio DownloadManager
                // especificando que se requiere un servicio de Descarga.
                manager.enqueue(request); //La descarga se coloca en la cola (se realiza cuando detecta que hay conexión a una red).
            }
        }
    }

    /** MÉTODO QUE DESCARGA SÓLO LOS ARCHIVOS SELECCIONADOS **/
    public void descargarExpedienteSeleccionados(Map<Integer, ArchivosAltaDB> archivosAltaDBSeleccionados) {

        connectionDetector = new ConnectionDetector(getActivity());
        isInternetPresent = connectionDetector.isConnectingToInternet(); //Verdadero o falso.

        for (int i = 0; i < archivosAltaDBList.size(); i++) {

            //Revisa si existe la llave correspondiente.
            if (archivosAltaDBSeleccionados.get(i) != null) {
                if (!isInternetPresent) {
                    Toast.makeText(getActivity(), "La operación requiere de Internet", Toast.LENGTH_SHORT).show();
                } else {

                    //Se instancia un objeto de tipo DownloadManager que contiene todos las opciones deseadas.
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(
                            Url.URL_WEBSERVICE + Url.getArchivoProspecto + archivosAltaDBSeleccionados.get(i).getId()));
                    //Título y descripción de la descarga.
                    request.setTitle(archivosAltaDBSeleccionados.get(i).getNombre());
                    request.setDescription("Descargando...");

                    request.allowScanningByMediaScanner(); //Permite acomodar el archivo donde debe ir (e.g. un mp3 guardarlo en Música).
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Permite observar cómo va la descarga.
                    /*String nameOfFile = URLUtil.guessFileName(
                            Url.URL_WEBSERVICE + Url.getArchivoProspecto + archivosAltaDBSeleccionados.get(i).getId(),
                            null, MimeTypeMap.getFileExtensionFromUrl(
                                    Url.URL_WEBSERVICE + Url.getArchivoProspecto + archivosAltaDBSeleccionados.get(i).getId())); //Nombre del archivo descargado.
                    */
                    String nameOfFile = archivosAltaDBSeleccionados.get(i).getNombre();
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameOfFile); //Lugar donde se guardará y el nombre que llevará el archivo.
                    DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE); //Instanciamos el propio DownloadManager
                    // especificando que se requiere un servicio de Descarga.
                    manager.enqueue(request); //La descarga se coloca en la cola (se realiza cuando detecta que hay conexión a una red).
                }
            }
        }
    }

    @Override
    public void onClickDescarga(int position, boolean isChecked) {
        if (isChecked) {
            archivosSeleccionados.put(position, archivosAltaDBList.get(position));
        } else {
            archivosSeleccionados.remove(position);
        }
    }

    public void visibilidad(int visibility) {
        progressBar.setVisibility(visibility);
    }

    /**
     * MENSAJE QUE SE RECIBE DE LA CLASE ArchivosFragmentAdd, RECIBE LA INDICACIÓN
     * DE VOLVER A MOSTRAR LA LISTA DE EXPEDIENTE
     **/
    @Subscribe
    public void getImagenesSubidas(String imagenesSubidas) {
        if (imagenesSubidas.equals(getString(R.string.uploader_citas_ok))) {
            obtenerArchivosProspecto();
        }
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
    }
}
