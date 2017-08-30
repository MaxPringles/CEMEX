package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by czamora on 9/14/16.
 */
public class CatalogoActividadesPGVDB extends RealmObject {

    @PrimaryKey
    private Long id;

    private int idCatalogo;
    private String Descripcion;
    private int idPadre;

    @Ignore
    private boolean checked;

    public CatalogoActividadesPGVDB() {
    }

    public CatalogoActividadesPGVDB(Long id, int idCatalogo, String descripcion, int idPadre, boolean checked) {
        this.id = id;
        this.idCatalogo = idCatalogo;
        Descripcion = descripcion;
        this.idPadre = idPadre;
        this.checked = checked;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(int idPadre) {
        this.idPadre = idPadre;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
