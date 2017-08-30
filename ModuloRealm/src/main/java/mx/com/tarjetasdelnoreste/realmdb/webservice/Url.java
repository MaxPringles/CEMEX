package mx.com.tarjetasdelnoreste.realmdb.webservice;

/**
 * Created by czamora on 9/13/16.
 */
public class Url {

    //Versión del Web Service
    //Sólo incrementar en caso de un cambio en el consumo del WS
    //Esta versión sólo se lleva en móviles
    public final static double VERSION_WS = 1.222;

    //ID plataforma Android = 2
    public final static int ID_PLATAFORMA = 2;

    //URL Local
//    public final static String URL_WEBSERVICE = "http://192.168.69.31/";

    //URL Desarrollo
    public final static String URL_WEBSERVICE = "https://cemexprapi.tmanager.com.mx/";
    public final static String TOPICO_USUARIO = "dev-cemex-usuario-";
    public final static String TOPICO_ACTUALIZACIONES = "dev-cemex-actualizaciones-";
    public final static String TOPICO_VENDEDORES = "dev-cemex-vendedores";

    //URL QA
//    public final static String URL_WEBSERVICE = "https://cemexqaapi.tmanager.com.mx/";
//    public final static String TOPICO_USUARIO = "qa-cemex-usuario-";
//    public final static String TOPICO_ACTUALIZACIONES = "qa-cemex-actualizaciones-";
//    public final static String TOPICO_VENDEDORES = "qa-cemex-vendedores";

    //URL Productivo
//    public final static String URL_WEBSERVICE = "https://cemexapi.tmanager.com.mx/";
//    public final static String TOPICO_USUARIO = "prod-cemex-usuario-";
//    public final static String TOPICO_ACTUALIZACIONES = "prod-cemex-actualizaciones-";
//    public final static String TOPICO_VENDEDORES = "prod-cemex-vendedores";

    /**
     * Sección para la declaración de métodos
     */

    public final static String getToken = "login";
    public final static String cerrarSesion = "CEMEX/api/LogOut";
    public final static String getMenu = "api/Menu/obtenerMenu/";
    public final static String getUsuario = "api/Usuario";
    public final static String getCatalogoAccion = "/api/catalogo/nombre/Accion";

    public final static String getCatalogoEstado = "api/catalogo/nombre/Estado";
    public final static String getCatalogoMunicipio = "api/catalogo/nombre/Municipio";
    public final static String getCatalogoEstatusObra = "api/catalogo/nombre/StatusObra";
    public final static String getCatalogoSubsegmentoProducto = "api/catalogo/subsegmentoproducto";
    public final static String getCatalogoTipoProspecto = "api/catalogo/nombre/TipoProspecto";
    public final static String getCatalogoCampania = "api/catalogo/nombre/Campania";
    public final static String getCatalogoServicio = "api/catalogo/nombre/Servicio";
    public final static String getCatalogoActividadesPGV = "api/catalogo/nombre/ActividadesPGV";
    public final static String getCatalogoSemaforo = "api/catalogo/nombre/Semaforo";
    public final static String getCatalogoMotivoExclusion = "api/catalogo/nombre/MotivoPerdida";
    public final static String getCatalogoCompetidor = "api/catalogo/nombre/Competidor";
    public final static String getCatalogoOportunidadVenta = "api/catalogo/nombre/OportunidadVenta";
    public final static String getCatalogoEstatusPGV = "api/catalogo/nombre/EstatusPGV";
    public final static String getCatalogoTipoObra = "api/catalogo/nombre/TipoObra";
    public final static String getCatalogoCargo = "api/catalogo/nombre/Cargo";
    public final static String getCatalogoCliente = "api/cliente/consulta";
    public final static String getCatalogoObra = "api/obra/consulta";
    public final static String getCatalogoTipoNotificacion = "api/catalogo/nombre/TipoNotificacion";
    public final static String getProspecto = "api/Prospecto";
    public final static String setProspecto = "/api/prospecto/insertamovil";
    public final static String getContacto = "api/contacto";
    public final static String getProductoServicio = "api/Producto";
    public final static String getActividadProspecto = "api/ActividadProspecto";
    public final static String setActividadProspecto = "api/ActividadProspecto";
    public final static String setRuta = "/api/localizacion";
    public final static String setCoordenadas="/api/localizacion/RegistraCoordenadas";

    public final static String putObra = "/api/obra";
    public final static String getObraDetalle = "/api/obra";
    public final static String getNotificaciones = "api/notificaciones/filtro";

    /***************** NUEVOS SERVICIOS MONGO *******************************/
    public final static String getProspectoFiltro = "api/prospecto/filtromovil";
    public final static String getSemaforo = "api/reportes/semaforo";
    public final static String getArchivoProspecto = "api/documento/ObtieneArchivo/";
    public final static String setCargaArchivo = "api/documento/CargaArchivo";
    public final static String getArchivosProspecto = "api/prospecto/obtienearchivosalta";
    public final static String setContacto = "api/contacto";
    public final static String getContactos = "api/contacto/obtienecontactos";
    public final static String setArchivosAlta = "api/prospecto/archivosalta";
    public final static String setActividadAlta = "api/prospecto/actividadesproductos";
    public final static String setAltaActividadAdministrativa = "api/actividad/agrega";
    public final static String setDescartar = "api/prospecto/motivoexclusion";
    public final static String getOportunidadVentaInicial = "api/prospecto/oportunidadventainicial";
    public final static String getOportunidadVentaPaso = "api/prospecto/obtienepaso";
    public final static String getActividadesTodo = "api/actividad/vendedor";
    public final static String putMotivoExclusion = "api/prospecto/motivoexclusion";
    public final static String getProductosUpCross = "api/catalogo/tipoSubSegmentosProducto";
    public final static String getServiciosUpCros = "api/catalogo/nombre/tipoServicios";

    /********************************CÓDIGOS 400**************************************/
    public final static String codigoSolicitudEnviadaPreviamente = "140017";
}
