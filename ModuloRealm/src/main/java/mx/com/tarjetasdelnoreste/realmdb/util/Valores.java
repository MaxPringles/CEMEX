package mx.com.tarjetasdelnoreste.realmdb.util;

/**
 * Created by czamora on 9/13/16.
 */
public class Valores {

    public final static String idEmpresa = "1";
    public final static String plataforma = "2";
    //Id de Productos de la Base de datos
    public final static String producto = "2210";
    //Id de Servicios de la Base de datos
    public final static String servicio = "2211";
    //Código de respuesta que indica que el token ha expirado y se debe
    //volver a la pantalla de Login.
    public final static int TOKEN_EXPIRADO = 401;

    public final static String SHARED_PREFERENCES_VARIABLES_GLOBALES = "sharedPreferencesVariablesGlobales";
    public final static String SHARED_PREFERENCES_URL_WS = "sharedPreferencesUrlWS";
    public final static String SHARED_PREFERENCES_TOKEN = "sharedPreferencesToken";
    public final static String SHARED_PREFERENCES_SESION_INICIADA = "sharedPreferencesSesionIniciada";
    public final static String SHARED_PREFERENCES_ID_VENDEDOR = "sharedPreferencesIdVendedor";
    public final static String SHARED_PREFERENCES_IMAGEN_VENDEDOR = "sharedPreferencesImagenVendedor";
    public final static String SHARED_PREFERENCES_NOMBRE_VENDEDOR = "sharedPreferencesNombreVendedor";

    public final static String SHARED_PREFERENCES_GET_USUARIO = "sharedPreferencesGetUsuario";

    public final static String SHARED_PREFERENCES_ES_NUEVO_PROSPECTO = "sharedPreferencesEsNuevoProspecto";

    //Guarda la InformacionGeneral en caso de rotación de pantalla.
    public static final String SHAREDPREFERENCES_INFORMACION_GENERAL = "sharedPreferencesInformacionGeneral";
    //Guarda la InformacionContacto en caso de rotación de pantalla.
    public static final String SHAREDPREFERENCES_INFORMACION_CONTACTO = "sharedPreferencesInformacionContacto";
    //Guarda la posición (página) en que se queda el ViewPager de Alta de Prospecto.
    public static final String SHAREDPREFERENCES_INFORMACION_CONTACTO_VIEWPAGER = "sharedPreferencesViewPager";
    //Guarda la información del prospecto al cual se le asignará una cita.
    public static final String SHAREDPREFERENCES_PLAN_SEMANAL_ID_PROSPECTO = "sharedPreferencesPlanIdProspecto";
    //Guarda el id de la actividad actual.
    public static final String SHAREDPREFERENCES_ID_ACTIVIDAD_ACTUAL = "sharedPreferencesActividadActual";
    //Guarda la bandera para saber si volver a cargar el calendario o no (al volver de la pantalla de TimePickerFragment).
    //public static final String SHAREDPREFERENCES_PLAN_SEMANAL_CALENDARIO_NORMAL = "sharedPreferencesPlanCalendarioNormal";
    //Guarda los Subsegmentos seleccionados en la alta de prospectos
    public static final String SHAREDPREFERENCES_PLAN_SEMANAL_DOUBLE_BACK = "sharedPreferencesDoubleChek";

    public static final String SHAREDPREFERENCES_OFERTA_INTEGRAL = "sharedPreferencesOfertaIntegral";

    public static final String SHAREDPREFERENCES_OFERTA_INTEGRAL_TEMPORAL = "sharedPreferencesOfertaIntegralTemporal";
    //Guarda los Subsegmentos seleccionados en la alta de prospectos
    public static final String SHAREDPREFERENCES_SUBSEGMENTOS_SELECCIONADOS = "sharedPreferencesSubsegmentosSeleccionados";
    //Guarda los Subsegmentos seleccionados en la alta de prospectos
    public static final String SHAREDPREFERENCES_SUBSEGMENTOS_SELECCIONADOS_TEMPORAL = "sharedPreferencesSubsegmentosSeleccionadosTemporal";
    //Guarda los Productos seleccionados en la alta de prospectos
    public static final String SHAREDPREFERENCES_PRODUCTOS_SELECCIONADOS = "sharedPreferencesProductosSeleccionados";
    //Guarda los Servicios seleccionados en la alta de prospectos
    public static final String SHAREDPREFERENCES_SERVICIOS_SELECCIONADOS = "sharedPreferencesServiciosSeleccionados";
    //Verifica si ya se ha activado la alarma para enviar coordenadas.
    public static final String SHAREDPREFERENCES_ALARMA_COORDENADAS = "sharedPreferencesAlarmaCoordenadas";
    //Verifica si ya se han descargado los catálogos.
    public static final String SHAREDPREFERENCES_DESCARGA_CATALOGOS = "sharedPreferencesDescaragaDeCatalogos";
    //Verifica si es oferta integral
    public static final String SHAREDPREFERENCES_ES_OFERTA_INTEGRAL = "sharedPreferencesEsOfertaIntegral";
    //Verifica si seleccionaron todos los productos
    public static final String SHAREDPREFERENCES_TODOS_LOS_PRODUCTOS = "sharedPreferencesTodosLosProductos";
    //Verifica si seleccionaron todos los servicios
    public static final String SHAREDPREFERENCES_TODOS_LOS_SERVICIOS = "sharedPreferencesTodosLosServicios";
    /** VALORES RECUPERACIÓN DE PRODUCTOS Y SERVICIOS EN CITAS/VISITAS **/
    public static final String SHAREDPREFERENCES_ID_PROSPECTO = "idProspecto";
    public static final String SHAREDPREFERENCES_ID_TIPO_PROSPECTO = "idTipoProspecto";
    public static final String SHAREDPREFERENCES_ID_OBRA = "idObra";
    public static final String SHAREDPREFERENCES_CITAS_PRODUCTOS = "subsegmentosProductos";
    public static final String SHAREDPREFERENCES_CITAS_PRODUCTOS_TEMPORAL = "subsegmentosProductosTemporal";
    public static final String SHAREDPREFERENCES_CITAS_SERVICIOS = "serviciosOfertaIntegral";
    public static final String SHAREDPREFERENCES_CITAS_SERVICIOS_TEMPORAL = "serviciosOfertaIntegralTemporal";
    public static final String SHAREDPREFERENCES_CITAS_OFERTA_INTEGRAL = "ofertaIntegral";
    public static final String SHAREDPREFERENCES_FOLIO = "sharedPreferencesFolio";
    public static final String SHAREDPREFERENCES_HOLDING= "sharedPreferencesHolding";

    public static final String SHAREDPREFERENCES_MOTIVO_EXCLUSION = "aplicaMotivoExclusion";
    public static final String SHAREDPREFEREBCES_TIENE_ACTIVIDAD_ANTERIOR = "tieneActividadAnterior";
    public static final String SHAREDPREFERENCES_CHECKBOX_OFERTA_INTEGRAL = "checkBoxOfertaIntegral";
    public static final String SHAREDPREFERENCES_CANCELO_PRODUCTO_SERVICIO = "canceloProductoServicio";
    /** VALOR PARA RECUPERACIÓN DEL OBJETO DE ALTA DE ACTIVIDADES **/
    public static final String SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES = "jsonAltaActividades";
    public static final String SHAREDPREFERENCES_PLAN_SEMANAL_ALTA_ACTIVIDADES_ADMINISTRATIVAS = "jsonAltaActividadesAdministrativas";

    /** VALOR PARA SABER EN QUE ACTIVIDAD SE ENCUENTRA **/
    public static final String SHAREDPREFERENCES_MOSTRAR_TODOS_LOS_DETALLES = "mostrarTodosLosDetalles";

    /********** VALORES PARA MOSTRAR FRAGMENTOS ***************************/
    public static final String FRAGMENT_GENERAL_MOSTRAR = "prospectosFragment";
    public static final String FRAGMENT_PROSPECTOS_PRINCIPAL = "prospectosPrincipal";
    public static final String FRAGMENT_PROSPECTOS = "altaProspectos";
    public static final String FRAGMENT_PROSPECTOS_CONTACTOS = "verContactos";
    public static final String FRAGMENT_PROSPECTOS_ARCHIVOS = "archivosProspectos";
    public static final String FRAGMENT_MAPA = "verMapa";
    public static final String FRAGMENT_PLAN_CALENDAR = "calendarFragment";
    public static final String FRAGMENT_PLAN_VENTAS = "planVentas";
    public static final String FRAGMENT_PLAN_ADMINISTRATIVAS_VENTAS = "planAdministrativasVentas";
    public static final String FRAGMENT_PLAN_ADMINISTRATIVAS = "planAdministrativas";
    public static final String FRAGMENT_PLAN_TIMEPICKER_INICIO = "timePickerFragment";
    public static final String FRAGMENT_PLAN_TIMEPICKER_FIN = "timePickerFragmentFin";
    public static final String FRAGMENT_PROSPECTOS_OFERTA_INTEGRAL = "ofertaIntegral";
    public static final String FRAGMENT_CONTACTAR_CLIENTE_PROSPECTO = "contactarClienteProspectoFragment";
    public static final String FRAGMENT_VISITAR_PROSPECTO = "visitarProspectoFragment";
    public static final String FRAGMENT_RECABAR_INFORMACION = "recabarInformacionFragment";
    public static final String FRAGMENT_CALIFICAR_OPORTUNIDAD = "calificarOportunidadFragment";
    public static final String FRAGMENT_PREPARAR_PROPUESTA_DE_VALOR = "prepararPropuestaDeValorFragment";
    public static final String FRAGMENT_PRESENTAR_PROPUESTA = "presentarPropuestaFragment";
    public static final String FRAGMENT_RECIBIR_RESPUESTA = "recibirRespuestaFragment";
    public static final String FRAGMENT_CERRAR_VENTA = "cerrarVentaFragment";

    public static final String FRAGMENT_PRODUCTOS_SERVICIOS = "citasProductosServicios";
    public static final String FRAGMENT_CITAS_VISITAS = "citasVisitasFragment";


    /********* VALORES PARA EL MAPA **************/
    public static final String MAPA_TITLE = "mapaTitle";
    public static final String MAPA_LATLNG = "mapaLatlng";

    /********* VALORES PARA INFORMACIÓN DE OBRA **************/
    public static final String OBRA_ID_PROSPECTO = "idProspecto";

    /******** VALORES PARA PASAR EL idProspecto A LA PANTALLA DE CONTACTOS ********/
    public static final String CONTACTOS_ID_PROSPECTO = "idProspectoContactos";
    public static final String CONTACTOS_NOMBRE_PROSPECTO = "nombreProspectoContactos";

    /** VALORES PARA ESTATUS DE ENVÍO DE COORDENADA **/
    public static final int ESTATUS_NO_ENVIADO = 1;
    public static final int ESTATUS_ENVIANDO = 2;
    public static final int ESTATUS_ENVIADO = 3;
    public static final int ESTATUS_PROBLEMA_ENVIO = 4;

    /** VALORES ALARMA **/
    public static final int VALOR_ALARMA_INICIO_COORDENADAS = 100;
    public static final int VALOR_ALARMA_FIN_COORDENADAS = 101;
    public static final String ALARMA_ENVIO_DE_COORDENADAS = "alarmaEnvioDeCoordenadas";
    public static final int VALOR_ALARMA_HORA_INICIO = 8; //Hora de inicio 8:00.
    public static final int VALOR_ALARMA_HORA_FIN = 19; //Hora de fin 18:00.

    public static final String VALOR_ALARMA_ACTIVIDADES_TITULO = "alarmaActividadesTitulo";
    public static final String VALOR_ALARMA_ACTIVIDADES_MENSAJE = "alarmaActividadesMensaje";

    /** ID DE ENVIO OFFLINE **/
    public static final int ID_ENVIO_ALTA_PROSPECTOS = 1;
    public static final int ID_ENVIO_ALTA_CONTACTOS = 2;
    public static final int ID_ENVIO_ALTA_ARCHIVOS = 3;
    public static final int ID_ENVIO_ALTA_OBRA = 5;

    public static final int ID_ENVIO_ALTA_ACTIVIDAD = 6;
    public static final int ID_ENVIO_ALTA_ACTIVIDAD_SIN_ANTERIOR = 7;
    public static final int ID_ENVIO_ALTA_ACTIVIDAD_ADMINISTRATIVA = 8;
    public static final int ID_ENVIO_DESCARTAR=9;

    /** ID DE ACTIVIDADES **/

    public static final int ID_ACTIVIDAD_REAGENDADA = 2;

    public static final int ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO = 2;
    public static final int ID_ACTIVIDAD_CONTACTAR_CLIENTE = 3;
    public static final int ID_ACTIVIDAD_VISITAR_PROSPECTO = 4;
    public static final int ID_ACTIVIDAD_RECABAR_INFORMACION = 5;
    public static final int ID_ACTIVIDAD_CALIFICAR_OPORTUNIDAD = 6;
    public static final int ID_ACTIVIDAD_PREPARAR_PROPUESTA_DE_VALOR = 7;
    public static final int ID_ACTIVIDAD_DESCARTAR_OPORTUNIDAD = 8;
    public static final int ID_ACTIVIDAD_PRESENTAR_PROPUESTA = 9;
    public static final int ID_ACTIVIDAD_RECIBIR_RESPUESTA = 11; /******************/
    public static final int ID_ACTIVIDAD_NEGOCIAR_AJUSTAR_PROPUESTA = 12;
    public static final int ID_ACTIVIDAD_CERRAR_VENTA = 13;

    /** TIPO DE PROSPECTO **/
    public static final int ID_TIPO_PROSPECTO_NUEVO_PROSPECTO = 1;
    public static final int ID_TIPO_PROSPECTO_CROSS_SELLING = 2;
    public static final int ID_TIPO_PROSPECTO_UP_SELLING = 3;

    /** ID ESTATUS PROSPECTO **/
    public static final int ID_ESTATUS_PROSPECTO_SIN_CONTACTO = 1;
    public static final int ID_ESTATUS_PROSPECTO_AGENDADO = 3;
    public static final int ID_ESTATUS_PROSPECTO_VISITA_1_10 = 4;
    public static final int ID_ESTATUS_PROSPECTO_CALIFICA = 5;
    public static final int ID_ESTATUS_PROSPECTO_NO_CALIFICA = 6;
    public static final int ID_ESTATUS_PROSPECTO_PREPARAR_PROPUESTA = 7;
    public static final int ID_ESTATUS_PROSPECTO_PROPUESTA_ENTREGADA = 8;
    public static final int ID_ESTATUS_PROSPECTO_EN_NEGOCIACION = 9;
    public static final int ID_ESTATUS_PROSPECTO_GANADA = 10;
    public static final int ID_ESTATUS_PROSPECTO_PERDIDA = 11;

    /** BUNDLE **/

    public static final String BUNDLE_ID_SUBSEGMENTO = "idSubsegmento";
    public static final String BUNDLE_ID_ACTIVIDAD = "idActividad";

    /** VALORES PARA NOTIFICACIONES **/
    public static final String SHARED_PREFERENCES_NOTIFICACIONES = "sharedPreferencesNotificaciones";
    public static final int NOTIFICACIONES_EDITAR_ACTIVIDAD = 8;
    public static final int NOTIFICACIONES_EDITAR_CONTACTO = 5;
    public static final int NOTIFICACIONES_EDITAR_PROSPECTO = 3;
    public static final int NOTIFICACIONES_ELIMINAR_CONTACTO = 6;
    public static final int NOTIFICACIONES_ELIMINAR_ACTIVIDAD = 9;
    public static final int NOTIFICACIONES_ELIMINAR_ARCHIVO = 11;
    public static final int NOTIFICACIONES_ELIMINAR_PROSPECTO = 2;
    public static final int NOTIFICACIONES_NUEVO_ACTIVIDAD = 7;
    public static final int NOTIFICACIONES_NUEVO_ARCHIVO = 10;
    public static final int NOTIFICACIONES_NUEVO_CONTACTO = 4;
    public static final int NOTIFICACIONES_NUEVO_PROSPECTO = 1;
    public static final int NOTIFICACIONES_EDITAR_CATALOGO = 14;

    /** VALORES NOTIFICACION DE CATALOGO **/
    public static final String NOTIFICACIONES_CATALOGO_ESTADO = "Estado";
    public static final String NOTIFICACIONES_CATALOGO_MUNICIPIO = "Municipio";
    public static final String NOTIFICACIONES_CATALOGO_ESTATUS_OBRA = "StatusObra";
    public static final String NOTIFICACIONES_CATALOGO_SUBSEGMENTO_PRODUCTO = "subsegmentoproducto";
    public static final String NOTIFICACIONES_CATALOGO_TIPO_PROSPECTO = "TipoProspecto";
    public static final String NOTIFICACIONES_CATALOGO_CAMPANIA = "Campania";
    public static final String NOTIFICACIONES_CATALOGO_SERVICIOS = "Servicio";
    public static final String NOTIFICACIONES_CATALOGO_ACTIVIDADES_PGV = "ActividadesPGV";
    public static final String NOTIFICACIONES_CATALOGO_SEMAFORO = "Semaforo";
    public static final String NOTIFICACIONES_CATALOGO_MOTIVOS_EXCLUSION = "MotivoPerdida";
    public static final String NOTIFICACIONES_CATALOGO_COMPETIDOR = "Competidor";
    public static final String NOTIFICACIONES_CATALOGO_OPORTUNIDAD_VENTA = "OportunidadVenta";
    public static final String NOTIFICACIONES_CATALOGO_ESTATUS_PGV = "EstatusPGV";
    public static final String NOTIFICACIONES_CATALOGO_TIPO_OBRA = "TipoObra";
    public static final String NOTIFICACIONES_CATALOGO_CARGO = "Cargo";
    public static final String NOTIFICACIONES_CATALOGO_TIPO_NOTIFICACION = "TipoNotificacion";

    /** VALORES PARA COLOCAR NOMBRE AL ARCHIVO DE EXPEDIENTE Y COLOCAR COMENTARIOS**/
    public static final String SHARED_PREFERENCES_NOMBRE_ARCHIVO = "sharedPreferencesNombreArchivo";
    public static final String SHARED_PREFERENCES_COLOCAR_COMENTARIOS = "sharedPreferencesColocarComentarios";

    /** VALORES idPadre DEL TIPO DE ACTIVIDAD EN PLAN SEMANAL **/
    public static final int ID_PADRE_ACTIVIDADES_VENTA = 1;
    public static final int ID_PADRE_ACTIVIDADES_ADMINISTRATIVAS_VENTA = 2;
    public static final int ID_PADRE_ACTIVIDADES_ADMINISTRATIVAS = 3;
    public static final int ID_TIPO_ASIGNACION_ADMINISTRATIVAS = 2;

    public static final String BUNDLE_CALENDAR = "bundleCalendar";
    public static final String BUNDLE_ACTIVIDADES_ADMINISTRATIVAS = "bundleActividadesAdministrativas";
    public static final String BUNDLE_IR_A_CALENDARIO = "bundleIrACalendario";

    //NOTA: Este es el único idDialog que está en el archivo de Valores, esto debido a que
    //es la única parte del código donde se requiere hacer esta diferenciación (debido a
    //que se tiene que hacer una funcionalidad distinta con respecto a la operación de
    //agregar archivos al expediente del prospecto).
    public static final String ID_COLOCAR_COMENTARIO = "idColocarComentario";

}
