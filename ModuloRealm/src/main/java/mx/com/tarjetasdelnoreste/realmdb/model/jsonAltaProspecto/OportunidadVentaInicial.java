
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class OportunidadVentaInicial {

    @SerializedName("esOfertaIntegral")
    private Boolean mEsOfertaIntegral;
    @SerializedName("servicios")
    private List<Servicio> mServicios;
    @SerializedName("subsegmentosProductos")
    private List<SubsegmentosProducto> mSubsegmentosProductos;

    public Boolean getEsOfertaIntegral() {
        return mEsOfertaIntegral;
    }

    public void setEsOfertaIntegral(Boolean esOfertaIntegral) {
        mEsOfertaIntegral = esOfertaIntegral;
    }

    public List<Servicio> getServicios() {
        return mServicios;
    }

    public void setServicios(List<Servicio> servicios) {
        mServicios = servicios;
    }

    public List<SubsegmentosProducto> getSubsegmentosProductos() {
        return mSubsegmentosProductos;
    }

    public void setSubsegmentosProductos(List<SubsegmentosProducto> subsegmentosProductos) {
        mSubsegmentosProductos = subsegmentosProductos;
    }

}
