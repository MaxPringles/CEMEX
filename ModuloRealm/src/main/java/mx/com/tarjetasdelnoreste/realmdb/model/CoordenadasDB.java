package mx.com.tarjetasdelnoreste.realmdb.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by czamora on 11/7/16.
 */
public class CoordenadasDB extends RealmObject {

    @PrimaryKey
    private Long fechaCoordenada;

    private double latitudCoordenada;
    private double longitudCoordenada;
    private int enviado;
    private int idAccion;


    public CoordenadasDB() {

    }



    public CoordenadasDB(Long fechaCoordenada, double latitudCoordenada, double longitudCoordenada, int enviado,
                         int idAccion) {
        this.fechaCoordenada = fechaCoordenada;
        this.latitudCoordenada = latitudCoordenada;
        this.longitudCoordenada = longitudCoordenada;
        this.enviado = enviado;
        this.idAccion = idAccion;
    }

    public int getIdAccion() {
        return idAccion;
    }

    public void setIdAccion(int idAccion) {
        this.idAccion = idAccion;
    }

    public Long getFechaCoordenada() {
        return fechaCoordenada;
    }

    public void setFechaCoordenada(Long fechaCoordenada) {
        this.fechaCoordenada = fechaCoordenada;
    }

    public double getLatitudCoordenada() {
        return latitudCoordenada;
    }

    public void setLatitudCoordenada(double latitudCoordenada) {
        this.latitudCoordenada = latitudCoordenada;
    }

    public double getLongitudCoordenada() {
        return longitudCoordenada;
    }

    public void setLongitudCoordenada(double longitudCoordenada) {
        this.longitudCoordenada = longitudCoordenada;
    }

    public int getEnviado() {
        return enviado;
    }

    public void setEnviado(int enviado) {
        this.enviado = enviado;
    }
}
