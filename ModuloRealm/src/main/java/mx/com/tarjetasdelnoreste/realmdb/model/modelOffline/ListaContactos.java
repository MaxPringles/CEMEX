package mx.com.tarjetasdelnoreste.realmdb.model.modelOffline;

import java.util.List;

import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;

/**
 * Created by usr_micro13 on 19/01/2017.
 */

public class ListaContactos {

    private List<Contacto> contactoList;

    public ListaContactos() {

    }

    public ListaContactos(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }

    public List<Contacto> getContactoList() {
        return contactoList;
    }

    public void setContactoList(List<Contacto> contactoList) {
        this.contactoList = contactoList;
    }
}
