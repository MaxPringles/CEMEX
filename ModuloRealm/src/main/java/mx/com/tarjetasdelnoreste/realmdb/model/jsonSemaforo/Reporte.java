
package mx.com.tarjetasdelnoreste.realmdb.model.jsonSemaforo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


@SuppressWarnings("unused")
public  class Reporte {

    @SerializedName("descripcion")
    private String mDescripcion;
    @SerializedName("estatusIncluidos")
    private List<Long> mEstatusIncluidos;
    @SerializedName("idCatalogo")
    private Long mIdCatalogo;
    @SerializedName("idPaso")
    private int mIdPaso;
    @SerializedName("index")
    private Long mIndex;
    @SerializedName("total")
    private Long mTotal;

    public String getDescripcion() {
        return mDescripcion;
    }

    public void setDescripcion(String descripcion) {
        mDescripcion = descripcion;
    }

    public List<Long> getEstatusIncluidos() {
        return mEstatusIncluidos;
    }

    public void setEstatusIncluidos(List<Long> estatusIncluidos) {
        mEstatusIncluidos = estatusIncluidos;
    }

    public Long getIdCatalogo() {
        return mIdCatalogo;
    }

    public void setIdCatalogo(Long idCatalogo) {
        mIdCatalogo = idCatalogo;
    }

    public int getIdPaso() {
        return mIdPaso;
    }

    public void setIdPaso(int idPaso) {
        mIdPaso = idPaso;
    }

    public Long getIndex() {
        return mIndex;
    }

    public void setIndex(Long index) {
        mIndex = index;
    }

    public Long getTotal() {
        return mTotal;
    }

    public void setTotal(Long total) {
        mTotal = total;
    }

}
