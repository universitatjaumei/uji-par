package es.uji.apps.par.ficheros.registros;

import java.util.Locale;

import es.uji.apps.par.exceptions.RegistroSerializaException;
import es.uji.apps.par.model.Sala;
import es.uji.apps.par.model.Sesion;

public class RegistroSesionProgramada
{
    private String codigoSala;
    private String fechaSesion;
    private int numeroSesiones;

    public String getCodigoSala()
    {
        return codigoSala;
    }

    public void setCodigoSala(String codigoSala)
    {
        this.codigoSala = codigoSala;
    }

    public String getFechaSesion()
    {
        return fechaSesion;
    }

    public void setFechaSesion(String fechaSesion)
    {
        this.fechaSesion = fechaSesion;
    }

    public int getNumeroSesiones()
    {
        return numeroSesiones;
    }

    public void setNumeroSesiones(int numeroSesiones)
    {
        this.numeroSesiones = numeroSesiones;
    }

    public String serializa() throws RegistroSerializaException
    {
        Sala.checkValidity(codigoSala);
        Sesion.checkFechaCelebracion(fechaSesion);
        String result = String.format(Locale.ENGLISH, "5%-12s%s%02d", codigoSala, fechaSesion,
                numeroSesiones);

        return result;
    }
}
