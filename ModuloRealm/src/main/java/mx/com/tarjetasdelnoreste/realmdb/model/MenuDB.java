package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;

/**
 * Created by czamora on 9/13/16.
 */
public class MenuDB extends RealmObject {

    private Long id;
    private String Nombre;
    private String CssIconClass;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCssIconClass() {
        return CssIconClass;
    }

    public void setCssIconClass(String cssIconClass) {
        CssIconClass = cssIconClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
