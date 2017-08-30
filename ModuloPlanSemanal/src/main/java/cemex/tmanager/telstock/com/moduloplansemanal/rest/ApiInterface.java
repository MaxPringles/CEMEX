package cemex.tmanager.telstock.com.moduloplansemanal.rest;

import org.json.JSONObject;

import java.util.List;

import cemex.tmanager.telstock.com.moduloplansemanal.model.ItemsCatalogoPOJO;
import cemex.tmanager.telstock.com.moduloplansemanal.model.JSONfiltro;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoPOJO;
import mx.com.tarjetasdelnoreste.realmdb.model.GetCatalogoSubsegmento;
import mx.com.tarjetasdelnoreste.realmdb.model.JsonFiltroVendedorAsignado;
import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.json.Json;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividadesNuevas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.OfertaIntegral;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividadesAdministrativas.JsonAltaActividadesAdministrativas;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades.JsonMostrarActividades;
import mx.com.tarjetasdelnoreste.realmdb.webservice.Url;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Cesar on 11/09/2016.
 */
public interface ApiInterface {

    @GET(Url.getCatalogoActividadesPGV)
//    Call<List<GetCatalogoPOJO>> getCatalogoActividadesPGV();
    Call<GetCatalogoPOJO> getCatalogoActividadesPGV();

    @GET(Url.getCatalogoSubsegmentoProducto)
    Call<List<GetCatalogoSubsegmento>> getCatalogoSubsegmentoProducto();

    @GET(Url.getCatalogoTipoProspecto)
//    Call<List<GetCatalogoPOJO>> getCatalogoTipoProspecto();
    Call<GetCatalogoPOJO> getCatalogoTipoProspecto();

    @POST(Url.getProspectoFiltro)
    Call<List<Json>> getProspectoFiltro(@Body JSONfiltro jsonFiltro);

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

    @GET(Url.getOportunidadVentaPaso + "/{idPaso}/{idProspecto}")
    Call<OfertaIntegral> getOportunidadVentaPaso(@Path("idPaso") String idPaso, @Path("idProspecto") String idProspecto);

    @POST(Url.getActividadesTodo)
        Call<List<JsonMostrarActividades>> getActividadesTodo(@Body JsonFiltroVendedorAsignado jsonFiltro);
}

