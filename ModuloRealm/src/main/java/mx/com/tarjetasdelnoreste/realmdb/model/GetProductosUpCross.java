package mx.com.tarjetasdelnoreste.realmdb.model;

import java.util.ArrayList;

/**
 * Created by czamora on 9/14/16.
 */
public class GetProductosUpCross {

    String descripcion;

    ArrayList<ItemsSupport> productos;

    public ArrayList<ItemsSupport> getItems() {
        return productos;
    }

    public void setItems(ArrayList<ItemsSupport> productos) {
        this.productos = productos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
