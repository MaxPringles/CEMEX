
package mx.com.tarjetasdelnoreste.realmdb.model.jsonObra;

import com.google.gson.annotations.SerializedName;

public class JsonObra {

    @SerializedName("direccion")
    private Direccion mDireccion;
    @SerializedName("duracionMeses")
    private Long mDuracionMeses;
    @SerializedName("fechaInicioObra")
    private Long mFechaInicioObra;
    @SerializedName("id")
    private String mId;
    @SerializedName("idCampania")
    private Long mIdCampania;
    @SerializedName("idCliente")
    private String mIdCliente;
    @SerializedName("idEstatusObra")
    private Long mIdEstatusObra;
    @SerializedName("idTipoObra")
    private Long mIdTipoObra;
    @SerializedName("mesesRestantes")
    private Long mMesesRestantes;

    @SerializedName("nombre")
    private String mNombre;
    @SerializedName("numeroObra")
    private String mNumeroObra;

    @SerializedName("numeroHolding")
    private String mNumeroHolding;

    @SerializedName("status")
    private Long mStatus;
    @SerializedName("volumenObra")
    private double mVolumenObra;
    @SerializedName("idAltaOffline")
    private String idAltaOffline;

    public Direccion getDireccion() {
        return mDireccion;
    }

    public void setDireccion(Direccion direccion) {
        mDireccion = direccion;
    }

    public Long getDuracionMeses() {
        return mDuracionMeses;
    }

    public void setDuracionMeses(Long duracionMeses) {
        mDuracionMeses = duracionMeses;
    }

    public Long getFechaInicioObra() {
        return mFechaInicioObra;
    }

    public void setFechaInicioObra(Long fechaInicioObra) {
        mFechaInicioObra = fechaInicioObra;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Long getIdCampania() {
        return mIdCampania;
    }

    public void setIdCampania(Long idCampania) {
        mIdCampania = idCampania;
    }

    public String getIdCliente() {
        return mIdCliente;
    }

    public void setIdCliente(String idCliente) {
        mIdCliente = idCliente;
    }

    public Long getIdEstatusObra() {
        return mIdEstatusObra;
    }

    public void setIdEstatusObra(Long idEstatusObra) {
        mIdEstatusObra = idEstatusObra;
    }

    public Long getIdTipoObra() {
        return mIdTipoObra;
    }

    public void setIdTipoObra(Long idTipoObra) {
        mIdTipoObra = idTipoObra;
    }

    public Long getMesesRestantes() {
        return mMesesRestantes;
    }

    public void setMesesRestantes(Long mesesRestantes) {
        mMesesRestantes = mesesRestantes;
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

    public String getmNumeroHolding() {
        return mNumeroHolding;
    }

    public void setmNumeroHolding(String mNumeroHolding) {
        this.mNumeroHolding = mNumeroHolding;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public double getVolumenObra() {
        return mVolumenObra;
    }

    public void setVolumenObra(double volumenObra) {
        mVolumenObra = volumenObra;
    }

    public String getIdAltaOffline() {
        return idAltaOffline;
    }

    public void setIdAltaOffline(String idAltaOffline) {
        this.idAltaOffline = idAltaOffline;
    }
}
