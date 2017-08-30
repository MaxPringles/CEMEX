package com.telstock.tmanager.cemex.modulocitas;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.telstock.tmanager.cemex.modulocitas.adapters.AdapterAgregarExpediente;
import com.telstock.tmanager.cemex.modulocitas.model.ArchivosAlta;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiClient;
import com.telstock.tmanager.cemex.modulocitas.rest.ApiInterface;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.zelory.compressor.Compressor;
import mx.com.tarjetasdelnoreste.realmdb.ArchivosAltaRealm;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.funciones.Funciones;
import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by czamora on 11/18/16.
 */

public class ArchivosFragmentAdd extends Fragment implements View.OnClickListener {

    Context context;

    private LinearLayout btnArchivo;
    private LinearLayout btnFoto;
    private LinearLayout btnCancelar;
    private LinearLayout btnGuardar;

    //Variables para vista y adición de archivos.
    private static final double TAMANO_MAXIMO = 10.0; // MB
    private static final int REQUEST_A_FILE_CHOOSER = 13;
    private static final int REQUEST_CAMARA = 14;
    private static final String ALBUM = "Apprecia";
    private static final String JPEG_PREFIJO = "uploader";
    private static final String JPEG_SUFIJO = ".jpg";
    private static final int CODIGO_DE_PERMISOS = 123;

    ArrayList<String> listNombresAgregarExpediente = new ArrayList<>();
    ArrayList<String> listUrlsAgregarExpediente = new ArrayList<>();
    ArrayList<String> listIdsImagenes = new ArrayList<>();
    private int contarArchivos = 0;

    private RecyclerView recyclerViewAgregar;
    private RecyclerView.Adapter adapterAgregar;
    private RecyclerView.LayoutManager layoutManager;

    private String nombreArchivo;
    private String urlArchivo;
    private byte[] bytData;

    private ApiInterface apiInterface;

    //Variables para recuperar el idProspecto.
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String idProspecto;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_archivos_add, container, false);

        context = getActivity();

        //Se inicializa la interfaz que contiene los métodos de WS
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);

        //Se inicializa la SharedPreferences.
        prefs = context.getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, Context.MODE_PRIVATE);
        editor = prefs.edit();
        idProspecto = prefs.getString(Valores.CONTACTOS_ID_PROSPECTO, "");

        List<ArchivosAltaDB> archivosAltaDBList = ArchivosAltaRealm.mostrarListaArchivos(idProspecto);
        for(ArchivosAltaDB a : archivosAltaDBList) {
            listIdsImagenes.add(a.getId());
        }

        btnArchivo = (LinearLayout) view.findViewById(R.id.btn_archivo_agregar);
        btnFoto = (LinearLayout) view.findViewById(R.id.btn_foto_agregar);
        btnCancelar = (LinearLayout) view.findViewById(R.id.btn_cancelar_agregar);
        btnGuardar = (LinearLayout) view.findViewById(R.id.btn_guardar_agregar);
        recyclerViewAgregar = (RecyclerView) view.findViewById(R.id.recyclerView_agregar);

        //Se instancia el RecyclerView
        recyclerViewAgregar.setHasFixedSize(true); //Evita problemas en la vista al añadir o remover
        //elementos continuamente.

        //Se instancia el LayoutManager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAgregar.setLayoutManager(layoutManager);
        //Se liga el array de registros al adaptador.
        adapterAgregar = new AdapterAgregarExpediente(listNombresAgregarExpediente);
        recyclerViewAgregar.setAdapter(adapterAgregar); //Se añade el adaptador al RecyclerView.
        adapterAgregar.notifyDataSetChanged(); //Permite notificar cualquier cambio al RecyclerView.

        btnArchivo.setOnClickListener(this);
        btnFoto.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);

        //Pregunta permisos de cámara y para escribir en la memoria externa
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Deshabilita el botón para tomas fotografía
            btnFoto.setEnabled(false);
            this.requestPermissions(new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CODIGO_DE_PERMISOS);
        }

        return view;
    }

    //Método que permite agregar un archivo desde el dispositivo y/o tomar una foto para agregar
    //a la lista.
    private void dispatchTakeAgregarExpediente(Integer requestCode) {

        //Pregunta permisos de cámara y para escribir en la memoria externa
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{
                    Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CODIGO_DE_PERMISOS);
        }

        File nombreTemp;

        switch (requestCode) {
            case REQUEST_A_FILE_CHOOSER:
                try {
                    Intent getContentIntent = FileUtils.createGetContentIntent();
                    Intent intent = Intent.createChooser(
                            getContentIntent,
                            getResources().getString(
                                    R.string.prospectos_seleccionar_archivo));
                    getActivity().startActivityForResult(intent, REQUEST_A_FILE_CHOOSER);
                } catch (Exception e) {

                }
                break;

            case REQUEST_CAMARA:

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    nombreTemp = Funciones.archivoTemporal(context, JPEG_PREFIJO, idProspecto,
                            JPEG_SUFIJO, ALBUM);

                    nombreArchivo = nombreTemp.getName();
                    urlArchivo = nombreTemp.getAbsolutePath();

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(nombreTemp));

                    getActivity().startActivityForResult(takePictureIntent, REQUEST_CAMARA);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_archivo_agregar) {
            dispatchTakeAgregarExpediente(REQUEST_A_FILE_CHOOSER);

        } else if (v.getId() == R.id.btn_foto_agregar) {
            dispatchTakeAgregarExpediente(REQUEST_CAMARA);

        } else if (v.getId() == R.id.btn_cancelar_agregar) {
            Funciones.onBackPressedFunction(context, true);

        } else if (v.getId() == R.id.btn_guardar_agregar) {
            if (listUrlsAgregarExpediente.size() > 0) {
                showProgressDialog();

                mandarImagen(v);
            } else {
                Toast.makeText(context, getString(R.string.uploader_empty), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void mandarImagen(final View view) {

        if (contarArchivos < listUrlsAgregarExpediente.size()) {

            File file = new File(listUrlsAgregarExpediente.get(contarArchivos));
            RequestBody requestFile;

            //En caso de ser una imagen, el archivo se comprime.
            if (esImagen(file.getName())) {

                File compressedImageBitmap = new Compressor.Builder(context)
                        .setQuality(50)
                        .build()
                        .compressToFile(file);

                requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), compressedImageBitmap);
            } else {
                requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);
            }

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("archivo", file.getName(), requestFile);

            // add another part within the multipart request
            String descriptionString = "archivo";
            RequestBody description =
                    RequestBody.create(
                            MediaType.parse("multipart/form-data"), descriptionString);

            Call<ResponseBody> cargarArchivo = apiInterface.setCargaArchivo(body, description);

            cargarArchivo.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response.body() != null && response.code() == 200) {
                        try {
                            listIdsImagenes.add(response.body().string());

                            //Se aumenta el contador hasta que se manden todas las imágenes.
                            contarArchivos ++;
                            mandarImagen(view);

                        } catch (IOException e) {
                            Log.e("SUBIR_ARCHIVO", e.toString());
                            Snackbar.make(view, getString(R.string.uploader_fail), Snackbar.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                        //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                        AlertTokenToLogin.showAlertDialog(context);
                    }  else {
                        Snackbar.make(view, getString(R.string.uploader_fail), Snackbar.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("SUBIR_ARCHIVO", t.toString());
                    Snackbar.make(view, getString(R.string.uploader_fail), Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            ligarImagenesProspecto(view);
        }
    }

    public Boolean esImagen(String nombreArchivo) {
        String strExtension = nombreArchivo.substring(nombreArchivo
                .lastIndexOf("."));

        return (strExtension.toLowerCase().contentEquals(".bmp")
                || strExtension.toLowerCase().contentEquals(".emf")
                || strExtension.toLowerCase().contentEquals(".exif")
                || strExtension.toLowerCase().contentEquals(".gif")
                || strExtension.toLowerCase().contentEquals(".icon")
                || strExtension.toLowerCase().contentEquals(".jpeg")
                || strExtension.toLowerCase().contentEquals(".jpg")
                || strExtension.toLowerCase().contentEquals(".memorybmp")
                || strExtension.toLowerCase().contentEquals(".png")
                || strExtension.toLowerCase().contentEquals(".tiff") || strExtension
                .contentEquals(".wmf"));
    }

    public void ligarImagenesProspecto(final View view) {

        ArchivosAlta archivosAlta = new ArchivosAlta();
        archivosAlta.setIdProspecto(idProspecto);
        archivosAlta.setArchivosAlta(listIdsImagenes);

        String json = new Gson().toJson(archivosAlta);

        Call<ResponseBody> callLigarImagenes = apiInterface.setArchivoAlta(archivosAlta);

        callLigarImagenes.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!= null && response.code() == 200) {

                    //Se muestra mensaje de éxito y se regresa a la pantalla anterior.
                    Toast.makeText(context, getString(R.string.uploader_success), Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();

                    listNombresAgregarExpediente.clear();
                    //Se liga el array de registros al adaptador.
                    adapterAgregar = new AdapterAgregarExpediente(listNombresAgregarExpediente);
                    recyclerViewAgregar.setAdapter(adapterAgregar); //Se añade el adaptador al RecyclerView.
                    adapterAgregar.notifyDataSetChanged(); //Permite notificar cualquier cambio al RecyclerView.

                    EventBus.getDefault().post(getString(R.string.uploader_citas_ok));
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(context);
                }  else {
                    Snackbar.make(view, getString(R.string.uploader_fail), Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("SUBIR_ARCHIVO", t.toString());
                Snackbar.make(view, getString(R.string.uploader_fail), Snackbar.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                /*********** AGREGAREXPEDIENTE: ONACTIVITYRESULT **************/
                case REQUEST_A_FILE_CHOOSER:
                    parsearArchivos(data, getActivity(), urlArchivo, false);

                    break;
                case REQUEST_CAMARA:
                    parsearArchivos(data, getActivity(), urlArchivo, true);

                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_DE_PERMISOS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    /**
     * Hace un Parsing de los Archivos que se Agregaran en la Lista
     *
     * @param intent
     * @param context
     * @param path
     * @param camara
     * @return Boolean
     */
    private Boolean parsearArchivos(Intent intent, Context context, String path, Boolean camara) {
        Uri mUri = null;
        String mPath = null;

        if (camara) {
            mPath = path;
        } else {
            mUri = intent.getData();
            mPath = FileUtils.getPath(context, mUri);
        }

        if (mPath != null && FileUtils.isLocal(mPath)) {
            File file = new File(mPath);

            // Evalua que el Path no este Vacio
            if (mPath.isEmpty()) {
                Toast.makeText(context, getString(R.string.uploader_ruta_del_archivo_vacia), Toast.LENGTH_LONG).show();
                return false;
            }

            // Evalua el Tamaño del Archivo
            String[] tamano = FileUtils.getReadableFileSize(
                    (int) (long) file.length()).split(" ");
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            Number number = null;
            try {
                number = format.parse(tamano[0]);
            } catch (ParseException e) {
                FirebaseCrash.log("Error al parsear el archivo");
                FirebaseCrash.report(e);
                e.printStackTrace();
            }
            if (tamano[1].contentEquals("GB")) {
                Toast.makeText(context,
                        getString(
                                R.string.uploader_tamano_del_archivo_no_permitido_1)
                                + " "
                                + number.doubleValue()
                                + tamano[1]
                                + " "
                                + getResources()
                                .getString(
                                        R.string.uploader_tamano_del_archivo_no_permitido2)
                                + " " + TAMANO_MAXIMO + "MB",
                        Toast.LENGTH_LONG).show();
                return false;
            } else if (tamano[1].contentEquals("MB") && number.doubleValue() > TAMANO_MAXIMO) {
                Toast.makeText(context,
                        getString(
                                R.string.uploader_tamano_del_archivo_no_permitido_1)
                                + " "
                                + number.doubleValue()
                                + tamano[1]
                                + " "
                                + getResources()
                                .getString(
                                        R.string.uploader_tamano_del_archivo_no_permitido2)
                                + " " + TAMANO_MAXIMO + "MB",
                        Toast.LENGTH_LONG).show();
                return false;
            }

            // Evalua si la Extención del Archivo esta Permitida
            if (!validarExtension(file.getName())) {
                return false;
            }

            // Evalua si se Puede Tratar el Archivo
            try {
                bytData = readFile(file);
            } catch (IOException e) {
                Toast.makeText(context, getString(
                        R.string.uploader_url_del_archivo_corrupta), Toast.LENGTH_LONG).show();
                return false;
            }

            // Evalua si ya Existe el Nombre del Archivo en la Lista
            if (!listNombresAgregarExpediente.contains(file.getName())) {
                /* Actualiza el ListView */
                listNombresAgregarExpediente.add(file.getName());
                listUrlsAgregarExpediente.add(mPath);

                /*list_view_archivos.clearChoices();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.listview_content, listNombresAgregarExpediente);
                adapter.setDropDownViewResource(R.layout.listview_content);
                list_view_archivos.setAdapter(adapter);*/
                //Se liga el array de registros al adaptador.
                adapterAgregar = new AdapterAgregarExpediente(listNombresAgregarExpediente);
                recyclerViewAgregar.setAdapter(adapterAgregar); //Se añade el adaptador al RecyclerView.
                adapterAgregar.notifyDataSetChanged(); //Permite notificar cualquier cambio al RecyclerView.


            } else {
                Toast.makeText(context,
                        getString(
                                R.string.uploader_archivo_existente_1)
                                + " '"
                                + file.getName()
                                + "' "
                                + getResources().getString(
                                R.string.uploader_archivo_existente_2),
                        Toast.LENGTH_LONG).show();
                return false;
            }
        } else {
            Toast.makeText(context, getString(
                    R.string.uploader_url_del_archivo_null), Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * Valida la Extension del Archivo
     *
     * @param nombreArchivo
     * @return Boolean
     */
    public Boolean validarExtension(String nombreArchivo) {

        Boolean blnArchivoPermitido = false;
        String strExtension = nombreArchivo.substring(nombreArchivo
                .lastIndexOf("."));

        // Parsing de las Extensiones de Imagenes
        if (strExtension.toLowerCase().contentEquals(".bmp")
                || strExtension.toLowerCase().contentEquals(".emf")
                || strExtension.toLowerCase().contentEquals(".exif")
                || strExtension.toLowerCase().contentEquals(".gif")
                || strExtension.toLowerCase().contentEquals(".icon")
                || strExtension.toLowerCase().contentEquals(".jpeg")
                || strExtension.toLowerCase().contentEquals(".jpg")
                || strExtension.toLowerCase().contentEquals(".memorybmp")
                || strExtension.toLowerCase().contentEquals(".png")
                || strExtension.toLowerCase().contentEquals(".tiff")
                || strExtension.toLowerCase().contentEquals(".wmf")) {
            blnArchivoPermitido = true;
            return blnArchivoPermitido;
        }

        // Parsing de las Extensiones de PDFs
        if (strExtension.toLowerCase().contentEquals(".pdf")) {
            blnArchivoPermitido = true;
            return blnArchivoPermitido;
        }

        // Parsing de las Extensiones de Archivos de Texto
        if (strExtension.toLowerCase().contentEquals(".txt")) {
            blnArchivoPermitido = true;
            return blnArchivoPermitido;
        }

        // Parsing de las Extensiones de Excel
        if (strExtension.toLowerCase().contentEquals(".xlsx")
                || strExtension.toLowerCase().contentEquals(".xlsm")
                || strExtension.toLowerCase().contentEquals(".xlsb")
                || strExtension.toLowerCase().contentEquals(".xls")
                || strExtension.toLowerCase().contentEquals(".xltx")
                || strExtension.toLowerCase().contentEquals(".xltm")
                || strExtension.toLowerCase().contentEquals(".xlt")
                || strExtension.toLowerCase().contentEquals(".xls")
                || strExtension.toLowerCase().contentEquals(".xlam")
                || strExtension.toLowerCase().contentEquals(".xla")
                || strExtension.toLowerCase().contentEquals(".xps")
                || strExtension.toLowerCase().contentEquals(".xlsx")) {
            blnArchivoPermitido = true;
            return blnArchivoPermitido;
        }

        // Parsing de las Extensiones de Power Point
        if (strExtension.toLowerCase().contentEquals(".pptx")
                || strExtension.toLowerCase().contentEquals(".pptm")
                || strExtension.toLowerCase().contentEquals(".ppt")
                || strExtension.toLowerCase().contentEquals(".potx")
                || strExtension.toLowerCase().contentEquals(".potm")
                || strExtension.toLowerCase().contentEquals(".pot")
                || strExtension.toLowerCase().contentEquals(".ppsx")
                || strExtension.toLowerCase().contentEquals(".ppsm")
                || strExtension.toLowerCase().contentEquals(".pps")
                || strExtension.toLowerCase().contentEquals(".ppam")
                || strExtension.toLowerCase().contentEquals(".ppa")
                || strExtension.toLowerCase().contentEquals(".pptx")
                || strExtension.toLowerCase().contentEquals(".odp")) {
            blnArchivoPermitido = true;
            return blnArchivoPermitido;
        }

        // Parsing de las Extensiones de Word
        if (strExtension.toLowerCase().contentEquals(".docx")
                || strExtension.toLowerCase().contentEquals(".docm")
                || strExtension.toLowerCase().contentEquals(".doc")
                || strExtension.toLowerCase().contentEquals(".dotx")
                || strExtension.toLowerCase().contentEquals(".dotm")
                || strExtension.toLowerCase().contentEquals(".dot")
                || strExtension.toLowerCase().contentEquals(".docx")) {
            blnArchivoPermitido = true;
            return blnArchivoPermitido;
        }

        Toast.makeText(context,
                getResources().getString(
                        R.string.uploader_archivo_no_permitido_1)
                        + " '"
                        + nombreArchivo
                        + "' "
                        + getResources().getString(
                        R.string.uploader_archivo_no_permitido_2),
                Toast.LENGTH_LONG).show();

        return blnArchivoPermitido;
    }

    /**
     * Convierte un Archivo a un Arreglo de Bytes
     *
     * @param file
     * @return byte[]
     * @throws IOException
     */
    public static byte[] readFile(File file) throws IOException {
        // Abre el Archivo
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

        try {
            // Obtiene la Longitud y la Revisa
            long longlength = randomAccessFile.length();
            int length = (int) longlength;

            if (length != longlength) {
                throw new IOException("File size >= 2 GB");
            }

            // Lee el Archivo y Regresa los Datos
            byte[] data = new byte[length];
            randomAccessFile.readFully(data);
            return data;
        } finally {
            randomAccessFile.close();
        }
    }

    public void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Espere un momento por favor...");
        progressDialog.show();
    }
}