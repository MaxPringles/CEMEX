
package mx.com.tarjetasdelnoreste.realmdb.model.jsonSemaforo;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JsonSemaforo {

    @SerializedName("reporte")
    private List<Reporte> mReporte;
    @SerializedName("totalRegistros")
    private Long mTotalRegistros;

    public List<Reporte> getReporte() {
        return mReporte;
    }

    public void setReporte(List<Reporte> reporte) {
        mReporte = reporte;
    }

    public Long getTotalRegistros() {
        return mTotalRegistros;
    }

    public void setTotalRegistros(Long totalRegistros) {
        mTotalRegistros = totalRegistros;
    }

}
