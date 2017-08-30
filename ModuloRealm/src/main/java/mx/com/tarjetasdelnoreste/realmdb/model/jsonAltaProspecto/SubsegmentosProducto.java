
package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SubsegmentosProducto {

    @SerializedName("idSubsegmento")
    private String mIdSubsegmento = "";
    @SerializedName("nombre")
    private String mNombre = "";
    @SerializedName("productos")
    private List<Producto> mProductos;
    @SerializedName("todosSeleccion")
    private Boolean mTodosSeleccion = false;

    public String getIdSubsegmento() {
        return mIdSubsegmento;
    }

    public void setIdSubsegmento(String idSubsegmento) {
        mIdSubsegmento = idSubsegmento;
    }

    public String getNombre() {
        return mNombre;
    }

    public void setNombre(String nombre) {
        mNombre = nombre;
    }

    public List<Producto> getProductos() {
        return mProductos;
    }

    public void setProductos(List<Producto> productos) {
        mProductos = productos;
    }

    public Boolean getTodosSeleccion() {
        return mTodosSeleccion;
    }

    public void setTodosSeleccion(Boolean todosSeleccion) {
        mTodosSeleccion = todosSeleccion;
    }

}
