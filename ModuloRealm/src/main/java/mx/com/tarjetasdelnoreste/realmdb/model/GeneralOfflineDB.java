package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by usr_micro13 on 18/01/2017.
 */

public class GeneralOfflineDB extends RealmObject {

    @PrimaryKey
    private Long fechaOffline;
    private int idOperacion;
    private String idProspecto;
    private String jsonInformacion;
    private int statusEnvio;
    private String nombreArchivos; //Se usa únicamente para el alta de archivos Offline.

    public GeneralOfflineDB() {

    }

    public GeneralOfflineDB(Long fechaOffline, int idOperacion, String idProspecto, String jsonInformacion, int statusEnvio) {
        this.fechaOffline = fechaOffline;
        this.idOperacion = idOperacion;
        this.idProspecto = idProspecto;
        this.jsonInformacion = jsonInformacion;
        this.statusEnvio = statusEnvio;
    }

    //Constructor que se usa únicamente en el alta de archivos Offline.
    public GeneralOfflineDB(Long fechaOffline, int idOperacion, String idProspecto, String jsonInformacion, int statusEnvio, String nombreArchivos) {
        this.fechaOffline = fechaOffline;
        this.idOperacion = idOperacion;
        this.idProspecto = idProspecto;
        this.jsonInformacion = jsonInformacion;
        this.statusEnvio = statusEnvio;
        this.nombreArchivos = nombreArchivos;
    }

    public Long getFechaOffline() {
        return fechaOffline;
    }

    public void setFechaOffline(Long fechaOffline) {
        this.fechaOffline = fechaOffline;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    public String getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        this.idProspecto = idProspecto;
    }

    public String getJsonInformacion() {
        return jsonInformacion;
    }

    public void setJsonInformacion(String jsonInformacion) {
        this.jsonInformacion = jsonInformacion;
    }

    public int getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(int statusEnvio) {
        this.statusEnvio = statusEnvio;
    }

    public String getNombreArchivos() {
        return nombreArchivos;
    }

    public void setNombreArchivos(String nombreArchivos) {
        this.nombreArchivos = nombreArchivos;
    }
}
