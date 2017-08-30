package mx.com.tarjetasdelnoreste.realmdb.servicios;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import mx.com.tarjetasdelnoreste.realmdb.ActividadesRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoActividadesPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoCampaniaRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoCargoRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoClienteRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoCompetidorRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoEstadosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoEstatusPGVRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoMotivoExclusionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoMunicipiosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoOportunidadVentaRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoProductosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSemaforoRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoServiciosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoStatusObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoSubsegmentosRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoNotificacionRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoObraRealm;
import mx.com.tarjetasdelnoreste.realmdb.CatalogoTipoProspectoRealm;
import mx.com.tarjetasdelnoreste.realmdb.ContactosRealm;
import mx.com.tarjetasdelnoreste.realmdb.JsonGlobalProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.PlanSemanalRealm;
import mx.com.tarjetasdelnoreste.realmdb.ProspectosRealm;
import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.funciones.AlertTokenToLogin;
import mx.com.tarjetasdelnoreste.realmdb.model.ActividadesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoActividadesPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCampaniaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCargoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoCompetidorDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstadosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoEstatusPGVDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMotivoExclusionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoMunicipiosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoOportunidadVentaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSemaforoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoStatusObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoSubsegmentosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoNotificacionDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoTipoProspectoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ContactosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoSubsegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.ItemsSupport;
import mx.com.tarjetasdelnoreste.realmdb.model.JSONFiltroProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonGlobalProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.NotificacionFirebaseDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Actividade;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.JsonAltaProspecto;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiClient;
import mx.com.tarjetasdelnoreste.realmdb.rest.ApiInterface;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by usr_micro13 on 07/02/2017.
 */

public class ServiceNotificaciones extends IntentService {

    Realm realmDB;
    ApiInterface apiInterface;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Gson gson;

    public ServiceNotificaciones() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        apiInterface = ApiClient.getClient(getApplicationContext()).create(ApiInterface.class);

        prefs = getApplicationContext().getSharedPreferences(Valores.SHARED_PREFERENCES_VARIABLES_GLOBALES, MODE_PRIVATE);
        editor = prefs.edit();

        //Revisa si ya hay notificaciones en proceso.
        if (!prefs.getBoolean(Valores.SHARED_PREFERENCES_NOTIFICACIONES, false)) {
            //En caso de que no haya ningún proceso activo, entonces se cambia la
            //sharedPreferences para indicar que habrá procesos activos.

            editor.putBoolean(Valores.SHARED_PREFERENCES_NOTIFICACIONES, true);
            editor.commit();

            //Se revisa la BD en busca de notificaciones.
            revisarNotificacionesEnBD();
        }
    }

    /** MÉTODO QUE REVISA SI HAY NOTIFICACIONES NO ENVIADAS **/
    private void revisarNotificacionesEnBD() {

        realmDB = Realm.getDefaultInstance();

        NotificacionFirebaseDB notificacionesResultados  = realmDB.where(NotificacionFirebaseDB.class)
                .equalTo("statusEnvio", Valores.ESTATUS_NO_ENVIADO)
                .findFirst();

        if (notificacionesResultados != null) {

            NotificacionFirebaseDB notificacionFirebaseDB = new NotificacionFirebaseDB(
                    notificacionesResultados.getIdNotificacion(),
                    notificacionesResultados.getIdTipoNotificacion(),
                    notificacionesResultados.getIdPrioridad(),
                    notificacionesResultados.getIdReferencia(),
                    notificacionesResultados.getTitle(),
                    notificacionesResultados.getMessage(),
                    notificacionesResultados.getStatusEnvio()
            );

            //Se cierra la instancia de Realm.
            realmDB.close();

            //Realiza la operación que indica la notificación.
            operacionNotificacion(notificacionFirebaseDB);

        } else { //En caso de que no haya notificaciones por mandar, se borra la
            //sharedPreferences para indicar que no hay procesos activos.

            //Se cambian los estatus de PROBLEMA_ENVIO a NO_ENVIADO, para que se intenten realizar
            //esas operaciones la próxima vez que se ejecute el servicio.
            List<NotificacionFirebaseDB> notificacionFirebaseDBList = realmDB.where(NotificacionFirebaseDB.class)
                    .equalTo("statusEnvio", Valores.ESTATUS_PROBLEMA_ENVIO)
                    .findAll();

            if (notificacionFirebaseDBList.size() > 0) {
                realmDB.beginTransaction();
                for (int i = 0; i < notificacionFirebaseDBList.size(); i++) {
                    //Se cambia el estatus y se guarda nuevamente el Realm.
                    notificacionFirebaseDBList.get(i).setStatusEnvio(Valores.ESTATUS_NO_ENVIADO);
                }

                //realmDB.copyToRealmOrUpdate(notificacionFirebaseDBList);
                realmDB.commitTransaction();
            }

            //Se cierra la instancia de Realm.
            realmDB.close();

            //Se indica que ya no hay ningún proceso en ejecución.
            editor.putBoolean(Valores.SHARED_PREFERENCES_NOTIFICACIONES, false);
            editor.commit();
        }
    }

    /** MÉTODO QUE INDICA QUÉ OPERACIÓN REALIZAR, DEPENDIENDO DEL TIPO DE NOTIFICACIÓN **/
    private void operacionNotificacion(NotificacionFirebaseDB notificacionFirebaseDB) {

        //Se cambia el estatus a ENVIANDO.
        cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIANDO);

        switch (notificacionFirebaseDB.getIdTipoNotificacion()) {

            case Valores.NOTIFICACIONES_NUEVO_PROSPECTO:
                nuevoProspecto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_NUEVO_CONTACTO:
                obtenerContactos(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_NUEVO_ARCHIVO:
                obtenerArchivosProspecto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_NUEVO_ACTIVIDAD:
                nuevoProspecto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_EDITAR_PROSPECTO:
                nuevoProspecto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_EDITAR_CONTACTO:
                obtenerContactos(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_EDITAR_ACTIVIDAD:
                nuevoProspecto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_ELIMINAR_PROSPECTO:
                eliminarProspecto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_ELIMINAR_CONTACTO:
                eliminarContacto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_ELIMINAR_ARCHIVO:
                eliminarArchivo(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_ELIMINAR_ACTIVIDAD:
                eliminarActividad(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_EDITAR_CATALOGO:
                editarCatalogo(notificacionFirebaseDB);
                break;

        }
    }

    /*************
     * MÉTODO QUE OBTIENE EL CATÁLOGO QUE SE DEBE DESCARGAR DEPENDIENDO DEL ID DE REFERENCIA
     **************/

    private void editarCatalogo(final NotificacionFirebaseDB notificacionFirebaseDB) {

        switch (notificacionFirebaseDB.getIdReferencia()) {

            case Valores.NOTIFICACIONES_CATALOGO_ESTADO:
                descargaEstados(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_MUNICIPIO:
                descargaCatalogoMunicipio(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_ESTATUS_OBRA:
                descargaEstatusObra(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_SUBSEGMENTO_PRODUCTO:
                descargarSubsegmentoProducto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_TIPO_PROSPECTO:
                descargarTipoProspecto(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_CAMPANIA:
                descargarCampania(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_SERVICIOS:
                descargarServicios(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_ACTIVIDADES_PGV:
                descargarActividadesPGV(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_SEMAFORO:
                descargaSemaforo(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_MOTIVOS_EXCLUSION:
                descargaMotivoExclusion(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_COMPETIDOR:
                descargaCompetidor(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_OPORTUNIDAD_VENTA:
                descargaOportunidadVenta(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_ESTATUS_PGV:
                descargaEstatusPGV(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_TIPO_OBRA:
                descargaTipoObra(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_CARGO:
                descargaCargo(notificacionFirebaseDB);
                break;

            case Valores.NOTIFICACIONES_CATALOGO_TIPO_NOTIFICACION:
                descargaTipoNotificacion(notificacionFirebaseDB);
                break;
        }
    }

    /************
     * OBTENCIÓN DE LOS DATOS DEL PROSPECTO, CONTACTOS Y ACTIVIDADES
     ***************/
    private void nuevoProspecto(final NotificacionFirebaseDB notificacionFirebaseDB) {

        JSONFiltroProspecto jsonFiltroProspecto = new JSONFiltroProspecto();

        jsonFiltroProspecto.setIdVendedorAsignado(prefs.getString(Valores.SHARED_PREFERENCES_ID_VENDEDOR, ""));
        jsonFiltroProspecto.setId(notificacionFirebaseDB.getIdReferencia());

        Call<List<Json>> getProspectosFiltro = apiInterface.getProspectoFiltro(jsonFiltroProspecto);

        getProspectosFiltro.enqueue(new Callback<List<Json>>() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<List<Json>> call, Response<List<Json>> response) {
                Log.d("", "");
                if (response.body() != null && response.code() == 200) {

                    //Se eliminan las tablas para que se llenen nuevamente en caso de que haya
                    //existido algún cambio.
                    ProspectosRealm.eliminarTabla();
                    ActividadesRealm.eliminarTabla();

                    List<Json> jsonList = response.body();

                    //Variables relacionadas con la información de Prospectos.
                    List<ProspectosDB> prospectosDBList = new ArrayList<>();
                    ProspectosDB prospectosDB;

                    //Variables relacionadas con la información de Actividades.
                    RealmList<ActividadesDB> actividadesDBList;

                    //Variables relacionadas con el guardado del json global de cada prospecto.
                    JsonGlobalProspectosDB jsonGlobalProspectosDB;
                    List<JsonGlobalProspectosDB> jsonGlobalProspectosDBList = new ArrayList<>();
                    Gson gson = new Gson(); //Objeto que convierte el objeto en un json.

                    String idProspecto;

                    for (int i = 0; i < jsonList.size(); i++) {

                        prospectosDB = new ProspectosDB();
                        actividadesDBList = new RealmList<>();
                        jsonGlobalProspectosDB = new JsonGlobalProspectosDB();

                        try {
                            idProspecto = jsonList.get(i).getId();

                            //Se guardan los datos correspondientes al json global del prospecto.
                            jsonGlobalProspectosDB.setIdProspecto(idProspecto);
                            jsonGlobalProspectosDB.setJsonGlobalProspectos(gson.toJson(jsonList.get(i)));
                            jsonGlobalProspectosDBList.add(jsonGlobalProspectosDB);

                            prospectosDB.setId(idProspecto);

                            prospectosDB.setNumeroRegistro(jsonList.get(i).getNumeroRegistro());
                            prospectosDB.setEstaDescartado(jsonList.get(i).isEstaDescartado());
                            prospectosDB.setFotografia(jsonList.get(i).getFotografia());
                            prospectosDB.setIdEstatusObraProspecto(jsonList.get(i).getEstatusObra().getId());
                            prospectosDB.setDescripcionEstatusObraProspecto(jsonList.get(i).getEstatusObra().getDescripcion());
                            prospectosDB.setIdSubsegmento(jsonList.get(i).getSubSegmento().getId());
                            prospectosDB.setDescripcionIdSubsegmento(jsonList.get(i).getSubSegmento().getDescripcion());
                            prospectosDB.setIdTipoProspecto(jsonList.get(i).getTipoProspecto().getId());
                            prospectosDB.setDescripcionTipoProspecto(jsonList.get(i).getTipoProspecto().getDescripcion());
                            prospectosDB.setIdEstatusProspecto(jsonList.get(i).getEstatusProspecto().getId());
                            prospectosDB.setDescripcionEstatusProspecto(jsonList.get(i).getEstatusProspecto().getDescripcion());

                            //Se devuelve la última actividad para guardarla en el objeto de prospectos.
                            obtenerActividadesFiltro(prospectosDB, idProspecto, jsonList.get(i).getActividades(), actividadesDBList);

                            prospectosDB.setObra(jsonList.get(i).getObra().getNombre());
                            prospectosDB.setCliente(jsonList.get(i).getCliente().getNombre());
                            prospectosDB.setIdCampania(jsonList.get(i).getObra().getIdCampania());
                            //prospectosDB.setDescripcionCampania(jsonList.get(i).getCliente().getCampania().getDescripcion());
                            prospectosDB.setComentarios(jsonList.get(i).getCliente().getComentarios());
                            prospectosDB.setRazonSocial(jsonList.get(i).getCliente().getRazonSocial());
                            prospectosDB.setRFC(jsonList.get(i).getCliente().getRfc());
                            prospectosDB.setTelefono(jsonList.get(i).getCliente().getTelefono());

                            //Información relacionada con la ubicación del prospecto.
                            prospectosDB.setCalle(jsonList.get(i).getObra().getDireccion().getCalle());
                            prospectosDB.setNumero(jsonList.get(i).getObra().getDireccion().getNumero());
                            prospectosDB.setColonia(jsonList.get(i).getObra().getDireccion().getColonia());
                            prospectosDB.setCodigoPostal(jsonList.get(i).getObra().getDireccion().getCodigoPostal());
                            prospectosDB.setNombrePais(jsonList.get(i).getObra().getDireccion().getPais().getNombre());
                            prospectosDB.setNombreEstado(jsonList.get(i).getObra().getDireccion().getEstado().getNombre());
                            prospectosDB.setNombreMunicipio(jsonList.get(i).getObra().getDireccion().getMunicipio().getNombre());
                            prospectosDB.setComentariosUbicacion(jsonList.get(i).getObra().getDireccion().getComentarios());
                            prospectosDB.setLatitud(jsonList.get(i).getObra().getDireccion().getUbicacion().getLatitud());
                            prospectosDB.setLongitud(jsonList.get(i).getObra().getDireccion().getUbicacion().getLongitud());
                        } catch (Exception e) {
                            Log.e("ERROR_PROSPECTOS", e.toString());
                        }

                        //Se guarda la lista de actividades que tendrá el prospecto.
                        //NOTA: No es necesario guardar la lista de actividades en Realm directamente, ya que
                        //al incluir la lista como un atributo de ProspectosDB, Realm realiza el
                        //guardado de ambos objetos automáticamente.
                        prospectosDB.setActividadesDBRealmList(actividadesDBList);

                        prospectosDBList.add(prospectosDB);
                    }

                    //Se guarda la lista de Contactos en Realm.
                    ProspectosRealm.guardarListaProspectos(prospectosDBList);

                    //Se guarda la lista de json's globales de prospectos.
                    JsonGlobalProspectosRealm.guardarJsonGlobalProspectos(jsonGlobalProspectosDBList);

                    //Se obtienen las actividades de los prospectos.
                    obtenerActividadProspectos();

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no, se busca por la siguiente notificación en la BD.
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                } else {
                    //Se coloca el status en PROBLEMA_ENVIO, esto permitirá que esta operación no
                    //se vuelva a intentar hasta la próxima vez que algo active el servicio,
                    //permitiendo al mismo tiempo seguir con las operaciones en cola.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_PROBLEMA_ENVIO);
                    FirebaseCrash.log("Cuarto estado notificación");

                    //Sea exitoso o no, se busca por la siguiente notificación en la BD.
                    revisarNotificacionesEnBD();
                }
            }

            @Override
            public void onFailure(Call<List<Json>> call, Throwable t) {
                Log.d("NOTIF_NUEVO_PROSPECTO", t.toString());

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    private void obtenerActividadesFiltro(ProspectosDB prospectosDB, String idProspecto, List<Actividade> actividadeList,
                                          List<ActividadesDB> actividadesDBList) {

        ActividadesDB actividadesDB;
        String descripcionActividad = ""; //Variable que obtiene la última actividad del prospecto.
        long idActividad = 0; //Variable que obtiene el id de la última actividad del prospecto.
        String idActividadAnterior = "";
        int estatusAgenda = 0; //Variable que obtiene el estatus de la última actividad del prospecto.

        if (actividadeList.size() > 0) {
            for (int i = 0; i < actividadeList.size(); i++) {

                actividadesDB = new ActividadesDB(); //Se inicializa nuevamente para no duplicar los registros.
                descripcionActividad = actividadeList.get(i).getTipoActividad().getDescripcion();
                idActividad = actividadeList.get(i).getTipoActividad().getId();
                estatusAgenda = actividadeList.get(i).getEstatusAgenda();
                idActividadAnterior = actividadeList.get(i).getId();

                //Llave compuesta del idProspecto y el idActividad.
                actividadesDB.setCompoundId(idProspecto + actividadeList.get(i).getId());

                actividadesDB.setIdTipoActividad(actividadeList.get(i).getTipoActividad().getId());
                actividadesDB.setDescripcionActividad(descripcionActividad);
                actividadesDB.setFechaHoraCitaInicio(actividadeList.get(i).getFechaHoraCitaInicio());
                actividadesDB.setFechaHoraCitaFin(actividadeList.get(i).getFechaHoraCitaFin());
                actividadesDB.setComentarios(actividadeList.get(i).getComentario());
                actividadesDB.setFechaInicioObra(actividadeList.get(i).getFechaInicioObra());
                actividadesDB.setConfirmarVolumenObra(actividadeList.get(i).getConfirmarVolumenObra());
                actividadesDB.setIdArchivoAdjunto(actividadeList.get(i).getIdArchivoAdjunto());
                actividadesDB.setIdProspecto(idProspecto);

                actividadesDB.setIdActividad(actividadeList.get(i).getId());

                //Se agrega el nuevo contacto a la lista de contactos.
                actividadesDBList.add(actividadesDB);
            }

            //Revisa el status de la penúltima actividad, para saber si es reagendada o no.
            if (actividadeList.size() > 1) {
                if (actividadeList.get(actividadeList.size() - 2).getEstatusAgenda() == Valores.ID_ACTIVIDAD_REAGENDADA)  {
                    estatusAgenda = Valores.ID_ACTIVIDAD_REAGENDADA;
                }
            }
        }

        //Se llenan los campos de actividad de ProspectosDB.
        prospectosDB.setDescripcionActividad(descripcionActividad);
        prospectosDB.setIdActividad(idActividad);
        prospectosDB.setEstatusAgenda(estatusAgenda);
        prospectosDB.setIdActividadAnterior(idActividadAnterior);

    }

    /** MÉTODO QUE LLENA LA TABLA DE PLANSEMANALDB **/
    public void obtenerActividadProspectos() {

        List<ProspectosDB> listaProspectosDB = ProspectosRealm.mostrarListaProspectosConActividad();
        ArrayList<PlanSemanalDB> listaPlanSemanal = new ArrayList<>();
        PlanSemanalDB planSemanalDB;
        for(ProspectosDB p : listaProspectosDB) {
            for(int i = 0;i < p.getActividadesDBRealmList().size(); i++) {
                planSemanalDB = new PlanSemanalDB();
                planSemanalDB.setIdProspecto(p.getId());
                planSemanalDB.setObra(p.getObra());
                planSemanalDB.setCliente(p.getCliente());
                planSemanalDB.setDescripcionTipoP(p.getDescripcionTipoProspecto());
                planSemanalDB.setIdActividad((int) p.getIdActividad());
                planSemanalDB.setDescripcionObra(p.getActividadesDBRealmList().get(i).getDescripcionActividad());
                planSemanalDB.setIdActividadAnterior(p.getActividadesDBRealmList().get(i).getIdActividad());

                planSemanalDB.setCalle(p.getCalle());
                planSemanalDB.setNumero(p.getNumero());
                planSemanalDB.setColonia(p.getColonia());
                planSemanalDB.setCodigoPostal(p.getCodigoPostal());
                planSemanalDB.setIdPais(p.getNombrePais());
                planSemanalDB.setIdEstado(p.getNombreEstado());
                planSemanalDB.setIdMunicipio(p.getNombreMunicipio());
                planSemanalDB.setComentariosUbicacion(p.getComentariosUbicacion());
                planSemanalDB.setImagen(p.getFotografia());
                planSemanalDB.setEstaDescartado(p.isEstaDescartado());
                planSemanalDB.setHoraInicio(p.getActividadesDBRealmList().get(i).getFechaHoraCitaInicio());
                planSemanalDB.setHoraFin(p.getActividadesDBRealmList().get(i).getFechaHoraCitaFin());
                planSemanalDB.setId(p.getActividadesDBRealmList().get(i).getCompoundId());
                planSemanalDB.setLatitud(p.getLatitud());
                planSemanalDB.setLongitud(p.getLongitud());

                planSemanalDB.setIdEstatusProspecto(p.getIdEstatusProspecto());

                listaPlanSemanal.add(planSemanalDB);
            }
        }

        PlanSemanalRealm.guardarListaPlanSemanal(listaPlanSemanal);
    }

    /********************************************************************************************/

    /*****************************************
     * OBTENCIÓN DE ARCHIVOS PARA UN PROSPECTO
     *****************************************/
    private void obtenerArchivosProspecto(final NotificacionFirebaseDB notificacionFirebaseDB) {

        Call<List<ArchivosAltaDB>> archivosAltaCall = apiInterface.getArchivosProspecto(notificacionFirebaseDB.getIdReferencia());

        archivosAltaCall.enqueue(new Callback<List<ArchivosAltaDB>>() {
            @Override
            public void onResponse(Call<List<ArchivosAltaDB>> call, Response<List<ArchivosAltaDB>> response) {
                if (response.body() != null && response.code() == 200) {
                    guardarArchivosProspecto(response.body(), notificacionFirebaseDB.getIdReferencia());

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no, se busca por la siguiente notificación en la BD.
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                } else {
                    //Se coloca el status en PROBLEMA_ENVIO, esto permitirá que esta operación no
                    //se vuelva a intentar hasta la próxima vez que algo active el servicio,
                    //permitiendo al mismo tiempo seguir con las operaciones en cola.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_PROBLEMA_ENVIO);

                    FirebaseCrash.log("Cuarto estado notificación");

                    //Sea exitoso o no, se busca por la siguiente notificación en la BD.
                    revisarNotificacionesEnBD();
                }
            }

            @Override
            public void onFailure(Call<List<ArchivosAltaDB>> call, Throwable t) {
                Log.e("GETARCHIVOS", t.toString());
                FirebaseCrash.log("Error GetArchivos");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    private void guardarArchivosProspecto(List<ArchivosAltaDB> archivosAltaList, String idProspecto) {

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


    /**********************************************************************************************/

    /************************************************
     * MÉTODO QUE OBTIENE LOS CONTACTOS DEL PROSPECTO
     ************************************************/
    public void obtenerContactos(final NotificacionFirebaseDB notificacionFirebaseDB) {

        Call<List<Contacto>> contactoCall = apiInterface.getContactos(notificacionFirebaseDB.getIdReferencia());

        contactoCall.enqueue(new Callback<List<Contacto>>() {
            @Override
            public void onResponse(Call<List<Contacto>> call, Response<List<Contacto>> response) {
                if (response.body() != null && response.code() == 200) {
                    //Se eliminan las tablas para que se llenen nuevamente en caso de que haya
                    //existido algún cambio.
                    ContactosRealm.eliminarTablaContactos();
                    guardarContactos(response.body(), notificacionFirebaseDB.getIdReferencia());

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                } else {
                    //Se coloca el status en PROBLEMA_ENVIO, esto permitirá que esta operación no
                    //se vuelva a intentar hasta la próxima vez que algo active el servicio,
                    //permitiendo al mismo tiempo seguir con las operaciones en cola.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_PROBLEMA_ENVIO);

                    FirebaseCrash.log("Cuarto estado notificación");

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                }
            }

            @Override
            public void onFailure(Call<List<Contacto>> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.e("GETCONTACTOS", t.toString());
                    FirebaseCrash.log("Error ContactosDB");
                    FirebaseCrash.report(t);

                    //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                    //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                    //problemas con la conexión a internet, por lo que no tiene caso intentar
                    //hacer el siguiente envío.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                }
            }
        });
    }

    /** MÉTODO QUE GUARDA LOS CONTACTOS OBTENIDOS EN LA BD **/
    private void guardarContactos(List<Contacto> contactoList, String idProspecto) {

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
    }

    /** MÉTODO QUE ELIMINA EL PROSPECTO ESPECIFICADO **/
    public void eliminarProspecto(NotificacionFirebaseDB notificacionFirebaseDB) {

        realmDB = Realm.getDefaultInstance();

        //Primero se borran todos los contactos del prospecto especificado.
        RealmResults<ContactosDB> contactosDBRealmResults = realmDB.where(ContactosDB.class)
                .equalTo("idProspecto", notificacionFirebaseDB.getIdReferencia())
                .findAll();

        if (contactosDBRealmResults.size() > 0) {
            realmDB.beginTransaction();
            contactosDBRealmResults.deleteAllFromRealm();
            realmDB.commitTransaction();
        }

        //Segundo se borran todos los archivos del prospecto especificado.
        RealmResults<ArchivosAltaDB> archivosAltaDBRealmResults = realmDB.where(ArchivosAltaDB.class)
                .equalTo("idProspecto", notificacionFirebaseDB.getIdReferencia())
                .findAll();

        if (archivosAltaDBRealmResults.size() > 0) {
            realmDB.beginTransaction();
            archivosAltaDBRealmResults.deleteAllFromRealm();
            realmDB.commitTransaction();
        }

        //Tercero se borran todas las actividades del prospecto especificado.
        RealmResults<ActividadesDB> actividadesDBRealmResults = realmDB.where(ActividadesDB.class)
                .equalTo("idProspecto", notificacionFirebaseDB.getIdReferencia())
                .findAll();

        if (actividadesDBRealmResults.size() > 0) {
            realmDB.beginTransaction();
            actividadesDBRealmResults.deleteAllFromRealm();
            realmDB.commitTransaction();
        }

        //Finalmente se borra el prospecto de la BD.
        ProspectosDB prospectosDB = realmDB.where(ProspectosDB.class)
                .equalTo("id", notificacionFirebaseDB.getIdReferencia())
                .findFirst();

        if (prospectosDB != null) {
            realmDB.beginTransaction();
            prospectosDB.deleteFromRealm();
            realmDB.commitTransaction();
        }

        realmDB.close();

        //Se cambia el estatus a ENVIADO.
        cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
        //Se muestra la notificación en el dispositivo.
        showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());
        //Sea exitoso o no, se busca por la siguiente notificación en la BD.
        revisarNotificacionesEnBD();
    }

    /** MÉTODO QUE ELIMINA LOS CONTACTOS DEL PROSPECTO ESPECIFICADO Y DESPUÉS LOS VUELVE A
     * RECUPERAR (para actualizarlos) **/
    public void eliminarContacto(NotificacionFirebaseDB notificacionFirebaseDB) {

        realmDB = Realm.getDefaultInstance();

        RealmResults<ContactosDB> contactosDBRealmResults = realmDB.where(ContactosDB.class)
                .equalTo("idProspecto", notificacionFirebaseDB.getIdReferencia())
                .findAll();

        if (contactosDBRealmResults.size() > 0) {
            realmDB.beginTransaction();
            contactosDBRealmResults.deleteAllFromRealm();
            realmDB.commitTransaction();
        }

        realmDB.close();

        errorTokenExpirado();
        //Una vez borrados los contactos del prospecto. Se vuelven a obtener
        //todos nuevamente.
        //obtenerContactos(notificacionFirebaseDB);
    }

    /** MÉTODO QUE ELIMINA LOS ARCHIVOS DEL PROSPECTO ESPECIFICADO Y DESPUÉS LOS VUELVE A
     * RECUPERAR (para actualizarlos) **/
    public void eliminarArchivo(NotificacionFirebaseDB notificacionFirebaseDB) {

        realmDB = Realm.getDefaultInstance();

        RealmResults<ArchivosAltaDB> archivosAltaDBRealmResults = realmDB.where(ArchivosAltaDB.class)
                .equalTo("idProspecto", notificacionFirebaseDB.getIdReferencia())
                .findAll();

        if (archivosAltaDBRealmResults.size() > 0) {
            realmDB.beginTransaction();
            archivosAltaDBRealmResults.deleteAllFromRealm();
            realmDB.commitTransaction();
        }

        realmDB.close();

        //Una vez borrados los archivos del prospecto. Se vuelven a obtener
        //todos nuevamente.
        obtenerArchivosProspecto(notificacionFirebaseDB);
    }

    /** MÉTODO QUE ELIMINA LA ACTIVIDAD ESPECIFICADA **/
    public void eliminarActividad(NotificacionFirebaseDB notificacionFirebaseDB) {

        realmDB = Realm.getDefaultInstance();

        ActividadesDB actividadesDB = realmDB.where(ActividadesDB.class)
                .equalTo("idActividad", notificacionFirebaseDB.getIdReferencia())
                .findFirst();

        if (actividadesDB != null) {
            realmDB.beginTransaction();
            actividadesDB.deleteFromRealm();
            realmDB.commitTransaction();
        }

        realmDB.close();

        //Se cambia el estatus a ENVIADO.
        cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
        //Se muestra la notificación en el dispositivo.
        showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());
        //Sea exitoso o no, se busca por la siguiente notificación en la BD.
        revisarNotificacionesEnBD();
    }

    public void descargaEstados(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getEstadosCall = apiInterface.getCatalogoEstado();

        getEstadosCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {
                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoEstadosDB catalogoEstadosDB;
                    ArrayList<CatalogoEstadosDB> listaEstadosDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoEstadosDB = new CatalogoEstadosDB();

                        catalogoEstadosDB.setId(items.getId());
                        catalogoEstadosDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoEstadosDB.setDescripcion(items.getDescripcion());
                        catalogoEstadosDB.setIdPadre(items.getIdPadre());
                        listaEstadosDB.add(catalogoEstadosDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoEstadosRealm.guardarListaEstados(listaEstadosDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Error Descarga Estados", t.toString());
                FirebaseCrash.log("Error Descarga Estados");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });

    }

    public void descargaCatalogoMunicipio(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getMunicipioCall = apiInterface.getCatalogoMunicipio();

        getMunicipioCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoMunicipiosDB catalogoMunicipiosDB;
                    ArrayList<CatalogoMunicipiosDB> listaMunicipiosDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoMunicipiosDB = new CatalogoMunicipiosDB();

                        catalogoMunicipiosDB.setId(items.getId());
                        catalogoMunicipiosDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoMunicipiosDB.setDescripcion(items.getDescripcion());
                        catalogoMunicipiosDB.setIdPadre(items.getIdPadre());
                        listaMunicipiosDB.add(catalogoMunicipiosDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoMunicipiosRealm.guardarListaMunicipios(listaMunicipiosDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Municipios", t.toString());
                FirebaseCrash.log("Descarga Municipios");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);

            }
        });
    }

    public void descargaEstatusObra(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getEstatusObraPOJOCall = apiInterface.getCatalogoEstatusObra();

        getEstatusObraPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoStatusObraDB catalogoStatusObraDB;
                    ArrayList<CatalogoStatusObraDB> listaStatusObraDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoStatusObraDB = new CatalogoStatusObraDB();

                        catalogoStatusObraDB.setId(items.getId());
                        catalogoStatusObraDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoStatusObraDB.setDescripcion(items.getDescripcion());
                        catalogoStatusObraDB.setIdPadre(items.getIdPadre());
                        listaStatusObraDB.add(catalogoStatusObraDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoStatusObraRealm.guardarListaStatusObra(listaStatusObraDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Estatus Obra", t.toString());
                FirebaseCrash.log("Descarga Estatus Obra");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargarSubsegmentoProducto(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<List<GetCatalogoSubsegmento>> getSubsegmentoPOJOCall = apiInterface.getCatalogoSubsegmentoProducto();

        getSubsegmentoPOJOCall.enqueue(new Callback<List<GetCatalogoSubsegmento>>() {
            @Override
            public void onResponse(Call<List<GetCatalogoSubsegmento>> call, Response<List<GetCatalogoSubsegmento>> response) {
                if (response.body() != null && response.code() == 200 && response.body().size() > 0) {

                    List<GetCatalogoSubsegmento> subsegmentos = response.body();
                    CatalogoSubsegmentosDB catalogoSubsegmentosDB;
                    ArrayList<CatalogoSubsegmentosDB> listaSubsegmentoDB = new ArrayList<>();
//
                    for (GetCatalogoSubsegmento items : subsegmentos) { //Se guardan los objetos individualmente.
                        catalogoSubsegmentosDB = new CatalogoSubsegmentosDB();

                        catalogoSubsegmentosDB.setId(items.getId());
                        catalogoSubsegmentosDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoSubsegmentosDB.setDescripcion(items.getDescripcion());
                        catalogoSubsegmentosDB.setIdPadre(items.getIdPadre());

                        CatalogoProductosRealm.guardarListaProductos(items.getProductos());
                        listaSubsegmentoDB.add(catalogoSubsegmentosDB);
                    }
//
//                    //Se guarda en la base de Realm.
                    CatalogoSubsegmentosRealm.guardarListaSubsegmentos(listaSubsegmentoDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<List<GetCatalogoSubsegmento>> call, Throwable t) {
                Log.d("Descarga Subsegmento", t.toString());
                FirebaseCrash.log("Descarga Subsegmento");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargarTipoProspecto(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getTipoProspectoPOJOCall = apiInterface.getCatalogoTipoProspecto();

        getTipoProspectoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoTipoProspectoDB catalogoTipoProspectoDB;
                    ArrayList<CatalogoTipoProspectoDB> listaTipoProspectoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoTipoProspectoDB = new CatalogoTipoProspectoDB();

                        catalogoTipoProspectoDB.setId(items.getId());
                        catalogoTipoProspectoDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoTipoProspectoDB.setDescripcion(items.getDescripcion());
                        catalogoTipoProspectoDB.setIdPadre(items.getIdPadre());
                        listaTipoProspectoDB.add(catalogoTipoProspectoDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoTipoProspectoRealm.guardarListaTipoProspecto(listaTipoProspectoDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Tipo Prospecto", t.toString());
                FirebaseCrash.log("Descarga Tipo Prospecto");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargarCampania(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getTipoProspectoPOJOCall = apiInterface.getCampania();

        getTipoProspectoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoCampaniaDB catalogoCampaniaDB;
                    ArrayList<CatalogoCampaniaDB> listaTipoProspectoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoCampaniaDB = new CatalogoCampaniaDB();

                        catalogoCampaniaDB.setId(items.getId());
                        catalogoCampaniaDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoCampaniaDB.setDescripcion(items.getDescripcion());
                        catalogoCampaniaDB.setIdPadre(items.getIdPadre());
                        listaTipoProspectoDB.add(catalogoCampaniaDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoCampaniaRealm.guardarListaCampania(listaTipoProspectoDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Tipo Prospecto", t.toString());
                FirebaseCrash.log("Descarga Tipo Prospecto");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargarServicios(final NotificacionFirebaseDB notificacionFirebaseDB) {
        final Call<GetCatalogoPOJO> getServicioCall = apiInterface.getServicio();

        getServicioCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoServiciosDB catalogoServicioDB;
                    ArrayList<CatalogoServiciosDB> listaServiciosDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoServicioDB = new CatalogoServiciosDB();

                        catalogoServicioDB.setId(items.getId());
                        catalogoServicioDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoServicioDB.setDescripcion(items.getDescripcion());
                        catalogoServicioDB.setIdPadre(items.getIdPadre());
                        listaServiciosDB.add(catalogoServicioDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoServiciosRealm.guardarListaServicios(listaServiciosDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Error Servicio", t.toString());
                FirebaseCrash.log("Descarga Servicio");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargarActividadesPGV(final NotificacionFirebaseDB notificacionFirebaseDB) {
        final Call<GetCatalogoPOJO> getActividadesPGVCall = apiInterface.getCatalogoActividadesPGV();

        getActividadesPGVCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoActividadesPGVDB catalogoActividadPGVDB;
                    ArrayList<CatalogoActividadesPGVDB> listaActividadesPGVDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoActividadPGVDB = new CatalogoActividadesPGVDB();

                        catalogoActividadPGVDB.setId(items.getId());
                        catalogoActividadPGVDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoActividadPGVDB.setDescripcion(items.getDescripcion());
                        catalogoActividadPGVDB.setIdPadre(items.getIdPadre());
                        listaActividadesPGVDB.add(catalogoActividadPGVDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoActividadesPGVRealm.guardarListaActividadesPGV(listaActividadesPGVDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();


                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Error Servicio", t.toString());
                FirebaseCrash.log("Descarga Servicio");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaSemaforo(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getSemaforoPOJOCall = apiInterface.getCatalogoSemaforo();

        getSemaforoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoSemaforoDB catalogoSemaforoDB;
                    ArrayList<CatalogoSemaforoDB> listaSemaforoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoSemaforoDB = new CatalogoSemaforoDB();

                        catalogoSemaforoDB.setId(items.getId());
                        catalogoSemaforoDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoSemaforoDB.setDescripcion(items.getDescripcion());
                        catalogoSemaforoDB.setIdPadre(items.getIdPadre());
                        listaSemaforoDB.add(catalogoSemaforoDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoSemaforoRealm.guardarListaSemaforo(listaSemaforoDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Descarga Semaforo", t.toString());
                FirebaseCrash.log("Descarga Semaforo");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaMotivoExclusion(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getMotivoExclusionPOJOCall = apiInterface.getCatalogoMotivosExclusion();

        getMotivoExclusionPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoMotivoExclusionDB catalogoMotivoExclusionDB;
                    ArrayList<CatalogoMotivoExclusionDB> listaMotivoExclusionDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoMotivoExclusionDB = new CatalogoMotivoExclusionDB();

                        catalogoMotivoExclusionDB.setId(items.getId());
                        catalogoMotivoExclusionDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoMotivoExclusionDB.setDescripcion(items.getDescripcion());
                        catalogoMotivoExclusionDB.setIdPadre(items.getIdPadre());
                        listaMotivoExclusionDB.add(catalogoMotivoExclusionDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoMotivoExclusionRealm.guardarListaMotivoExclusion(listaMotivoExclusionDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Motivo Exclusion", t.toString());
                FirebaseCrash.log("Descarga MotivoExclusion");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaCompetidor(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getCompetidorPOJOCall = apiInterface.getCatalogoCompetidor();

        getCompetidorPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoCompetidorDB catalogoCompetidorDB;
                    ArrayList<CatalogoCompetidorDB> listaCompetidorDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoCompetidorDB = new CatalogoCompetidorDB();

                        catalogoCompetidorDB.setId(items.getId());
                        catalogoCompetidorDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoCompetidorDB.setDescripcion(items.getDescripcion());
                        catalogoCompetidorDB.setIdPadre(items.getIdPadre());
                        listaCompetidorDB.add(catalogoCompetidorDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoCompetidorRealm.guardarListaCompetidor(listaCompetidorDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Competidor", t.toString());
                FirebaseCrash.log("Descarga Competidor");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaOportunidadVenta(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getOportunidadVentaPOJOCall = apiInterface.getCatalogoOportunidadVenta();

        getOportunidadVentaPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoOportunidadVentaDB catalogoOportunidadVentaDB;
                    ArrayList<CatalogoOportunidadVentaDB> listaOportunidadVentaDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoOportunidadVentaDB = new CatalogoOportunidadVentaDB();

                        catalogoOportunidadVentaDB.setId(items.getId());
                        catalogoOportunidadVentaDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoOportunidadVentaDB.setDescripcion(items.getDescripcion());
                        catalogoOportunidadVentaDB.setIdPadre(items.getIdPadre());
                        listaOportunidadVentaDB.add(catalogoOportunidadVentaDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoOportunidadVentaRealm.guardarListaOportunidadVenta(listaOportunidadVentaDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Oportunidad Venta", t.toString());
                FirebaseCrash.log("Descarga Oportunidad Venta");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaEstatusPGV(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getEstatusPGVPOJOCall = apiInterface.getCatalogoEstatusPGV();

        getEstatusPGVPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoEstatusPGVDB catalogoEstatusPGVDB;
                    ArrayList<CatalogoEstatusPGVDB> listaEstatusPGVDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoEstatusPGVDB = new CatalogoEstatusPGVDB();

                        catalogoEstatusPGVDB.setId(items.getId());
                        catalogoEstatusPGVDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoEstatusPGVDB.setDescripcion(items.getDescripcion());
                        catalogoEstatusPGVDB.setIdPadre(items.getIdPadre());
                        listaEstatusPGVDB.add(catalogoEstatusPGVDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoEstatusPGVRealm.guardarListaEstatusPGV(listaEstatusPGVDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Estatus PGV", t.toString());
                FirebaseCrash.log("Descarga Estatus PGV");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaTipoObra(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getTipoObraPOJOCall = apiInterface.getCatalogoTipoObra();

        getTipoObraPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoTipoObraDB catalogoTipoObraDB;
                    ArrayList<CatalogoTipoObraDB> listaTipoObraDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoTipoObraDB = new CatalogoTipoObraDB();

                        catalogoTipoObraDB.setId(items.getId());
                        catalogoTipoObraDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoTipoObraDB.setDescripcion(items.getDescripcion());
                        catalogoTipoObraDB.setIdPadre(items.getIdPadre());
                        listaTipoObraDB.add(catalogoTipoObraDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoTipoObraRealm.guardarListaTipoObra(listaTipoObraDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();
                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Tipo Obra", t.toString());
                FirebaseCrash.log("Descarga Tipo Obra");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaCargo(final NotificacionFirebaseDB notificacionFirebaseDB) {

        final Call<GetCatalogoPOJO> getCargoPOJOCall = apiInterface.getCatalogoCargo();

        getCargoPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoCargoDB catalogoCargoDB;
                    ArrayList<CatalogoCargoDB> listaCargoDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoCargoDB = new CatalogoCargoDB();

                        catalogoCargoDB.setId(items.getId());
                        catalogoCargoDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoCargoDB.setDescripcion(items.getDescripcion());
                        catalogoCargoDB.setIdPadre(items.getIdPadre());
                        listaCargoDB.add(catalogoCargoDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoCargoRealm.guardarListaCargo(listaCargoDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();

                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Tipo Obra", t.toString());
                FirebaseCrash.log("Descarga Tipo Obra");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    public void descargaTipoNotificacion(final NotificacionFirebaseDB notificacionFirebaseDB) {
        final Call<GetCatalogoPOJO> getTipoNotificacionPOJOCall = apiInterface.getCatalogoTipoNotificacion();

        getTipoNotificacionPOJOCall.enqueue(new Callback<GetCatalogoPOJO>() {
            @Override
            public void onResponse(Call<GetCatalogoPOJO> call, Response<GetCatalogoPOJO> response) {
                if (response.body() != null && response.code() == 200 && response.body().getItems().size() > 0) {

                    GetCatalogoPOJO catalogo = response.body();
                    List<ItemsSupport> itemsList = catalogo.getItems();
                    CatalogoTipoNotificacionDB catalogoTipoNotificacionDB;
                    ArrayList<CatalogoTipoNotificacionDB> listaTipoNotificacionDB = new ArrayList<>();

                    for (ItemsSupport items : itemsList) { //Se guardan los objetos individualmente.
                        catalogoTipoNotificacionDB = new CatalogoTipoNotificacionDB();

                        catalogoTipoNotificacionDB.setId(items.getId());
                        catalogoTipoNotificacionDB.setIdCatalogo(items.getIdCatalogo());
                        catalogoTipoNotificacionDB.setDescripcion(items.getDescripcion());
                        catalogoTipoNotificacionDB.setIdPadre(items.getIdPadre());
                        listaTipoNotificacionDB.add(catalogoTipoNotificacionDB);
                    }

                    //Se guarda en la base de Realm.
                    CatalogoTipoNotificacionRealm.guardarListaTipoNotificacion(listaTipoNotificacionDB);

                    //Se cambia el status a ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_ENVIADO);
                    //Se muestra la notificación en el dispositivo.
                    showNotification(notificacionFirebaseDB.getTitle(), notificacionFirebaseDB.getMessage());

                    //Sea exitoso o no,
                    revisarNotificacionesEnBD();


                } else if (response.code() == Valores.TOKEN_EXPIRADO) {
                    //Se regresa el status a NO_ENVIADO.
                    cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
                    //Borra las SharedPreferences para que el usuario sea dirigido al Login.
                    errorTokenExpirado();
                }
            }

            @Override
            public void onFailure(Call<GetCatalogoPOJO> call, Throwable t) {
                Log.d("Tipo Notificacion", t.toString());
                FirebaseCrash.log("Descarga Tipo Notificacion");
                FirebaseCrash.report(t);

                //Se regresa el status a NO_ENVIADO. Sin embargo no se intenta hacer el siguiente
                //envío, esto debido a que, si entra en el onFailure, es más probable que sea por
                //problemas con la conexión a internet, por lo que no tiene caso intentar
                //hacer el siguiente envío.
                cambiarEstatusEnvio(notificacionFirebaseDB, Valores.ESTATUS_NO_ENVIADO);
            }
        });
    }

    /**
     * MÉTODO QUE SE ENCARGA DE CABIAR LOS ESTATUS DE LOS REGISTROS EN LA TABLA DE REALM
     **/
    public void cambiarEstatusEnvio(NotificacionFirebaseDB notificacionFirebaseDB, int idEnvio) {

        realmDB = Realm.getDefaultInstance();

        //Se cambia el estatus y se guarda nuevamente el Realm.
        notificacionFirebaseDB.setStatusEnvio(idEnvio);

        realmDB.beginTransaction();
        realmDB.copyToRealmOrUpdate(notificacionFirebaseDB);
        realmDB.commitTransaction();

        realmDB.close();

    }

    /** MÉTODO QUE ELIMINA LAS SHAREDPREFERENCES PARA QUE EL USUARIO SE VAYA AL LOGIN Y NO
     * SEA REDIRECCIONADO AL HOME
     */
    public void errorTokenExpirado() {
        /*
        //Indica que se cerrará sesión, para que al abrir la pantalla de Login, no se haga el
        //redireccionamiento.
        editor.putBoolean(Valores.SHARED_PREFERENCES_SESION_INICIADA, false);
        //Se indica que ya no hay ningún proceso de notificación en ejecución.
        editor.putBoolean(Valores.SHARED_PREFERENCES_NOTIFICACIONES, false);
        editor.commit();

        Intent intent = new Intent();
        intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.LoginActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getApplicationContext().startActivity(intent);*/
    }

    /** MÉTODO QUE MUESTRA LA NOTIFICACIÓN **/
    private void showNotification(String title, String message){
        //Creating a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.simplifiedcoding.net"));
        //Se envia el extra a la actividad madre, para que ésta muestre el fragment deseado.
        Intent intent = new Intent();
        intent.setClassName("com.telstock.tmanager.cemex", "com.telstock.tmanager.cemex.LoginActivity");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}
