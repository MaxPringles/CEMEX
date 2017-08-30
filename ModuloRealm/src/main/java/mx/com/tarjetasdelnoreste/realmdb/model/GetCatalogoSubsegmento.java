package mx.com.tarjetasdelnoreste.realmdb.model;

import java.util.List;

/**
 * Created by czamora on 9/14/16.
 */
public class GetCatalogoSubsegmento {

    private int idCatalogo;
    private List<CatalogoProductoDB> productos;
    private Long id;
    private String descripcion;
    private int idPadre;

    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public List<CatalogoProductoDB> getProductos() {
        return productos;
    }

    public void setProductos(List<CatalogoProductoDB> productos) {
        this.productos = productos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String Descripcion) {
        this.descripcion = Descripcion;
    }

    public int getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(int idPadre) {
        this.idPadre = idPadre;
    }
}
