package mx.com.tarjetasdelnoreste.realmdb;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import mx.com.tarjetasdelnoreste.realmdb.model.ContactosDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Cargo;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Contacto;

/**
 * Created by czamora on 9/13/16.
 */
public class ContactosRealm extends MenuRealm {

    public ContactosRealm(Realm realm) {
        super(realm);
    }

    /** MÉTODO QUE GUARDA UNA LISTA DENTRO DE LA TABLA **/
    public static void guardarListaContactos(List<ContactosDB> listaContactos) {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.copyToRealmOrUpdate(listaContactos); //Inserta la lista en la DB.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /**
     * MÉTODO QUE ELIMINAR LA TABLA
     **/
    public static void eliminarTablaContactos() {

        realm.beginTransaction(); //Indica que se iniciará una operación en la DB.
        realm.delete(ContactosDB.class); //Borra los registros de la tabla.
        realm.commitTransaction(); //Cierra la transacción.
    }

    /** MÉTODO QUE DEVUELVE TODOS LOS REGISTROS DE LA TABLA, LOS CUALES DEVUELVE COMO UN
     * OBJETO TIPO Contacto, ESTO PARA FACILITAR EL ENVÍO DE LOS DATOS AL WS **/
    public static List<Contacto> mostrarListaContactos(String idProspecto) {

        //Se obtienen los contactos guardados en la BD.
        List<ContactosDB> contactosDBList = realm.where(ContactosDB.class)
                .equalTo("idProspecto", idProspecto)
                .findAll();

        //Se copian los contactos de ContactosDB a Contacto.
        List<Contacto> objetoContactoList = new ArrayList<>();
        Contacto objetoContacto;
        Cargo cargo;

        for (ContactosDB contactosDB : contactosDBList) {
            objetoContacto = new Contacto();
            cargo = new Cargo();
            objetoContacto.setApellidoMaterno(contactosDB.getApellidoMaterno());
            objetoContacto.setApellidoPaterno(contactosDB.getApellidoPaterno());
            cargo.setCargo(contactosDB.getCargo());
            cargo.setId(Integer.parseInt(contactosDB.getIdCargo()));
            objetoContacto.setCargo(cargo);
            objetoContacto.setComentarios(contactosDB.getComentarios());
            objetoContacto.setEmail(contactosDB.getEmail());
            objetoContacto.setExtension(contactosDB.getExtension());
            objetoContacto.setFotografia(""); ////// No se tiene contemplada la imagen en formulario de Contacto.
            objetoContacto.setId(0);
            objetoContacto.setNombres(contactosDB.getNombres());
            objetoContacto.setPrincipal(contactosDB.isPrincipal());
            objetoContacto.setTelefono(contactosDB.getTelefono());

            objetoContactoList.add(objetoContacto);
        }

        //Se devuelve la lista de contactos como un objeto tipo Contacto.
        return objetoContactoList;

    }
}
