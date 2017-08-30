package com.telstock.tmanager.cemex.rest;

import com.telstock.tmanager.cemex.model.GetMenuPOJO;
import com.telstock.tmanager.cemex.model.GetUsuarioPOJO;
import com.telstock.tmanager.cemex.model.Token;
import com.telstock.tmanager.cemex.prospectos.model.JSONfiltro;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoClienteDB.ClienteDB;
import mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB.ObraDB;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoSubsegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonFiltroVendedorAsignado;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades.JsonMostrarActividades;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by USRMICRO10 on 10/08/2016.
 */
public interface ApiInterface  {
//    @POST(Url.getToken)
//    Call<TokenPOJO> getToken(@Header("usuario") String usuario, @Header("contrasena") String contrasena);
    @FormUrlEncoded
    @POST(Url.getToken)
    Call<Token> getToken(@Field("username")String username,
                         @Field("password")String password,
                         @Field("idPlataforma")String idPlataforma,
                         @Field("idEmpresa")String idEmpresa,
                         @Field("imei") String imei
    );

    @GET(Url.getMenu)
    Call<GetMenuPOJO> getMenu(@Header("idPlataforma") String plataforma);

    @GET(Url.getUsuario)
    Call<GetUsuarioPOJO> getUsuario(@Query("me") String usuario);

    @GET(Url.getCatalogoEstado)
    Call<GetCatalogoPOJO> getCatalogoConPadre(@Query("Nombre") String nombre,
                                              @Query("idPadre") String idPadre);

    @GET(Url.getCatalogoEstado)
    Call<GetCatalogoPOJO> getCatalogoEstado();

    @GET(Url.getCatalogoAccion)
    Call<GetCatalogoPOJO> getCatalogoAccion();

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

//    @GET(Url.getCatalogoCliente)
//    Call<List<JsonCliente>> getCatalogoCliente();

    @GET(Url.getCatalogoCliente)
    Call<List<ClienteDB>> getCatalogoCliente();

//    @GET(Url.getCatalogoObra)
//    Call<List<JsonObra>> getCatalogoObra();

    @GET(Url.getCatalogoObra)
    Call<List<ObraDB>> getCatalogoObra();

    @POST(Url.getProspectoFiltro)
    Call<List<Json>> getProspectoFiltro(@Body JSONfiltro jsonFiltro);

    @GET(Url.getCatalogoTipoNotificacion)
    Call<GetCatalogoPOJO> getCatalogoTipoNotificacion();

    @POST(Url.getActividadesTodo)
    Call<List<JsonMostrarActividades>> getActividadesTodo(@Body JsonFiltroVendedorAsignado jsonFiltro);
}
