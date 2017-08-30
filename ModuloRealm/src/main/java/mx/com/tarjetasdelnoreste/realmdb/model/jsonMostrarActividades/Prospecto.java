
package mx.com.tarjetasdelnoreste.realmdb.model.jsonMostrarActividades;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

@SuppressWarnings("unused")
public class Prospecto extends RealmObject {

    @SerializedName("estatusProspecto")
    private EstatusProspecto mEstatusProspecto;
    @SerializedName("tipoProspecto")
    private TipoProspecto mTipoProspecto;
    @SerializedName("idCliente")
    private String mIdCliente;
    @SerializedName("idObra")
    private String mIdObra;
    @SerializedName("nombre")
    private String mNombre;
    @SerializedName("obra")
    private String mObra;
    @SerializedName("estaDescartado")
    private boolean mEstaDescartado;

    public EstatusProspecto getEstatusProspecto() {
        return mEstatusProspecto;
    }

    public void setEstatusProspecto(EstatusProspecto estatusProspecto) {
        mEstatusProspecto = estatusProspecto;
    }

    public TipoProspecto getTipoProspecto() {
        return mTipoProspecto;
    }

    public void setTipoProspecto(TipoProspecto mTipoProspecto) {
        this.mTipoProspecto = mTipoProspecto;
    }

    public String getIdCliente() {
        return mIdCliente;
    }

    public void setIdCliente(String idCliente) {
        mIdCliente = idCliente;
    }

    public String getIdObra() {
        return mIdObra;
    }

    public void setIdObra(String idObra) {
        mIdObra = idObra;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public String getObra() {
        return mObra;
    }

    public void setObra(String obra) {
        mObra = obra;
    }

    public boolean getEstaDescartado() {
        return mEstaDescartado;
    }

    public void setEstaDescartado(boolean mEstaDescartado) {
        this.mEstaDescartado = mEstaDescartado;
    }
}
