package mx.com.tarjetasdelnoreste.realmdb.model.modelOffline;

/**
 * Created by usr_micro13 on 16/02/2017.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czamora on 11/23/16.
 */

public class ArchivosAlta {

    @SerializedName("idProspecto")
    private String idProspecto;

    @SerializedName("archivosAlta")
    private List<String> archivosAlta;

    public String getIdProspecto() {
        return idProspecto;
    }

    public void setIdProspecto(String idProspecto) {
        this.idProspecto = idProspecto;
    }

    public List<String> getArchivosAlta() {
        return archivosAlta;
    }

    public void setArchivosAlta(List<String> archivosAlta) {
        this.archivosAlta = archivosAlta;
    }
}

