package es.uji.apps.par.model;

import java.util.Locale;

import es.uji.apps.par.db.ButacaDTO;

public class ButacaPassbook {

    private String enlace;
    private String zona;
    private String fila;
    private String numero;

    public ButacaPassbook(
        ButacaDTO butaca,
        Locale locale,
        String uuidCompra,
        String urlPassbook
    ) {
        this.fila = butaca.getFila();
        this.numero = butaca.getNumero();
        this.enlace = String.format("%s/rest/compra/%s/%s/passbook", urlPassbook, uuidCompra, butaca.getId());
        if(locale.getLanguage().equals("es")){
            this.zona = butaca.getParLocalizacion().getNombreEs();
        } else {
            this.zona = butaca.getParLocalizacion().getNombreVa();
        }
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
