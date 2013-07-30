package es.uji.apps.par.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DisponiblesLocalizacion
{
    private String localizacion;
    private int disponibles;

    public DisponiblesLocalizacion()
    {
    }

    public DisponiblesLocalizacion(String localizacion, int disponibles)
    {
        this.localizacion = localizacion;
        this.disponibles = disponibles;
    }

    public String getLocalizacion()
    {
        return localizacion;
    }

    public void setLocalizacion(String localizacion)
    {
        this.localizacion = localizacion;
    }

    public int getDisponibles()
    {
        return disponibles;
    }

    public void setDisponibles(int disponibles)
    {
        this.disponibles = disponibles;
    }

}