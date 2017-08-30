package mx.com.tarjetasdelnoreste.realmdb;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import mx.com.tarjetasdelnoreste.realmdb.model.ProspectosDB;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

/**
 * Created by czamora on 9/13/16.
 */
public class ProspectosRealm extends MenuRealm {

    public ProspectosRealm(Realm realm) {
        super(realm);
    }

    /**
     * MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA
     **/
    public static void guardarListaProspectos(List<ProspectosDB> listaProspectos) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaProspectos); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE GUARDA UN REGISTRO DENTRO DE LA TABLA
     **/
    public static void guardarProspecto(ProspectosDB prospectosDB) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(prospectosDB); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE ELIMINAR LA TABLA
     **/
    public static void eliminarTabla() {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.delete(ProspectosDB.class); //Borra los registros de la tabla.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA
     **/
    public static List<ProspectosDB> mostrarListaProspectos() {

        return realm.where(ProspectosDB.class).findAll();
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA
     **/
    public static List<ProspectosDB> mostrarFiltroProspecto(int idPaso) {

        switch (idPaso) {
            case 1:
                return realm.where(ProspectosDB.class)
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_CONTACTAR_NUEVO_PROSPECTO)
                        .equalTo("estaDescartado", false)
                        .or()
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_CONTACTAR_CLIENTE)
                        .equalTo("estaDescartado", false)
                        .findAll();
            case 2:
                return realm.where(ProspectosDB.class)
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_VISITAR_PROSPECTO)
                        .equalTo("estaDescartado", false)
                        .or()
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_RECABAR_INFORMACION)
                        .equalTo("estaDescartado", false)
                        .findAll();
            case 3:
                return realm.where(ProspectosDB.class)
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_CALIFICAR_OPORTUNIDAD)
                        .equalTo("estaDescartado", false)
                        .or()
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_PREPARAR_PROPUESTA_DE_VALOR)
                        .equalTo("estaDescartado", false)
//                        .or()
//                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_DESCARTAR_OPORTUNIDAD)
//                        .equalTo("estaDescartado", false)
                        .findAll();
            case 4:
                return realm.where(ProspectosDB.class)
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_PRESENTAR_PROPUESTA)
                        .equalTo("estaDescartado", false)
                        .findAll();
            case 5:
                return realm.where(ProspectosDB.class)
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_RECIBIR_RESPUESTA)
                        .equalTo("estaDescartado", false)
                        .or()
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_NEGOCIAR_AJUSTAR_PROPUESTA)
                        .equalTo("estaDescartado", false)
                        .or()
                        .equalTo("idActividad", Valores.ID_ACTIVIDAD_CERRAR_VENTA)
                        .equalTo("estaDescartado", false)
                        .notEqualTo("idEstatusProspecto", 10)
                        .notEqualTo("idEstatusProspecto", 11)
                        .findAll();
        }

        return realm.where(ProspectosDB.class).findAll();
    }

    public static List<ProspectosDB> mostrarFiltroProspectoIdEstatus(String []idEstatus) {

        List<ProspectosDB> prospectosDB; //Lista que almacena los prospectos con el idEstatus buscado.
        //Lista que almacena todos los prospectos con los idEstatus obtenidos.
        List<ProspectosDB> prospectosDBList = new ArrayList<>();

        for (int i = 0; i < idEstatus.length; i++) {

            prospectosDB = realm.where(ProspectosDB.class)
                    .equalTo("idEstatusProspecto", Long.parseLong(idEstatus[i]))
                    .equalTo("estaDescartado", false)
                    .isNotEmpty("descripcionActividad")
                    .findAll();

            if (prospectosDB.size() > 0) {
                prospectosDBList.addAll(prospectosDB);
            }
        }

        return prospectosDBList;
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA CON ESE TIPO DE PROSPECTO
     **/
    public static List<ProspectosDB> mostrarListaProspectosTipoDeProspecto(Long tipoProspecto) {

        return realm.where(ProspectosDB.class)
                .equalTo("idTipoProspecto", tipoProspecto)
                .findAll();
    }

    /**
     * MÈTODO QUE DEVUELVE LA ÚLTIMA FECHA DE ALTA DE PROSPECTO
     **/
    public static int ultimafechaAltaProspecto() {

        List<ProspectosDB> listaProspecto = realm.where(ProspectosDB.class).findAll();

        if (listaProspecto.size() == 0) {
            return 0;
        } else {
            return listaProspecto.get(listaProspecto.size() - 1).getFechaAltaProspecto();
        }
    }

    /**
     * MÉTODO QUE CUENTA EL NÚMERO DE PROSPECTOS DE CADA CLASIFICACIÓN
     **/
    public static int contarProspectosPasos(String actividadActual) {

        return (int) realm.where(ProspectosDB.class)
                .equalTo("descripcionActividad", actividadActual)
                .equalTo("estaDescartado", false)
                .notEqualTo("idEstatusProspecto", 10)
                .notEqualTo("idEstatusProspecto", 11)
                .notEqualTo("idActividad", Valores.ID_ACTIVIDAD_DESCARTAR_OPORTUNIDAD)
                .count();
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA CON ESA ACTIVIDAD
     **/
    public static List<ProspectosDB> mostrarListaProspectosActividad(String actividad) {

        return realm.where(ProspectosDB.class)
                .equalTo("descripcionActividad", actividad)
                .findAll();
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA CON ESE NOMBRE
     **/
    public static List<ProspectosDB> mostrarListaProspectosPorNombre(String nombre) {

        return realm.where(ProspectosDB.class)
                .contains("Obra", nombre, Case.INSENSITIVE)
                .findAll();
    }

    /**
     * MÉTODO QUE DEVUELVE TODOS LOS REGISTROS CON EL NÚMERO DE REGISTRO ESPECIFICADO
     **/
    public static List<ProspectosDB> mostrarListaProspectosPorNumeroRegistro(long numeroRegistro) {

        return realm.where(ProspectosDB.class)
                .equalTo("numeroRegistro", numeroRegistro)
                .findAll();
    }

    /**
     * MÉTODO QUE DEVUELVE EL RESULTADO DEL FILTRO
     **/
    public static List<ProspectosDB> mostrarListaProspectosFiltro(Long idTipoProspecto, Long idSubsegmento, Long idActividad) {

        RealmResults<ProspectosDB> prospectosFiltrados = realm.where(ProspectosDB.class).findAll();

        //En caso de que no se haya seleccionado ningún filtro, se devuelven todos los prospectos.
        if (idTipoProspecto == 0 && idSubsegmento == 0 && idActividad == 0) {
            return prospectosFiltrados;
        }

        if (idTipoProspecto != 0) {
            prospectosFiltrados = prospectosFiltrados.where().equalTo("idTipoProspecto", idTipoProspecto).findAll();
        }
        if (idSubsegmento != 0) {
            prospectosFiltrados = prospectosFiltrados.where().equalTo("idSubsegmento", idSubsegmento).findAll();
        }
        if (idActividad != 0) {
            prospectosFiltrados = prospectosFiltrados.where().equalTo("idActividad", idActividad).findAll();
        }

       return prospectosFiltrados;
    }

    public static List<ProspectosDB> mostrarListaProspectosConActividad() {
        return realm.where(ProspectosDB.class)
                .isNotEmpty("actividadesDBRealmList")
                .findAll();
    }

    /**
     * MÉTODO QUE DEVUELVE EL RESULTADO DEL FILTRO
     **/
    public static List<ProspectosDB> mostrarListaProspectosPorIds(List<Long> ids) {

        List<ProspectosDB> listaProspectosIds = new ArrayList<>();
        ProspectosDB prospectosDB;

        for (int i = 0; i < ids.size(); i++) {
            prospectosDB = realm.where(ProspectosDB.class)
                    .equalTo("id", ids.get(i))
                    .findFirst();

            if (prospectosDB != null) {
                listaProspectosIds.add(prospectosDB);
            }
        }

        return listaProspectosIds;
    }

    /** MÉTODO QUE DEVUELVE LOS DATOS DEL PROSPECTO CON EL id DESEADO **/
    public static ProspectosDB mostrarProspectoId(String idProspecto) {

        return realm.where(ProspectosDB.class)
                .equalTo("id", idProspecto)
                .findFirst();
    }
}
