package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TipoInforme {
    private String id;
    private String nombre;
    private String nombreCA;
    private String nombreES;

    public TipoInforme()
    {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreCA() {
        return nombreCA;
    }

    public void setNombreCA(String nombreCA) {
        this.nombreCA = nombreCA;
    }

    public String getNombreES() {
        return nombreES;
    }

    public void setNombreES(String nombreES) {
        this.nombreES = nombreES;
    }
}
