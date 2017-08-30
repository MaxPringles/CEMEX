package mx.com.tarjetasdelnoreste.realmdb.model;

import android.net.Uri;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by usr_micro13 on 14/12/2016.
 */

public class AlarmasActividadesDB extends RealmObject {

    @PrimaryKey
    private String idProspectoAlarma;
    private String uriAlarma;

    public String getIdProspectoAlarma() {
        return idProspectoAlarma;
    }

    public void setIdProspectoAlarma(String idProspectoAlarma) {
        this.idProspectoAlarma = idProspectoAlarma;
    }

    public String getUriAlarma() {
        return uriAlarma;
    }

    public void setUriAlarma(String uriAlarma) {
        this.uriAlarma = uriAlarma;
    }
}
