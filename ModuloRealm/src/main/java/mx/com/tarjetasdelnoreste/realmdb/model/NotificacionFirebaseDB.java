package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import mx.com.tarjetasdelnoreste.realmdb.MenuRealm;

/**
 * Created by usr_micro13 on 07/02/2017.
 */

public class NotificacionFirebaseDB extends RealmObject {

    @PrimaryKey
    private String idNotificacion;
    private int idTipoNotificacion;
    private int idPrioridad;
    private String idReferencia;
    private String title;
    private String message;
    private int statusEnvio;

    public NotificacionFirebaseDB() {

    }

    public NotificacionFirebaseDB(String idNotificacion, int idTipoNotificacion,
                                  int idPrioridad, String idReferencia, String title,
                                  String message, int statusEnvio) {
        this.idNotificacion = idNotificacion;
        this.idTipoNotificacion = idTipoNotificacion;
        this.idPrioridad = idPrioridad;
        this.idReferencia = idReferencia;
        this.title = title;
        this.message = message;
        this.statusEnvio = statusEnvio;
    }

    public String getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(String idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public int getIdTipoNotificacion() {
        return idTipoNotificacion;
    }

    public void setIdTipoNotificacion(int idTipoNotificacion) {
        this.idTipoNotificacion = idTipoNotificacion;
    }

    public int getIdPrioridad() {
        return idPrioridad;
    }

    public void setIdPrioridad(int idPrioridad) {
        this.idPrioridad = idPrioridad;
    }

    public String getIdReferencia() {
        return idReferencia;
    }

    public void setIdReferencia(String idReferencia) {
        this.idReferencia = idReferencia;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusEnvio() {
        return statusEnvio;
    }

    public void setStatusEnvio(int statusEnvio) {
        this.statusEnvio = statusEnvio;
    }
}
