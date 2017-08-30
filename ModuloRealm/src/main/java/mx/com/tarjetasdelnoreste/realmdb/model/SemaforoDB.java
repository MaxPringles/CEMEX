package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by usr_micro13 on 25/01/2017.
 */

public class SemaforoDB extends RealmObject {

    @PrimaryKey
    private int idPaso;
    private long totalPaso;
    private String estatusIncluidos;

    public int getIdPaso() {
        return idPaso;
    }

    public void setIdPaso(int idPaso) {
        this.idPaso = idPaso;
    }

    public long getTotalPaso() {
        return totalPaso;
    }

    public void setTotalPaso(long totalPaso) {
        this.totalPaso = totalPaso;
    }

    public String getEstatusIncluidos() {
        return estatusIncluidos;
    }

    public void setEstatusIncluidos(String estatusIncluidos) {
        this.estatusIncluidos = estatusIncluidos;
    }
}
