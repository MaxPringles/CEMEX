
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades;

import com.google.gson.annotations.SerializedName;

public class Producto {

    @SerializedName("comentariosMotivoPerdida")
    private String mComentariosMotivoPerdida;
    @SerializedName("competidor")
    private Competidor mCompetidor;
    @SerializedName("duracionMeses")
    private Long mDuracionMeses;
    @SerializedName("estatusObra")
    private EstatusObra mEstatusObra;
    @SerializedName("fechaInicioObra")
    private String mFechaInicioObra;
    @SerializedName("id")
    private String mId;
    @SerializedName("mesesRestantes")
    private Long mMesesRestantes;
    @SerializedName("motivoExclusion")
    private MotivoExclusion mMotivoExclusion;
    @SerializedName("nombre")
    private String mNombre;
    @SerializedName("numeroObra")
    private String mNumeroObra;
    @SerializedName("obra")
    private Obra mObra;
    @SerializedName("oportunidadVenta")
    private OportunidadVenta mOportunidadVenta;
    @SerializedName("seleccionado")
    private Boolean mSeleccionado;
    @SerializedName("semaforo")
    private Semaforo mSemaforo;
    @SerializedName("volumenProximosMeses")
    private Double mVolumenProximosMeses;
    @SerializedName("volumenRestante")
    private Double mVolumenRestante;
    @SerializedName("volumenTotal")
    private Double mVolumenTotal;

    public String getComentariosMotivoPerdida() {
        return mComentariosMotivoPerdida;
    }

    public void setComentariosMotivoPerdida(String comentariosMotivoPerdida) {
        mComentariosMotivoPerdida = comentariosMotivoPerdida;
    }

    public Competidor getCompetidor() {
        return mCompetidor;
    }

    public void setCompetidor(Competidor competidor) {
        mCompetidor = competidor;
    }

    public Long getDuracionMeses() {
        return mDuracionMeses;
    }

    public void setDuracionMeses(Long duracionMeses) {
        mDuracionMeses = duracionMeses;
    }

    public EstatusObra getEstatusObra() {
        return mEstatusObra;
    }

    public void setEstatusObra(EstatusObra estatusObra) {
        mEstatusObra = estatusObra;
    }

    public String getFechaInicioObra() {
        return mFechaInicioObra;
    }

    public void setFechaInicioObra(String fechaInicioObra) {
        mFechaInicioObra = fechaInicioObra;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Long getMesesRestantes() {
        return mMesesRestantes;
    }

    public void setMesesRestantes(Long mesesRestantes) {
        mMesesRestantes = mesesRestantes;
    }

    public MotivoExclusion getMotivoExclusion() {
        return mMotivoExclusion;
    }

    public void setMotivoExclusion(MotivoExclusion motivoExclusion) {
        mMotivoExclusion = motivoExclusion;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public String getNumeroObra() {
        return mNumeroObra;
    }

    public void setNumeroObra(String numeroObra) {
        mNumeroObra = numeroObra;
    }

    public Obra getObra() {
        return mObra;
    }

    public void setObra(Obra obra) {
        mObra = obra;
    }

    public OportunidadVenta getOportunidadVenta() {
        return mOportunidadVenta;
    }

    public void setOportunidadVenta(OportunidadVenta oportunidadVenta) {
        mOportunidadVenta = oportunidadVenta;
    }

    public Boolean getSeleccionado() {
        return mSeleccionado;
    }

    public void setSeleccionado(Boolean seleccionado) {
        mSeleccionado = seleccionado;
    }

    public Semaforo getSemaforo() {
        return mSemaforo;
    }

    public void setSemaforo(Semaforo semaforo) {
        mSemaforo = semaforo;
    }

    public Double getVolumenProximosMeses() {
        return mVolumenProximosMeses;
    }

    public void setVolumenProximosMeses(Double volumenProximosMeses) {
        mVolumenProximosMeses = volumenProximosMeses;
    }

    public Double getVolumenRestante() {
        return mVolumenRestante;
    }

    public void setVolumenRestante(Double volumenRestante) {
        mVolumenRestante = volumenRestante;
    }

    public Double getVolumenTotal() {
        return mVolumenTotal;
    }

    public void setVolumenTotal(Double volumenTotal) {
        mVolumenTotal = volumenTotal;
    }

}
