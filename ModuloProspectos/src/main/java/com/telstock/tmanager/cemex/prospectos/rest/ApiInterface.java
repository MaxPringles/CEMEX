package com.telstock.tmanager.cemex.prospectos.rest;

import com.telstock.tmanager.cemex.prospectos.model.ArchivosAlta;
import com.telstock.tmanager.cemex.prospectos.model.JSONfiltro;
import com.telstock.tmanager.cemex.prospectos.model.ProductosServiciosUpCross;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoServiciosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoSubsegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.GetProductosUpCross;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OfertaIntegral;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.JsonAltaProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonSemaforo.JsonSemaforo;
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

    @POST(Url.setProspecto)
    Call<ResponseBody> setProspecto(@Body JsonAltaProspecto params);

    @POST(Url.setContacto + "/{idProspecto}")
    Call<Void> setContactos(@Path("idProspecto") String idProspecto,
            @Body List<Contacto> params);

    /************** NUEVOS SERVICIOS MONGO **********************/

    @POST(Url.getProspectoFiltro)
    Call<List<Json>> getProspectoFiltro(@Body JSONfiltro jsonFiltro);

    @POST(Url.getSemaforo)
    Call<JsonSemaforo> getSemaforo(@Body JSONfiltro jsonFiltro);

    @Multipart
    @POST(Url.setCargaArchivo)
    Call<ResponseBody> setCargaArchivo(@Part MultipartBody.Part file,
            @Part("descripcion") RequestBody description,
            @Part("idAltaOffline") String idAltaOffline);

    @PUT(Url.setArchivosAlta)
    Call<ResponseBody> setArchivoAlta(@Body ArchivosAlta jsonObject);

    //@GET(Url.getProspecto)
    //Call<GetProspectosPOJO> getProspectos();

    @GET(Url.getArchivosProspecto + "/{idProspecto}")
    Call<List<ArchivosAltaDB>> getArchivosProspecto(@Path("idProspecto") String idProspecto);

    @GET(Url.getContactos + "/{idProspecto}")
    Call<List<Contacto>> getContactos(
            @Path("idProspecto") String idProspecto
    );

    @GET(Url.getOportunidadVentaPaso + "/{idPaso}/{idProspecto}")
    Call<OfertaIntegral> getOportunidadVentaPaso(@Path("idPaso") String idPaso, @Path("idProspecto") String idProspecto);


    @GET(Url.getCatalogoCliente)
    Call<List<ClienteDB>> getCatalogoCliente();

//    @GET(Url.getCatalogoObra)
//    Call<List<JsonObra>> getCatalogoObra();

    @GET(Url.getCatalogoObra)
    Call<List<ObraDB>> getCatalogoObra();

    @POST(Url.getProductosUpCross)
    Call<List<GetProductosUpCross>> getProductosUpCross(@Body ProductosServiciosUpCross productosServiciosUpCross);

    @POST(Url.getServiciosUpCros)
    Call<GetCatalogoPOJO> getServiciosUpCross(@Body ProductosServiciosUpCross productosServiciosUpCross);
}
