
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonAltaActividadesNuevas {

    @SerializedName("actividad")
    private Actividad mActividad;
    @SerializedName("idPaso")
    private int mIdPaso;
    @SerializedName("idStatus")
    private long mIdStatusProspecto;
    @SerializedName("servicios")
    private List<Servicio> mServicios;
    @SerializedName("subsegmentoProductos")
    private List<SubsegmentoProducto> mSubsegmentoProductos;
    @SerializedName("esOfertaIntegral")
    private boolean esOfertaIntegral;

    public Actividad getActividad() {
        return mActividad;
    }

    public void setActividad(Actividad actividad) {
        mActividad = actividad;
    }

    public int getIdPaso() {
        return mIdPaso;
    }

    public void setIdPaso(int idPaso) {
        mIdPaso = idPaso;
    }

    public long getIdStatusProspecto() {
        return mIdStatusProspecto;
    }

    public void setIdStatusProspecto(long mIdStatusProspecto) {
        this.mIdStatusProspecto = mIdStatusProspecto;
    }

    public List<Servicio> getServicios() {
        return mServicios;
    }

    public void setServicios(List<Servicio> mServicios) {
        this.mServicios = mServicios;
    }

    public List<SubsegmentoProducto> getSubsegmentoProductos() {
        return mSubsegmentoProductos;
    }

    public void setSubsegmentoProductos(List<SubsegmentoProducto> mSubsegmentoProductos) {
        this.mSubsegmentoProductos = mSubsegmentoProductos;
    }

    public boolean isEsOfertaIntegral() {
        return esOfertaIntegral;
    }

    public void setEsOfertaIntegral(boolean esOfertaIntegral) {
        this.esOfertaIntegral = esOfertaIntegral;
    }
}
