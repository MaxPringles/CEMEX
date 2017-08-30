package mx.com.tarjetasdelnoreste.realmdb.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by movil4 on 30/12/16.
 */

public class ObraDetalleDB extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    private String mId;
    @SerializedName("fechaInicioObra")
    private Long mFechaInicioObra;
    @SerializedName("duracionMeses")
    private Long mDuracionMeses;
    @SerializedName("mesesRestantes")
    private Long mMesesRestantes;
    @SerializedName("idEstatusObra")
    private Long mIdEstatusObra;
    @SerializedName("idTipoObra")
    private Long mIdTipoObra;

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public Long getFechaInicioObra() {
        return mFechaInicioObra;
    }

    public void setFechaInicioObra(Long mFechaInicioObra) {
        this.mFechaInicioObra = mFechaInicioObra;
    }

    public Long getDuracionMeses() {
        return mDuracionMeses;
    }

    public void setDuracionMeses(Long mDuracionMeses) {
        this.mDuracionMeses = mDuracionMeses;
    }

    public Long getMesesRestantes() {
        return mMesesRestantes;
    }

    public void setMesesRestantes(Long mMesesRestantes) {
        this.mMesesRestantes = mMesesRestantes;
    }

    public Long getIdEstatusObra() {
        return mIdEstatusObra;
    }

    public void setIdEstatusObra(Long mIdEstatusObra) {
        this.mIdEstatusObra = mIdEstatusObra;
    }

    public Long getIdTipoObra() {
        return mIdTipoObra;
    }

    public void setIdTipoObra(Long mIdTipoObra) {
        this.mIdTipoObra = mIdTipoObra;
    }
}
