package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by USRMICRO10 on 20/09/2016.
 */
public class CatalogoProductoServicioDB extends RealmObject{

    @PrimaryKey
    private int idProducto;

    private int idProspecto;
    private int idTipoSorP;
    private int idSubSegmento;
    private String descProducto;
    private int cantidadvol;
    private String unidadVol;
    private boolean checked;

    public int getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(int idProspecto) {
        this.idProspecto = idProspecto;
    }

    public int getIdTipoSorP() {
        return idTipoSorP;
    }

    public void setIdTipoSorP(int idTipoSorP) {
        this.idTipoSorP = idTipoSorP;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdSubSegmento() {
        return idSubSegmento;
    }

    public void setIdSubSegmento(int idSubSegmento) {
        this.idSubSegmento = idSubSegmento;
    }

    public String getDescProducto() {
        return descProducto;
    }

    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    public int getCantidadvol() {
        return cantidadvol;
    }

    public void setCantidadvol(int cantidadvol) {
        this.cantidadvol = cantidadvol;
    }

    public String getUnidadVol() {
        return unidadVol;
    }

    public void setUnidadVol(String unidadVol) {
        this.unidadVol = unidadVol;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
