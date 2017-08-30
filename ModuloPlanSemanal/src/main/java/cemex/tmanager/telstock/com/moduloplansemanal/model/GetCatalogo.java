package cemex.tmanager.telstock.com.moduloplansemanal.model;

/**
 * Created by Cesar on 12/09/2016.
 */
public class GetCatalogo {

    private Long id;
    private int idCatalogo;
    private String Descripcion;
    private int idPadre;

    public GetCatalogo(Long id, int idCatalogo, String Descripcion, int idPadre) {
        this.id = id;
        this.idCatalogo = idCatalogo;
        this.Descripcion = Descripcion;
        this.idPadre = idPadre;
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
}
