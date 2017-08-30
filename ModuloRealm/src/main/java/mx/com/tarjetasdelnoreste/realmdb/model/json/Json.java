
package mx.com.tarjetasdelnoreste.realmdb.model.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Json {

    @SerializedName("actividades")
    private List<Actividade> mActividades;
    @SerializedName("cliente")
    private Cliente mCliente;
    @SerializedName("estaDescartado")
    private Boolean mEstaDescartado;
    @SerializedName("estatusObra")
    private EstatusObra mEstatusObra;
    @SerializedName("estatusProspecto")
    private EstatusProspecto mEstatusProspecto;
    @SerializedName("fotografia")
    private String mFotografia;
    @SerializedName("id")
    private String mId;
    @SerializedName("idVendedorAsignado")
    private String mIdVendedorAsignado;
    @SerializedName("motivoExclusion")
    private MotivoExclusion mMotivoExclusion;
    @SerializedName("numeroRegistro")
    private Long mNumeroRegistro;
    @SerializedName("obra")
    private Obra mObra;
    @SerializedName("subSegmento")
    private SubSegmento mSubSegmento;
    @SerializedName("tipoProspecto")
    private TipoProspecto mTipoProspecto;
    @SerializedName("fechaAltaProspecto")
    private int mFechaAltaProspecto;

    public List<Actividade> getActividades() {
        return mActividades;
    }

    public void setActividades(List<Actividade> actividades) {
        mActividades = actividades;
    }

    public Cliente getCliente() {
        return mCliente;
    }

    public void setCliente(Cliente cliente) {
        mCliente = cliente;
    }

    public Boolean isEstaDescartado() {
        return mEstaDescartado;
    }

    public void setEstaDescartado(Boolean estaDescartado) {
        mEstaDescartado = estaDescartado;
    }

    public EstatusObra getEstatusObra() {
        return mEstatusObra;
    }

    public void setEstatusObra(EstatusObra estatusObra) {
        mEstatusObra = estatusObra;
    }

    public EstatusProspecto getEstatusProspecto() {
        return mEstatusProspecto;
    }

    public void setEstatusProspecto(EstatusProspecto estatusProspecto) {
        mEstatusProspecto = estatusProspecto;
    }

    public String getFotografia() {
        return mFotografia;
    }

    public void setFotografia(String fotografia) {
        mFotografia = fotografia;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getIdVendedorAsignado() {
        return mIdVendedorAsignado;
    }

    public void setIdVendedorAsignado(String idVendedorAsignado) {
        mIdVendedorAsignado = idVendedorAsignado;
    }

    public MotivoExclusion getMotivoExclusion() {
        return mMotivoExclusion;
    }

    public void setMotivoExclusion(MotivoExclusion motivoExclusion) {
        mMotivoExclusion = motivoExclusion;
    }

    public Long getNumeroRegistro() {
        return mNumeroRegistro;
    }

    public void setNumeroRegistro(Long numeroRegistro) {
        mNumeroRegistro = numeroRegistro;
    }

    public Obra getObra() {
        return mObra;
    }

    public void setObra(Obra obra) {
        mObra = obra;
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

    public int getFechaAltaProspecto() {
        return mFechaAltaProspecto;
    }

    public void setFechaAltaProspecto(int mFechaAltaProspecto) {
        this.mFechaAltaProspecto = mFechaAltaProspecto;
    }
}
