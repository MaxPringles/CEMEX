package mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USRMICRO10 on 13/12/2016.
 */

public class Cargo {

    @SerializedName("id")
    private int id;
    @SerializedName("descripcion")
    private String cargo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
