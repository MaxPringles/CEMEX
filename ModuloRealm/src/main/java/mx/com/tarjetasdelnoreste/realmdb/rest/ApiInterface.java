package mx.com.tarjetasdelnoreste.realmdb.rest;


import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.BuzonNotificacionesDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CoordenadasDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoSubsegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.JSONFiltroProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonFiltroNotificaciones;
import mx.com.tarjetasdelnoreste.realmdb.model.ObraDetalleDB;
import mx.com.tarjetasdelnoreste.realmdb.model.Ruta;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividadesNuevas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas.JsonAltaActividadesAdministrativas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.JsonAltaProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonDescartar.JSONdescartar;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonObra.JsonObra;
import mx.com.tarjetasdelnoreste.realmdb.model.modelOffline.ArchivosAlta;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by USRMICRO10 on 01/09/2016.
 */
public interface ApiInterface {

    @POST(Url.setRuta)
    Call<String> setRuta(@Body Ruta ruta);

    @POST(Url.setCoordenadas)
    Call<String> setCoordenadas(@Body List<Ruta> listaRuta);


    @PUT(Url.putObra + "/{idObra}")
    Call<ResponseBody> putObra(@Path("idObra") String idObra,
                               @Body JsonObra jsonObra);

    @GET(Url.getObraDetalle + "/{idObra}")
    Call<ObraDetalleDB> getObraDetalle(@Path("idObra") String idObra);

    @POST(Url.getNotificaciones)
    Call<List<BuzonNotificacionesDB>> getNotificaciones(
            @Body JsonFiltroNotificaciones jsonFiltroNotificaciones
            );

    /** OPERACIONES PARA EL ENVÍO OFFLINE **/

    @Multipart
    @POST(Url.setCargaArchivo)
    Call<ResponseBody> setCargaArchivo(@Part MultipartBody.Part file,
                                       @Part("descripcion") RequestBody description,
                                       @Part("idAltaOffline") String idAltaOffline);

    @POST(Url.setProspecto)
    Call<ResponseBody> setProspecto(@Body JsonAltaProspecto params);

    @POST(Url.setContacto + "/{idProspecto}")
    Call<Void> setContactos(@Path("idProspecto") String idProspecto,
                            @Body List<Contacto> params);

    @PUT(Url.setActividadAlta + "/{idProspecto}")
    Call<ResponseBody> setActividadAlta(@Path("idProspecto") String idProspecto,
                                        @Body JsonAltaActividades params);

    @PUT(Url.setActividadAlta + "/{idProspecto}")
    Call<ResponseBody> setActividadAltaSinActividadAnterior(@Path("idProspecto") String idProspecto,
                                                            @Body JsonAltaActividadesNuevas params);

    @POST(Url.setAltaActividadAdministrativa + "/{idTipoAsignacion}" + "/{idReferencia}")
    Call<ResponseBody> setActividadAdministrativa(@Path("idTipoAsignacion") int idTipoAsignacion,
                                                  @Path("idReferencia") String idReferencia,
                                                  @Body JsonAltaActividadesAdministrativas params);

    @PUT(Url.setDescartar)
    Call<ResponseBody> setDescartar(@Body JSONdescartar jsonDescartar);

    /******** OPERACIONES LIGADAS A NOTIFICACIONES ********/
    @POST(Url.getProspectoFiltro)
    Call<List<Json>> getProspectoFiltro(@Body JSONFiltroProspecto jsonFiltroProspecto);

    @GET(Url.getArchivosProspecto + "/{idProspecto}")
    Call<List<ArchivosAltaDB>> getArchivosProspecto(@Path("idProspecto") String idProspecto);

    @GET(Url.getContactos + "/{idProspecto}")
    Call<List<Contacto>> getContactos(
            @Path("idProspecto") String idProspecto
    );

    @PUT(Url.setArchivosAlta)
    Call<ResponseBody> setArchivoAlta(@Body ArchivosAlta jsonObject);
    /*****************************************************/

    @GET(Url.getCatalogoEstado)
    Call<GetCatalogoPOJO> getCatalogoEstado();

    @GET(Url.getCatalogoMunicipio)
    Call<GetCatalogoPOJO> getCatalogoMunicipio();

    @GET(Url.getCatalogoEstatusObra)
    Call<GetCatalogoPOJO> getCatalogoEstatusObra();

    @GET(Url.getCatalogoSubsegmentoProducto)
    Call<List<GetCatalogoSubsegmento>> getCatalogoSubsegmentoProducto();

    @GET(Url.getCatalogoTipoProspecto)
    Call<GetCatalogoPOJO> getCatalogoTipoProspecto();

    @GET(Url.getCatalogoCampania)
    Call<GetCatalogoPOJO> getCampania();

    @GET(Url.getCatalogoServicio)
    Call<GetCatalogoPOJO> getServicio();

    @GET(Url.getCatalogoActividadesPGV)
    Call<GetCatalogoPOJO> getCatalogoActividadesPGV();

    @GET(Url.getCatalogoSemaforo)
    Call<GetCatalogoPOJO> getCatalogoSemaforo();

    @GET(Url.getCatalogoMotivoExclusion)
    Call<GetCatalogoPOJO> getCatalogoMotivosExclusion();

    @GET(Url.getCatalogoCompetidor)
    Call<GetCatalogoPOJO> getCatalogoCompetidor();

    @GET(Url.getCatalogoOportunidadVenta)
    Call<GetCatalogoPOJO> getCatalogoOportunidadVenta();

    @GET(Url.getCatalogoEstatusPGV)
    Call<GetCatalogoPOJO> getCatalogoEstatusPGV();

    @GET(Url.getCatalogoTipoObra)
    Call<GetCatalogoPOJO> getCatalogoTipoObra();

    @GET(Url.getCatalogoCargo)
    Call<GetCatalogoPOJO> getCatalogoCargo();

    @GET(Url.getCatalogoCliente)
    Call<List<ClienteDB>> getCatalogoCliente();

    @GET(Url.getCatalogoObra)
    Call<List<ObraDB>> getCatalogoObra();

    @GET(Url.getCatalogoTipoNotificacion)
    Call<GetCatalogoPOJO> getCatalogoTipoNotificacion();

}
