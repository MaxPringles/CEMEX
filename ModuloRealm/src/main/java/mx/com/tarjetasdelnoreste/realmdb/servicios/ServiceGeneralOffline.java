package mx.com.tarjetasdelnoreste.realmdb.servicios;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import id.zelory.compressor.Compressor;
import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.GeneralOfflineRealm;
import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.model.GeneralOfflineDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividadesNuevas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas.JsonAltaActividadesAdministrativas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.JsonAltaProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonDescartar.JSONdescartar;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonObra.JsonObra;
import mx.com.tarjetasdelnoreste.realmdb.model.modelOffline.ArchivosAlta;
import mx.com.tarjetasdelnoreste.realmdb.model.modelOffline.ListaContactos;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiClient;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiInterface;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by usr_micro13 on 18/01/2017.
 */

public class ServiceGeneralOffline extends IntentService {

    Realm realmDB;
    ApiInterface apiInterface;
    List<GeneralOfflineDB> generalOfflineDBList = new ArrayList<>();
    int numeroEnvio = 0;
    Gson gson;

    //Variables de soporte para dar de alta archivos.
    ArrayList<String> listUrlsAgregarExpediente;
    ArrayList<String> listNombresAgregarExpediente;
    ArrayList<String> listIdsImagenes;
    int contarArchivos;
    Context context;

    public ServiceGeneralOffline() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        realmDB = Realm.getDefaultInstance();
        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);
        context = getApplicationContext();

        List<GeneralOfflineDB> listaRealm =
                realmDB.where(GeneralOfflineDB.class)
                        .equalTo("statusEnvio", Valores.ESTATUS_NO_ENVIADO)
                        .findAll();

        if (listaRealm.size() > 0) {

            for (int i = 0; i < listaRealm.size(); i++) {

                generalOfflineDBList.add(new GeneralOfflineDB(
                        listaRealm.get(i).getFechaOffline(),
                        listaRealm.get(i).getIdOperacion(),
                        listaRealm.get(i).getIdProspecto(),
                        listaRealm.get(i).getJsonInformacion(),
                        listaRealm.get(i).getStatusEnvio(),
                        listaRealm.get(i).getNombreArchivos()
                ));
            }

            realmDB.close();
            colaEnvios();
        } else {
            realmDB.close();
        }
    }

    public void colaEnvios() {

        if (numeroEnvio < generalOfflineDBList.size()) {

            cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIANDO);

            switch (generalOfflineDBList.get(numeroEnvio).getIdOperacion()) {
                case Valores.ID_ENVIO_ALTA_PROSPECTOS:

                    //Si el idProspecto (que realmente guarda la uri de la foto) contiene un "."
                    //o una "/" indica que primero debe enviarse la foto.
                    if (generalOfflineDBList.get(numeroEnvio).getIdProspecto().contains(".")
                            || generalOfflineDBList.get(numeroEnvio).getIdProspecto().contains("/")) {
                        //Significa que primero es necesario enviar la foto.
                        altaArchivoProspecto();
                    } else {
                        //Indica que el prospecto está listo para enviarse.
                        altaProspectos();
                    }

                    break;
                case Valores.ID_ENVIO_ALTA_CONTACTOS:
                    altaContactos();
                    break;
                case Valores.ID_ENVIO_ALTA_ARCHIVOS:
                    altaArchivoObtenerUris();
                    break;
                case Valores.ID_ENVIO_ALTA_OBRA:
                    altaObra();
                    break;
                case Valores.ID_ENVIO_ALTA_ACTIVIDAD:
                    altaActividad();
                    break;
                case Valores.ID_ENVIO_ALTA_ACTIVIDAD_SIN_ANTERIOR:
                    altaActividadSinAnterior();
                    break;
                case Valores.ID_ENVIO_ALTA_ACTIVIDAD_ADMINISTRATIVA:
                    altaActividadAdministrativa();
                    break;
                case Valores.ID_ENVIO_DESCARTAR:
                altaDescartar();
                break;
            }
        }
    }

    public void altaProspectos() {

        gson = new Gson();

        final JsonAltaProspecto jsonAltaProspecto = gson.fromJson(generalOfflineDBList.get(numeroEnvio).getJsonInformacion(),
                JsonAltaProspecto.class);

        //Si idProspecto (que realmente es el idFotografia) está vacío, significa que
        //que la foto del prospecto no existe o no se envió Offline.
        if (!generalOfflineDBList.get(numeroEnvio).getIdProspecto().equals("")) {
            //Se coloca el idProspecto (que realmente es el idFotografia) en el campo de foto
            //del json.
            jsonAltaProspecto.setFotografia(generalOfflineDBList.get(numeroEnvio).getIdProspecto());
        }

        Call<ResponseBody> setProspecto = apiInterface.setProspecto(jsonAltaProspecto);

        setProspecto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                    Toast.makeText(getApplicationContext(), "El prospecto " + jsonAltaProspecto.getCliente().getNombre()
                                    + " - " + jsonAltaProspecto.getCliente().getObra() + " se ha dado de alta exitosamente",
                            Toast.LENGTH_LONG).show();

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertService(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                //Se cambia el status a ENVIADO.
                                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OFFLINE_PROSPECTO", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });

    }

    public void altaContactos() {

        gson = new Gson();

        Type listObject = new TypeToken<List<Contacto>>() {
        }.getType();
        List<Contacto> listaContactos = gson.fromJson(generalOfflineDBList.get(numeroEnvio).getJsonInformacion(),
                listObject);

        final Call<Void> altaContacto = apiInterface.setContactos(generalOfflineDBList.get(numeroEnvio).getIdProspecto(),
                listaContactos);

        altaContacto.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                    Realm realm = Realm.getDefaultInstance();
                    ProspectosDB prospectosDB = realm.where(ProspectosDB.class)
                            .equalTo("id", generalOfflineDBList.get(numeroEnvio).getIdProspecto())
                            .findFirst();

                    if (prospectosDB != null) {
                        Toast.makeText(getApplicationContext(), "Se han añadido correctamente los contactos del prospecto "
                                        + prospectosDB.getCliente() + " - " + prospectosDB.getObra(),
                                Toast.LENGTH_LONG).show();

                    }

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertService(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                //Se cambia el status a ENVIADO.
                                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("OFFLINE_CONTACTO", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void altaArchivoProspecto() {

        File file = new File(generalOfflineDBList.get(numeroEnvio).getIdProspecto());

        /*Bitmap bmp = BitmapFactory.decodeFile(uriFoto);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, bos);*/
        //File compressedImageBitmap = Compressor.getDefault(context).compressToFile(file);
        File compressedImageBitmap = new Compressor.Builder(getApplicationContext())
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

                if (response.code() == 200) {

                    //Una vez obtenido el id de la imagen, se actualiza el objeto en Realm.
                    try {

                        realmDB = Realm.getDefaultInstance();

                        generalOfflineDBList.get(numeroEnvio).setIdProspecto(response.body().string());

                        realmDB.beginTransaction();
                        realmDB.copyToRealmOrUpdate(generalOfflineDBList.get(numeroEnvio));
                        realmDB.commitTransaction();

                        realmDB.close();

                        //Se revisa nuevamente la cola de envíos, para que ahora se
                        //intente enviar al prospecto con foto incluida.
                        colaEnvios();

                    } catch (IOException e) {
                        e.printStackTrace();

                        //Se regresa el status a NO_ENVIADO.
                        cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                        //En este caso sí se suma al contador, para seguir con el siguiente envío en la cola.
                        numeroEnvio++;
                        colaEnvios();
                    }

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertService(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //En este caso sí se suma al contador, para seguir con el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OFFLINE_ARCHIVO", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void altaArchivoObtenerUris() {

        listUrlsAgregarExpediente = new ArrayList<>();
        listNombresAgregarExpediente = new ArrayList<>();
        listIdsImagenes = new ArrayList<>();
        contarArchivos = 0;

        String[] imagenesUris = generalOfflineDBList.get(numeroEnvio).getJsonInformacion().split("\\*");
        String[] imagenesNombres = generalOfflineDBList.get(numeroEnvio).getNombreArchivos().split("\\*");

        for (int i = 0; i < imagenesUris.length; i++) {
            listUrlsAgregarExpediente.add(imagenesUris[i]);
            listNombresAgregarExpediente.add(imagenesNombres[i]);
        }

        altaArchivos();
    }

    public void altaArchivos() {

        if (contarArchivos < listUrlsAgregarExpediente.size()) {

            File file = new File(listUrlsAgregarExpediente.get(contarArchivos));
            RequestBody requestFile;

            //En caso de ser una imagen, el archivo se comprime.
            if (esImagen(file.getName())) {

                File compressedImageBitmap = new Compressor.Builder(getApplicationContext())
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
                    MultipartBody.Part.createFormData("archivo", listNombresAgregarExpediente.get(contarArchivos), requestFile);

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

                            listIdsImagenes.add(response.body().string());

                            //Se aumenta el contador hasta que se manden todas las imágenes.
                            contarArchivos++;
                            altaArchivos();

                        } catch (IOException e) {
                            Log.e("SUBIR_ARCHIVO", e.toString());

                        }
                    } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                        //Se regresa el status a NO_ENVIADO.
                        cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                        //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                        AlertTokenToLogin.showAlertService(getApplicationContext());
                    } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                        if(response.errorBody() != null) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {
                                    //Se aumenta el contador hasta que se manden todas las imágenes.
                                    contarArchivos++;
                                    altaArchivos();
                                } else {
                                    Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e("ErrorBody",response.code() + "");
                            }
                        }
                    } else {
                        //Se regresa el status a NO_ENVIADO.
                        cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                        //Sea exitoso o no, se aumenta el contador para que se ejecute
                        // el siguiente envío en la cola.
                        numeroEnvio++;
                        colaEnvios();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("OFFLINE_ALTA_ARCHIVOS", t.toString());

                    //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                    //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                    //problemas con la conexión a internet, por lo que no tiene caso intentar
                    //hacer el siguiente envío.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                }
            });
        } else {

            altaLigarArchivoProspecto();
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

    public void altaLigarArchivoProspecto() {

        ArchivosAlta archivosAlta = new ArchivosAlta();
        archivosAlta.setIdProspecto(generalOfflineDBList.get(numeroEnvio).getIdProspecto());
        archivosAlta.setArchivosAlta(listIdsImagenes);

        String json = new Gson().toJson(archivosAlta);

        Call<ResponseBody> callLigarImagenes = apiInterface.setArchivoAlta(archivosAlta);

        callLigarImagenes.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null && response.code() == 200) {

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                    Realm realm = Realm.getDefaultInstance();
                    ProspectosDB prospectosDB = realm.where(ProspectosDB.class)
                            .equalTo("id", generalOfflineDBList.get(numeroEnvio).getIdProspecto())
                            .findFirst();

                    if (prospectosDB != null) {
                        Toast.makeText(getApplicationContext(), "Se han añadido correctamente los archivos del prospecto "
                                        + prospectosDB.getCliente() + " - " + prospectosDB.getObra(),
                                Toast.LENGTH_LONG).show();

                    }

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertService(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                //Se cambia el status a ENVIADO.
                                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void altaObra() {

        gson = new Gson();

        final JsonObra jsonObra = gson.fromJson(generalOfflineDBList.get(numeroEnvio).getJsonInformacion(),
                JsonObra.class);

        Call<ResponseBody> putObra = apiInterface.putObra(jsonObra.getId(), jsonObra);

        putObra.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                    Toast.makeText(getApplicationContext(), "La obra " +
                                    jsonObra.getNombre() +
                                    " se ha actualizado correctamente",
                            Toast.LENGTH_LONG).show();

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertService(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                //Se cambia el status a ENVIADO.
                                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OFFLINE_OBRA", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void altaActividad() {

        gson = new Gson();

        final JsonAltaActividades jsonAltaActividades = gson.fromJson(generalOfflineDBList.get(numeroEnvio).getJsonInformacion(),
                JsonAltaActividades.class);

        Call<ResponseBody> sendActividadAlta = apiInterface.setActividadAlta(
                generalOfflineDBList.get(numeroEnvio).getIdProspecto(), jsonAltaActividades);

        String json = new Gson().toJson(jsonAltaActividades);

        sendActividadAlta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.code() == 200) {

                        //Se cambia el status a ENVIADO.
                        cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                        Realm realm = Realm.getDefaultInstance();
                        ProspectosDB prospectosDB = realm.where(ProspectosDB.class)
                                .equalTo("id", generalOfflineDBList.get(numeroEnvio).getIdProspecto())
                                .findFirst();

                        if (prospectosDB != null) {
                            Toast.makeText(getApplicationContext(), "La cita del prospecto " +
                                            prospectosDB.getCliente() + " - " + prospectosDB.getObra() +
                                            " se ha dado de alta exitosamente",
                                    Toast.LENGTH_LONG).show();

                        }

                        //Sea exitoso o no, se aumenta el contador para que se ejecute
                        // el siguiente envío en la cola.
                        numeroEnvio++;
                        colaEnvios();
                    } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                        //Se regresa el status a NO_ENVIADO.
                        cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                        //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                        AlertTokenToLogin.showAlertService(getApplicationContext());
                    } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                        if(response.errorBody() != null) {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                    //Se cambia el status a ENVIADO.
                                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                                } else {
                                    Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                Log.e("ErrorBody",response.code() + "");
                            }
                        }
                    } else {
                        //Se regresa el status a NO_ENVIADO.
                        cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                        //Sea exitoso o no, se aumenta el contador para que se ejecute
                        // el siguiente envío en la cola.
                        numeroEnvio++;
                        colaEnvios();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OFFLINE_ALTA_ACTIVIDAD", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void altaActividadSinAnterior() {

        gson = new Gson();

        JsonAltaActividadesNuevas jsonAltaActividades = gson.fromJson(generalOfflineDBList.get(numeroEnvio).getJsonInformacion(),
                JsonAltaActividadesNuevas.class);

        Call<ResponseBody> sendActividadAlta = apiInterface.setActividadAltaSinActividadAnterior(
                generalOfflineDBList.get(numeroEnvio).getIdProspecto(), jsonAltaActividades);

        String json = new Gson().toJson(jsonAltaActividades);

        sendActividadAlta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                    Realm realm = Realm.getDefaultInstance();
                    ProspectosDB prospectosDB = realm.where(ProspectosDB.class)
                            .equalTo("id", generalOfflineDBList.get(numeroEnvio).getIdProspecto())
                            .findFirst();

                    if (prospectosDB != null) {
                        Toast.makeText(getApplicationContext(), "La cita del prospecto " +
                                        prospectosDB.getCliente() + " - " + prospectosDB.getObra() +
                                        " se ha dado de alta exitosamente",
                                Toast.LENGTH_LONG).show();

                    }

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertService(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                //Se cambia el status a ENVIADO.
                                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OFFLINE_ALTA_ACTIVIDAD", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void altaActividadAdministrativa() {

        gson = new Gson();

        final JsonAltaActividadesAdministrativas jsonAltaActividadesAdministrativas = gson.fromJson(generalOfflineDBList.get(numeroEnvio).getJsonInformacion(),
                JsonAltaActividadesAdministrativas.class);

        //El ID_TIPO_ASIGNACION_ADMINISTRATIVAS indica que la actividad que se está dando de alta es
        //Administrativa. El valor de esta variable es 2 y nunca cambia.
        //Nótese que el idProspecto es realmente el idVendedor.
        Call<ResponseBody> sendActividadAdministrativaAlta = apiInterface.setActividadAdministrativa(Valores.ID_TIPO_ASIGNACION_ADMINISTRATIVAS,
                generalOfflineDBList.get(numeroEnvio).getIdProspecto(), jsonAltaActividadesAdministrativas);

        String json = new Gson().toJson(jsonAltaActividadesAdministrativas);

        sendActividadAdministrativaAlta.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                    //Revisa si la actividad que se ha dado de alta es Administrativa de Venta
                    //o Administrativa para mostrar el Toast adecuadamente.
                    if (jsonAltaActividadesAdministrativas.getTipoActividad().getIdPadre() ==
                            Valores.ID_PADRE_ACTIVIDADES_ADMINISTRATIVAS_VENTA) {
                        Toast.makeText(getApplicationContext(), "La Actividad Administrativa de Venta se ha agendado exitosamente",
                                Toast.LENGTH_LONG).show();
                    } else if (jsonAltaActividadesAdministrativas.getTipoActividad().getIdPadre() ==
                            Valores.ID_PADRE_ACTIVIDADES_ADMINISTRATIVAS) {
                        Toast.makeText(getApplicationContext(), "La Actividad Administrativa se ha agendado exitosamente",
                                Toast.LENGTH_LONG).show();
                    }

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertService(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                //Se cambia el status a ENVIADO.
                                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("OFFLINE_ALTA_ACTIVIDAD", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }


    public void altaDescartar(){

        Gson gson = new Gson();

        final JSONdescartar jsonDescartar  = gson.fromJson(generalOfflineDBList.get(numeroEnvio).getJsonInformacion(),
                JSONdescartar.class);

        Call<ResponseBody> callDescartar = apiInterface.setDescartar(jsonDescartar);

        callDescartar.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null && response.code() == 200) {
                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);

                    Realm realm = Realm.getDefaultInstance();

                    ProspectosDB prospectosDB = realm.where(ProspectosDB.class)
                            .equalTo("id", jsonDescartar.getIdProspecto())
                            .findFirst();

                    if (prospectosDB != null) {
                        Toast.makeText(getApplicationContext(), "El prospecto " + prospectosDB.getCliente() +
                        " - " + prospectosDB.getObra() + " ha sido descartado correctamente",
                                Toast.LENGTH_LONG).show();
                    }

                    numeroEnvio++;
                    colaEnvios();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
                    //Muestra diálogo indicando que la sesión ha expirado y devuelve al Login.
                    AlertTokenToLogin.showAlertDialog(getApplicationContext());
                } else if (response.code() == 400) { // Si no contesta exitoso sólo muestra un mensaje de error
                    if(response.errorBody() != null) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            if (jObjError.getString("code").equals(Url.codigoSolicitudEnviadaPreviamente)) {

                                //Se cambia el status a ENVIADO.
                                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_ENVIADO);
                            } else {
                                Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Log.e("ErrorBody",response.code() + "");
                        }
                    }
                } else {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);

                    //Sea exitoso o no, se aumenta el contador para que se ejecute
                    // el siguiente envío en la cola.
                    numeroEnvio++;
                    colaEnvios();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Descartar", t.toString());
                FirebaseCrash.log("Error Descartar");
                FirebaseCrash.report(t);

                cambiarEstatusEnvio(generalOfflineDBList.get(numeroEnvio), Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    /**
     * MÉTODO QUE SE ENCARGA DE CABIAR LOS ESTATUS DE LOS REGISTROS EN LA TABLA DE REALM
     **/
    public void cambiarEstatusEnvio(GeneralOfflineDB generalOfflineDB, int idEnvio) {

        realmDB = Realm.getDefaultInstance();

        //Se cambia el estatus y se guarda nuevamente el Realm.
        generalOfflineDB.setStatusEnvio(idEnvio);

        realmDB.beginTransaction();
        realmDB.copyToRealmOrUpdate(generalOfflineDB);
        realmDB.commitTransaction();

        realmDB.close();

    }


}
