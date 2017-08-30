package com.telstock.tmanager.cemex.modulocitas.rest;

import com.telstock.tmanager.cemex.modulocitas.model.ArchivosAlta;
import com.telstock.tmanager.cemex.modulocitas.model.JSONdescartar;
import com.telstock.tmanager.cemex.modulocitas.model.JSONfiltro;
import com.telstock.tmanager.cemex.modulocitas.model.ProspectoPerdido;

import org.json.JSONObject;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.ArchivosAltaDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoProductoServicioDB;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OfertaIntegral;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonObra.JsonObra;
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
import retrofit2.http.Query;

/**
 * Created by czamora on 9/28/16.
 */
public interface ApiInterface {

    @POST(Url.getProspectoFiltro)
    Call<List<Json>> getProspectoFiltro(@Body JSONfiltro jsonFiltro);

    @Multipart
    @POST(Url.setCargaArchivo)
    Call<ResponseBody> setCargaArchivo(@Part MultipartBody.Part file,
                                       @Part("descripcion") RequestBody description);

    @PUT(Url.setArchivosAlta)
    Call<ResponseBody> setArchivoAlta(@Body ArchivosAlta jsonObject);

    @PUT(Url.setDescartar)
    Call<ResponseBody> setDescartar(@Body JSONdescartar jsonDescartar);

    @PUT(Url.setActividadAlta + "/{idProspecto}")
    Call<ResponseBody> setActividadAlta(@Path("idProspecto") String idProspecto,
                                        @Body JsonAltaActividades params);


    @PUT(Url.putObra + "/{idObra}")
    Call<ResponseBody> putObra(@Path("idObra") String idObra,
                               @Body JsonObra jsonObra);


    @GET(Url.getArchivosProspecto + "/{idProspecto}")
    Call<List<ArchivosAltaDB>> getArchivosProspecto(@Path("idProspecto") String idProspecto);

    @GET(Url.getOportunidadVentaInicial + "/{idProspecto}")
    Call<OfertaIntegral> getOportunidadVentaInicial(@Path("idProspecto") String idProspecto);

    @GET(Url.getOportunidadVentaPaso + "/{idPaso}/{idProspecto}")
    Call<OfertaIntegral> getOportunidadVentaPaso(@Path("idPaso") String idPaso, @Path("idProspecto") String idProspecto);

    @PUT(Url.putMotivoExclusion)
    Call<ResponseBody> putMotivoExclusion(@Body ProspectoPerdido prospectoPerdido);

}
