
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class JsonAltaProspecto {

    @SerializedName("cliente")
    private Cliente mCliente;
    @SerializedName("contactos")
    private List<Contacto> mContactos;
    @SerializedName("estatusObra")
    private EstatusObra mEstatusObra;
    @SerializedName("fotografia")
    private String mFotografia;
    @SerializedName("idVendedorAsignado")
    private String mIdVendedorAsignado;
    @SerializedName("oportunidadVentaInicial")
    private OportunidadVentaInicial mOportunidadVentaInicial;
    @SerializedName("subSegmento")
    private SubSegmento mSubSegmento;
    @SerializedName("tipoProspecto")
    private TipoProspecto mTipoProspecto;
    @SerializedName("idAltaOffline")
    private String idAltaOffline;

    public Cliente getCliente() {
        return mCliente;
    }

    public void setCliente(Cliente cliente) {
        mCliente = cliente;
    }

    public List<Contacto> getContactos() {
        return mContactos;
    }

    public void setContactos(List<Contacto> contactos) {
        mContactos = contactos;
    }

    public EstatusObra getEstatusObra() {
        return mEstatusObra;
    }

    public void setEstatusObra(EstatusObra estatusObra) {
        mEstatusObra = estatusObra;
    }

    public String getFotografia() {
        return mFotografia;
    }

    public void setFotografia(String fotografia) {
        mFotografia = fotografia;
    }

    public String getIdVendedorAsignado() {
        return mIdVendedorAsignado;
    }

    public void setIdVendedorAsignado(String idVendedorAsignado) {
        mIdVendedorAsignado = idVendedorAsignado;
    }

    public OportunidadVentaInicial getOportunidadVentaInicial() {
        return mOportunidadVentaInicial;
    }

    public void setOportunidadVentaInicial(OportunidadVentaInicial oportunidadVentaInicial) {
        mOportunidadVentaInicial = oportunidadVentaInicial;
    }

    public SubSegmento getSubSegmento() {
        return mSubSegmento;
    }

    public void setSubSegmento(SubSegmento subSegmento) {
        mSubSegmento = subSegmento;
    }

    public TipoProspecto getTipoProspecto() {
        return mTipoProspecto;
    }

    public void setTipoProspecto(TipoProspecto tipoProspecto) {
        mTipoProspecto = tipoProspecto;
    }

    public String getIdAltaOffline() {
        return idAltaOffline;
    }

    public void setIdAltaOffline(String idAltaOffline) {
        this.idAltaOffline = idAltaOffline;
    }
}
