package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;

/**
 * Created by czamora on 9/13/16.
 */
public class User extends RealmObject {

    private String name;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int edad;

    public User() {

    }

    public User(String name, String apellidoPaterno, String apellidoMaterno, int edad) {
        this.name = name;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }
}
