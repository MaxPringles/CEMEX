package mx.com.tarjetasdelnoreste.realmdb.model.CatalogoObraDB;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ObraDB extends RealmObject{

    @PrimaryKey
    private String id;

    private double volumenObra;
    private ObraDBDireccion direccion;
    private int mesesRestantes;
    private String numeroObra;
    private String nombre;
    private int idEstatusObra;
    private int fechaInicioObra;
    private String idCliente;
    private int duracionMeses;
    private int idTipoObra;
    private int idCampania;
    private int status;

    public double getVolumenObra() {
        return this.volumenObra;
    }

    public void setVolumenObra(double volumenObra) {
        this.volumenObra = volumenObra;
    }

    public ObraDBDireccion getDireccion() {
        return this.direccion;
    }

    public void setDireccion(ObraDBDireccion direccion) {
        this.direccion = direccion;
    }

    public int getMesesRestantes() {
        return this.mesesRestantes;
    }

    public void setMesesRestantes(int mesesRestantes) {
        this.mesesRestantes = mesesRestantes;
    }

    public String getNumeroObra() {
        return this.numeroObra;
    }

    public void setNumeroObra(String numeroObra) {
        this.numeroObra = numeroObra;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdEstatusObra() {
        return this.idEstatusObra;
    }

    public void setIdEstatusObra(int idEstatusObra) {
        this.idEstatusObra = idEstatusObra;
    }

    public int getFechaInicioObra() {
        return this.fechaInicioObra;
    }

    public void setFechaInicioObra(int fechaInicioObra) {
        this.fechaInicioObra = fechaInicioObra;
    }

    public String getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public int getDuracionMeses() {
        return this.duracionMeses;
    }

    public void setDuracionMeses(int duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIdTipoObra() {
        return this.idTipoObra;
    }

    public void setIdTipoObra(int idTipoObra) {
        this.idTipoObra = idTipoObra;
    }

    public int getIdCampania() {
        return this.idCampania;
    }

    public void setIdCampania(int idCampania) {
        this.idCampania = idCampania;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
