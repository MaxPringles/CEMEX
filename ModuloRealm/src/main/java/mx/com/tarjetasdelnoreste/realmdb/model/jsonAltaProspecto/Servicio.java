
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import com.google.gson.annotations.SerializedName;

public class Servicio {

    @SerializedName("comentariosMotivoPerdida")
    private String mComentariosMotivoPerdida = "";
    @SerializedName("competidor")
    private Competidor mCompetidor;
    @SerializedName("duracionMeses")
    private Long mDuracionMeses = (long) 0;
    @SerializedName("estatusObra")
    private EstatusObra mEstatusObra;
    @SerializedName("fechaInicioObra")
    private String mFechaInicioObra = "";
    @SerializedName("id")
    private String mId = "";
    @SerializedName("mesesRestantes")
    private Long mMesesRestantes  = (long) 0;
    @SerializedName("motivoExclusion")
    private MotivoExclusion mMotivoExclusion;
    @SerializedName("nombre")
    private String mNombre = "";
    @SerializedName("numeroDeObra")
    private Long mNumeroDeObra = (long) 0;
    @SerializedName("obra")
    private Obra mObra;
    @SerializedName("oportunidadVenta")
    private OportunidadVenta mOportunidadVenta;
    @SerializedName("periodoProximosMeses")
    private Long mPeriodoProximosMeses = (long) 0;
    @SerializedName("periodoRestante")
    private Long mPeriodoRestante = (long) 0;
    @SerializedName("periodoTotal")
    private Long mPeriodoTotal = (long) 0;
    @SerializedName("seleccionado")
    private Boolean mSeleccionado = false;
    @SerializedName("semaforo")
    private Semaforo mSemaforo;

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

    public Long getNumeroDeObra() {
        return mNumeroDeObra;
    }

    public void setNumeroDeObra(Long numeroDeObra) {
        mNumeroDeObra = numeroDeObra;
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

    public Long getPeriodoProximosMeses() {
        return mPeriodoProximosMeses;
    }

    public void setPeriodoProximosMeses(Long periodoProximosMeses) {
        mPeriodoProximosMeses = periodoProximosMeses;
    }

    public Long getPeriodoRestante() {
        return mPeriodoRestante;
    }

    public void setPeriodoRestante(Long periodoRestante) {
        mPeriodoRestante = periodoRestante;
    }

    public Long getPeriodoTotal() {
        return mPeriodoTotal;
    }

    public void setPeriodoTotal(Long periodoTotal) {
        mPeriodoTotal = periodoTotal;
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

}
