
package mx.com.tarjetasdelnoreste.realmdb.model.jsonDescartar;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class MotivoExclusion {

    @SerializedName("comentarios")
    private String mComentarios;
    @SerializedName("id")
    private Long mId;

    public String getComentarios() {
        return mComentarios;
    }

    public void setComentarios(String comentarios) {
        mComentarios = comentarios;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

}
