
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class OfertaIntegral {

    @SerializedName("esOfertaIntegral")
    private Boolean mEsOfertaIntegral;
    @SerializedName("servicios")
    private List<Servicio> mServicios;
    @SerializedName("subsegmentosProductos")
    private List<SubsegmentoProducto> mSubsegmentosProductos;

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

    public List<SubsegmentoProducto> getSubsegmentosProductos() {
        return mSubsegmentosProductos;
    }

    public void setSubsegmentosProductos(List<SubsegmentoProducto> subsegmentosProductos) {
        mSubsegmentosProductos = subsegmentosProductos;
    }

}
