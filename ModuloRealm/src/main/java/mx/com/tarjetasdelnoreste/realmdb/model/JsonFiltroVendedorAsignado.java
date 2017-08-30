package mx.com.tarjetasdelnoreste.realmdb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by movil4 on 29/12/16.
 */

public class JsonFiltroVendedorAsignado {

    @SerializedName("idVendedor")
    private String mIdVendedorAsignado;

    public String getIdVendedorAsignado() {
        return mIdVendedorAsignado;
    }

    public void setIdVendedorAsignado(String idVendedorAsignado) {
        mIdVendedorAsignado = idVendedorAsignado;
    }

}
